package com.assignment1.cjiang2_habittracker;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class allHabits extends AppCompatActivity {

    private static ListView allHabitsListView;
    private static TextView ifNoHabitsText;

    private static ArrayAdapter<Habit> adapter;

    private static ArrayList<Habit> allHabitList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_habits);

        setTitle(R.string.all_habits);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        allHabitsListView = (ListView) findViewById(R.id.habitsList);
        ifNoHabitsText = (TextView)findViewById(R.id.ifNoHabits);

        // initialize datas
        allHabitList = MainActivity.homeHabitHolder.getHabitList();

        // setting adapter
        adapter = new ArrayAdapter<Habit>(this,
                R.layout.list_items, allHabitList);
        allHabitsListView.setAdapter(adapter);

        allHabitsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Habit currentHabit = MainActivity.homeHabitHolder.getHabitFromAll(position);
                listItemAlertDialog(currentHabit);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        allHabitList = MainActivity.homeHabitHolder.getHabitList();
        ifNoHabits();
        adapter.notifyDataSetChanged();
    }

    public static void ifNoHabits(){
        if(MainActivity.homeHabitHolder.getHabitList().size() == 0){
            ifNoHabitsText.setText("No habit recorded.");
        }
        else{
            ifNoHabitsText.setText("");
        }
    }

    private void listItemAlertDialog(final Habit currentHabit) {
        final ArrayList<String> toDeleteCompletedDate = new ArrayList<String>();
        final ArrayList<String> allCompletedDateArrayList = currentHabit.getAllCompletedDate();
        final String array[] = new String[allCompletedDateArrayList.size()-1];
        for(int i =0; i < allCompletedDateArrayList.size()-1; i++){
            array[i] = allCompletedDateArrayList.get(i+1).replace("="," ");
        }
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(currentHabit.getHabit()+" - "+Integer.toString(currentHabit.getCount())+" Complete");
        alertDialogBuilder.setMultiChoiceItems(array, null, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                if (isChecked) {
                    toDeleteCompletedDate.add(array[which]);
                } else if (toDeleteCompletedDate.contains(array[which])) {
                    toDeleteCompletedDate.remove(array[which]);
                }
            }
        });
        alertDialogBuilder.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                // TODO Auto-generated catch block
            }
        });

        alertDialogBuilder.setNegativeButton("Delete Habit",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                // TODO Auto-generated catch block
                MainActivity.homeHabitHolder.deleteHabit(currentHabit);
                adapter.notifyDataSetChanged();
                try {
                    FileOutputStream fos = openFileOutput(MainActivity.FILENAME,
                            Context.MODE_PRIVATE);
                    MainActivity.homeHabitHolder.saveHabitFile(fos);
                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });

        alertDialogBuilder.setNeutralButton("Delete Date History",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                // TODO Auto-generated catch block
                deleteDateHistory(currentHabit, toDeleteCompletedDate);
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void deleteDateHistory(Habit currentHabit, ArrayList<String> toDeleteCompletedDate)
    {
        ArrayList<String> finalCompletedDate = currentHabit.getAllCompletedDate();
        for(String date: toDeleteCompletedDate)
        {
            finalCompletedDate.remove(date);
        }
        currentHabit.setAllCompletedDate(finalCompletedDate);
        currentHabit.setCount("-");
        saveRecordFile();

    }

    private void saveRecordFile() {
        try {
            FileOutputStream fos = openFileOutput(MainActivity.FILENAME,
                    Context.MODE_PRIVATE);
            MainActivity.homeHabitHolder.saveHabitFile(fos);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
