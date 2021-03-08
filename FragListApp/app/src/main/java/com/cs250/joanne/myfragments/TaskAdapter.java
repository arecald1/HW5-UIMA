package com.cs250.joanne.myfragments;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by joanne. - lets us connect the layout for our item with the data inside the item.
 * So basically its how we build our view. It seems like the controller between the Item class
 * and the item_layout XML file
 */
public class TaskAdapter extends ArrayAdapter<Task> {

    int resource;

    public TaskAdapter(Context ctx, int res, List<Task> tasks)
    {
        super(ctx, res, tasks);
        resource = res;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayout taskView;
        Task it = getItem(position);

        if (convertView == null) {
            taskView = new LinearLayout(getContext());
            String inflater = Context.LAYOUT_INFLATER_SERVICE;
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(inflater);
            vi.inflate(resource, taskView, true);
        } else {
            taskView = (LinearLayout) convertView;
        }

        // Now its like building a view for an activity

        TextView whatView = (TextView) taskView.findViewById(R.id.taskName);
        whatView.setText(it.getWhat());

        Format dateFormatter = new SimpleDateFormat("MM-dd-yy");
        TextView dateView = (TextView) taskView.findViewById(R.id.date);
        if (it.getCompleted()) {
            dateView.setText(dateFormatter.format(it.getCompleteDate()));
        } else {
            dateView.setText(dateFormatter.format(it.getDeadline()));
        }

        TextView categoryView = (TextView) taskView.findViewById(R.id.category);
        categoryView.setText(it.getCategory());


        return taskView;
    }

}
