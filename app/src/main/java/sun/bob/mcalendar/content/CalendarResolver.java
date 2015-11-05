package sun.bob.mcalendar.content;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import sun.bob.mcalendar.beans.TaskBean;
import sun.bob.mcalendar.utils.RecurrenceUtil;
import sun.bob.mcalendar.utils.TimeStampUtil;
import sun.bob.mcalendarview.utils.CurrentCalendar;
import sun.bob.mcalendarview.vo.DateData;

/**
 * Created by bob.sun on 15/10/23.
 */
public class CalendarResolver {

    public static final String[] ACCOUNT_FIELDS = { CalendarContract.Calendars.NAME,
            CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,
            CalendarContract.Calendars.CALENDAR_COLOR,
            CalendarContract.Calendars.VISIBLE};
    public static final String[] EVENTS_FIELDS = {
            CalendarContract.Events._ID,
            CalendarContract.Events.ALL_DAY,
            CalendarContract.Events.DESCRIPTION,
            CalendarContract.Events.DTEND,
            CalendarContract.Events.DTSTART,
            CalendarContract.Events.TITLE,
            CalendarContract.Events.RDATE,
            CalendarContract.Events.RRULE,
            CalendarContract.Events.EXDATE,
            CalendarContract.Events.EXRULE
    };

    public static final Uri CALENDAR_URI = Uri.parse("content://com.android.calendar/calendars");
    public static final Uri EVENTS_URI = Uri.parse("content://com.android.calendar/events");

    ContentResolver contentResolver;
    Set<String> accounts = new HashSet<String>();
    private ArrayList<TaskBean> allTasks;

    private static CalendarResolver staticInstance;

    private int currentPage;
    private int itemPerPage = 10;

    private CalendarResolver(Context ctx) {
        contentResolver = ctx.getContentResolver();
        currentPage = 0;
    }

    public static CalendarResolver getStaticInstance(Context context) {
        if (staticInstance == null){
            staticInstance = new CalendarResolver(context);
        }
        return staticInstance;
    }

    public Set<String> getCalendarAccounts() {
        // Fetch a list of all calendars sync'd with the device and their display names
        Cursor cursor = contentResolver.query(CALENDAR_URI, ACCOUNT_FIELDS, null, null, null);

        try {
            if(cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    String name = cursor.getString(0);
                    String displayName = cursor.getString(1);
                    // This is actually a better pattern:
                    String color = cursor.getString(cursor.getColumnIndex(
                            CalendarContract.Calendars.CALENDAR_COLOR));
                    Boolean selected = !cursor.getString(3).equals("0");
                    accounts.add(displayName);
                }
            }
        } catch (AssertionError ex) {
            // TODO: log exception and bail
        }
        return accounts;
    }

    public ArrayList<TaskBean> getAllEvents(){
        Cursor cursor = contentResolver.query(EVENTS_URI, EVENTS_FIELDS, null, null, null);
        if (allTasks == null){
            allTasks = new ArrayList<>();
        } else {
            return allTasks;
        }
        TaskBean toAdd;
        if (cursor.getCount() > 0){
            while (cursor.moveToNext()){
                toAdd = new TaskBean().populate(cursor);
                if (toAdd.getrRule() != null){
                    allTasks.addAll(RecurrenceUtil.getAllRecurrence(
                            toAdd,
                            CurrentCalendar.getCurrentDateData(),
                            CurrentCalendar.getCurrentDateData().setYear(CurrentCalendar.getCurrentDateData().getYear() + 1)
                    ));
                } else {
                    allTasks.add(toAdd);
                }
            }
        }
        cursor.close();
        Collections.sort(allTasks);
        return allTasks;
    }

    public ArrayList<TaskBean> refreshAllEvents(){
        allTasks = null;
        System.gc();
        return getAllEvents();
    }

    public ArrayList<TaskBean> getEventsOn(DateData dateData){

        String currentTS = TimeStampUtil.toUnixTimeStamp(dateData);
        Long nextTS = Long.parseLong(currentTS);
        nextTS += 86400000;

        Cursor cursor = contentResolver.query(EVENTS_URI,
                EVENTS_FIELDS,
                new StringBuilder().append(CalendarContract.Events.DTSTART).append(">=? AND ").append(CalendarContract.Events.DTEND).append("<?").append(" AND ( ").append(CalendarContract.Events.RRULE).append(" IS NULL OR ").append(CalendarContract.Events.RRULE).append(" ='' )").toString(),
                new String[]{currentTS, nextTS.toString()},
                null);
        ArrayList<TaskBean> ret = new ArrayList<>();
        if (cursor.getCount() > 0){
            while (cursor.moveToNext()){
                ret.add(new TaskBean().populate(cursor));
            }
        }
        cursor = contentResolver.query(EVENTS_URI,
                                            EVENTS_FIELDS,
                                        CalendarContract.Events.RRULE + "!= ''",
                                        null,
                                        null);
        TaskBean toAdd;
        if (cursor.getCount() > 0){
            while (cursor.moveToNext()){
                toAdd = new TaskBean().populate(cursor);
                if (toAdd.getrRule() != null){
                    ret.addAll(RecurrenceUtil.getAllRecurrence(
                            toAdd,
                            dateData,
                            TimeStampUtil.toDateData(nextTS)
                    ));
                } else {
                    ret.add(toAdd);
                }
            }
        }
        cursor.close();
        Collections.sort(ret);
        return ret;
    }

    public ArrayList<TaskBean> getEventsByMonth(int year, int month){
        ArrayList<TaskBean> ret = new ArrayList<>();
        Long start = Long.parseLong(TimeStampUtil.toUnixTimeStamp(new DateData(year, month, 1)));
        Calendar tmp = Calendar.getInstance();
        tmp.set(year, month - 1, 1);
        tmp.set(year, month - 1, tmp.getMaximum(Calendar.DAY_OF_MONTH));
        Long end = tmp.getTimeInMillis();
        end += 86400000;
        Cursor cursor = contentResolver.query(EVENTS_URI,
                EVENTS_FIELDS,
                new StringBuilder().append(CalendarContract.Events.DTSTART).append(">=? AND ").append(CalendarContract.Events.DTEND).append("<?").append(" AND ( ").append(CalendarContract.Events.RRULE).append(" IS NULL OR ").append(CalendarContract.Events.RRULE).append(" ='' )").toString(),
                new String[]{start.toString(), end.toString()},
                null);
        if (cursor.getCount() > 0){
            while (cursor.moveToNext()){
                ret.add(new TaskBean().populate(cursor));
            }
        }
        cursor.close();
        cursor = contentResolver.query(EVENTS_URI,
                EVENTS_FIELDS,
                CalendarContract.Events.RRULE + "!= ''",
                null,
                null);
        TaskBean toAdd;
        if (cursor.getCount() > 0){
            while (cursor.moveToNext()){
                toAdd = new TaskBean().populate(cursor);
                if (toAdd.getrRule() != null){
                    ret.addAll(RecurrenceUtil.getAllRecurrence(
                            toAdd,
                            TimeStampUtil.toDateData(start),
                            TimeStampUtil.toDateData(end)
                    ));
                } else {
                    ret.add(toAdd);
                }
            }
        }
        cursor.close();
        Collections.sort(ret);
        return ret;
    }

    public ArrayList<ArrayList<TaskBean>> getAllTasksGroupByDay(){
        ArrayList<ArrayList<TaskBean>> ret = new ArrayList<>();
        if (allTasks == null|| allTasks.size() == 0){
            return ret;
        }
        TaskBean last = allTasks.get(0);
        ArrayList<TaskBean> toAdd = new ArrayList<>();
        for (TaskBean task : allTasks){
            if (task.getStartDate().equals(last.getStartDate())){
                last = task;
                toAdd.add(task);
            } else {
                ret.add(toAdd);
                toAdd = new ArrayList<>();
                toAdd.add(task);
                last = task;
            }
        }
        return ret;
    }


    public TaskBean getTaskById(long id){
        TaskBean ret = null;
        Cursor cursor = contentResolver.query(EVENTS_URI,
                EVENTS_FIELDS,
                new StringBuilder().append(CalendarContract.Events._ID).append("=?").toString(),
                new String[]{"" + id},
                null);
        if (cursor.getCount() > 0){
            while (cursor.moveToNext()){
                ret = new TaskBean().populate(cursor);
            }
        }
        return ret;
    }

    public int getCurrentPage(){
        return currentPage;
    }

    public int getMaxPage(){
        return allTasks.size() / itemPerPage;
    }

    public ArrayList<TaskBean> getNextPage(){
        int old = currentPage;
        currentPage += currentPage == getMaxPage() ? 0 : 1;
        return (ArrayList<TaskBean>) allTasks.subList(old * itemPerPage, currentPage * itemPerPage);
    }

    public ArrayList<TaskBean> getPrevOage(){
        int old = currentPage;
        currentPage -= currentPage == 0 ? 0 : 1;
        return (ArrayList<TaskBean>) allTasks.subList(old * itemPerPage, currentPage * itemPerPage);
    }
}
