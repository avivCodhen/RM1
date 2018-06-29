package com.strongest.savingdata.Utils;

import android.os.Handler;
import android.view.View;

public class MyUtils {


public static class Interface{

    public static void disableClick(final View v, int time){
        v.setClickable(false);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                v.setClickable(true);
            }
        }, time);
    }
    }

    public static String[] stringsToArray(String...strings){
            return strings;
    }

}

