package com.cs250.joanne.myfragments;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import static android.content.ContentValues.TAG;

public class expandedTaskFrag extends Fragment {

    private Task temp;

    private TextView taskView;
    private TextView dueDate;
    private TextView completeDate;
    private TextView categoryView;

    private TableRow completeView;

    private Bundle bundle;
    private int taskPosition;

    private Button markComplete;

    private MainActivity myact;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "expandedTaskFrag: Created");

        myact = (MainActivity) getActivity();
        bundle = this.getArguments();
        taskPosition = bundle.getInt("taskPosition");

        final View view = inflater.inflate(R.layout.fragment_expanded_task_frag, container, false);

        taskView = (TextView) view.findViewById(R.id.expanded_task_name); //link all views
        dueDate = (TextView) view.findViewById(R.id.expanded_due_date);
        categoryView = (TextView) view.findViewById(R.id.expanded_task_category);

        markComplete = (Button) view.findViewById(R.id.mark_complete_btn); //mark task complete

        if (bundle.getString("From").equals("TODO")) {
            temp = myact.myTasks.get(taskPosition); //works
        } else if (bundle.getString("From").equals("DONE")) {
            temp = myact.completedTasks.get(taskPosition);
        }


        String taskName = temp.getWhat(); //populate all textViews

        taskView.setText(taskName);
        dueDate.setText(temp.dateToString(temp.getDeadline()));
        categoryView.setText(temp.getCategory());

        if (temp.getCompleted()) { //if task is already completed
            completeDateRow(view);
            Log.d(TAG, "expandedTaskFrag: temp.getComplete() == true");
        } else {
            Log.d(TAG, "expandedTaskFrag: temp.getComplete() == false");
            markComplete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    temp.setComplete();
                    myact.completedTasks.add(temp);
                    myact.myTasks.remove(temp);

                    taskView.setText("COMPLETE");   //REMOVE
                    myact.aa.notifyDataSetChanged();

                    completeDateRow(view);
                    getFragmentManager().popBackStack();
                }
            });
        }
        // Inflate the layout for this fragment
        return view;
    }

    //if the task is already completed or if it is marked complete
    public void completeDateRow(View view) {
        completeView = (TableRow) view.findViewById(R.id.complete_row);
        completeView.setVisibility(View.VISIBLE);

        completeDate = (TextView) view.findViewById(R.id.expanded_complete_date);
        completeDate.setText(temp.dateToString(temp.getCompleteDate()));

        markComplete.setVisibility(View.GONE);

        Log.d(TAG, "completeDateRow: end of method");
    }
}