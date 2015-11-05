package sun.bob.mcalendar.activities;

import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.Calendar;

import sun.bob.mcalendar.R;
import sun.bob.mcalendar.adapters.DropdownAdapter;
import sun.bob.mcalendar.beans.TaskBean;
import sun.bob.mcalendar.constants.Constants;
import sun.bob.mcalendar.content.CalendarProvider;
import sun.bob.mcalendar.content.CalendarResolver;
import sun.bob.mcalendar.utils.RecurrenceUtil;
import sun.bob.mcalendar.utils.TimeStampUtil;
import sun.bob.mcalendarview.utils.CurrentCalendar;
import sun.bob.mcalendarview.vo.DateData;

public class TaskActivity extends AppCompatActivity {

    private final int PICK_START = 1;
    private final int PICK_END = 2;
    public static final int SPINNER_REPEAT = 3;
    public static final int SPINNER_REMINDER = 4;
    int year, month, day;
    public DateData dateStart, dateEnd;
    private EditText title, description;
    private Spinner repetition, reminder;
    long taskId;
    boolean edit;
    TaskBean taskBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        year = getIntent().getIntExtra("year",0);
        month = getIntent().getIntExtra("month", 0);
        day = getIntent().getIntExtra("day", 0);

        edit = getIntent().getBooleanExtra("edit", false);
        taskId = getIntent().getLongExtra("taskId", -1);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TaskBean taskBean = genTask();
                if (!edit){
                    if (taskBean != null){
                        // TODO: 15/11/3 Insert by calendar id.
                        taskId = CalendarProvider.getStaticInstance(TaskActivity.this).insertTask(taskBean, 1);
                    }
                } else {
                    CalendarProvider.getStaticInstance(TaskActivity.this).updateTask(taskId, taskBean);
                }
                int reminderSpinnerPos = reminder.getSelectedItemPosition();
                if ( reminderSpinnerPos != 0){
                    CalendarProvider.getStaticInstance(TaskActivity.this).insertReminderForTask(taskId, Constants.REMINDER_VALUE[reminderSpinnerPos], CalendarContract.Reminders.METHOD_DEFAULT);
                }
                TaskActivity.this.finish();
            }
        });

        initUI();
    }

    private void initUI(){
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        findViewById(R.id.id_edit_start).setOnClickListener(new DatePickerTrigger(dateStart, PICK_START));
        findViewById(R.id.id_edit_end).setOnClickListener(new DatePickerTrigger(dateEnd, PICK_END));
        repetition = ((Spinner) findViewById(R.id.id_edit_repetition));
        repetition.setAdapter(new DropdownAdapter(this, android.R.layout.simple_spinner_item).setWhich(SPINNER_REPEAT));
        reminder = ((Spinner) findViewById(R.id.id_edit_reminder));
        reminder.setAdapter(new DropdownAdapter(this, android.R.layout.simple_spinner_item).setWhich(SPINNER_REMINDER));
        title = (EditText) findViewById(R.id.id_task_title_text);
        description = (EditText) findViewById(R.id.id_edit_description);

        if (edit){
            taskBean = CalendarResolver.getStaticInstance(this).getTaskById(taskId);
            if (taskBean == null)
                return;
            title.setText(taskBean.getTitle());
            description.setText(taskBean.getDescription());
            dateStart = taskBean.getStartDate();
            ((TextView) findViewById(R.id.id_edit_start_date)).setText(dateStart.getYear() + "-" + dateStart.getMonthString() + "-" + dateStart.getDayString());
            ((TextView) findViewById(R.id.id_edit_start_time)).setText(dateStart.getHourString()+":"+dateStart.getMinuteString());
            dateEnd = taskBean.getEndDate();
            if (dateEnd == null){
                dateEnd = dateStart;
            }
            ((TextView) findViewById(R.id.id_edit_end_date)).setText(dateEnd.getYear() +"-" + dateEnd.getMonthString() + "-" + dateEnd.getDayString());
            ((TextView) findViewById(R.id.id_edit_end_time)).setText(dateEnd.getHourString()+":"+dateEnd.getMinuteString());

        }
    }

    private TaskBean genTask(){
        String titleText = title.getText().toString();
        if (titleText == null || "".equalsIgnoreCase(titleText.trim())){
            Snackbar.make(title, "Title can not be empty!", Snackbar.LENGTH_SHORT).show();
            return null;
        }

        if (dateStart == null || dateEnd == null){
            Snackbar.make(title, "Please select start date & end date", Snackbar.LENGTH_SHORT).show();
            return null;
        }
        TaskBean task = new TaskBean();
        task.setTitle(title.getText().toString());
        task.setStartDate(TimeStampUtil.toUnixTimeStamp(dateStart));
        task.setEndDate(TimeStampUtil.toUnixTimeStamp(dateEnd));
        task.setDescription(description.getText().toString());
        RecurrenceUtil.populateRRule(task, repetition.getSelectedItemPosition());
        return task;
    }

    public class DatePickerTrigger implements View.OnClickListener{
        DateData dateData;
        int which;
        DatePickerTrigger(DateData dateData, int which){
            this.dateData = dateData;
            this.which = which;
        }
        @Override
        public void onClick(View v) {
            DatePickerDialog.newInstance(new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePickerDialog view, final int year, final int monthOfYear, final int dayOfMonth) {
                    Calendar now = Calendar.getInstance();
                    TimePickerDialog.newInstance(
                            new TimePickerDialog.OnTimeSetListener() {
                                 @Override
                                 public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
                                     dateData = new DateData(year, monthOfYear + 1, dayOfMonth);
                                     dateData.setHour(hourOfDay);
                                     dateData.setMinute(minute);
                                     String hourString = hourOfDay > 9 ? String.format("%d", hourOfDay) : String.format("0%d", hourOfDay);
                                     if (which == PICK_START){
                                         ((TextView) findViewById(R.id.id_edit_start_date))
                                                 .setText(new StringBuilder().append("").append(year).append("-").append(monthOfYear + 1).append("-").append(dayOfMonth).toString());
                                         ((TextView) findViewById(R.id.id_edit_start_time))
                                                 .setText(new StringBuilder().append("").append(hourString).append(":").append(minute).toString());
                                         TaskActivity.this.dateStart = dateData;
                                     } else {
                                         ((TextView) findViewById(R.id.id_edit_end_date))
                                                 .setText(new StringBuilder().append("").append(year).append("-").append(monthOfYear + 1).append("-").append(dayOfMonth).toString());
                                         ((TextView) findViewById(R.id.id_edit_end_time))
                                                 .setText(new StringBuilder().append("").append(hourString).append(":").append(minute).toString());
                                         TaskActivity.this.dateEnd = dateData;
                                     }
                                 }
                            }, now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE), true)
                            .show(getFragmentManager(), "TimePickerDialog");
                }
            },year, month - 1, day).show(getFragmentManager(), "DatePickerDialog");
        }
    }
}
