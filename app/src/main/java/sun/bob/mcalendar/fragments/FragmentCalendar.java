package sun.bob.mcalendar.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Outline;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;

import sun.bob.mcalendar.activities.MainActivity;
import sun.bob.mcalendar.R;
import sun.bob.mcalendar.activities.TaskActivity;
import sun.bob.mcalendar.adapters.TaskAdapter;
import sun.bob.mcalendar.beans.TaskBean;
import sun.bob.mcalendar.constants.ClickedBackground;
import sun.bob.mcalendar.content.CalendarResolver;
import sun.bob.mcalendarview.MarkStyle;
import sun.bob.mcalendarview.listeners.OnDateClickListener;
import sun.bob.mcalendarview.listeners.OnMonthChangeListener;
import sun.bob.mcalendarview.mCalendarView;
import sun.bob.mcalendarview.utils.CurrentCalendar;
import sun.bob.mcalendarview.vo.DateData;
import sun.bob.mcalendarview.vo.MarkedDates;

/**
 * Created by bob.sun on 15/10/21.
 */
public class FragmentCalendar extends Fragment {
    RecyclerView recyclerView;
    mCalendarView calendarView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){
        View ret = inflater.inflate(R.layout.fragment_calendar, null);
        calendarView =
                ((mCalendarView) ret.findViewById(R.id.id_mcalendarview))
                .hasTitle(false)
                .setMarkedStyle(MarkStyle.DOT, Color.rgb(63, 81, 181))
                .setOnMonthChangeListener(new MonthChangeListener())
                .setOnDateClickListener(new DateClickListener());
        recyclerView = (RecyclerView) ret.findViewById(R.id.id_calendar_task);
        recyclerView.setAdapter(new TaskAdapter(getActivity(), CurrentCalendar.getCurrentDateData()));
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return ret;
    }

    class MonthChangeListener extends OnMonthChangeListener {

        @Override
        public void onMonthChange(int year, int month) {
            ((MainActivity) getActivity()).toolbar.setTitle(new StringBuilder().append("").append(year).append(" - ").append(month).toString());
//            for(TaskBean taskBean : CalendarResolver.getStaticInstance(getActivity()).getEventsByMonth(year, month)){
//                MarkedDates.getInstance().add(taskBean.getStartDate());
//            }
        }
    }
    class DateClickListener extends OnDateClickListener{

        private View lastClicked;
        private DateData lastClickedDate;

        @Override
        public void onDateClick(View view, DateData date) {
//            Intent newTaskIntent = new Intent(getActivity(), TaskActivity.class);
//            newTaskIntent.putExtra("year", date.getYear());
//            newTaskIntent.putExtra("month", date.getMonth());
//            newTaskIntent.putExtra("day", date.getDay());
//            getActivity().startActivity(newTaskIntent);
            if (lastClicked != null){
                if (lastClickedDate.equals(CurrentCalendar.getCurrentDateData())){
                    lastClicked.setBackground(MarkStyle.todayBackground);
                } else {
                    lastClicked.setBackground(null);
                }
            }
            view.setBackground(ClickedBackground.background);
            lastClicked = view;
            lastClickedDate = date;
            ((TaskAdapter) recyclerView.getAdapter()).changeDate(date);
        }
    }
}
