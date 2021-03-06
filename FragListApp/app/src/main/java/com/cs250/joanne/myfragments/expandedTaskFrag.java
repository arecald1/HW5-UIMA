package com.cs250.joanne.myfragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;

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
    private SharedPreferences expandedPrefs;
    private SharedPreferences.Editor expanedPeditor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "expandedTaskFrag: Created");


//        final View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_expanded_task_frag, null);
//        final PopupWindow popupWindow = new PopupWindow(view, WindowManager.LayoutParams.MATCH_PARENT,
//                WindowManager.LayoutParams.MATCH_PARENT);


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

                    myact.aa.notifyDataSetChanged();

                    //Check deadline vs completed date for stats
                    checkDeadlineComplete();

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

    public void checkDeadlineComplete() {
        expandedPrefs = getActivity().getPreferences(Context.MODE_PRIVATE);
        expanedPeditor = expandedPrefs.edit();

        SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
        int status = fmt.format(temp.getCompleteDate()).compareTo(fmt.format(temp.getDeadline()));
        Log.d(TAG, "checkDeadlineComplete: status: " + status +  "\nCompletionDate: " + temp.getCompleteDate() + "\ndeadine: " + temp.getDeadline());

        if (status < 0) {
            int doneAfter = expandedPrefs.getInt("doneAfterDeadline", 0);
            expanedPeditor.putInt("doneAfterDeadline", ++doneAfter);
            Log.d(TAG, "checkDeadlineComplete: doneBy: " + doneAfter);
        } else {
            int doneBy = expandedPrefs.getInt("doneByDeadline", 0);
            expanedPeditor.putInt("doneByDeadline", ++doneBy);
            Log.d(TAG, "checkDeadlineComplete: doneBy: " + doneBy);
        }
        expanedPeditor.apply();
    }
}