package com.cs250.joanne.myfragments;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.LENGTH_LONG;

public class TaskFrag extends Fragment {

    private TextView taskView;
    private TextView categoryView;
    private TextView mDisplayDate;
    private DatePickerDialog.OnDateSetListener onDateSetListener;
    //private FragmentTransaction transaction;

    private Date curDate;

    private Button save;
    private Button cancel;
    private MainActivity myact;
    private Bundle editTaskInfo;

    public TaskFrag() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Currently gives user empty fields to create a new task
        // Inflate the layout for this fragment - store the view so we can return it at the end of the
        // function
        View view = inflater.inflate(R.layout.item_frag, container, false);

        myact = (MainActivity) getActivity();
        myact.getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.taskUpdateColor)));

        //setHasOptionsMenu(true);
        //this.setMenuVisibility(false);
        //myact.toolbar.setNavigationIcon(null);

        taskView = (EditText) view.findViewById(R.id.item_text);
        categoryView = (EditText) view.findViewById(R.id.task_category);

        // Set up date to produce an android date picker view
        mDisplayDate = (TextView) view.findViewById(R.id.task_date);
        mDisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(getContext(), android.R.style.Theme_Holo_Dialog_MinWidth,
                        onDateSetListener, year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                // we subtract -1900 because the Date constructor adds 1900 to the year
                curDate = new Date(year - 1900, month, dayOfMonth);

                mDisplayDate.setText(month + 1 + "/" + dayOfMonth + "/" + year);
            }
        };


        save = (Button) view.findViewById(R.id.add_btn);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Task myTask;

                // This gets rid of an open keyboard once the "save" button is selected
                View view = getActivity().getCurrentFocus();
                hideKeyboardFrom(getActivity().getApplicationContext(), view);

                // Check all fields here with if statement and provide toast with error, and keep them on this page
                if (taskView.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity().getApplicationContext(), "Task Name cannot be left empty", LENGTH_LONG).show();
                    return;
                } else if (curDate == null) {
                    Toast.makeText(getActivity().getApplicationContext(), "Deadline Date must be selected", LENGTH_LONG).show();
                    return;
                }

                // Check if category is blank and create object
                if (categoryView.getText().toString().equals("")) {
                    myTask = new Task(taskView.getText().toString(), new String("misc"), curDate);
                } else {
                    myTask = new Task(taskView.getText().toString(), categoryView.getText().toString(), curDate);
                }

                String toastOutput;
                Task updatedTask;

                // If this frag was triggered from using plus symbol in action bar, add task to myTasks
                // if it was triggered by trying to edit a task, make sure to update target task instead
                if (editTaskInfo.getString("From").equals("add")) {
                    // Then go from my activity to myTasks and add the new object to the list
                    myact.myTasks.add(myTask);
                    toastOutput = "Added item to current task list";
                } else {
                    // Get the correct task from the correct list
                    if (editTaskInfo.getString("From").equals("TODO")) {
                        updatedTask = myact.myTasks.get(editTaskInfo.getInt("position"));
                    } else {
                        updatedTask = myact.completedTasks.get(editTaskInfo.getInt("position"));
                    }

                    // Update the target task Object (this updates it in whatever view/list we show it in later
                    updatedTask.setWhat(myTask.getWhat());
                    updatedTask.setCategory(myTask.getCategory());
                    updatedTask.setDeadline(myTask.getDeadline());
                    toastOutput = "Task Updated";
                }

                // A toast then triggers everytime we add a new task
                Toast.makeText(getActivity().getApplicationContext(), toastOutput, LENGTH_SHORT).show();

                // This changes the fragment to the fragment that created this fragment
                getFragmentManager().popBackStack();
            }
        });


        cancel = (Button) view.findViewById(R.id.cancel_btn);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Task myTask;

                // this portion is to get rid of an open keyboard once the "save" button is selected
                View view = getActivity().getCurrentFocus();
                hideKeyboardFrom(getActivity().getApplicationContext(), view);

                // A toast then triggers every time we add a new item
                Toast.makeText(getActivity().getApplicationContext(), "operation canceled", LENGTH_SHORT).show();

                // This changes the fragment to the fragment that created this fragment
                getFragmentManager().popBackStack();
            }
        });

        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        myact.setActionBarTitle("Task Update");

        editTaskInfo = this.getArguments();

        // This gets the bundle sent from a fragment to see whether or not the fields
        // need to be filled in (because the user is trying to edit a task vs add a task)
        if (editTaskInfo.getString("From").equals("add")) {
            // resets the text fields when it is reloaded
            taskView.setText("");
            categoryView.setText("");
        } else {
            // Sets all the fields to match the task we are trying to edit
            taskView.setText(editTaskInfo.getString("name"));
            categoryView.setText(editTaskInfo.getString("category"));
            int year = editTaskInfo.getInt("year");
            int month = editTaskInfo.getInt("month");
            int day = editTaskInfo.getInt("day");
            mDisplayDate.setText(month + 1 + "/" + day + "/" + (year + 1900));
        }
    }

//    @Override
//    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
//
//        menu.findItem(R.id.item_frag).setVisible(false);
//        menu.findItem(R.id.todo_frag).setVisible(false);
//        menu.findItem(R.id.stats_frag).setVisible(false);
//
//        super.onCreateOptionsMenu(menu, inflater);
//    }

    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}
