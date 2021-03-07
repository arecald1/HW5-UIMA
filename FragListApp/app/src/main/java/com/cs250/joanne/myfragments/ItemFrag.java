package com.cs250.joanne.myfragments;


import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

import static android.widget.Toast.LENGTH_SHORT;

public class ItemFrag extends Fragment {

    private TextView taskView;
    private TextView categoryView;
    private TextView mDisplayDate;
    private DatePickerDialog.OnDateSetListener onDateSetListener;

    private Date curDate;

    private Button btn;
    private MainActivity myact;

    public ItemFrag() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment - store the view so we can return it at the end of the
        // function
        View view = inflater.inflate(R.layout.item_frag, container, false);

        myact = (MainActivity) getActivity();

        taskView = (EditText) view.findViewById(R.id.item_text);
        categoryView = (EditText) view.findViewById(R.id.task_category);

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
                curDate = new Date(year, month, dayOfMonth);
                mDisplayDate.setText(month + 1 + "/" + dayOfMonth + "/" + year);
            }
        };


        btn = (Button) view.findViewById(R.id.add_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // make object
                Item myitem = new Item(taskView.getText().toString(), categoryView.getText().toString(), curDate);
                //Then go from my activity to myItems and add the new object to the list
                myact.myItems.add(myitem);
                // A toast then triggers everytime we add a new item
                Toast.makeText(getActivity().getApplicationContext(), "added item", LENGTH_SHORT).show();
            }
        });

        return view;
    }


}
