package com.assignment1.cjiang2_habittracker;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;

/**
 * Created by Zone on 2016/10/1.
 */

public class allHabitsActivityTest extends ActivityInstrumentationTestCase2 {

    public allHabitsActivityTest(){
        super(allHabits.class);
    }

    public void testStart() throws Exception {
        Activity activity = getActivity();
    }
}

