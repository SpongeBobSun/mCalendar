package sun.bob.mcalendar.adapters;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import sun.bob.mcalendar.R;
import sun.bob.mcalendar.activities.TaskActivity;
import sun.bob.mcalendar.beans.TaskBean;
import sun.bob.mcalendar.constants.Constants;
import sun.bob.mcalendarview.utils.CurrentCalendar;

/**
 * Created by bob.sun on 15/11/2.
 */
public class DayCardViewHolder extends RecyclerView.ViewHolder {
    private TextView day, month;
    private LinearLayout container;
    public DayCardViewHolder(View itemView) {
        super(itemView);
        day = (TextView) itemView.findViewById(R.id.id_task_card_day);
        month = (TextView) itemView.findViewById(R.id.id_task_card_month);
        container = (LinearLayout) itemView.findViewById(R.id.id_task_container);
    }

    public DayCardViewHolder populate(ArrayList<TaskBean> tasks){
        container.removeAllViews();
        View toAdd;
        for(TaskBean task : tasks){
            toAdd = LayoutInflater.from(container.getContext()).inflate(R.layout.layout_task_content, container, false);
            ((TextView) toAdd.findViewById(R.id.id_card_task_title)).setText(task.getTitle());
            ((TextView) toAdd.findViewById(R.id.id_card_task_detail)).setText(task.getDescription());
            ((TextView) toAdd.findViewById(R.id.id_task_card_start)).setText(new StringBuilder().append(task.getStartDate().getHourString()).append(":").append(task.getStartDate().getMinuteString()).toString());
            ((TextView) toAdd.findViewById(R.id.id_task_card_end)).setText(new StringBuilder().append(task.getEndDate().getHourString()).append(":").append(task.getEndDate().getMinuteString()).toString());
            toAdd.setOnClickListener(new OnTaskClickedListener(task.getId()));
            toAdd.setOnLongClickListener(new OnTaskLongClickedListenner(task.getId()));
            container.addView(toAdd);
        }
        day.setText(tasks.get(0).getStartDate().getDayString());
        month.setText(Constants.MONTH_TEXT[tasks.get(0).getStartDate().getMonth()]);
        return this;
    }

    class OnTaskClickedListener implements View.OnClickListener{
        String id;
        public OnTaskClickedListener(String id){
            this.id = id;
        }

        @Override
        public void onClick(View v) {
            Intent newTaskIntent = new Intent(v.getContext(), TaskActivity.class);
            newTaskIntent.putExtra("year", CurrentCalendar.getCurrentDateData().getYear());
            newTaskIntent.putExtra("month", CurrentCalendar.getCurrentDateData().getMonth());
            newTaskIntent.putExtra("day", CurrentCalendar.getCurrentDateData().getDay());
            newTaskIntent.putExtra("taskId", Long.parseLong(id));
            newTaskIntent.putExtra("edit", true);
            v.getContext().startActivity(newTaskIntent);
//            Snackbar.make(v, "Event Id: " + id, Snackbar.LENGTH_LONG).show();
        }
    }

    class OnTaskLongClickedListenner implements View.OnLongClickListener{
        String id;
        public OnTaskLongClickedListenner(String id){
            this.id = id;
        }

        @Override
        public boolean onLongClick(View v) {

            return false;
        }
    }
}
