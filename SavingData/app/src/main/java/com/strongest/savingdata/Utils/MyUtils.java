package com.strongest.savingdata.Utils;

import android.os.Handler;
import android.view.View;

import com.google.api.client.repackaged.org.apache.commons.codec.binary.StringUtils;

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


    public static String toTitleCase(String givenString) {
        String[] arr = givenString.split(" ");
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < arr.length; i++) {
            sb.append(Character.toUpperCase(arr[i].charAt(0)))
                    .append(arr[i].substring(1)).append(" ");
        }
        return sb.toString().trim();
    }
}

