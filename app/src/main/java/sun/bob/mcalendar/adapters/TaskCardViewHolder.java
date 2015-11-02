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

    TextView title, detail, start, end;

    public TaskCardViewHolder(View itemView) {
        super(itemView);
        title = (TextView) itemView.findViewById(R.id.id_card_task_title);
        detail = (TextView) itemView.findViewById(R.id.id_card_task_detail);
        start = (TextView) itemView.findViewById(R.id.id_task_card_start);
        end = (TextView) itemView.findViewById(R.id.id_task_card_end);
    }

    public TaskCardViewHolder populateWithDateData(DateData dateData){
        this.setStart(String.format("%s:%s", dateData.getHourString(), dateData.getMinuteString()));
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
