package com.strongest.savingdata.Database.Managers;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.strongest.savingdata.Database.Workout.DefaultDBWorkoutHelper;

public class DefaultWorkoutsDataManager implements DataManagerAPI {

    public static final String DEFAULT = "Blank Template";
    public static final String FBW_TEMPLATE= "Full_Body_Workout_Template";
    public static final String AB_TEMPLATE= "AB_Superset_Template";
    public static final String ABC_DROPSET_strength_TEMPLATE= "ABC_Dropset_Strength_Template";

    SQLiteDatabase db;
    DefaultDBWorkoutHelper defaultDBWorkoutHelper;

    public DefaultWorkoutsDataManager(Context context) {
        defaultDBWorkoutHelper = new DefaultDBWorkoutHelper(context);
        db = defaultDBWorkoutHelper.getWritableDatabase();
    }

    @Override
    public Cursor readLayoutTableCursor(String tableName) {
        String sql = "SELECT * FROM " + tableName;
        Cursor c = null;
        try {
            c = db.rawQuery(sql, null);
        } catch (Exception e) {
            return null;
        }

        return c;
    }
}
