package com.assignment1.cjiang2_habittracker;

import android.content.Context;
import android.test.ActivityInstrumentationTestCase2;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Zone on 2016/10/1.
 */

public class HabitHolderTest extends ActivityInstrumentationTestCase2 {
    public HabitHolderTest(){
        super(HabitHolder.class);
    }

    public void testAddHabit(){
        HabitHolder testHabitHolder = new HabitHolder();

        Date testDate = new Date();
        ArrayList<String> testRecurrence = new ArrayList<String>();
        testRecurrence.add("Sat");
        Habit testHabit = new Habit("test",testDate,testRecurrence, 0);

        testHabitHolder.addHabit(testHabit, testDate);
        assertTrue(testHabitHolder.hasHabit(testHabit));
    }

    public void testDeleteHabit(){
        HabitHolder testHabitHolder = new HabitHolder();

        Date testDate = new Date();
        ArrayList<String> testRecurrence = new ArrayList<String>();
        testRecurrence.add("Sat");
        Habit testHabit = new Habit("test",testDate,testRecurrence, 0);

        testHabitHolder.addHabit(testHabit, testDate);
        assertTrue(testHabitHolder.hasHabit(testHabit));

        testHabitHolder.deleteHabit(testHabit);
        assertTrue(testHabitHolder.hasHabit(testHabit));
    }

    public void testCompleteHabit(){
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        HabitHolder testHabitHolder = new HabitHolder();

        Date testDate = new Date();
        ArrayList<String> testRecurrence = new ArrayList<String>();
        testRecurrence.add("Sat");
        Habit testHabit = new Habit("test",testDate,testRecurrence, 0);

        testHabitHolder.addHabit(testHabit, testDate);
        assertTrue(testHabitHolder.hasHabit(testHabit));

        testHabitHolder.completeHabit(testHabit, df.format(testDate));
        assertTrue(testHabitHolder.hasHabit(testHabit));

        ArrayList<Habit> toCompleteTest = testHabitHolder.getToCompleteHabitList();

        assertTrue(toCompleteTest.contains(testHabit));
    }

}
