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
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import static android.content.ContentValues.TAG;

public class expandedTaskFrag extends Fragment {

    private TextView taskView;
    private TextView dueDate;
    private TextView categoryView;

    private Bundle bundle;
    private int taskPosition;

    private Button markComplete;

    private MainActivity myact;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        myact = (MainActivity) getActivity();
        bundle = this.getArguments();
        taskPosition = bundle.getInt("taskPosition");

        View view = inflater.inflate(R.layout.fragment_expanded_task_frag, container, false);

        taskView = (TextView) view.findViewById(R.id.expanded_task_name);
        dueDate = (TextView) view.findViewById(R.id.expanded_due_date);
        categoryView = (TextView) view.findViewById(R.id.expanded_task_category);

        final Task temp = myact.myTasks.get(taskPosition); //works

        String taskName = temp.getWhat();

        taskView.setText(taskName);
        dueDate.setText(temp.dateToString(temp.getDeadline()));
        categoryView.setText(temp.getCategory());

        markComplete = (Button) view.findViewById(R.id.mark_complete_btn); //mark task complete
        markComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myact.completedTasks.add(temp);
                myact.myTasks.remove(temp);

                taskView.setText("COMPLETE");   //REMOVE

                myact.aa.notifyDataSetChanged();
            }
        });

        // Inflate the layout for this fragment
        return view;
    }
}