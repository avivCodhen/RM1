package com.strongest.savingdata.Database.Progress;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Cohen on 12/30/2017.
 */

public class DBProgressHelper extends SQLiteOpenHelper{

    private static final String DB_NAME = "fragment_manager.db";
    private static final int DB_VERSION = 1;

    public static final String NEW_EXERCISE_ID = "new_exercise_id";
    public static final String NEW_REP_ID = "new_rep";
    public static final String NEW_METHOD_ID = "new_method";
    public static final String NEW_SETS = "new_sets";
    public static final String NEW_SUPERSET = "new_superset";
    public static final String NEW_REST = "new_rest";
    public static final String NEW_WEIGHT = "new_weight";
    public static final String OLD_POSITION = "old_position";
    public static final String NEW_POSITION = "new_position";
    public static final String BODY_ID = "body_id";
    public static final String PHASE = "phase";
    public static final String CHANGES = "changes";

    public static final String WORKOUT_ID = "workout_id";
    public static final String EXERCISE_PROFILE_ID = "exercise_profile_id";

    public static final String TABLE_PROGRESS_MODELS = "progress_models";
    public static final String TABLE_PROGRESS_DATA_MODELS = "progress_data_models";

    private final String PROGRESS_MODELS_COMMAND = "CREATE TABLE " +
            TABLE_PROGRESS_MODELS + "(id INTEGER PRIMARY KEY AUTOINCREMENT," + BODY_ID + " INTEGER, " + PHASE + " INTEGER, "
            + NEW_WEIGHT + " INTEGER, " + NEW_REST + " INTEGER, "
            + NEW_EXERCISE_ID + " INTEGER, " + NEW_REP_ID + " INTEGER," + NEW_METHOD_ID + " INTEGER, "
            + OLD_POSITION + " INTEGER,"
            + NEW_POSITION + " INTEGER," + NEW_SETS + " INTEGER, " + NEW_SUPERSET + " INTEGER )";


    private final String PROGRESS_DATA_MODELS_COMMAND = "CREATE TABLE "
            + TABLE_PROGRESS_DATA_MODELS +"(id INTEGER PRIMARY KEY AUTOINCREMENT,"+PHASE +" INTEGER, "+
            CHANGES + " INTEGER)";

    public DBProgressHelper(Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
       // db.execSQL(PROGRESS_MODELS_COMMAND);
       // db.execSQL(PROGRESS_DATA_MODELS_COMMAND);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
