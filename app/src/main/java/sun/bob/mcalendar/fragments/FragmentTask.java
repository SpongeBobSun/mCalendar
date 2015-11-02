package sun.bob.mcalendar.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import sun.bob.mcalendar.R;

import sun.bob.mcalendar.adapters.DayAdapter;
import sun.bob.mcalendar.adapters.TaskAdapter;
import sun.bob.mcalendar.adapters.OnTaskClickListener;
import sun.bob.mcalendar.content.CalendarResolver;

/**
 * Created by bob.sun on 15/10/21.
 */
public class FragmentTask extends Fragment {
//    TaskAdapter taskAdapter;
    DayAdapter dayAdapter;
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){
        View ret = inflater.inflate(R.layout.fragment_task, null);
        RecyclerView recyclerView = (RecyclerView) ret.findViewById(R.id.id_task_list);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this.getActivity());
        recyclerView.setLayoutManager(layoutManager);
//        if (taskAdapter == null)
//            taskAdapter = new TaskAdapter(this.getActivity());
//        recyclerView.setAdapter(taskAdapter);
        if (dayAdapter == null){
            CalendarResolver.getStaticInstance(getActivity()).refreshAllEvents();
            dayAdapter = new DayAdapter(getActivity());
        }
        recyclerView.setAdapter(dayAdapter);
        return ret;
    }
}
