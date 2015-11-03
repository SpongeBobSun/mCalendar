package sun.bob.mcalendar.utils;

import android.text.format.Time;

import com.android.calendarcommon2.DateException;
import com.android.calendarcommon2.RecurrenceProcessor;
import com.android.calendarcommon2.RecurrenceSet;

import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

import sun.bob.mcalendar.beans.TaskBean;
import sun.bob.mcalendar.constants.Constants;
import sun.bob.mcalendarview.vo.DateData;

/**
 * Created by bob.sun on 15/10/23.
 */
public class RecurrenceUtil {
    public static ArrayList<TaskBean> getAllRecurrence(TaskBean taskBean, DateData start, DateData end){
        ArrayList<TaskBean> ret = new ArrayList<>();
        RecurrenceSet rule = new RecurrenceSet(
                taskBean.getrRule(),
                taskBean.getrDate(),
                taskBean.getExRule(),
                taskBean.getExDate()
                );
        RecurrenceProcessor processor = new RecurrenceProcessor();
        Time time = new Time(TimeZone.getDefault().getID());
        time.set(taskBean.getStartDateLong());
        long[] result = null;
        try {
            result = processor.expand( time,
                    rule,
                    TimeStampUtil.toUnixLong(start),
                    TimeStampUtil.toUnixLong(end));
        } catch (DateException e) {
            e.printStackTrace();
            return ret;
        }
        TaskBean toAdd;
        DateData timeData;
        int hour, minute;
        hour = taskBean.getEndDate().getHour();
        minute = taskBean.getEndDate().getMinute();

        for (long l : result){
            toAdd = new TaskBean().populate(taskBean);
            toAdd.setStartDate(new Long(l).toString());
            timeData = TimeStampUtil.toDateData(l);
            timeData.setHour(hour);
            timeData.setMinute(minute);
            toAdd.setEndDate(TimeStampUtil.toUnixTimeStamp(timeData));
            ret.add(toAdd);
        }
        return ret;
    }

    public static TaskBean populateRRule(TaskBean taskBean, int which){
        switch (which){
            case Constants.RRULE_DAILY:
                taskBean.setrRule("FREQ=DAILY");
                break;
            case Constants.RRULE_WEEKLY:
                taskBean.setrRule("FREQ=WEEKLY");
                break;
            case Constants.RRULE_MONTHLY:
                taskBean.setrRule("FREQ=MONTHLY");
                break;
            case Constants.RRULE_YEARLY:
                taskBean.setrRule("FREQ=YEARLY");
                break;
            default:
                taskBean.setrRule("");
        }
        return taskBean;
    }
}
