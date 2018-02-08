package com.strongest.savingdata.Database.Progress;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.strongest.savingdata.Database.Managers.DataManager;
import com.strongest.savingdata.Database.Managers.ExercisesDataManager;
import com.strongest.savingdata.Database.Managers.ProgramDataManager;

import static com.strongest.savingdata.Database.Progress.DBProgressHelper.PHASE;

/**
 * Created by Cohen on 12/26/2017.
 */

public class ProgressDataManager extends DataManager {

    private DBProgressHelper progressDB;
    private ExercisesDataManager edm;
    private final Context context;
    private final ProgramDataManager pdm;
    private SQLiteDatabase db;
    private String currentTableName;

    public ProgressDataManager(Context context, ProgramDataManager pdm, ExercisesDataManager edm) {
        super(context);
        progressDB = new DBProgressHelper(context);
        this.edm = edm;
        db = progressDB.getWritableDatabase();
        this.context = context;
        this.pdm = pdm;
        currentTableName = pdm.getCurrentProgramTable();
    }

    /*public ArrayList<ProgressModelNode> readProgresModelNode(int workout_id) {
       *//* String SQL = "SELECT * FROM " + currentTableName + "WHERE " + PHASE +"=?" +" AND "+
                DBProgressHelper.WORKOUT_ID;*//*
        String SQL = "SELECT * FROM " + currentTableName +  " WHERE " +
                DBProgressHelper.WORKOUT_ID + "=?";
        Cursor c = db.rawQuery(SQL, new String[]{String.valueOf(workout_id)});
        return parseProgressModelNodes(c);
    }
*/
    public void close() {
        if (db != null) {
            db.close();
        }
    }

    public void saveProgressModelNode() {

    }


    public void saveProgressModelsData(int phases, int changes) {
        ContentValues v = new ContentValues();
        v.put(PHASE, phases);
        v.put(DBProgressHelper.CHANGES, changes);
        db.insert(DBProgressHelper.TABLE_PROGRESS_DATA_MODELS, null, v);
    }

    public ProgressModelsData readProgresModelsData() {
        String SQL = "SELECT * FROM" + DBProgressHelper.TABLE_PROGRESS_DATA_MODELS;
        return parseProgressModelsData(db.rawQuery(SQL, null));
    }

    private ProgressModelsData parseProgressModelsData(Cursor c) {

        ProgressModelsData data = new ProgressModelsData();
        data.setNumOfPhases(c.getInt(c.getColumnIndex(PHASE)));
        data.setNumOfChanges(c.getInt(c.getColumnIndex(DBProgressHelper.CHANGES)));
        return data;
    }

    /*private ArrayList<ProgressModelNode> parseProgressModelNodes(Cursor c) {
        ArrayList<ProgressModelNode> list = new ArrayList<>();
        ProgressModelNode node;
        BeansHolder prev = new BeansHolder();
        BeansHolder next = new BeansHolder();
        if (c != null && c.moveToFirst()) {
            do {
                int exId = c.getInt(c.getColumnIndex(NEW_EXERCISE_ID));
                int repId = c.getInt(c.getColumnIndex(NEW_REP_ID));
                int metId = c.getInt(c.getColumnIndex(NEW_METHOD_ID));
                int weight = c.getInt(c.getColumnIndex(NEW_WEIGHT));
                int rest = c.getInt(c.getColumnIndex(NEW_REST));
                int workoutId = c.getInt(c.getColumnIndex(WORKOUT_ID));
                int exerciseProfileId = c.getInt(c.getColumnIndex(EXERCISE_PROFILE_ID));
                int phase = c.getInt(c.getColumnIndex(PHASE));
                //  String[] rest = context.getResources().getStringArray(R.array.rest_arr);
               // String restString = c.getString(c.getColumnIndex(REST));
                next.setExercise(edm.fetchById(TABLE_EXERCISES_ALL, exId));
                next.setRep(edm.fetchById(TABLE_REPS, repId));
                next.setMethod(edm.fetchById(TABLE_METHODS, metId));
                next.setRest(edm.fetchById(TABLE_REST, rest));
                next.setSets(c.getInt(c.getColumnIndex(SETS)));
                next.setWeight(weight);
                node = new ProgressModelNode(null, next,workoutId,exerciseProfileId,phase);
                list.add(node);
            } while (c.moveToNext());
        }


        return list;
    }*/

}
