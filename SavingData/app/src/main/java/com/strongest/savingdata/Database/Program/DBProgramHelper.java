package com.strongest.savingdata.Database.Program;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.strongest.savingdata.Database.Exercise.DBExercisesHelper.MUSCLE;

import static com.strongest.savingdata.Database.Exercise.DBExercisesHelper.TYPE;
import static com.strongest.savingdata.Database.Exercise.DBExercisesHelper.WEIGHT;

/**
 * Created by Cohen on 10/29/2017.
 */

public class DBProgramHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "program.db";
    private static final int DB_VERSION = 1;

    //program fields

    public static final String EXERCISE_ID = "exercise_id";
    public static final String REP_ID = "rep";
    public static final String METHOD_ID = "method";
    public static final String SETS = "sets";
    public static final String SUPERSET = "superset";
    public static final String STATS_DAMAGE = "damage";
    public static final String STATS_METABOLIC = "metabolic";
    public static final String STATS_MECHANICAL = "mechanical";
    public static final String REST = "rest";
    public static final String NAME = "name";
    public static final String WORKOUT_ID = "workout_id";
    public static final String EXERCISE_PROFILE_ID = "exercise_profile_id";
    public static final String FIRST_EXERCISE = "first_exercise";

    //template fields
    public static final String BODY_TEMPLATE_STR = "body_template_str",
            ROUNDS_PER_WEEK = "rounds_per_week",
            RECOMMENDED_WORKOUTS = "recommended_workouts",
            WORKOUTS_NAMES_ARRAY = "workouts_names_array";

    //reference table fields
    public static final String TEMPLATE_ID = "template_id";
    public static final String PROGRAM_ID = "program_id";


    //progressordata fields
    /**
     * exercise_id, sets, reps, rest, weight
     */
    /*public static final String REPS = "reps";
    public static final String DATE = "date";*/


    //table names
  /*  public static final String TABLE_PROGRAM = "program";
    public static final String TABLE_PROGRAMS = "programs";*/
    public static final String TABLE_TEMPLATES = "templates";
    public static final String TABLE_PROGRESSOR = "progressor";
    public static final String TABLE_PROGRAM_REFERENCE = "table_program_reference";
    public static final String TABLE_TEMPLATES_REFERENCE = "table_template_reference";
    public static final String TABLE_LAYOUT_REFERENCE = "table_layout_reference";
    public static final String TABLE_PROGRAM = "table_layout_reference";



    //layout table
    public static final String LAYOUT_NAME = "layout_name";
    public static final String DATE_CREATED = "date_created";

    //template reference fields
    public static final String TEMPLATE_NAME = "template_name";

    //program table
    public static final String PROGRAM_NAME = "program_name";


  /*  private final String PROGRAMS_TABLE_COMMAND = "CREATE TABLE "
            + TABLE_PROGRAMS + "(id INTEGER PRIMARY KEY AUTOINCREMENT," + PROGRAM_NAME + " TEXT, "+ ")";
*/
    /*private final String TEMPLATES_NAMES_TABLE_COMMAND = "CREATE TABLE "
            + TABLE_TEMPLATES + "(id INTEGER PRIMARY KEY AUTOINCREMENT," + TEMPLATE_NAME + " TEXT)";*/

    //commands
    private final String LAYOUT_CREATE_COMMAND = "CREATE TABLE " + "programs" + "(id INTEGER PRIMARY KEY AUTOINCREMENT, "
            + SETS + " TEXT, " + SUPERSET + " INTEGER, " + NAME + " TEXT," + WORKOUT_ID + " INTEGER," + EXERCISE_PROFILE_ID + " INTEGER, " + TYPE + " INTEGER, "
            + WEIGHT + " INTEGER, " + REST + " TEXT, " + FIRST_EXERCISE + " INTEGER, "
            + EXERCISE_ID + " TEXT, " + REP_ID + " TEXT," + METHOD_ID + " INTEGER DEFAULT -1, " + MUSCLE + " INTEGER,"
            + STATS_DAMAGE + " INTEGER," + STATS_MECHANICAL + " INTEGER, " + STATS_METABOLIC + " INTEGER )";


    private final String TEMPLATE_TABLE_COMMAND = "CREATE TABLE "
            + "templates" + "(id INTEGER PRIMARY KEY AUTOINCREMENT,"
            + TEMPLATE_NAME + " TEXT, " + WORKOUTS_NAMES_ARRAY + " TEXT, "
            + ROUNDS_PER_WEEK + " INTEGER, " + RECOMMENDED_WORKOUTS + " INTEGER, " + BODY_TEMPLATE_STR + " TEXT)";

    private final String LAYOUT_REFERENCE_COMMAND = "CREATE TABLE "
            + TABLE_LAYOUT_REFERENCE + " (id INTEGER PRIMARY KEY AUTOINCREMENT, " + LAYOUT_NAME + " TEXT, " +
            DATE_CREATED + " TEXT)";

    private final String TEMPLATES_REFERENCE_COMMAND = "CREATE TABLE "
            + TABLE_TEMPLATES_REFERENCE + " (id INTEGER PRIMARY KEY AUTOINCREMENT, " + TEMPLATE_NAME + " TEXT, " +
            DATE_CREATED + " TEXT)";


    private final String PROGRAM_REFERENCE_COMMAND = "CREATE TABLE "
            + TABLE_PROGRAM_REFERENCE + " (id INTEGER PRIMARY KEY AUTOINCREMENT, " + TEMPLATE_NAME + " TEXT, " +
            PROGRAM_NAME + " TEXT)";

    private final String PROGRAM_COMMAND = "CREATE TABLE "
            + "programs" + " (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            LAYOUT_NAME + " TEXT)";;

   /* private final String PROGRESSORDATA_TABLE_COMMAND = "CREATE TABLE " + TABLE_PROGRESSOR
            + " (id INTEGER PRIMARY KEY AUTOINCREMENT," + EXERCISE_ID+" INTEGER, "+SETS+" INTEGER,"
            + REPS +" TEXT, "+REST+" TEXT,"+WEIGHT+" TEXT)";*/

    public DBProgramHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(LAYOUT_REFERENCE_COMMAND);
        db.execSQL(PROGRAM_REFERENCE_COMMAND);
        db.execSQL(TEMPLATES_REFERENCE_COMMAND);
        //  db.execSQL(PROGRESSORDATA_TABLE_COMMAND);
    }

    public String getLayoutCommand(String tableName) {
        String command = LAYOUT_CREATE_COMMAND.replace("programs", tableName);
        return command;
    }

    public String getTemplateCreateCommand(String tableName) {
        String command = TEMPLATE_TABLE_COMMAND.replace(TABLE_TEMPLATES, tableName);
        return command;
    }

    public String getProgramCreateCommand(String tableName){
        String command = PROGRAM_COMMAND.replace("programs", tableName);
        return command;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
