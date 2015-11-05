package sun.bob.mcalendar.beans;

import android.database.Cursor;
import android.provider.CalendarContract;

import sun.bob.mcalendar.utils.TimeStampUtil;
import sun.bob.mcalendarview.vo.DateData;

/**
 * Created by bob.sun on 15/10/23.
 */
public class TaskBean implements Comparable {
    String id;
    String allDay;
    String title;
    String description;
    String startDate;
    String endDate;

    String rDate;
    String rRule;
    String exDate;
    String exRule;

    public TaskBean(){

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean getAllDay() {
        return allDay == "1";
    }

    public void setAllDay(String allDay) {
        this.allDay = allDay;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public DateData getStartDate() {
        return TimeStampUtil.toDateData(startDate);
    }

    public Long getStartDateLong(){
        return Long.parseLong(startDate);
    }

    public String getStartDateString(){
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public DateData getEndDate() {
        return TimeStampUtil.toDateData(endDate);
    }

    public String getEndDateString(){
        return endDate;
    }

    public Long getEndDateLong(){
        return Long.parseLong(endDate);
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getrDate() {
        return rDate;
    }

    public void setrDate(String rDate) {
        this.rDate = rDate;
    }

    public String getrRule() {
        return rRule;
    }

    public void setrRule(String rRule) {
        this.rRule = rRule;
    }

    public String getExDate() {
        return exDate;
    }

    public void setExDate(String exDate) {
        this.exDate = exDate;
    }

    public String getExRule() {
        return exRule;
    }

    public void setExRule(String exRule) {
        this.exRule = exRule;
    }

    public TaskBean populate(Cursor cursor){
        this.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(CalendarContract.Events.TITLE)));
        this.setId(cursor.getString(cursor.getColumnIndexOrThrow(CalendarContract.Events._ID)));
        this.setAllDay(cursor.getString(cursor.getColumnIndexOrThrow(CalendarContract.Events.ALL_DAY)));
        this.setStartDate(cursor.getString(cursor.getColumnIndexOrThrow(CalendarContract.Events.DTSTART)));
        this.setEndDate(cursor.getString(cursor.getColumnIndexOrThrow(CalendarContract.Events.DTEND)));
        this.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(CalendarContract.Events.DESCRIPTION)));
        this.setrDate(cursor.getString(cursor.getColumnIndexOrThrow(CalendarContract.Events.RDATE)));
        this.setrRule(cursor.getString(cursor.getColumnIndexOrThrow(CalendarContract.Events.RRULE)));
        this.setExDate(cursor.getString(cursor.getColumnIndexOrThrow(CalendarContract.Events.EXDATE)));
        this.setExRule(cursor.getString(cursor.getColumnIndexOrThrow(CalendarContract.Events.EXRULE)));
        return this;
    }

    public static boolean isRecurrence(Cursor cursor){
        return cursor.getString(cursor.getColumnIndexOrThrow(CalendarContract.Events.RRULE)) == null;
    }

    public TaskBean populate(TaskBean taskBean){
        this.setTitle(taskBean.getTitle());
        this.setAllDay(taskBean.getAllDay() ? "1" : "0");
        this.setDescription(taskBean.getDescription());
        this.setEndDate(taskBean.endDate);
        this.setStartDate(taskBean.startDate);
        this.setExDate(taskBean.getExDate());
        this.setExRule(taskBean.getExRule());
        this.setId(taskBean.getId());
        this.setrDate(taskBean.getrDate());
        this.setrRule(taskBean.getrRule());
        return this;
    }

    @Override
    public int compareTo(Object another) {
        return (int) (Long.parseLong(this.startDate) / 1000 - Long.parseLong(((TaskBean) another).startDate) / 1000);
    }
}
