package com.strongest.savingdata.Database.Managers;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.util.Log;

import com.strongest.savingdata.Database.Progress.ProgressDataManager;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.inject.Inject;

/**
 * Created by Cohen on 10/16/2017.
 */

public class DataManager {


    public DefaultWorkoutsDataManager getDefaultWorkoutsDataManager() {
        if (defaultWorkoutsDataManager == null) {
            defaultWorkoutsDataManager = new DefaultWorkoutsDataManager(context);
        }
        return defaultWorkoutsDataManager;
    }

    public enum Data {
        ARTICLE, EXERCISE, PROGRAM, STATS, ALL
    }

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    public static final String PREFS = "sharedprefs";

    public static final String WORKOUT_ORDER = "workoutorder";
    public static final String ROUTINE = "routine";
    public static final String EXERCISE_LEVEL = "exercise level";
    public static final String WORKOUT_VOLUME = "workout volume";
    public static final String LEVEL_INT = "level";
    public static final String EXERCISE_FILE = "ex.txt";
    public static final String PROGRAM_FILE = "program.txt";
    public static final String PROGRAM_VALUE = "program_value";


    private ExercisesDataManager exerciseDataManager;
    private ArticleDataManager articleDataManager;
    private WorkoutDataManager workoutDataManager;
    private ProgressDataManager progressDataManager;
    private MusclesDataManager musclesDataManager;
    private WeightToolsDataManager weightToolsDataManager;
    private DefaultWorkoutsDataManager defaultWorkoutsDataManager;

    @Inject
    public Context context;

    @Inject
    public DataManager(Context context) {
        this.context = context;
    }

    public ExercisesDataManager getExerciseDataManager() {
        if (exerciseDataManager == null) {
            exerciseDataManager = new ExercisesDataManager(this, context);
            return exerciseDataManager;
        } else {
            return exerciseDataManager;
        }
    }

    public ArticleDataManager getArticleDataManager() {
        if (articleDataManager == null) {
            articleDataManager = new ArticleDataManager(context);
            return articleDataManager;
        } else {
            return articleDataManager;
        }
    }

    public WorkoutDataManager getWorkoutDataManager() {
        if (workoutDataManager == null) {
            workoutDataManager = new WorkoutDataManager(getExerciseDataManager(), context);
            return workoutDataManager;
        } else {
            return workoutDataManager;

        }

    }

    public ProgressDataManager getProgressDataManager() {
        if (progressDataManager == null) {
            progressDataManager = new ProgressDataManager(context, getWorkoutDataManager(), getExerciseDataManager());
            return progressDataManager;
        } else {
            return progressDataManager;
        }
    }


    public MusclesDataManager getMuscleDataManager() {
        if (musclesDataManager != null) {
            return musclesDataManager;
        } else {
            musclesDataManager = new MusclesDataManager(context);
            return musclesDataManager;
        }

    }

    public WeightToolsDataManager getWeightToolsDataManager() {
        if (weightToolsDataManager != null) {
            return weightToolsDataManager;
        } else {
            weightToolsDataManager = new WeightToolsDataManager(context);
            return weightToolsDataManager;
        }

    }


    public void closeDataBases() {
        if (exerciseDataManager != null) {
            exerciseDataManager.close();
            exerciseDataManager = null;
        }
        if (workoutDataManager != null) {
            workoutDataManager.close();
            workoutDataManager = null;
        }

        if (articleDataManager != null) {
            articleDataManager.close();
            articleDataManager = null;
        }

        if (weightToolsDataManager != null) {
            weightToolsDataManager.close();
            weightToolsDataManager = null;
        }


    }


    public void saveObjectToFile(Object obj, String file) {
        try {
            FileOutputStream fou = context.openFileOutput(file, Context.MODE_PRIVATE);
            ObjectOutputStream out = new ObjectOutputStream(fou);
            out.writeObject(obj);
            out.close();
            fou.close();
        } catch (FileNotFoundException e) {
            Log.d("aviv", "saveObjectToFile: " + e.toString());
            e.printStackTrace();
        } catch (IOException e) {
            Log.d("aviv", "second error: " + e.toString());
            e.printStackTrace();
        }
    }

    public Object readObjectFromFile(String file) {
        Object obj = null;
        try {
            FileInputStream in = context.openFileInput(file);
            ObjectInputStream objectIn = new ObjectInputStream(in);
            obj = objectIn.readObject();
            in.close();
            objectIn.close();


        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.d("aviv", "First readObjectFromFile: " + e.toString());

        } catch (ClassNotFoundException e) {
            Log.d("aviv", "Second readObjectFromFile: " + e.toString());

            e.printStackTrace();
        } catch (IOException e) {
            Log.d("aviv", "Third readObjectFromFile: " + e.toString());
            e.printStackTrace();
        }
        return obj;
    }

    public SharedPreferences getPrefs() {
        return context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
    }

    public SharedPreferences.Editor getPrefsEditor() {
        return editor = getPrefs().edit();
    }


}
