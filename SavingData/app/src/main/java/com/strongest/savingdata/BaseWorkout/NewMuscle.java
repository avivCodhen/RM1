package com.strongest.savingdata.BaseWorkout;

import com.strongest.savingdata.Database.Managers.DataManager;
import com.strongest.savingdata.Database.Muscles.DBMuscleHelper;

import android.database.Cursor;

import static com.strongest.savingdata.Database.Muscles.DBMuscleHelper.CHILDREN;
import static com.strongest.savingdata.Database.Muscles.DBMuscleHelper.IMAGE;
import static com.strongest.savingdata.Database.Muscles.DBMuscleHelper.MUSCLE_DISPLAY;
import static com.strongest.savingdata.Database.Muscles.DBMuscleHelper.MUSCLE_INT;
import static com.strongest.savingdata.Database.Muscles.DBMuscleHelper.MUSCLE_NAME;
import static com.strongest.savingdata.Database.Muscles.DBMuscleHelper.MUSCLE_SIZE;
import static com.strongest.savingdata.Database.Muscles.DBMuscleHelper.PARENT;

/**
 * Created by Cohen on 2/4/2018.
 */

public class NewMuscle {

    private int id,muscle_int;
    private String muscle_name, muscle_size, muscle_display, parent, image, children;

    public NewMuscle() {

    }


    public static NewMuscle createMuscle(DataManager dm, String muscle) {


        Cursor c = dm.getMuscleDataManager().getMuscleFromDB(muscle);
        NewMuscle m = new NewMuscle();
        m.muscle_name = c.getString(c.getColumnIndex(MUSCLE_NAME));
        m.muscle_int = c.getInt(c.getColumnIndex(MUSCLE_INT));
        m.muscle_display = c.getString(c.getColumnIndex(MUSCLE_DISPLAY));
        m.muscle_size = c.getString(c.getColumnIndex(MUSCLE_SIZE));
        m.parent = c.getString(c.getColumnIndex(PARENT));
        m.image = c.getString(c.getColumnIndex(IMAGE));
        m.children = c.getString(c.getColumnIndex(CHILDREN));
        return m;
    }


    public String getMuscle_display() {
        return muscle_display;
    }



    public String getParent() {
        return parent;
    }



    public String getImage() {
        return image;
    }

    public String getMuscle_size() {
        return muscle_size;
    }

    public String getMuscle_name() {
        return muscle_name;
    }

    public int getMuscle_int() {
        return muscle_int;
    }

    public String getChildren() {
        return children;
    }
}
