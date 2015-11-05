package sun.bob.mcalendar.constants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import sun.bob.mcalendar.R;

/**
 * Created by bob.sun on 15/10/30.
 */
public class Constants {
    //RRules
    public static final List REPEAT_LIST = Arrays.asList(new String[]{"Once", "Every Day", "Every Week", "Every Month", "Every Year"});
    public static final int RRULE_DAILY = 1;
    public static final int RRULE_WEEKLY = 2;
    public static final int RRULE_MONTHLY = 3;
    public static final int RRULE_YEARLY = 4;


    public static final List REMINDER_LIST = Arrays.asList(new String[]{"None", "10 min before", "30 min before", "1 hour before", "3 hour before"});
    public static final int[] REMINDER_VALUE = new int[]{0, 10, 30, 60, 180};

    public static final int MONTH_TEXT[] = {-1, R.string.Jan, R.string.Feb, R.string.Mar, R.string.Apr, R.string.May, R.string.Jun, R.string.Jul, R.string.Aug, R.string.Sep, R.string.Oct, R.string.Nov, R.string.Dec};
}
