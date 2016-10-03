package com.assignment1.cjiang2_habittracker;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

/**
 * Created by Zone on 2016/9/26.
 */
public class HabitHolder {

    private ArrayList<Habit> habitList;
    private ArrayList<Habit> ToCompleteHabitList;

    public HabitHolder(){
        this.habitList =  new ArrayList<Habit>();
        this.ToCompleteHabitList = new ArrayList<Habit>();
    }

    public ArrayList<Habit> getHabitList() {
        return this.habitList;
    }

    public void generateStatusLists(Date currentDate) {
        // get ToComplete-habits and completed-Habits
        DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");
        DateFormat df2 = new SimpleDateFormat("EEE");
        for (Habit habit: this.habitList)
        {
            ArrayList<String> weekList = habit.getHabitRecurrence();
            if (weekList.contains(df2.format(currentDate)))
            {
                this.ToCompleteHabitList.add(habit);
            }
        }
    }

    public ArrayList<Habit> getToCompleteHabitList() {
        return ToCompleteHabitList;
    }

    public Habit getHabitFromAll(Integer index) {
        return this.habitList.get(index);
    }

    public Habit getHabitFromToComplete(Integer index) {
        return this.ToCompleteHabitList.get(index);
    }

    private Habit stringToHabit(String line) {
        // String manipulator to process info from file
        Habit habit;
        String[] parts = line.split("\\|");
        String habitDesc = parts[0];
        String habitDateString = parts[1];
        ArrayList<String> habitWeek = new ArrayList<String>(Arrays.asList(parts[2].split(",")));
        String habitCompletedDate = parts[3];
        String habitCount = parts[4];
        ArrayList<String> allhabitCompletedDate = new ArrayList<String>(Arrays.asList(parts[5].split(",")));

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date habitDate = new Date();
        try {
            habitDate = df.parse(habitDateString);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
        }
        habit = new Habit(habitDesc, habitDate, habitWeek, Integer.parseInt(habitCount));
        habit.setCompletedDate(habitCompletedDate);
        habit.setAllCompletedDate(allhabitCompletedDate);

        return habit;
    }

    public void loadHabitFile(FileInputStream fis, Date currentDate) {
        Habit habit;
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(fis));
            String line = in.readLine();
            while (line != null) {
                habit = stringToHabit(line);
                this.habitList.add(habit);
                line = in.readLine();
            }
            generateStatusLists(currentDate);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
        } catch (IOException e) {
            // TODO Auto-generated catch block
            throw new RuntimeException();
        }
    }

    public void saveHabitFile(FileOutputStream fos) {
        try {
            for (Habit habit : this.habitList)
            {
                fos.write((habit.returnSaveFileString()+'\n').getBytes());
            }
            fos.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void clearHabitFile(FileOutputStream fos){
        clearHabitList();
        saveHabitFile(fos);
    }

    public void clearHabitList(){
        this.habitList.clear();
        this.ToCompleteHabitList.clear();
    }

    public void addHabit(Habit habitToAdd, Date currentDate)
    {
        this.habitList.add(habitToAdd);
        this.ToCompleteHabitList.clear();
        generateStatusLists(currentDate);
    }

    public void deleteHabit(Habit habitToDelete)
    {
        this.habitList.remove(habitToDelete);
        this.ToCompleteHabitList.remove(habitToDelete);
    }

    public void completeHabit(Habit habitToComplete, String completeDate)
    {
        habitToComplete.setCount("+");
        habitToComplete.setCompletedDate(completeDate);
        habitToComplete.addToAllCompletedDate(completeDate);
    }

    public boolean hasHabit(Habit habit){
        return this.habitList.contains(habit);
    }

}
