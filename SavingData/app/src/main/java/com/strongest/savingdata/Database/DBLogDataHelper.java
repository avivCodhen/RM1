package com.strongest.savingdata.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DBLogDataHelper extends SQLiteOpenHelper{

    private static final int version = 1;
    private static final String DB_NAME = "logdata.db";
    public final String LOG_TABLE_REPLACE = "logtable";
    public static final String DATE= "date";
    public static final String REP_ID = "rep";
    public static final String SETS = "sets";
    public static final String REST = "rest";
    public static final String INNER_TYPE = "inner_type";
    public static final String WEIGHT = "weight";
    public static final String TYPE = "type";

    private final String LAYOUT_CREATE_COMMAND = "CREATE TABLE " + LOG_TABLE_REPLACE + "(id INTEGER PRIMARY KEY AUTOINCREMENT, "
            + SETS + " TEXT, " + INNER_TYPE+" INTEGER,"+ DATE + " TEXT,"+ " INTEGER, " + TYPE + " INTEGER, "
            + WEIGHT + " TEXT, " + REST + " TEXT, "
            + REP_ID + " TEXT)";

    public DBLogDataHelper(Context context) {
        super(context, DB_NAME, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public String getCreateCommand(String tableName){
        String command = LAYOUT_CREATE_COMMAND.replace(LOG_TABLE_REPLACE, tableName);
        return command;
    }
}
