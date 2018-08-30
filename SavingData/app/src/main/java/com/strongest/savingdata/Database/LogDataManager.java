package com.strongest.savingdata.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.strongest.savingdata.AModels.workoutModel.PLObject;
import com.strongest.savingdata.AModels.workoutModel.WorkoutLayoutTypes;
import com.strongest.savingdata.Database.Exercise.ExerciseSet;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


/**
 * this class saves SETS data of an exercise.
 */
public class LogDataManager {

    Context context;
    private DBLogDataHelper dbLogDataHelper;
    SQLiteDatabase db;

    public LogDataManager(Context context) {

        this.context = context;
        this.dbLogDataHelper = new DBLogDataHelper(context);
        db = dbLogDataHelper.getWritableDatabase();
    }

    public void insert(PLObject.ExerciseProfile exerciseProfile) {


        inserter(exerciseProfile);
        for (PLObject.ExerciseProfile ep : exerciseProfile.exerciseProfiles) {
            if (ep.exercise != null)
                inserter(ep);
        }

    }

    private void inserter(PLObject.ExerciseProfile exerciseProfile) {
        String tableName = trimExerciseNameSpace(exerciseProfile.getExercise().getName());
        String date = new SimpleDateFormat("MMMM dd, yyyy", Locale.US).format(new Date());
        String currentTime = new SimpleDateFormat("HH:mm:ss").format(new Date());
        String result = date + ", " + currentTime;

        if (isTableExists(tableName)) {
            for (PLObject.SetsPLObject set : exerciseProfile.getSets()){
                db.insert(tableName, null, getContentValues(result,set ));

            }
        } else {
            //this means table does not exist
            Log.d("aviv", "insert from LogDataManager:  table does not exist");
            createTable(tableName);
            if (isTableExists(tableName)) {
                for (PLObject.SetsPLObject set : exerciseProfile.getSets()) {
                    db.insert(tableName, null, getContentValues(result, set));

                }
            }

        }

    }

    public boolean isTableExists(String tableName) {
        boolean isExist = false;
        Cursor cursor = db.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '" + tableName + "'", null);
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                isExist = true;
            }
            cursor.close();
        }
        return isExist;
    }

    private void createTable(String tableName) {
        db.execSQL(dbLogDataHelper.getCreateCommand(tableName));
    }


    //trims spaces to "_"
    public static String trimExerciseNameSpace(String exerciseName) {
        String trimmed = exerciseName.replace(" ", "_");
        return trimmed;
    }


    public ContentValues getContentValues(String date, PLObject.SetsPLObject setsPLObject) {

        ContentValues cv = new ContentValues();
            ExerciseSet e = setsPLObject.getExerciseSet();
            cv.put(DBLogDataHelper.REP_ID, e.getRep());
            cv.put(DBLogDataHelper.REST, e.getRest());
            cv.put(DBLogDataHelper.WEIGHT, e.getWeight());
            cv.put(DBLogDataHelper.DATE, date);
            cv.put(DBLogDataHelper.TYPE, setsPLObject.type.ordinal());

        return cv;
    }

    public ArrayList<LogData> readDates(String exerciseName) {
        Cursor c = db.rawQuery("SELECT * FROM " + trimExerciseNameSpace(exerciseName), null);
        String date = "";
        ArrayList<LogData> dates = new ArrayList<>();
        if (c != null && c.moveToFirst()) {
            do {

                if (c.getString(c.getColumnIndex(DBLogDataHelper.DATE)).equals(date)) {
                    continue;
                } else {
                    date = c.getString(c.getColumnIndex(DBLogDataHelper.DATE));
                    String[] arr = date.split(",");
                    dates.add(new LogData(date,arr[0], arr[1]));
                   /* d.reps = c.getString(c.getColumnIndex(DBLogDataHelper.REP_ID));
                    d.rest = c.getString(c.getColumnIndex(DBLogDataHelper.REST));
                    d.type = c.getString(c.getColumnIndex(DBLogDataHelper.TYPE));
                    d.weight = c.getString(c.getColumnIndex(DBLogDataHelper.WEIGHT));
                    d.date = date;*/
                }
            } while (c.moveToNext());
        }

        return dates;
    }

    public ArrayList<PLObject> readSets(String exerciseName, String dates){
        Cursor c = db.rawQuery("SELECT * FROM "
                + trimExerciseNameSpace(exerciseName)+" WHERE "+DBLogDataHelper.DATE+"=?", new String[]{dates});
        ArrayList<PLObject> sets = new ArrayList<>();
        if (c != null && c.moveToFirst()) {
            do {
                PLObject.SetsPLObject set = new PLObject.SetsPLObject();
                ExerciseSet e = new ExerciseSet();
                    e.setRep(c.getString(c.getColumnIndex(DBLogDataHelper.REP_ID)));
                   e.setRest(c.getString(c.getColumnIndex(DBLogDataHelper.REST)));
                   e.setWeight(c.getDouble(c.getColumnIndex(DBLogDataHelper.WEIGHT)));
                   set.type = WorkoutLayoutTypes.getEnum(c.getInt(c.getColumnIndex(DBLogDataHelper.TYPE)));
                   set.setExerciseSet(e);
                   sets.add(set);

            } while (c.moveToNext());
        }
        return sets;
    }
}
