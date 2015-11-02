package sun.bob.mcalendar.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import sun.bob.mcalendar.R;
import sun.bob.mcalendar.beans.TaskBean;
import sun.bob.mcalendarview.vo.DateData;

/**
 * Created by bob.sun on 15/10/22.
 */
public class TaskCardViewHolder extends RecyclerView.ViewHolder {

    TextView day, month, title, detail, start, end;

    public TaskCardViewHolder(View itemView, boolean singleDay) {
        super(itemView);
        title = (TextView) itemView.findViewById(R.id.id_card_task_title);
        detail = (TextView) itemView.findViewById(R.id.id_card_task_detail);
        start = (TextView) itemView.findViewById(R.id.id_task_card_start);
        end = (TextView) itemView.findViewById(R.id.id_task_card_end);
        day = (TextView) itemView.findViewById(R.id.id_task_card_day);
        month = (TextView) itemView.findViewById(R.id.id_task_card_month);
    }

    public TaskCardViewHolder populateWithDateData(DateData dateData){
        this.setDay(dateData.getDay())
                .setMonth(dateData.getMonth())
                .setStart(String.format("%s:%s", dateData.getHourString(), dateData.getMinuteString()));
        return this;
    }


    public TaskCardViewHolder setDay(int day) {
        this.day.setText(String.format("%d", day));
        return this;
    }

    public TaskCardViewHolder setMonth(int month) {
        this.month.setText(String.format("%d", month));
        return this;
    }

    public TaskCardViewHolder setTitle(String title) {
        this.title.setText(title);
        return this;
    }

    public TaskCardViewHolder setDetail(String detail) {
        this.detail.setText(detail);
        return this;
    }

    public TaskCardViewHolder setStart(String start) {
        this.start.setText(start);
        return this;
    }

    public TaskCardViewHolder setEnd(String end) {
        this.end.setText(end);
        return this;
    }
}
