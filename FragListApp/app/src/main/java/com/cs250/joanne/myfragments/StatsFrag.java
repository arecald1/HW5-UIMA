package com.cs250.joanne.myfragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static android.content.ContentValues.TAG;


public class StatsFrag extends Fragment {

    private TextView viewDoneBy;
    private TextView viewDoneAfter;
    private TextView viewPastDue;
    private TextView viewToBeDone;
    private TextView viewTotal;

    SharedPreferences statsPref;
    SharedPreferences.Editor statsPeditor;

    int numBefore;
    int numAfter;
    int numPastDue;
    int numToBeDone;

    private MainActivity myact;

    public StatsFrag() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stats, container, false);


        myact = (MainActivity) getActivity();
        myact.getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.statsColor)));

        viewDoneBy = (TextView) view.findViewById(R.id.done_by_deadline);
        viewDoneAfter = (TextView) view.findViewById(R.id.done_after_deadline);
        viewPastDue = (TextView) view.findViewById(R.id.past_due);
        viewToBeDone = (TextView) view.findViewById(R.id.to_be_done);
        viewTotal = (TextView) view.findViewById(R.id.total_tasks);

        statsPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        statsPeditor = statsPref.edit();

        numBefore = statsPref.getInt("doneByDeadline", 1); //needs to be 1 because of default completed task
        numAfter = statsPref.getInt("doneAfterDeadline", 0);

        viewDoneBy.setText("" + numBefore);
        viewDoneAfter.setText("" + numAfter);

        checkPastActive();
        viewPastDue.setText(statsPref.getInt("pastDue", 0) + "");
        viewToBeDone.setText(statsPref.getInt("toBeDone", 0) + "");
        viewTotal.setText(getTotal());

        // Inflate the layout for this fragment
        return view;
    }

    private String getTotal() {
        int active = myact.myTasks.size();
        int complete = numBefore + numAfter;
        return (active + complete) + "";
    }

    private void checkPastActive() {
        numPastDue = 0;
        numToBeDone = 0;
        int status = 0;
        SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
        String curDate = fmt.format(System.currentTimeMillis());

        Log.d(TAG, "checkPastActive: myTasks.Size(): " + myact.myTasks.size());

        for (Task temp : myact.myTasks) {
            status = fmt.format(temp.getDeadline()).compareTo(curDate);
            Log.d(TAG, "checkDeadlineComplete: status: " + status +  "\nCompletionDate: " + temp.getCompleteDate() + "\ncurrentTime: " + curDate);

            if (status < 0) {
                numPastDue++;
            } else {
                numToBeDone++;
            }
        }
        statsPeditor.putInt("pastDue", numPastDue);
        statsPeditor.putInt("toBeDone", numToBeDone);
        statsPeditor.apply();
    }
}