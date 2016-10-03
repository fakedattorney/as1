package com.assignment1.cjiang2_habittracker;

// http://stackoverflow.com/questions/10108774/how-to-implement-the-android-actionbar-back-button Implement of back button
// http://stackoverflow.com/questions/9861483/datepickerdialog daypicker dialog referrence

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import android.content.Context;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class AddHabits extends AppCompatActivity {

    private static EditText habitEditText;
    private static EditText habitRecurrenceText;
    private static EditText habitOptimalText;
    private static Date currentDate;
    private static ArrayList<String> weekSelected;
    private int mYear, mMonth, mDay, mHour, mMinute;
    final String[] weekList = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.add_todo);
        setContentView(R.layout.activity_add_habits);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Button saveButton = (Button) findViewById(R.id.ok);
        habitEditText = (EditText) findViewById(R.id.habit);
        habitOptimalText = (EditText) findViewById(R.id.habitDate);
        habitOptimalText.setFocusable(false);
        habitRecurrenceText = (EditText) findViewById(R.id.habitWeek);
        habitRecurrenceText.setFocusable(false);
        habitRecurrenceText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WeekPickerAlertDialog();
            }
        });

        habitOptimalText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                // Launch Date Picker Dialog
                DatePickerDialog dpd = new DatePickerDialog(AddHabits.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // Display Selected date in textbox
                                habitOptimalText.setText(year + "-" +(monthOfYear + 1)+"-" + dayOfMonth);
                            }
                        }, mYear, mMonth, mDay);
                dpd.show();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int textlength = habitEditText.getText().length();
                if (textlength == 0) {
                    emptyDescAlertDialog("Description cannot be empty!");
                }
                textlength = habitRecurrenceText.getText().length();
                if (textlength == 0) {
                    emptyDescAlertDialog("Week of Recurrence cannot be empty!");
                } else {
                    textlength = habitOptimalText.getText().length();
                    if (textlength != 0)
                    {
                        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                        try {
                            String optimalDate = habitOptimalText.getText().toString();
                            currentDate = df.parse(optimalDate);
                        } catch (ParseException e) {
                            currentDate = new Date();
                        }
                    }
                    else {
                        currentDate = new Date();
                    }
                    Habit newHabit = new Habit(habitEditText.getText().toString(), currentDate, weekSelected, 0);
                    currentDate = new Date();
                    MainActivity.homeHabitHolder.addHabit(newHabit, currentDate);
                    saveRecordFile();
                    finish();
                }
            }
        });

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

    private void emptyDescAlertDialog(String errorMsg) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(errorMsg);
        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                // TODO Auto-generated catch block
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void WeekPickerAlertDialog() {
        weekSelected = new ArrayList<>();
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder.setTitle(R.string.recurrence);
        alertDialogBuilder.setMultiChoiceItems(weekList, null, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                if (isChecked) {
                    weekSelected.add(weekList[which]);
                } else if (weekSelected.contains(weekList[which])) {
                    weekSelected.remove(weekList[which]);
                }
            }
        });
        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                // TODO Auto-generated catch block
                habitRecurrenceText.setText(weekSelected.toString().replace("[","").replace("]",""));
            }
        });
        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                // TODO Auto-generated catch block
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


}