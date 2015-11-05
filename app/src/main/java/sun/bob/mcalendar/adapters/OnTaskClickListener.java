package sun.bob.mcalendar.adapters;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import sun.bob.mcalendar.activities.TaskActivity;
import sun.bob.mcalendar.beans.TaskBean;
import sun.bob.mcalendarview.utils.CurrentCalendar;

/**
 * Created by bob.sun on 15/10/27.
 */
public class OnTaskClickListener implements RecyclerView.OnClickListener{

    private RecyclerView recyclerView;

    public OnTaskClickListener(RecyclerView recyclerView){
        this.recyclerView = recyclerView;
    }

    @Override
    public void onClick(View v) {
        TaskBean taskBean = ((TaskAdapter) recyclerView.getAdapter()).getTasks().get(recyclerView.getChildLayoutPosition(v));
        Intent newTaskIntent = new Intent(v.getContext(), TaskActivity.class);
        newTaskIntent.putExtra("year", CurrentCalendar.getCurrentDateData().getYear());
        newTaskIntent.putExtra("month", CurrentCalendar.getCurrentDateData().getMonth());
        newTaskIntent.putExtra("day", CurrentCalendar.getCurrentDateData().getDay());
        newTaskIntent.putExtra("taskId", Long.parseLong(taskBean.getId()));
        newTaskIntent.putExtra("edit", true);
        v.getContext().startActivity(newTaskIntent);
//        Snackbar.make(v, "Cicked on task id - " + taskBean.getId() + ", title: " + taskBean.getTitle(), Snackbar.LENGTH_LONG).show();
    }
}