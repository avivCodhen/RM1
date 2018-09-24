package com.strongest.savingdata.Utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.google.api.client.repackaged.org.apache.commons.codec.binary.StringUtils;
import com.strongest.savingdata.AModels.workoutModel.PLObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

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

    public static <T extends Parcelable> T copy(T orig) {
        Parcel p = Parcel.obtain();
        orig.writeToParcel(p, 0);
        p.setDataPosition(0);
        T copy = null;
        try {
            copy = (T) orig.getClass().getDeclaredConstructor(new Class[]{Parcel.class}).newInstance(p);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return copy;
    }

    public static <T extends PLObject> T clone(PLObject plObject) throws IOException, ClassNotFoundException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(bos);
        } catch (IOException e) {
            e.printStackTrace();
        }
        oos.writeObject(plObject);
        oos.flush();
        oos.close();
        bos.close();
        byte[] byteData = bos.toByteArray();
        ByteArrayInputStream bais = new ByteArrayInputStream(byteData);
        PLObject object = (PLObject)  new ObjectInputStream(bais).readObject();
        return (T)object;
    }

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }
}

