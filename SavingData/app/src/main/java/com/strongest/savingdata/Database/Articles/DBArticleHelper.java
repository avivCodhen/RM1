package com.strongest.savingdata.Database.Articles;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Cohen on 10/13/2017.
 */


public class DBArticleHelper extends SQLiteOpenHelper {

    private static final String DB_NAME="articles.db";
    private static final int DB_VERSION = 1;

    public static final String TABLE_NAME = "articles";

    public static final String ID = "_id";
    public static final String TITLE = "title";
    public static final String SUMMARY = "summary";
    public static final String LINK = "link";
    public static final String PAGE = "page";
    public static final String IMAGE = "image";

    private final String CREATE_COMMAND = "CREATE TABLE "+ TABLE_NAME+"("+ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+TITLE+" TEXT,"+SUMMARY+" TEXT,"+LINK+" TEXT,"+IMAGE+" BLOB,"+PAGE+" TEXT)";



    public DBArticleHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_COMMAND);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }



}
