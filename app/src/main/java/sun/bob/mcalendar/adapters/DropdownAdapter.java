package sun.bob.mcalendar.adapters;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import sun.bob.mcalendar.activities.TaskActivity;
import sun.bob.mcalendar.constants.Constants;

/**
 * Created by bob.sun on 15/10/30.
 */
public class DropdownAdapter extends ArrayAdapter {

    private AppCompatActivity context;
    private int which;
    private List<String> list;

    public DropdownAdapter(Context context, int resource) {
        super(context, resource);
        this.context = (AppCompatActivity) context;
    }

    public DropdownAdapter setWhich(int which){
        this.which = which;
        if (which == TaskActivity.SPINNER_REPEAT){
            list = Constants.REPEAT_LIST;
        } else {
            list = Constants.REMINDER_LIST;
        }
        return this;
    }

    public View getView(int position, View convertView, ViewGroup parent){
        TextView ret = new TextView(context);
        ret.setText(list.get(position));
        return ret;
    }

    public int getCount(){
        return list.size();
    }

    public Object getItem(int position){
        return list.get(position);
    }
}

