package com.assignment1.cjiang2_habittracker;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Zone on 2016/9/21.
 */
public class Habit {
    private String habitDesc;
    private Date date;
    private ArrayList<String> habitRecurrence;
    private String completedDate;
    private int count;
    private ArrayList<String> allCompletedDate;

    private static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");
    private static DateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd");

    public Habit(String habitDesc, Date date, ArrayList<String> habitRecurrence, int count) {
        this.habitDesc = habitDesc;
        this.date = date;
        this.habitRecurrence = habitRecurrence;
        this.completedDate = "None";
        this.count = count;
        this.allCompletedDate = new ArrayList<String>();
        this.allCompletedDate.add(dateFormat.format(date));
    }

    @Override
    public String toString(){
        return this.habitDesc;
    }

    public String returnSaveFileString(){
        return this.habitDesc+"|"+
                dateFormat.format(this.date)+ "|"
                +this.habitRecurrence.toString().replace("[","").replace("]","").replaceAll("\\s+","")+ "|"
                +this.completedDate+"|"
                +Integer.toString(this.count)+"|"
                +this.allCompletedDate.toString().replace("[","").replace("]","").replaceAll("\\s+","");
    }

    // getters and setters
    public String getHabit() {
        return this.habitDesc;
    }

    public void setHabit(String habitDesc) throws HabitTooLongException {
        if (habitDesc.length() > 50) {
            throw new HabitTooLongException();
        }
        this.habitDesc = habitDesc;
    }

    public String getDateString() {
        return dateFormat2.format(this.date);
    }

    public ArrayList<String> getHabitRecurrence() {
        return this.habitRecurrence;
    }

    public String getHabitRecurrenceInString() {
        return this.habitRecurrence.toString().replace("[","").replace("]","");
    }

    public String getCompletedDate() {
            return this.completedDate;
    }

    public void setCompletedDate(String completedDate) {
        this.completedDate = completedDate;
    }

    public void setAllCompletedDate(ArrayList<String> allCompletedDate){
        this.allCompletedDate = allCompletedDate;
    }

    public void addToAllCompletedDate(String date){
        this.allCompletedDate.add(date);
    }

    public ArrayList<String> getAllCompletedDate(){
        return this.allCompletedDate;
    }

    public int getCount() {
        return count;
    }

    public void setCount(String sign) {
        if (sign.equals("+")) {
            this.count++;
        }
        else{
            this.count--;
        }
    }
}
