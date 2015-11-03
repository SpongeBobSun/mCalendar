package sun.bob.mcalendar.utils;

import android.util.Log;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

import sun.bob.mcalendarview.vo.DateData;

/**
 * Created by bob.sun on 15/10/23.
 */
public class TimeStampUtil {
    public static String toUnixTimeStamp(DateData dateData){
        Log.e("Returned time stamp:", new StringBuilder().append("").append(dateData.getYear()).append("-").append(dateData.getMonthString()).append("-").append(dateData.getDayString()).append(" ").append(dateData.getHourString()).append(":").append(dateData.getMinuteString()).append(":00.000000000").toString());
        return new Long(Timestamp.valueOf(new StringBuilder().append("").append(dateData.getYear()).append("-").append(dateData.getMonthString()).append("-").append(dateData.getDayString()).append(" ").append(dateData.getHourString()).append(":").append(dateData.getMinuteString()).append(":00.000000000").toString()).getTime()).toString();
    }

    public static long toUnixLong(DateData dateData){
        return new Long(Timestamp.valueOf(new StringBuilder().append("").append(dateData.getYear()).append("-").append(dateData.getMonthString()).append("-").append(dateData.getDayString()).append(" ").append(dateData.getHourString()).append(":").append(dateData.getMinuteString()).append(":00.000000000").toString()).getTime());
    }

    public static DateData toDateData(String timestamp){
        if (timestamp == null){
            timestamp = "0";
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Long.parseLong(timestamp));
        DateData ret = new DateData(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH));
        ret.setHour(calendar.get(Calendar.HOUR_OF_DAY));
        ret.setMinute(calendar.get(Calendar.MINUTE));
        return ret;
    }

    public static DateData toDateData(long l){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(l);
        DateData ret = new DateData(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH));
        ret.setHour(calendar.get(Calendar.HOUR_OF_DAY));
        ret.setMinute(calendar.get(Calendar.MINUTE));
        return ret;
    }

    public static DateData getLastMonth(int year, int month){
        if (month == 1){
            month = 12;
            year -= 1;
        } else {
            month -= 1;
        }
        return new DateData(year, month, 1);
    }

    public static DateData getNextMonth(int year, int month){
        if (month == 12){
            month = 1;
            year += 1;
        } else {
            month += 1;
        }
        return new DateData(year, month, 1);
    }
}
