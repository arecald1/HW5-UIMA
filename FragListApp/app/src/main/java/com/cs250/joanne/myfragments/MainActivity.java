package com.cs250.joanne.myfragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.google.android.material.navigation.NavigationView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    // These are all here for demonstration purposes but we probably only need the ArrayList in main(?)
    protected Fragment task;
    protected Fragment list;
    protected Fragment doneList;
    protected Fragment statsPage;

    private FragmentTransaction transaction;

    protected TaskAdapter aa;
    protected TaskAdapter completedAdapter;
    protected ArrayList<Task> myTasks;
    protected ArrayList<Task> completedTasks; // Protected so it has package access (our fragments can access it as well)

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setTitle("MAIN ACTIVITY:");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // create ArrayList of Tasks
        myTasks = new ArrayList<Task>();
        completedTasks = new ArrayList<Task>();
        createDefaultTaskLists();
        // make array adapter to bind arraylist to listview with custom Task layout
        // we connect them here but we don't actually do anything past this here (because the main
        // interaction happens in our fragments
        aa = new TaskAdapter(this, R.layout.item_layout, myTasks);
        completedAdapter = new TaskAdapter(this, R.layout.item_layout, completedTasks);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        // Task and list are created from their respective constructors here
        task = new TaskFrag();
        list = new TodoListFrag();
        doneList = new DoneListFrag();
        statsPage = new StatsFrag();

        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, list, "TODO").commit();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds Tasks to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        //task.setHasOptionsMenu(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        // if the user clicked on the plus, take them to add new task screen
        if (id == R.id.add_task) {
            Bundle add = new Bundle();
            add.putString("From", "add");
            task.setArguments(add);

            transaction = getSupportFragmentManager().beginTransaction();

            // Replace whatever is in the fragment_container view with this fragment,
            // and add the transaction to the back stack so the user can navigate back
            transaction.replace(R.id.fragment_container, this.task);
            transaction.addToBackStack(null);

            // Commit the transaction
            transaction.commit();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.todo_frag) {
            transaction = getSupportFragmentManager().beginTransaction();

            // Replace whatever is in the fragment_container view with this fragment,
            // and add the transaction to the back stack so the user can navigate back
            transaction.replace(R.id.fragment_container, list, "TODO");
            transaction.addToBackStack("TODO");

            // Commit the transaction
            transaction.commit();

        } else if (id == R.id.done_frag) {
            transaction = getSupportFragmentManager().beginTransaction();

            // Replace whatever is in the fragment_container view with this fragment,
            // and add the transaction to the back stack so the user can navigate back
            transaction.replace(R.id.fragment_container, doneList, "DONE");
            transaction.addToBackStack("DONE");

            // Commit the transaction
            transaction.commit();
        } else if (id == R.id.stats_frag) {
            transaction = getSupportFragmentManager().beginTransaction();

            transaction.replace(R.id.fragment_container, statsPage, "STATS");
            transaction.addToBackStack("STATS");
            transaction.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    // Creates a default list of tasks in the TODO and DONE page
    private void createDefaultTaskLists() {
        // year 121 = 2021 because for some reason, the constructor add 1900 to the year, thus 123 = 2023
        myTasks.add(new Task("task 1", "earliest", new Date(121, 1, 11)));
        myTasks.add(new Task("task 3", "latest", new Date(123, 2, 2)));
        myTasks.add(new Task("task 2", "misc", new Date()));

        Task completed = new Task("task 0", "started complete", new Date(2017, 7, 28));
        completed.setComplete();
        completedTasks.add(completed);

        Collections.sort(myTasks, new TaskComparator());
    }

}
