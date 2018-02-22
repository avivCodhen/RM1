package com.strongest.savingdata.Database.Managers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.strongest.savingdata.AlgorithmLayout.LayoutManager;
import com.strongest.savingdata.BaseWorkout.ProgramTemplate;
import com.strongest.savingdata.Database.Exercise.BeansHolder;
import com.strongest.savingdata.Database.Exercise.DBExercisesHelper;
import com.strongest.savingdata.Database.Program.DBProgramHelper;
import com.strongest.savingdata.AlgorithmLayout.PLObjects;

import java.util.ArrayList;
import java.util.Collection;

import static com.strongest.savingdata.Database.Exercise.DBExercisesHelper.*;
import static com.strongest.savingdata.Database.Program.DBProgramHelper.BODY_TEMPLATE_STR;
import static com.strongest.savingdata.Database.Program.DBProgramHelper.EXERCISE_ID;
import static com.strongest.savingdata.Database.Program.DBProgramHelper.EXERCISE_PROFILE_ID;
import static com.strongest.savingdata.Database.Program.DBProgramHelper.INNER_TYPE;
import static com.strongest.savingdata.Database.Program.DBProgramHelper.LAYOUT_NAME;
import static com.strongest.savingdata.Database.Program.DBProgramHelper.PROGRAM_NAME;
import static com.strongest.savingdata.Database.Program.DBProgramHelper.RECOMMENDED_WORKOUTS;
import static com.strongest.savingdata.Database.Program.DBProgramHelper.REP_ID;
import static com.strongest.savingdata.Database.Program.DBProgramHelper.REST;
import static com.strongest.savingdata.Database.Program.DBProgramHelper.ROUNDS_PER_WEEK;
import static com.strongest.savingdata.Database.Program.DBProgramHelper.SETS;
import static com.strongest.savingdata.Database.Program.DBProgramHelper.SUPERSET;
import static com.strongest.savingdata.Database.Program.DBProgramHelper.TABLE_LAYOUT_REFERENCE;
import static com.strongest.savingdata.Database.Program.DBProgramHelper.TABLE_PROGRAM_REFERENCE;
import static com.strongest.savingdata.Database.Program.DBProgramHelper.TABLE_TEMPLATES;
import static com.strongest.savingdata.Database.Program.DBProgramHelper.TABLE_TEMPLATES_REFERENCE;
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

    public void insertData(String tableName, ContentValues[] contentValues) {
        for (ContentValues v : contentValues) {
            getDb(programHelper).insert(tableName,
                    null,
                    v);

        }
    }

    public void insertTables(boolean update, Object obj, Tables... tables) {
        ContentValues v;
        ContentValues[] values;
        LayoutManager lm = null;
        ProgramTemplate p = null;
        for (Tables table : tables) {
            switch (table) {
                case PROGRAM:
                    lm = (LayoutManager) obj;
                    p = lm.getProgramTemplate();
                    values = getPLObjectsContentValues(lm.getLayout());
                    //creates a new layout table
                    getDb(programHelper).execSQL(programHelper.getLayoutCommand(lm.getDbName()));
                    v = getLayoutReferenceContentValues(p.getDbName(), lm.getDbName());

                    //inserts name to layout reference
                    insertData(TABLE_LAYOUT_REFERENCE, new ContentValues[]{v});
                    updateCurrentTables(0);
                    //creates reference in current program table
                    insertData(currentProgramTable, new ContentValues[]{v});
                    //inserts data to the new layout table
                    insertData(lm.getDbName(), values);
                    break;
                case TEMPLATE:
                    if (p == null) {
                        p = (ProgramTemplate) obj;
                    }
                    v = getTemplateContentValues(p);
                    getDb(programHelper).execSQL(programHelper.getTemplateCreateCommand(p.getDbName()));
                    insertData(p.getDbName(), new ContentValues[]{v});

            }
        }
        if (update) {

        }

       /* String newName = createUserTemplate(p.getTemplateName());
        insertTableToTemplates(newName);*/
    }

  /*  public String createUserTemplate(String name) {
        name = name + String.valueOf(0);
        String newName = tagTable(name);
        getDb(programHelper).execSQL(programHelper.getTemplateCreateCommand(newName));
        return newName;
    }*/


    public void createNewProgramTable(String dbName) {
        String newDBName = generateTableName(dbName);
        getDb(programHelper).execSQL(programHelper.getProgramCreateCommand(newDBName));
        ContentValues v = new ContentValues();
        v.put(PROGRAM_NAME, newDBName);
        insertData(TABLE_PROGRAM_REFERENCE, new ContentValues[]{v});
    }


    public void delete(String table) {
        getDb(programHelper).delete(table, null, null);
        getDb(programHelper).delete("SQLITE_SEQUENCE", "NAME = ?", new String[]{table});
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

    private SQLiteDatabase getDb(SQLiteOpenHelper helper) {
        db = helper.getWritableDatabase();
        return db;
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


    public Collection<PLObjects> parse(String tableNale, Cursor c) {
        return null;
    }

    public Cursor readLayoutTableCursor(int backBy) {
        updateCurrentTables(backBy);

        if (currentProgramTable != null) {
            String sql = "SELECT * FROM " + currentProgramTable + " ORDER BY ID ASC";
            Cursor c = null;
            try {
                c = db.rawQuery(sql, null);
                c.moveToLast();

            } catch (Exception e) {
                e.toString();
            }
            if (c.getCount() >= 0) {
                String currentLayout = c.getString(c.getColumnIndex(LAYOUT_NAME));
                c = db.rawQuery("SELECT * FROM " + currentLayout, null);
            }
            /*c.moveToNext();
            String laySQL = sql.replace(currentProgramTable, c.getString(c.getColumnIndex(LAYOUT_NAME)));
            String tempSQL = sql.replace(currentProgramTable, c.getString(c.getColumnIndex(TEMPLATE_NAME)));*/
            return c;
        } else {
            return null;
        }
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

    public ContentValues getLayoutReferenceContentValues(String template, String layout) {
        ContentValues v = new ContentValues();
        v.put(LAYOUT_NAME, layout);
        return v;
    }

    public ContentValues[] getPLObjectsContentValues(ArrayList<PLObjects> p) {
        ContentValues[] contentValues = new ContentValues[p.size()];
        ContentValues v;
        int i = 0;
        for (PLObjects ep : p) {
            v = new ContentValues();
            v.put(DBExercisesHelper.TYPE, ep.getType().ordinal());
            v.put(DBProgramHelper.WORKOUT_ID, ep.getWorkoutId());
            switch (ep.getType()) {
                case WorkoutView:
                    PLObjects.WorkoutText workoutText = (PLObjects.WorkoutText) ep;
                    v.put(DBProgramHelper.NAME, workoutText.getWorkoutName());
                    //       v.put(DBProgramHelper.WORKOUT_ID, workoutText.getWorkoutId());
                    break;
                case BodyView:
                    PLObjects.BodyText bodyText = (PLObjects.BodyText) ep;
                    v.put(DBExercisesHelper.MUSCLE, (bodyText.getMuscle().getMuscle_name()));
                    //    v.put(DBProgramHelper.workou);
                    break;
                case ExerciseView:

                    PLObjects.ExerciseProfile exerciseProfile = (PLObjects.ExerciseProfile) ep;
                    if (exerciseProfile.getBeansHolders() != null || exerciseProfile.isFirstExercise()) {
                        BeansHolder beansHolder = exerciseProfile.getBeansHolders().get(0);
                        v.put(EXERCISE_PROFILE_ID, exerciseProfile.getExerciseProfileId());
                        v.put(MUSCLE, exerciseProfile.getmBeansHolder().getExercise().getMuscle().getMuscle_name());
                        if (exerciseProfile.getBeansHolders().get(0) != null) {


                        /*
                        * ID has been changed from saving the bean id, to saving the bean name
                        * */

                            //v.put(EXERCISE_ID, beansHolder.getExercise().getBean());
                            v.put(EXERCISE_ID, beansHolder.getExercise().getName());
                            //v.put(REP_ID, beansHolder.getRep().getBean());
                            v.put(REP_ID, beansHolder.getRep().getName());

                        /*if (beansHolder.getMethod() != null)
                            v.put(METHOD_ID, beansHolder.getMethod().getBean());
*/
                            v.put(INNER_TYPE, exerciseProfile.getInnerType().ordinal());
                            v.put(SETS, beansHolder.getSets().getName());
                            v.put(WEIGHT, beansHolder.getWeight());
                            v.put(REST, beansHolder.getRest().getName());
                            // v.put(SUPERSET, beansHolder.getSuperset());
                            if (beansHolder.getSuperset() != null) {
                                v.put(SUPERSET, beansHolder.getSuperset().getId());
                            } else {
                                v.put(SUPERSET, -1);

                            }
                        }
                    }
                    // v.put(FIRST_EXERCISE, exerciseProfile.isFirstExercise());
                    break;
            }
            contentValues[i] = v;
            i++;
        }
        return contentValues;
    }

    /*  private ContentValues getProgressorContentValues(ProgressorManager.Data data) {
          ContentValues v = new ContentValues();
          v.put(EXERCISE_ID, data.getExerciseId());
          v.put(SETS, data.getNumberOfSets());
          v.put(REPS, data.getRepsPerSet());
          v.put(REST, data.getRestsPerSet());
          v.put(WEIGHT, data.getWeightPerSet());
          v.put(DATE, data.getDate());
          return v;

      }
  */
  /*  public void setCurrentTable(int backBy) {
        updateCurrentTables(backBy);
    }*/

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
