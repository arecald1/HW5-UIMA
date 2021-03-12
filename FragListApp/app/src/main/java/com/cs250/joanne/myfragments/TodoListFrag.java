package com.cs250.joanne.myfragments;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Collections;


public class TodoListFrag extends Fragment {

    public static final int MENU_ITEM_EDITVIEW = Menu.FIRST;
    public static final int MENU_ITEM_DELETE = Menu.FIRST + 1;
    public static final int MENU_ITEM_COPY = Menu.FIRST + 2;

    private ListView myTask;
    private MainActivity myact;

    protected Fragment expandedTask;
    protected Fragment editTask;
    private FragmentTransaction transaction;

    Context cntx;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View myview = inflater.inflate(R.layout.list_frag, container, false);
        ((MainActivity) getActivity()).setActionBarTitle("Current Tasks");

        expandedTask = new expandedTaskFrag();

        myact = (MainActivity) getActivity();
        cntx = getActivity().getApplicationContext();

        myact.getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorPrimary)));

        // Get the ListView
        myTask = (ListView) myview.findViewById(R.id.mylist);
        // connect listview to the array adapter in MainActivity (aa in MainActivity)
        myTask.setAdapter(myact.aa);
        registerForContextMenu(myTask); // Register this list for the context menu (long click functionality)
        // refresh view
        myact.aa.notifyDataSetChanged();

        // program a short click on the list item - mainly programming the snackbar
        myTask.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Snackbar.make(view, "Selected Task " + (id + 1), Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();

                Bundle bundle = new Bundle();
                bundle.putInt("taskPosition", position);
                bundle.putString("From", "TODO");
                expandedTask.setArguments(bundle);

                transaction = getFragmentManager().beginTransaction();                      //Add here to show expanded task card
                transaction.replace(R.id.fragment_container, expandedTask, "EXPANDTASK"); //send index as bundle and then use myacts.myTasks to access
                transaction.addToBackStack("EXPANDTASK");
                transaction.commit();
            }
        });

        return myview;
    }

    // for a long click on a menu item use ContextMenu
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        // create menu in code instead of in xml file (xml approach preferred)
        menu.setHeaderTitle("Select Item");

        // Add menu items
        menu.add(0, MENU_ITEM_EDITVIEW, 0, R.string.menu_editview);
        menu.add(0, MENU_ITEM_DELETE, 0, R.string.menu_delete);
        menu.add(0, MENU_ITEM_COPY, 0, "Copy");

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        super.onContextItemSelected(item);

        AdapterView.AdapterContextMenuInfo menuInfo;
        menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int index = menuInfo.position; // position in array adapter (and in our list

        switch (item.getItemId()) {
            case MENU_ITEM_EDITVIEW: {

                Toast.makeText(cntx, "edit task " + (index + 1),
                        Toast.LENGTH_SHORT).show();


                // Get target task we need to edit
                Task taskOut = myact.myTasks.get(index);

                // Send the task fragment all the information of the selected task as well as the fragment its coming from
                Bundle toUpdateTask = new Bundle();
                toUpdateTask.putString("name", taskOut.getWhat());
                toUpdateTask.putString("category", taskOut.getCategory());
                toUpdateTask.putInt("year", taskOut.getDeadline().getYear());
                toUpdateTask.putInt("month", taskOut.getDeadline().getMonth());
                toUpdateTask.putInt("day", taskOut.getDeadline().getDate());
                toUpdateTask.putInt("position", index);
                toUpdateTask.putString("From", "TODO");

                // Attach this bundle to the target fragment we are calling
                myact.task.setArguments(toUpdateTask);

                // Switch to the edit task fragment
                transaction = myact.getSupportFragmentManager().beginTransaction();

                // Replace whatever is in the fragment_container view with this fragment,
                // and add the transaction to the back stack so the user can navigate back
                transaction.replace(R.id.fragment_container, myact.task, "UPDATE");
                transaction.addToBackStack("UPDATE");

                // Commit the transaction
                transaction.commit();

                return false;
            }
            case MENU_ITEM_DELETE: {
                myact.myTasks.remove(index);
                Toast.makeText(cntx, "task " + (index  + 1) + " deleted",
                        Toast.LENGTH_SHORT).show();
                // refresh view
                myact.aa.notifyDataSetChanged();
                return true;
            }
            case MENU_ITEM_COPY: {
                Task copy = new Task(myact.myTasks.get(index));
                myact.myTasks.add(copy);
                // make sure to sort the list after adding the copy
                Collections.sort(myact.myTasks, new TaskComparator());
                //toast
                Toast.makeText(cntx, "task " + (index  + 1) + " copied",
                        Toast.LENGTH_SHORT).show();
                // refresh view
                myact.aa.notifyDataSetChanged();
                return false;
            }
        }
        return false;
    }


    // Called at the start of the visible lifetime.
    @Override
    public void onStart(){
        super.onStart();
        Log.d ("Other Fragment2", "onStart");
        // Apply any required UI change now that the Fragment is visible.
    }

    // Called at the start of the active lifetime.
    @Override
    public void onResume(){
        super.onResume();
        Log.d ("Other Fragment", "onResume");
        // Resume any paused UI updates, threads, or processes required
        // by the Fragment but suspended when it became inactive.

        myact.setActionBarTitle("Current Tasks");
        Collections.sort(myact.myTasks, new TaskComparator());
    }

    // Called at the end of the active lifetime.
    @Override
    public void onPause(){
        Log.d ("Other Fragment", "onPause");
        // Suspend UI updates, threads, or CPU intensive processes
        // that don't need to be updated when the Activity isn't
        // the active foreground activity.
        // Persist all edits or state changes
        // as after this call the process is likely to be killed.
        super.onPause();
    }

    // Called to save UI state changes at the
    // end of the active lifecycle.
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        Log.d ("Other Fragment", "onSaveInstanceState");
        // Save UI state changes to the savedInstanceState.
        // This bundle will be passed to onCreate, onCreateView, and
        // onCreateView if the parent Activity is killed and restarted.
        super.onSaveInstanceState(savedInstanceState);
    }

    // Called at the end of the visible lifetime.
    @Override
    public void onStop(){
        Log.d ("Other Fragment", "onStop");
        // Suspend remaining UI updates, threads, or processing
        // that aren't required when the Fragment isn't visible.
        super.onStop();
    }

    // Called when the Fragment's View has been detached.
    @Override
    public void onDestroyView() {
        Log.d ("Other Fragment", "onDestroyView");
        // Clean up resources related to the View.
        super.onDestroyView();
    }

    // Called at the end of the full lifetime.
    @Override
    public void onDestroy(){
        Log.d ("Other Fragment", "onDestroy");
        // Clean up any resources including ending threads,
        // closing database connections etc.
        super.onDestroy();
    }

    // Called when the Fragment has been detached from its parent Activity.
    @Override
    public void onDetach() {
        Log.d ("Other Fragment", "onDetach");
        super.onDetach();
    }
}
