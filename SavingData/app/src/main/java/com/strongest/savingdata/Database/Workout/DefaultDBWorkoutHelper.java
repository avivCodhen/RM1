package com.strongest.savingdata.Database.Workout;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DefaultDBWorkoutHelper extends SQLiteOpenHelper {


    private static final String DB_NAME = "defaultWorkouts.db";
    // private static final String DB_NAME = "musclesdb.db";
    private static final int DB_VERSION = 1;
    public static final String DB_TABLE_NAME = "muscles";
    private String DB_PATH = "";
    Context context;

    public DefaultDBWorkoutHelper(Context context) {
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
            Log.d("aviv", "DefaultDBWorkoutHelper: ");
            e.printStackTrace();
        }
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

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
