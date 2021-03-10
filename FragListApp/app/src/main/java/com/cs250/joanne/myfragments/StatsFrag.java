package com.cs250.joanne.myfragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class StatsFrag extends Fragment {

    private TextView viewDoneBy;
    private TextView viewDoneAfter;
    private TextView viewPastDue;
    private TextView viewToBeDone;
    private TextView viewTotal;

    private MainActivity mainAct;

    public StatsFrag() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stats, container, false);
        viewDoneBy = (TextView) view.findViewById(R.id.done_by_deadline);
        viewDoneAfter = (TextView) view.findViewById(R.id.done_after_deadline);
        viewPastDue = (TextView) view.findViewById(R.id.past_due);
        viewToBeDone = (TextView) view.findViewById(R.id.to_be_done);
        viewTotal = (TextView) view.findViewById(R.id.total_tasks);



        // Inflate the layout for this fragment
        return view;
    }
}