package com.strongest.savingdata.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.strongest.savingdata.AModels.workoutModel.PLObject;

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

    public void insert(String exerciseName, ArrayList<LogData.LogDataSets> list) {
        inserter(exerciseName, list);
    }

    private void inserter(String tableNameToTrim, ArrayList<LogData.LogDataSets> list) {
        String tableName = trimExerciseNameSpace(tableNameToTrim);
        String result = getDateString();
       // ArrayList<LogData.LogDataSets> list = exerciseToLogDataSetList(exerciseProfile);
        if (isTableExists(tableName)) {
            for (LogData.LogDataSets l : list) {
                db.insert(tableName, null, getContentValues(l, result));
            }

        } else {
            //this means table does not exist

            createTable(tableName);
            if (isTableExists(tableName)) {
                for (LogData.LogDataSets l : list) {
                    db.insert(tableName, null, getContentValues(l, result));
                }
            }

        }

    }

    public boolean isTableExists(String tableName) {
        tableName = trimExerciseNameSpace(tableName);
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
        tableName = trimExerciseNameSpace(tableName);
        db.execSQL(dbLogDataHelper.getCreateCommand(tableName));
    }


    //trims spaces to "_"
    public static String trimExerciseNameSpace(String exerciseName) {
        String trimmed = exerciseName.replace(" ", "_");
        trimmed = trimmed.replace("-","_");
        return trimmed;
    }

    public static String getDateString(){
        String date = new SimpleDateFormat("MMMM dd, yyyy", Locale.US).format(new Date());
        String currentTime = new SimpleDateFormat("HH:mm").format(new Date());
        String result = date + ", " + currentTime;
        return result;
    }

    public LogData.LogDataSets setToLogDataSet(int position, PLObject.SetsPLObject setsPLObject) {

        String title = "";
        switch (setsPLObject.getInnerType()) {
            case SuperSetIntraSet:
                title += "Superset " + setsPLObject.getExerciseName();
                break;
            case IntraSet:
                title = "Dropset ";
                break;

            default:
                title = "Set " + (position + 1);
                break;
        }
        return new LogData.LogDataSets(
                title,
                setsPLObject.getExerciseSet().getRep(),
                setsPLObject.getExerciseSet().getRest(),
                setsPLObject.getExerciseSet().getWeight()
        );

    }

    public ArrayList<LogData.LogDataSets> setToLogDataSetList(PLObject.SetsPLObject setsPLObject, int position) {
        ArrayList<LogData.LogDataSets> list = new ArrayList<>();
        list.add(setToLogDataSet(position, setsPLObject));
        for (int j = 0; j < setsPLObject.getSuperSets().size(); j++) {
            list.add(setToLogDataSet(j, setsPLObject.getSuperSets().get(j)));
        }
        for (int j = 0; j < setsPLObject.getIntraSets().size(); j++) {
            list.add(setToLogDataSet(j, setsPLObject.getIntraSets().get(j)));

        }

        return list;

    }

    public ArrayList<LogData.LogDataSets> exerciseToLogDataSetList(PLObject.ExerciseProfile exerciseProfile) {
        ArrayList<LogData.LogDataSets> list = new ArrayList<>();

        for (int i = 0; i < exerciseProfile.getSets().size(); i++) {
            PLObject.SetsPLObject set = exerciseProfile.getSets().get(i);
            list.addAll(setToLogDataSetList(set, i));
        }

        return list;

    }


    public ContentValues getContentValues(LogData.LogDataSets logDataSets, String date) {

        ContentValues cv = new ContentValues();
        cv.put(DBLogDataHelper.REP_ID, logDataSets.rep);
        cv.put(DBLogDataHelper.REST, logDataSets.rest);
        cv.put(DBLogDataHelper.WEIGHT, logDataSets.weight);
        cv.put(DBLogDataHelper.DATE, date);
        cv.put(DBLogDataHelper.TITLE, logDataSets.title);

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
                    dates.add(new LogData(date, arr[0], arr[1]));

                }
            } while (c.moveToNext());
        }

        return dates;
    }

    public ArrayList<LogData.LogDataSets> readSets(String exerciseName, String dates) {
        if (!isTableExists(exerciseName)) {
            createTable(exerciseName);
        }
        Cursor c;
        c = db.rawQuery("SELECT * FROM "
                + trimExerciseNameSpace(exerciseName) + " WHERE " + DBLogDataHelper.DATE + "=?", new String[]{dates});
        ArrayList<LogData.LogDataSets> list = new ArrayList<>();
        LogData.LogDataSets logData;
        if (c != null && c.moveToFirst()) {
            do {
                logData = new LogData.LogDataSets(
                        c.getString(c.getColumnIndex(DBLogDataHelper.TITLE)),
                        c.getString(c.getColumnIndex(DBLogDataHelper.REP_ID)),
                        c.getString(c.getColumnIndex(DBLogDataHelper.REST)),
                        c.getDouble(c.getColumnIndex(DBLogDataHelper.WEIGHT))

                );
                list.add(logData);

            } while (c.moveToNext());
        }
        return list;
    }
}
