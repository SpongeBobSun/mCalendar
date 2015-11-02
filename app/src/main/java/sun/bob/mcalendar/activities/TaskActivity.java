package sun.bob.mcalendar.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.Calendar;

import sun.bob.mcalendar.R;
import sun.bob.mcalendar.adapters.DropdownAdapter;
import sun.bob.mcalendarview.utils.CurrentCalendar;
import sun.bob.mcalendarview.vo.DateData;

public class TaskActivity extends AppCompatActivity {

    private final int PICK_START = 1;
    private final int PICK_END = 2;
    public static final int SPINNER_REPEAT = 3;
    public static final int SPINNER_REMINDER = 4;
    int year, month, day;
    DateData dateStart;
    DateData dateEnd;

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

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: 15/10/29 Save event
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        initUI();
    }

    private void initUI(){
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        findViewById(R.id.id_edit_start).setOnClickListener(new DatePickerTrigger(dateStart, PICK_START));
        findViewById(R.id.id_edit_end).setOnClickListener(new DatePickerTrigger(dateEnd, PICK_END));
        ((Spinner) findViewById(R.id.id_edit_repetition)).setAdapter(new DropdownAdapter(this, android.R.layout.simple_spinner_item).setWhich(SPINNER_REPEAT));
        ((Spinner) findViewById(R.id.id_edit_reminder)).setAdapter(new DropdownAdapter(this, android.R.layout.simple_spinner_item).setWhich(SPINNER_REMINDER));
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
                                     } else {
                                         ((TextView) findViewById(R.id.id_edit_end_date))
                                                 .setText(new StringBuilder().append("").append(year).append("-").append(monthOfYear + 1).append("-").append(dayOfMonth).toString());
                                         ((TextView) findViewById(R.id.id_edit_end_time))
                                                 .setText(new StringBuilder().append("").append(hourString).append(":").append(minute).toString());
                                     }
                                 }
                            }, now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE), true)
                            .show(getFragmentManager(), "TimePickerDialog");
                }
            },year, month - 1, day).show(getFragmentManager(), "DatePickerDialog");
        }
    }
}
