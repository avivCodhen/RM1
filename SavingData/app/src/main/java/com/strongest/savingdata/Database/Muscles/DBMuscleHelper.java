package com.strongest.savingdata.Database.Muscles;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Cohen on 2/4/2018.
 */

public class DBMuscleHelper extends SQLiteOpenHelper {

    public static final String ID = "id";
    public static final String MUSCLE_NAME = "muscle_name";
    public static final String MUSCLE_INT = "muscle_int";
    public static final String MUSCLE_SIZE = "muscle_SIZE";
    public static final String MUSCLE_DISPLAY = "muscle_DISPLAY";
    public static final String PARENT = "parent";
    public static final String IMAGE = "image";
    public static final String CHILDREN = "children";


   // private static final String DB_NAME = "muscles.db";
    private static final String DB_NAME = "musclesdb.db";
    private static final int DB_VERSION = 1;
    public static final String DB_TABLE_NAME = "muscles";


    private String DB_PATH = "";
    private SQLiteDatabase db;
    private static final String CREATE_COMMAND = "CREATE TABLE " + DB_TABLE_NAME + " (" + ID + " INTEGER, "
            + MUSCLE_NAME + " TEXT, " + MUSCLE_INT + " INTEGER, " + MUSCLE_SIZE + " TEXT, " + MUSCLE_DISPLAY + " TEXT,"
            + PARENT + " TEXT," + IMAGE + " TEXT, " + CHILDREN + " TEXT)";
    private Context context;


    public DBMuscleHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
        if(android.os.Build.VERSION.SDK_INT >= 17){
            DB_PATH = context.getApplicationInfo().dataDir + "/databases/";
        }
        else
        {
            DB_PATH = "/data/data/" + context.getPackageName() + "/databases/";
        }

        try {
            createDataBase();
        } catch (IOException e) {
            Log.d("aviv", "onCreate: ");
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
      //  db.execSQL(CREATE_COMMAND);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void createDataBase() throws IOException
    {
        //If the database does not exist, copy it from the assets.

        boolean mDataBaseExist = checkDataBase();
        if(!mDataBaseExist)
        {
            this.getWritableDatabase();
            this.close();
            try
            {
                Log.d("aviv", "createDataBase: working!" );

                //Copy the database from assests
                copyDataBase();
            }
            catch (IOException mIOException)
            {
                Log.d("aviv", "not working: "+ mIOException.toString());
                throw new Error("ErrorCopyingDataBase");
            }
        }
    }

    //Check that the database exists here: /data/data/your package/databases/Da Name
    private boolean checkDataBase()
    {
        File dbFile = new File(DB_PATH + DB_NAME);
        //Log.v("dbFile", dbFile + "   "+ dbFile.exists());
        return dbFile.exists();
    }

    //Copy the database from assets
    private void copyDataBase() throws IOException
    {
        InputStream mInput = context.getAssets().open(DB_NAME);
        String outFileName = DB_PATH + DB_NAME;
        OutputStream mOutput = new FileOutputStream(outFileName);
        byte[] mBuffer = new byte[1024];
        int mLength;
        while ((mLength = mInput.read(mBuffer))>0)
        {
            mOutput.write(mBuffer, 0, mLength);
        }
        mOutput.flush();
        mOutput.close();
        mInput.close();
    }

    //Open the database, so we can query it
    public boolean openDataBase() throws SQLException
    {
        String mPath = DB_PATH + DB_NAME;
        //Log.v("mPath", mPath);
        db = SQLiteDatabase.openDatabase(mPath, null, SQLiteDatabase.CREATE_IF_NECESSARY);
        //mDataBase = SQLiteDatabase.openDatabase(mPath, null, SQLiteDatabase.NO_LOCALIZED_COLLATORS);
        return db != null;
    }
}
