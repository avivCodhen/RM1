package com.strongest.savingdata.Database.Exercise;

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
 * Created by Cohen on 9/8/2017.
 */

public class DBExercisesHelper extends SQLiteOpenHelper {




    //DB fields
    //public static final String DB_NAME = "exercises.db";
    public static final String DB_NAME = "exercisesdb.db";
    public static final int DB_VERSION = 1;

    //main fields
    public static final String NAME = "name";
    public static final String DETAIL = "details";
    public static final String LEVEL = "level";
    public static final String TYPE = "type";
    public static final String ID = "id";
    public static final String WEIGHT = "weight";
    public static final String DEFAULT_INT = "default_int";

    //exercise fields


    public static final String IMAGE = "image";
    public static final String MUSCLE = "primaryMuscle";

    public static final String METABOLIC_STRESS = "metabolicStress";
    public static final String MECHANICAL_STRESS = "mechanicalStress";

    public static final String M_BICEPS = "m_biceps";
    public static final String M_TRICEPS = "m_triceps";
    public static final String M_A_SHOULDERS = "m_anterior_shoulders";
    public static final String M_R_SHOULDERS = "m_rear_shoulders";
    public static final String P_POSTERIOR = "p_legs_posterior";
    public static final String P_ANTERIOR = "p_legs_anterior";

    public static final String D_CHEST = "chest";
    public static final String D_BACK = "back";
    public static final String D_A_LEGS = "anteriorLegs";
    public static final String D_P_LEGS = "posteriorLegs";
    public static final String D_R_SHOULDERS = "rearShoulders";
    public static final String D_A_SHOULDERS = "anteriorShoulders";
    public static final String D_BICEPS = "biceps";
    public static final String D_TRICEPS = "triceps";

    public static final String D_WRIST = "wrist";
    public static final String D_LOWER_BACK = "lowerBack";
    public static final String D_CORE = "core";
    public static final String D_TRAPEZIUS = "trapezius";
    public static final String D_CALVES = "calves";

    //reps fields

    public static final String PYRAMID = "pyramid";
    public static final String INTENSITY = "intensity";
    public static final String SETS= "sets";

    //method fields


    //stats fields


    public static final String ROUTINE = "routine";
    public static final String MIN_EXERCISE = "minExercise";
    public static final String MAX_EXERCISE = "maxExercise";
    public static final String MIN_B = "minB";
    public static final String MIN_S = "minS";
    public static final String MIN_C = "minC";
    public static final String MAX_B = "maxB";
    public static final String MAX_S = "maxS";
    public static final String MAX_C = "maxC";
    public static final String MAX_METHOD = "maxMethod";
    public static final String MUSCLES = "muscles";


    //rest fields
   /* public static final String SETS = "sets";
    public static final String SETS = "sets";*/
    //commands
    public final String MAIN_EXERCISE_COMMAND = "CREATE TABLE "
            + TABLE_EXERCISES_ALL + " ( _id INTEGER PRIMARY KEY AUTOINCREMENT, " + ID + " INTEGER, "
            + WEIGHT + " INTEGER DEFAULT 0, " + MUSCLES + " TEXT,"  +  NAME + " TEXT, " + IMAGE + " INTEGER, " + DETAIL + " TEXT, "
            + TYPE + " INTEGER, " + LEVEL + " INTEGER, " + MUSCLE + " TEXT, " + D_BACK + " INTEGER, "
            + D_CHEST + " INTEGER," + D_P_LEGS + " INTEGER, " + D_A_LEGS + " INTEGER, "
            + D_A_SHOULDERS + " INTEGER, " + D_R_SHOULDERS + " INTEGER, " + D_WRIST + " INTEGER, "
            + D_CALVES + " INTEGER, " + D_CORE + " INTEGER, " + D_LOWER_BACK + " INTEGER, "
            + D_TRAPEZIUS + " INTEGER, " + D_BICEPS + " INTEGER, " + D_TRICEPS + "  INTEGER, "
            + M_R_SHOULDERS + " INTEGER, " + M_A_SHOULDERS + " INTEGER, " + M_TRICEPS + " INTEGER, "
            + M_BICEPS + " INTEGER," + P_ANTERIOR + " INTEGER, " + P_POSTERIOR + " INTEGER) ";


    public final String GENERATOR_EXERCISE_COMMAND = "CREATE TABLE "
            + TABLE_EXERCISES_GENERATOR + " ( _id INTEGER PRIMARY KEY AUTOINCREMENT, "
            + ID+ " INTEGER, " + WEIGHT +" INTEGER DEFAULT 0,"+  MUSCLES + " TEXT,"  + NAME + " TEXT, "
            + IMAGE + " INTEGER, " + DETAIL + " TEXT, " + TYPE + " INTEGER, " + LEVEL + " INTEGER, "
            + MUSCLE + " TEXT, " + D_BACK + " INTEGER, " + D_CHEST + " INTEGER," + D_P_LEGS + " INTEGER, "
            + D_A_LEGS + " INTEGER, " + D_A_SHOULDERS + " INTEGER, " + D_R_SHOULDERS + " INTEGER, "
            + D_WRIST + " INTEGER, " + D_CALVES + " INTEGER, " + D_CORE + " INTEGER, "
            + D_LOWER_BACK + " INTEGER, " + D_TRAPEZIUS + " INTEGER, " + D_BICEPS + " INTEGER, "
            + D_TRICEPS + "  INTEGER, " + M_R_SHOULDERS + " INTEGER, " + M_A_SHOULDERS + " INTEGER, "
            + M_TRICEPS + " INTEGER, " + M_BICEPS + " INTEGER," + P_ANTERIOR + " INTEGER, "
            + P_POSTERIOR + " INTEGER) ";


    public final String MAIN_REPS_COMMAND = "CREATE TABLE " + TABLE_REPS + "(_id INTEGER PRIMARY KEY AUTOINCREMENT, " + ID + " INTEGER,"+ SETS +" INTEGER,"+DEFAULT_INT+" INTEGER, " + NAME + " TEXT," + DETAIL + " TEXT, " + LEVEL + " INTEGER," + PYRAMID + " INTEGER, " + INTENSITY + " INTEGER, " + METABOLIC_STRESS + " INTEGER, " + MECHANICAL_STRESS + " INTEGER)";
    //public final String GENERATOR_REPS_COMMAND = "CREATE TABLE " + TABLE_REPS_GENERATOR + "(_id INTEGER PRIMARY KEY AUTOINCREMENT, " + ID + " INTEGER," + SETS +" INTEGER,"+DEFAULT_INT+" INTEGER, " + NAME + " TEXT," + DETAIL + " TEXT, " + LEVEL + " INTEGER," + PYRAMID + " INTEGER, " + INTENSITY + " INTEGER, " + METABOLIC_STRESS + " INTEGER, " + MECHANICAL_STRESS + " INTEGER)";
    public final String MAIN_METHOD_COMMAND = "CREATE TABLE " + TABLE_METHODS + "(_id INTEGER PRIMARY KEY AUTOINCREMENT, " + ID + " INTEGER," + NAME + " TEXT," + DETAIL + " TEXT, " +DEFAULT_INT+" INTEGER, " + LEVEL + " INTEGER)";
    //public final String GENERATOR_METHOD_COMMAND = "CREATE TABLE " + TABLE_METHODS_GENERATOR + "(_id INTEGER PRIMARY KEY AUTOINCREMENT, " + ID + " INTEGER," + NAME + " TEXT," + DETAIL + " TEXT, " + LEVEL + " INTEGER)";
    public final String STATS_COMMAND = "CREATE TABLE " + TABLE_STATS + " (" + LEVEL + " INTEGER," + ROUTINE + " INTEGER," + MIN_EXERCISE + " INTEGER," + MAX_EXERCISE + " INTEGER," + MIN_B + " INTEGER," + MIN_S + " INTEGER," + MIN_C + " INTEGER," + MAX_B + " INTEGER," + MAX_S + " INTEGER," + MAX_METHOD + " INTEGER," + MAX_C + " INTEGER)";
    public final String REST_COMMAND = "CREATE TABLE " + TABLE_REST + " (" + ID + " INTEGER, " +DEFAULT_INT+" INTEGER, " + TYPE + " INTEGER, "+ NAME+" TEXT)";
    public final String SETS_COMMAND = "CREATE TABLE " + TABLE_SETS +"("+ ID + " INTEGER, "+DEFAULT_INT+" INTEGER, "  + NAME + " INTEGER, " +TYPE + " INTEGER)" ;



    //muscleTables

    public static final String TABLE_CHEST= "chest", TABLE_BACK = "back", TABLE_LEGS = "legs",
            TABLE_SHOULDERS = "shoulders",
            TABLE_ARMS = "arms", TABLE_CORE = "core";

    public static final String TABLE_SETS = "sets";
    public static final String TABLE_REST = "rest";
    public static final String TABLE_STATS = "stats";
    public static final String TABLE_EXERCISES_ALL = "exercises";
    public static final String TABLE_EXERCISES_GENERATOR = "generator_exercises";
    public static final String TABLE_REPS = "reps";
    public static final String TABLE_REPS_GENERATOR = "generator_reps";
    public static final String TABLE_METHODS = "method";
    public static final String TABLE_METHODS_GENERATOR = "generator_method";

   /* public static final String TABLE_REST = "rest";
    public static final String TABLE_STATS = "stats";
    public static final String TABLE_EXERCISES_ALL = "exerciseseng";
    public static final String TABLE_EXERCISES_GENERATOR = "exerciseseng";
    public static final String TABLE_REPS = "repseng";
    public static final String TABLE_REPS_GENERATOR = "repseng";
    public static final String TABLE_METHODS = "methodeng";
    public static final String TABLE_METHODS_GENERATOR = "methodeng";*/





    public final String COMMAND_ALL = MAIN_EXERCISE_COMMAND;



   /* public static final String TABLE_ARMS = "arms";
    public static final String TABLE_TRICEPS = "triceps";
    public static final String TABLE_BICEPS = "biceps";

    public static final String TABLE_LEGS = "legs";
    public static final String TABLE_SHOULDERS = "shoulders";
    public static final String TABLE_CHEST = "chest";
    public static final String TABLE_BACK = "back";*/

  /*  public String[] commands = {
            COMMAND_ALL,
            COMMAND_ARMS,
            COMMAND_BACK,
            COMMAND_CHEST,
            COMMAND_BICEPS,
            COMMAND_TRICEPS,
            COMMAND_SHOULDERS,
            COMMAND_LEGS
    };*/

    public String[] muscleTables = {
            TABLE_ARMS,
            TABLE_BACK,
            TABLE_CHEST,
            TABLE_LEGS,
            TABLE_SHOULDERS,
            TABLE_CORE,
    };



    private Context context;
    private String DB_PATH = "";
    private SQLiteDatabase db;
    public DBExercisesHelper(Context context) {
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

        /*if (context != null) {
            for (String t : muscleTables){
                String command = COMMAND_ALL.replace(TABLE_EXERCISES_ALL, t);
                db.execSQL(command);
            }
           // db.execSQL(COMMAND_ALL);
            //db.execSQL(MAIN_METHOD_COMMAND);
            db.execSQL(MAIN_REPS_COMMAND);
            db.execSQL(GENERATOR_EXERCISE_COMMAND);
           // db.execSQL(GENERATOR_METHOD_COMMAND);
           // db.execSQL(GENERATOR_REPS_COMMAND);
           // db.execSQL(STATS_COMMAND);
            db.execSQL(REST_COMMAND);
           // db.execSQL(SETS_COMMAND);
           // db.execSQL(PROGRAM_CREATE_COMMAND);

        } else {

        }*/
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

    @Override
    public synchronized void close()
    {
        if(db != null)
            db.close();
        super.close();
    }




    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void closeDatabaseConnection() {
        close();
    }

}
