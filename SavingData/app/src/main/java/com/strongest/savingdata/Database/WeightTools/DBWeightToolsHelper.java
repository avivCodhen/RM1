package com.strongest.savingdata.Database.WeightTools;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Cohen on 3/25/2018.
 */

public class DBWeightToolsHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "weight_tools.db";
    private static final int DB_VERSION = 1;

    public static final String ACTION = "action";
    public static final String NUMBER = "number";

    public static final String TABLE_NAME = "weight_tools";

    private final String COMMAND = "CREATE TABLE "+ TABLE_NAME +" ("+ACTION+" TEXT, "+ NUMBER+" INTEGER)";

    public DBWeightToolsHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(COMMAND);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
