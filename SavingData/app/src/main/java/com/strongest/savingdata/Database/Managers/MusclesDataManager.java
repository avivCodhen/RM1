package com.strongest.savingdata.Database.Managers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.strongest.savingdata.BaseWorkout.Muscle;
import com.strongest.savingdata.BaseWorkout.NewMuscle;
import com.strongest.savingdata.Database.Muscles.DBMuscleHelper;

import java.util.ArrayList;

import static com.strongest.savingdata.Database.Muscles.DBMuscleHelper.CHILDREN;
import static com.strongest.savingdata.Database.Muscles.DBMuscleHelper.DB_TABLE_NAME;
import static com.strongest.savingdata.Database.Muscles.DBMuscleHelper.IMAGE;
import static com.strongest.savingdata.Database.Muscles.DBMuscleHelper.MUSCLE_DISPLAY;
import static com.strongest.savingdata.Database.Muscles.DBMuscleHelper.MUSCLE_INT;
import static com.strongest.savingdata.Database.Muscles.DBMuscleHelper.MUSCLE_NAME;
import static com.strongest.savingdata.Database.Muscles.DBMuscleHelper.MUSCLE_SIZE;
import static com.strongest.savingdata.Database.Muscles.DBMuscleHelper.PARENT;

/**
 * Created by Cohen on 2/4/2018.
 */

public class MusclesDataManager {

    private DBMuscleHelper muscleHelper;
    private SQLiteDatabase db;
    private Context context;

    public MusclesDataManager(Context context) {

        this.context = context;
        muscleHelper = new DBMuscleHelper(context);
        db = muscleHelper.getWritableDatabase();
    }

    public void insertData(ArrayList<Muscle> data) {
        for (Muscle m : data) {
            db.insert(DB_TABLE_NAME, null,
                    getMuscleContentValues(m));
        }

    }

    public ArrayList<Muscle> getAllMuscles() {
        ArrayList<Muscle> list = new ArrayList<>();

        Cursor c = db.rawQuery("SELECT * FROM " + DBMuscleHelper.DB_TABLE_NAME, null);
        if (c != null && c.moveToFirst()) {
            do {
                list.add(getMuscleFromCursor(c));
            } while (c.moveToNext());
        }
        return list;
    }

    public Muscle getMuscleFromCursor(Cursor c) {
        Muscle m = new Muscle();
        m.setMuscle_name(c.getString(c.getColumnIndex(MUSCLE_NAME)));
        m.setMuscle_int(c.getInt(c.getColumnIndex(MUSCLE_INT)));
        m.setMuscle_display(c.getString(c.getColumnIndex(MUSCLE_DISPLAY)));
        m.setMuscle_size(c.getString(c.getColumnIndex(MUSCLE_SIZE)));
        m.setParent(c.getString(c.getColumnIndex(PARENT)));
        m.setImage(c.getString(c.getColumnIndex(IMAGE)));
        m.setChildren(c.getString(c.getColumnIndex(CHILDREN)));
        return m;
    }

    public Cursor getMuscleFromDB(String muscle) {
        Cursor c = db.rawQuery("SELECT * FROM " + DBMuscleHelper.DB_TABLE_NAME +
                " WHERE " + DBMuscleHelper.MUSCLE_NAME + "=?", new String[]{muscle});
        return c;
    }

    private ContentValues getMuscleContentValues(Muscle newMuscle) {
        ContentValues v = new ContentValues();
        v.put(IMAGE, newMuscle.getImage());
        v.put(PARENT, newMuscle.getParent());
        v.put(CHILDREN, newMuscle.getChildren());
        v.put(MUSCLE_DISPLAY, newMuscle.getMuscle_display());
        v.put(MUSCLE_INT, newMuscle.getMuscle_int());
        v.put(MUSCLE_NAME, newMuscle.getMuscle_name());
        v.put(MUSCLE_SIZE, newMuscle.getMuscle_size());
        return v;
    }

    public void delete() {
        Log.d("aviv", "delete: deleting muscles table");
        db.delete(DB_TABLE_NAME, null, null);
        //db.delete("SQLITE_SEQUENCE", "NAME = ?", new String[]{DB_TABLE_NAME});
    }
}
