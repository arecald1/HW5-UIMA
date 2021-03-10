package com.cs250.joanne.myfragments;

import android.content.Context;
import android.os.Bundle;
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

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.snackbar.Snackbar;

import java.util.Collections;

public class DoneListFrag extends Fragment {
    public static final int MENU_ITEM_EDITVIEW = Menu.FIRST;
    public static final int MENU_ITEM_DELETE = Menu.FIRST + 1;
    public static final int MENU_ITEM_COPY = Menu.FIRST + 2;

    private ListView completedTask;
    private MainActivity myact;

    protected Fragment expandedTask;
    private FragmentTransaction transaction;

    Context cntx;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View myview = inflater.inflate(R.layout.list_frag, container, false);

        expandedTask = new expandedTaskFrag();

        myact = (MainActivity) getActivity();
        cntx = getActivity().getApplicationContext();

        // Get the ListView
        completedTask = (ListView) myview.findViewById(R.id.mylist);
        // connect listview to the array adapter in MainActivity (completedAdapter in MainActivity)
        completedTask.setAdapter(myact.completedAdapter);
        registerForContextMenu(completedTask); // Register this list for the context menu (long click functionality)
        // refresh view
        myact.completedAdapter.notifyDataSetChanged();

        // program a short click on the list item - mainly programming the snackbar
        completedTask.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Snackbar.make(view, "Selected #" + id, Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();

                Bundle bundle = new Bundle();
                bundle.putInt("taskPosition", position);
                bundle.putString("From", "DONE");
                expandedTask.setArguments(bundle);

                transaction = getFragmentManager().beginTransaction();                      //Add here to show expanded task card
                transaction.replace(R.id.fragment_container, expandedTask, "EXPANDTASK"); //send index as bundle and then use myacts.completedTasks to access
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
        // usually context menus do not change so it does not need to be made dynamically,
        // however, Joanne mentioned that she wanted a more programmatic example for us to see
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

                Toast.makeText(cntx, "edit request",
                        Toast.LENGTH_SHORT).show();
                return false;
            }
            case MENU_ITEM_DELETE: {
                myact.completedTasks.remove(index);
                Toast.makeText(cntx, "job " + index + " deleted",
                        Toast.LENGTH_SHORT).show();
                // refresh view
                myact.completedAdapter.notifyDataSetChanged();
                return true;
            }
            case MENU_ITEM_COPY: {
                Task copy = new Task(myact.completedTasks.get(index));
                myact.completedTasks.add(copy);
                // refresh view
                myact.completedAdapter.notifyDataSetChanged();
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

        myact.setActionBarTitle("Completed Tasks");
        Collections.sort(myact.completedTasks, new TaskComparator());
    }
}
