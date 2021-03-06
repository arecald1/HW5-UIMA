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
public class ItemAdapter extends ArrayAdapter<Item> {

    int resource;

    public ItemAdapter(Context ctx, int res, List<Item> items)
    {
        super(ctx, res, items);
        resource = res;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayout itemView;
        Item it = getItem(position);

        if (convertView == null) {
            itemView = new LinearLayout(getContext());
            String inflater = Context.LAYOUT_INFLATER_SERVICE;
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(inflater);
            vi.inflate(resource, itemView, true);
        } else {
            itemView = (LinearLayout) convertView;
        }

        // Now its like building a view for an activity

        TextView whatView = (TextView) itemView.findViewById(R.id.taskName);
        whatView.setText(it.getWhat());

        Format dateFormatter = new SimpleDateFormat("MM-dd-yy");
        TextView dateView = (TextView) itemView.findViewById(R.id.date);
        if (it.getCompleted()) {
            dateView.setText(dateFormatter.format(it.getCompleteDate()));
        } else {
            dateView.setText(dateFormatter.format(it.getDeadline()));
        }

        TextView categoryView = (TextView) itemView.findViewById(R.id.category);
        categoryView.setText(it.getCategory());


        return itemView;
    }

}
