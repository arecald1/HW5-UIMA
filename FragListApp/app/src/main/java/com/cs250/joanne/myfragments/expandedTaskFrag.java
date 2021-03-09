package com.cs250.joanne.myfragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

public class expandedTaskFrag extends Fragment {

    private TextView taskView;
    private TextView dueDate;
    private TextView categoryView;

    private MainActivity myact;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        myact = (MainActivity) getActivity();

        View view = inflater.inflate(R.layout.fragment_expanded_task_frag, container, false);

        taskView = (TextView) view.findViewById(R.id.task_name);
        dueDate = (TextView) view.findViewById(R.id.task_date);
        categoryView = (TextView) view.findViewById(R.id.task_category);

        View taskView = inflater.inflate(R.layout.item_frag, container, false);


        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_expanded_task_frag, container, false);
    }
}