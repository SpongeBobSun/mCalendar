package sun.bob.mcalendar.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import sun.bob.mcalendar.R;
import sun.bob.mcalendar.beans.TaskBean;
import sun.bob.mcalendar.content.CalendarResolver;

/**
 * Created by bob.sun on 15/11/2.
 */
public class DayAdapter extends RecyclerView.Adapter<DayCardViewHolder> {

    private ArrayList<ArrayList<TaskBean>> days;
    private Context context;

    public DayAdapter(Context context){
        this.context = context;
        days = CalendarResolver.getStaticInstance(context).getAllTasksGroupByDay();
    }

    @Override
    public DayCardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new DayCardViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_day_card, parent, false));
    }

    @Override
    public void onBindViewHolder(DayCardViewHolder holder, int position) {
        holder.populate(days.get(position));
    }

    @Override
    public int getItemCount() {
        return days.size();
    }
}
