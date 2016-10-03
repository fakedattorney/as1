package com.assignment1.cjiang2_habittracker;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    public static final String FILENAME = "habitFile.sav";
    private static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");
    private static Date currentDate;

    public static HabitHolder homeHabitHolder = new HabitHolder();

    private static ListView todayHabitsListView;
    private static TextView ifNoHabitsText;

    private static ArrayAdapter<Habit> adapter;

    private static ArrayList<Habit> todayHabitList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle(R.string.today);

        todayHabitsListView = (ListView) findViewById(R.id.habitsList);
        ifNoHabitsText = (TextView)findViewById(R.id.ifNoHabits);
        FloatingActionButton addFab = (FloatingActionButton) findViewById(R.id.add);

        addFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, AddHabits.class));
            }
        });

        // initialize datas
        currentDate = new Date();
        homeHabitHolder.clearHabitList();
        openRecordFile();
        todayHabitList = homeHabitHolder.getToCompleteHabitList();

        // setting adapter
        adapter = new ArrayAdapter<Habit>(this,
                R.layout.list_items, todayHabitList);
        todayHabitsListView.setAdapter(adapter);

        todayHabitsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Habit currentHabit = homeHabitHolder.getHabitFromToComplete(position);
                listItemAlertDialog(currentHabit);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        currentDate = new Date();
        todayHabitList = homeHabitHolder.getToCompleteHabitList();
        ifNoHabits();
        adapter.notifyDataSetChanged();
    }

    public static void ifNoHabits(){
        // show if no habit on middle of screen
        if(homeHabitHolder.getToCompleteHabitList().size() == 0){
            ifNoHabitsText.setText("No habit to show for today.");
        }
        else{
            ifNoHabitsText.setText("");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // menu
        int id = item.getItemId();

        if (id == R.id.clear_all) {
            clearAlertDialog();
            return true;
        } else if (id == R.id.all) {
            startActivity(new Intent(MainActivity.this, allHabits.class));
        }

        return super.onOptionsItemSelected(item);
    }

    private void openRecordFile() {
        try {
            FileInputStream fis = openFileInput(FILENAME);
            homeHabitHolder.loadHabitFile(fis, currentDate);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
        }
    }

    private void clearAlertDialog() {
        // clear all dialog
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Clear all habits?");
        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                try {
                    FileOutputStream fos = openFileOutput(MainActivity.FILENAME,
                            Context.MODE_PRIVATE);
                    homeHabitHolder.clearHabitFile(fos);
                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                }
                ifNoHabits();
                adapter.notifyDataSetChanged();
            }
        });
        alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated catch block
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void listItemAlertDialog(final Habit currentHabit) {
        // dialog show detail of habit when selected
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(currentHabit.getHabit());
        alertDialogBuilder.setMessage(
                "\nDate: "+currentHabit.getDateString()+
                "\nRecurrence: "+currentHabit.getHabitRecurrenceInString()+
                "\nRecent Completed Date: "+currentHabit.getCompletedDate()+
                "\nCount: "+Integer.toString(currentHabit.getCount()));
        alertDialogBuilder.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                // TODO Auto-generated catch block
            }
        });
        alertDialogBuilder.setNegativeButton("Complete",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                // TODO Auto-generated catch block
                currentDate = new Date();
                homeHabitHolder.completeHabit(currentHabit, dateFormat.format(currentDate));

                adapter.notifyDataSetChanged();
                try {
                    FileOutputStream fos = openFileOutput(MainActivity.FILENAME,
                            Context.MODE_PRIVATE);
                    homeHabitHolder.saveHabitFile(fos);

                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

}
