package sun.bob.mcalendar.adapters;

import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import sun.bob.mcalendar.beans.TaskBean;

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
        Snackbar.make(v, "Cicked on task id - " + taskBean.getId() + ", title: " + taskBean.getTitle(), Snackbar.LENGTH_LONG).show();
    }
}