package sun.bob.mcalendar.content;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.CalendarContract;

import java.util.Calendar;

import sun.bob.mcalendar.beans.TaskBean;

/**
 * Created by bob.sun on 15/11/3.
 */
public class CalendarProvider {
    private static CalendarProvider staticInstance;
    private ContentResolver contentResolver;
    private Context context;

    private CalendarProvider(Context context) {
        this.context = context;
        contentResolver = context.getContentResolver();
    }

    public static CalendarProvider getStaticInstance(Context context) {
        if (staticInstance == null)
            staticInstance = new CalendarProvider(context);
        return staticInstance;
    }

    public long insertTask(TaskBean taskBean, long calendarId) {
        /**
         * ***************
         * * FBI-WARNING *
         * ***************
         * calendarId must greater than 0
         *
         * bob out.
         */

        ContentValues values = new ContentValues();
        values.put(CalendarContract.Events.TITLE, taskBean.getTitle());
        values.put(CalendarContract.Events.ALL_DAY, taskBean.getAllDay());
        values.put(CalendarContract.Events.DTSTART, taskBean.getStartDateLong());
        values.put(CalendarContract.Events.DTEND, taskBean.getEndDateLong());
        values.put(CalendarContract.Events.DESCRIPTION, taskBean.getDescription());
        values.put(CalendarContract.Events.RRULE, taskBean.getrRule());
        values.put(CalendarContract.Events.RDATE, taskBean.getrDate());
        values.put(CalendarContract.Events.EXDATE, taskBean.getExDate());
        values.put(CalendarContract.Events.EXRULE, taskBean.getExRule());
        values.put(CalendarContract.Events.CALENDAR_ID, calendarId);
        values.put(CalendarContract.Events.EVENT_TIMEZONE, Calendar.getInstance().getTimeZone().getID());

        // TODO: 15/11/3 Check permission?

        Uri insertUri = contentResolver.insert(CalendarContract.Events.CONTENT_URI, values);
        Long ret = Long.parseLong(insertUri.getLastPathSegment());
        return ret;
    }

    public void insertReminderForTask(long eventId, int minutes, int method) {
        ContentValues values = new ContentValues();
        values.put(CalendarContract.Reminders.EVENT_ID, eventId);
        values.put(CalendarContract.Reminders.MINUTES, minutes);
        values.put(CalendarContract.Reminders.METHOD, method);
        contentResolver.insert(CalendarContract.Reminders.CONTENT_URI, values);
    }

    public void updateTask(long taskId, TaskBean taskBean){
        ContentValues values = new ContentValues();
        values.put(CalendarContract.Events.TITLE, taskBean.getTitle());
        values.put(CalendarContract.Events.ALL_DAY, taskBean.getAllDay());
        values.put(CalendarContract.Events.DTSTART, taskBean.getStartDateLong());
        values.put(CalendarContract.Events.DTEND, taskBean.getEndDateLong());
        values.put(CalendarContract.Events.DESCRIPTION, taskBean.getDescription());
        values.put(CalendarContract.Events.RRULE, taskBean.getrRule());
        values.put(CalendarContract.Events.RDATE, taskBean.getrDate());
        values.put(CalendarContract.Events.EXDATE, taskBean.getExDate());
        values.put(CalendarContract.Events.EXRULE, taskBean.getExRule());

        contentResolver.update(CalendarContract.Events.CONTENT_URI, values, CalendarContract.Events._ID + "=?", new String[]{"" + taskId});
    }

}
