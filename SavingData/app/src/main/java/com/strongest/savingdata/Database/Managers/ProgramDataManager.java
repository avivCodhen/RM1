package com.strongest.savingdata.Database.Managers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.strongest.savingdata.AModels.workoutModel.Workout;
import com.strongest.savingdata.AModels.workoutModel.WorkoutLayoutTypes;
import com.strongest.savingdata.AModels.programModel.Program;
import com.strongest.savingdata.AService.WorkoutsService;
import com.strongest.savingdata.BaseWorkout.ProgramTemplate;
import com.strongest.savingdata.Database.Exercise.DBExercisesHelper;
import com.strongest.savingdata.Database.Program.DBProgramHelper;
import com.strongest.savingdata.AModels.workoutModel.PLObject;

import java.util.ArrayList;
import java.util.Collection;

import static com.strongest.savingdata.Database.Exercise.DBExercisesHelper.*;
import static com.strongest.savingdata.Database.Program.DBProgramHelper.BODY_TEMPLATE_STR;
import static com.strongest.savingdata.Database.Program.DBProgramHelper.DATE_CREATED;
import static com.strongest.savingdata.Database.Program.DBProgramHelper.EXERCISE_ID;
import static com.strongest.savingdata.Database.Program.DBProgramHelper.INNER_TYPE;
import static com.strongest.savingdata.Database.Program.DBProgramHelper.LAYOUT_NAME;
import static com.strongest.savingdata.Database.Program.DBProgramHelper.PROGRAM_NAME;
import static com.strongest.savingdata.Database.Program.DBProgramHelper.PROGRAM_TIME;
import static com.strongest.savingdata.Database.Program.DBProgramHelper.RECOMMENDED_WORKOUTS;
import static com.strongest.savingdata.Database.Program.DBProgramHelper.REP_ID;
import static com.strongest.savingdata.Database.Program.DBProgramHelper.REST;
import static com.strongest.savingdata.Database.Program.DBProgramHelper.ROUNDS_PER_WEEK;
import static com.strongest.savingdata.Database.Program.DBProgramHelper.TABLE_LAYOUT_REFERENCE;
import static com.strongest.savingdata.Database.Program.DBProgramHelper.TABLE_PROGRAM_REFERENCE;
import static com.strongest.savingdata.Database.Program.DBProgramHelper.TABLE_TEMPLATES;
import static com.strongest.savingdata.Database.Program.DBProgramHelper.TABLE_TEMPLATES_REFERENCE;
import static com.strongest.savingdata.Database.Program.DBProgramHelper.TABLE_WORKOUTS;
import static com.strongest.savingdata.Database.Program.DBProgramHelper.TEMPLATE_NAME;
import static com.strongest.savingdata.Database.Program.DBProgramHelper.WORKOUTS_NAMES_ARRAY;

/**
 * Created by Cohen on 10/16/2017.
 */


public class ProgramDataManager extends DataManager {

    private SQLiteDatabase db;
    private ExercisesDataManager exerciseDataManager;
    private Context context;
    private DBExercisesHelper exercisesHelper;
    private DBProgramHelper programHelper;
    private String currentProgramTable;
    private String currentLayoutTable;

    public String getCurrentLayoutTable() {
        return currentLayoutTable;
    }
    //private String currentTemplateTable;


    public enum Tables {
        PROGRAM_REFERENCE, LAYOUT_REFERENCE, TEMPLATE_REFERENCE, TEMPLATE, PROGRAM;
    }

    public ProgramDataManager(ExercisesDataManager exerciseDataManager, Context context) {
        super(context);
        this.exerciseDataManager = exerciseDataManager;
        this.context = context;
        exercisesHelper = new DBExercisesHelper(context);
        programHelper = new DBProgramHelper(context);
        //  updateCurrentTables(0);
    }

    private SQLiteDatabase getDb(SQLiteOpenHelper helper) {
        db = helper.getWritableDatabase();
        return db;
    }

    public void insertData(String tableName, ArrayList<ContentValues> contentValues) {
        for (ContentValues v : contentValues) {
            getDb(programHelper).insert(tableName,
                    null,
                    v);

        }
    }

    public void insertData(String tableName, ContentValues contentValues) {
        getDb(programHelper).insert(tableName,
                null,
                contentValues);
    }

    public void insertTables(boolean update, String dbName, ArrayList<Workout> layout) {
        ArrayList<ContentValues> values;
        values = getPLObjectsContentValues(layout);
        try {
            delete(dbName);

        } catch (Exception e) {

        }
        if (update) {
            try {
                delete(dbName);
            } catch (Exception e) {
                Log.d("aviv", "saveLayoutToDataBase: " + e.toString());
//                getDb(programHelper).execSQL(programHelper.getLayoutCommand());

            }
        }
        insertData(TABLE_WORKOUTS, values);


    }

  /*  public String createUserTemplate(String name) {
        name = name + String.valueOf(0);
        String newName = tagTable(name);
        getDb(programHelper).execSQL(programHelper.getTemplateCreateCommand(newName));
        return newName;
    }*/


   /* public void createNewProgramTable(String dbName) {
        String newDBName = generateTableName(dbName);
        getDb(programHelper).execSQL(programHelper.getProgramCreateCommand(newDBName));
        ContentValues v = new ContentValues();
        v.put(PROGRAM_NAME, newDBName);
        insertData(TABLE_PROGRAM_REFERENCE, v);
    }*/


    public void delete(String table) {
        //getDb(programHelper).execSQL("delete from " + TABLE_WORKOUTS, null);
        getDb(programHelper).delete(TABLE_WORKOUTS, null, null);
        //getDb(programHelper).delete("SQLITE_SEQUENCE", "NAME = ?", new String[]{table});
    }

    //create a table reference for the created program table
   /* public String generateTableName(String desiredTable) {
        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.US).format(new Date());
        String table = date.replace("-", "_");
        String currentTime = new SimpleDateFormat("HH:mm:ss").format(new Date());
        String time = "_" + currentTime.replace(":", "_");
        String results = desiredTable+"_" + table + time;
        currentProgramTable = results;
        getDb(programHelper).execSQL(programHelper.getLayoutCommand(results));
        insertTableToPrograms(results);
        return results;
    }*/

    //saves the template reference name to the db
    private void insertTableToTemplatesReference(String name) {
        ContentValues v = new ContentValues();
        v.put(TEMPLATE_NAME, name);
        getDb(programHelper).insert(TABLE_TEMPLATES, null, v);
    }

   /* public void insertTemplateData() {

    }*/

    /**
     * creates a new template table with a name.
     * if no name is given, it will generate a table based on date
     */


    //fetchs the last table and it's last digit and adds
    private String tagTable(String tableName) {
        Cursor c = getDb(programHelper).rawQuery("SELECT * FROM " + TABLE_TEMPLATES_REFERENCE, null);
        if (c.getCount() == 0) {
            return tableName;
        }
        c.moveToLast();
        String val = c.getString(c.getColumnIndex(TEMPLATE_NAME));
        String newVal = val.substring(val.length() - 1, val.length());
        int lastNum = Integer.parseInt(newVal) + 1;
        String newTable = tableName.substring(0, tableName.length());
        return newTable + String.valueOf(lastNum);
    }


    //saves the program reference name to db
   /* private void insertTableToPrograms(String date) {
        ContentValues v = new ContentValues();
        v.put(PROGRAM_DATES, date);
        getDb(programHelper).insert(TABLE_PROGRAMS, null, v);
    }*/

    //fetchs the weight in the current table, and adds the added weight
    public void updateWeight(double value, int exercise_id) {
        ContentValues v = new ContentValues();
        double result = value + getWeight(exercise_id);
        v.put(WEIGHT, result);
        try {
            getDb(programHelper).update(currentProgramTable, v, EXERCISE_ID + "=?", new String[]{String.valueOf(exercise_id)});
        } catch (Exception e) {

        }
    }

    //provides with the index of the referenced program table
    private void updateCurrentTables(int last) {
        String getLatestTable = "SELECT * FROM " + TABLE_PROGRAM_REFERENCE;
        Cursor c = getDb(programHelper).rawQuery(getLatestTable, null);
        c.moveToLast();
        for (int i = 0; i < last; i++) {
            c.moveToPrevious();
            c.moveToPrevious();
        }
        if (c.getCount() <= 0) {
            Log.d("aviv", "getCurrentProgramTable:  no such table");
        } else {
            currentProgramTable = c.getString(c.getColumnIndex(DBProgramHelper.PROGRAM_NAME));
            //currentTemplateTable = c.getString(c.getColumnIndex(DBProgramHelper.TEMPLATE_NAME));


        }

    }

    public void getCurrentLayourTable(int backBy) {
        Cursor c;
        String getLatestTable = "SELECT * FROM " + currentProgramTable;
        c = getDb(programHelper).rawQuery(getLatestTable, null);
        c.moveToLast();
        if (c.getCount() <= 0) {
            Log.d("aviv", "getCurrentLayoutTable:  no such table");
        } else {
            currentLayoutTable = c.getString(c.getColumnIndex(LAYOUT_NAME));
        }
    }


    private double getWeight(int id) {
        String sql = "SELECT " + WEIGHT + " FROM " + currentProgramTable + " WHERE " + EXERCISE_ID + "=" + id;
        Cursor c = getDb(programHelper).rawQuery(sql, null);
        c.moveToFirst();
        double val = c.getDouble(c.getColumnIndex(WEIGHT));
        c.close();
        return val;
    }

    public void close() {
        if (getDb(exercisesHelper) != null) {
            db.close();
        }
        if (getDb(programHelper) != null) {
            db.close();
        }
    }


    public Collection<PLObject> parse(String tableNale, Cursor c) {
        return null;
    }

    public void updateProgramName(String progName, String dbName) {
        ContentValues cv = new ContentValues();
        cv.put(PROGRAM_NAME, progName);
        db.update(TABLE_PROGRAM_REFERENCE, cv, LAYOUT_NAME + "= ?", new String[]{dbName});
       /* db.rawQuery("UPDATE "+TABLE_PROGRAM_REFERENCE+" SET "+PROGRAM_NAME+"="+ progName +
                " WHERE "+LAYOUT_NAME+"=?", new String[]{dbName});*/
    }

/*    public ArrayList<Program> getAllPrograms() {
        ArrayList<Program> programs = new ArrayList<>();
        Cursor c = getDb(programHelper).rawQuery("Select * from " + TABLE_PROGRAM_REFERENCE, null);
        if (c != null & c.moveToFirst()) {
            do {
                Program program = new Program(
                        c.getString(c.getColumnIndex(PROGRAM_NAME)),
                        c.getString(c.getColumnIndex(PROGRAM_TIME)),
                        c.getString(c.getColumnIndex(DATE_CREATED)),
                        c.getString(c.getColumnIndex(LAYOUT_NAME))
                );
                programs.add(program);
            }
            while (c.moveToNext());
        }
        return programs;
    }*/

   /* public Program readProgramTable(String currentDbName) {
        String sql = "SELECT * FROM " + TABLE_PROGRAM_REFERENCE + " WHERE " + LAYOUT_NAME + "=?";
        Cursor c = getDb(programHelper).rawQuery(sql, new String[]{currentDbName});
        if (c != null && c.moveToFirst()) {
            try {
                return new Program(
                        c.getString(c.getColumnIndex(PROGRAM_NAME)),
                        c.getString(c.getColumnIndex(PROGRAM_TIME)),
                        c.getString(c.getColumnIndex(DATE_CREATED)),
                        currentDbName
                );
            } catch (Exception E) {
                Log.d("aviv", "readProgramTable: " + E.toString());
            }

        }
        return null;

    }*/

    public Cursor readLayoutTableCursor(String currentDbName) {
        //updateCurrentTables(backBy);
        String sql = "SELECT * FROM " + currentDbName;
        Cursor c = null;
        try {
            c = getDb(programHelper).rawQuery(sql, null);
        } catch (Exception e) {
            return null;
        }
           /* if (c.getCount() >= 0) {
                String currentLayout = c.getString(c.getColumnIndex(LAYOUT_NAME));
                currentLayoutTable = currentLayout;
                c = db.rawQuery("SELECT * FROM " + currentLayout, null);
            }*/
        return c;

        // return c;
    }

  /*  public Cursor readWorkoutTableCursor(int backBy, int workout) {
        updateCurrentTables(backBy);
        String sql = "SELECT * FROM " + currentProgramTable + " WHERE " + DBProgramHelper.WORKOUT_ID + "=?";
        return db.rawQuery(sql, new String[]{String.valueOf(workout)});
    }*/


    //checks if exercise id is already in table, -1 = no id, n = position of id;
  /*  public boolean checkprogressorData(ProgressorManager.Data data) {
        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_PROGRESSOR + " WHERE " + EXERCISE_ID + "=" + data.getExerciseId(),
                null);
        if (c.getCount() <= 0) {
            return false;
        }
        return true;
    }*/


    public ContentValues getTemplateContentValues(ProgramTemplate p) {
        ContentValues v = new ContentValues();
        v.put(TEMPLATE_NAME, p.getTemplateName());
        v.put(BODY_TEMPLATE_STR, bodyTemplateListToString(p.getBodyTemplate()));
        v.put(RECOMMENDED_WORKOUTS, p.getRecommendedWorkoutsPerWeek());
        v.put(ROUNDS_PER_WEEK, p.getRoundsOfWorkoutsPerWeek());
        ArrayList<ArrayList<String>> arr = new ArrayList<>();
        arr.add(p.getWorkoutsNames());
        v.put(WORKOUTS_NAMES_ARRAY, bodyTemplateListToString(arr));
        return v;
    }

    public ContentValues getLayoutReferenceContentValues(String layout) {
        ContentValues v = new ContentValues();
        v.put(LAYOUT_NAME, layout);
        return v;
    }

    public ContentValues getProgramContentValues(Program program) {
        ContentValues v = new ContentValues();
        v.put(DBProgramHelper.LAYOUT_NAME, program.getDbName());
        v.put(DBProgramHelper.DATE_CREATED, program.getProgramDate());
        v.put(DBProgramHelper.PROGRAM_NAME, program.getProgramName());
        v.put(DBProgramHelper.PROGRAM_TIME, program.getTime());

        return v;
    }

    public ArrayList<ContentValues> getPLObjectsContentValues(ArrayList<Workout> w) {
        ArrayList<ContentValues> contentValues = new ArrayList<>();
        ContentValues v;
        for (Workout workout : w) {
            ArrayList<PLObject.ExerciseProfile> p = WorkoutsService.exerciseToPLObject((ArrayList) workout.exArray);
            ContentValues workoutV = new ContentValues();
            workoutV.put(DBExercisesHelper.TYPE, WorkoutLayoutTypes.WorkoutView.ordinal());
            workoutV.put(DBProgramHelper.NAME, workout.workoutName);
            contentValues.add(workoutV);
            for (PLObject ep : p) {
                v = new ContentValues();

                if (ep.type == WorkoutLayoutTypes.BodyView) {
                    v.put(DBExercisesHelper.TYPE, ep.getType().ordinal());
                    v.put(DBProgramHelper.WORKOUT_ID, ep.getWorkoutId());
                    PLObject.ExerciseProfile bodyText = (PLObject.ExerciseProfile) ep;
                    v.put(DBExercisesHelper.NAME, (bodyText.getTitle()));
                    v.put(INNER_TYPE, bodyText.innerType.ordinal());
                    v.put(DBProgramHelper.TITLE, bodyText.getTitle());
                    contentValues.add(v);
                    //    v.put(DBProgramHelper.workou);
                } else if (ep.type == WorkoutLayoutTypes.ExerciseProfile /*|| ep.type == WorkoutLayoutTypes.IntraSet*/) {
                    PLObject.ExerciseProfile exerciseProfile = (PLObject.ExerciseProfile) ep;
                    if (exerciseProfile.getInnerType() == WorkoutLayoutTypes.IntraExerciseProfile) {
                        break;
                    }
                    contentValues.add(saveExercise(exerciseProfile));
                    for (int j = 0; j < exerciseProfile.getSets().size(); j++) {
                        PLObject.SetsPLObject setsPLObject = exerciseProfile.getSets().get(j);
                        contentValues.add(saveSets(setsPLObject));


                        for (int k = 0; k < setsPLObject.superSets.size(); k++) {
                            PLObject.SetsPLObject intraSetPLObject = setsPLObject.superSets.get(k);
                            contentValues.add(saveSets(intraSetPLObject));
                        }
                        for (int k = 0; k < setsPLObject.intraSets.size(); k++) {

                            PLObject.SetsPLObject intraSet = setsPLObject.intraSets.get(k);
                            contentValues.add(saveSets(intraSet));
                        }
                    }

                    for (int j = 0; j < exerciseProfile.exerciseProfiles.size(); j++) {
                        PLObject.ExerciseProfile superset = exerciseProfile.exerciseProfiles.get(j);
                        contentValues.add(saveExercise(superset));

                    }

                }
            }
        }
        return contentValues;
    }

    private ContentValues saveSets(PLObject.SetsPLObject setsPLObject) {
        ContentValues supersetIntraCV;
        supersetIntraCV = new ContentValues();
        supersetIntraCV.put(DBExercisesHelper.TYPE, setsPLObject.getType().ordinal());
        supersetIntraCV.put(DBProgramHelper.WORKOUT_ID, setsPLObject.getWorkoutId());
        supersetIntraCV.put(DBExercisesHelper.TYPE, setsPLObject.getType().ordinal());
        supersetIntraCV.put(INNER_TYPE, setsPLObject.getInnerType().ordinal());
        supersetIntraCV.put(DBProgramHelper.WORKOUT_ID, setsPLObject.getWorkoutId());
        supersetIntraCV.put(REP_ID, setsPLObject.getExerciseSet().getRep());
        supersetIntraCV.put(REST, setsPLObject.getExerciseSet().getRest());
        supersetIntraCV.put(WEIGHT, setsPLObject.getExerciseSet().getWeight());
        return supersetIntraCV;
    }

   /* private ContentValues saveSets(PLObject.IntraSetPLObject intraSetPLObject) {
        ContentValues intraCv;
        intraCv = new ContentValues();
        intraCv.put(DBExercisesHelper.TYPE, intraSetPLObject.getType().ordinal());
        intraCv.put(DBProgramHelper.WORKOUT_ID, intraSetPLObject.getWorkoutId());
        intraCv.put(DBExercisesHelper.TYPE, intraSetPLObject.getType().ordinal());
        intraCv.put(INNER_TYPE, intraSetPLObject.getInnerType().ordinal());
        intraCv.put(DBProgramHelper.WORKOUT_ID, intraSetPLObject.getWorkoutId());
        intraCv.put(REP_ID, intraSetPLObject.getExerciseSet().getRep());
        intraCv.put(REST, intraSetPLObject.getExerciseSet().getRest());
        intraCv.put(WEIGHT, intraSetPLObject.getExerciseSet().getWeight());
        return intraCv;
    }*/

    private ContentValues saveExercise(PLObject.ExerciseProfile exerciseProfile) {
        ContentValues epCV = new ContentValues();
        epCV.put(DBExercisesHelper.TYPE, exerciseProfile.getType().ordinal());
        epCV.put(DBProgramHelper.WORKOUT_ID, exerciseProfile.getWorkoutId());
        if (exerciseProfile.getMuscle() != null) {
            epCV.put(MUSCLE, exerciseProfile.getMuscle().getMuscle_name());
        }
        if (exerciseProfile.getExercise() != null) {
            epCV.put(EXERCISE_ID, exerciseProfile.getExercise().getName());
        }
//        epCV.put(INNER_TYPE, exerciseProfile.getInnerType().ordinal());
        epCV.put(DBProgramHelper.COMMENT, exerciseProfile.comment);
        epCV.put(DEFAULT_INT, exerciseProfile.getDefaultInt());
        return epCV;
    }

    public String getCurrentProgramTable() {
        return currentProgramTable;
    }


    public String bodyTemplateListToString(ArrayList<ArrayList<String>> arr) {
        String phrase = "";
        String w = "%";
        String s = "$";

        for (int i = 0; i < arr.size(); i++) {
            for (int j = 0; j < arr.get(i).size(); j++) {
                phrase += arr.get(i).get(j) + s;
            }
            if (i != arr.size() - 1) {
                phrase += w;

            }

        }
        return phrase;
    }

    public ArrayList<ArrayList<String>> phraseToBodyTemplate(ArrayList<ArrayList<String>> arr, String phrase) {
        int i = 0;
        arr.add(new ArrayList<String>());
        boolean flag = false;
        while (phrase.length() > 0) {
            if (phrase.charAt(i) == '%') {
                phrase = phrase.substring(i + 1, phrase.length());
                arr.add(new ArrayList<String>());
                flag = true;
            }
            if (phrase.charAt(i) == '$') {
                String s = phrase.substring(0, i);
                arr.get(arr.size() - 1).add(s);
                phrase = phrase.substring(i + 1, phrase.length());
                flag = true;
            }
            if (flag) {
                i = -1;
                flag = false;
            }
            i++;

        }

        return arr;
    }
}
