package com.cs250.joanne.myfragments;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import static android.widget.Toast.LENGTH_SHORT;

public class ItemFrag extends Fragment {

    private TextView tv;
    private Button btn;
    private MainActivity myact;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment - store the view so we can return it at the end of the
        // function
        View view = inflater.inflate(R.layout.item_frag, container, false);

        myact = (MainActivity) getActivity();

        tv = (EditText) view.findViewById(R.id.item_text);
        btn = (Button) view.findViewById(R.id.add_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // make object
                Item myitem = new Item(tv.getText().toString());
                //Then go from my activity to myItems and add the new object to the list
                myact.myItems.add(myitem);
                // A toast then triggers everytime we add a new item
                Toast.makeText(getActivity().getApplicationContext(), "added item", LENGTH_SHORT).show();
            }
        });

        return view;
    }


}
