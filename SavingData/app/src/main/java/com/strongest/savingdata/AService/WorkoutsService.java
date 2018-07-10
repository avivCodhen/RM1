package com.strongest.savingdata.AService;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.strongest.savingdata.AModels.AlgorithmLayout.PLObject;
import com.strongest.savingdata.AModels.AlgorithmLayout.Workout;
import com.strongest.savingdata.AModels.AlgorithmLayout.WorkoutLayoutTypes;
import com.strongest.savingdata.AModels.AlgorithmLayout.WorkoutsModel;
import com.strongest.savingdata.BaseWorkout.Muscle;
import com.strongest.savingdata.BaseWorkout.Program;
import com.strongest.savingdata.Database.Exercise.Beans;
import com.strongest.savingdata.Database.Exercise.ExerciseSet;
import com.strongest.savingdata.Database.Managers.DataManager;
import com.strongest.savingdata.Database.Program.DBProgramHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import static com.strongest.savingdata.Activities.BaseActivity.TAG;
import static com.strongest.savingdata.Database.Exercise.DBExercisesHelper.DEFAULT_INT;
import static com.strongest.savingdata.Database.Exercise.DBExercisesHelper.MUSCLE;
import static com.strongest.savingdata.Database.Exercise.DBExercisesHelper.NAME;
import static com.strongest.savingdata.Database.Exercise.DBExercisesHelper.TABLE_EXERCISES_CUSTOM;
import static com.strongest.savingdata.Database.Exercise.DBExercisesHelper.TYPE;
import static com.strongest.savingdata.Database.Exercise.DBExercisesHelper.WEIGHT;
import static com.strongest.savingdata.Database.Program.DBProgramHelper.COMMENT;
import static com.strongest.savingdata.Database.Program.DBProgramHelper.EXERCISE_ID;
import static com.strongest.savingdata.Database.Program.DBProgramHelper.INNER_TYPE;
import static com.strongest.savingdata.Database.Program.DBProgramHelper.REP_ID;
import static com.strongest.savingdata.Database.Program.DBProgramHelper.REST;

public class WorkoutsService {
    public static final String NO_PROGRAM = "false";
    public static String CURRENT_PROGRAM_DBNAME = "current_program_dbname";


    private DataManager dataManager;

    public DataManager getDataManager() {
        return dataManager;
    }

    public WorkoutsService(DataManager dataManager) {

        this.dataManager = dataManager;
    }

    public boolean saveLayoutToDataBase(final boolean update, ArrayList<Workout> layout, String dbName) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // requestSplitToLayout();
                try {

                } catch (Exception e) {
                    throw new IllegalArgumentException("could not save layout to database " + e.toString());
                }
                dataManager.getProgramDataManager().insertTables(update, dbName, layout);
            }
        }).start();
        return true;
    }


    public ArrayList<Workout> readLayoutFromDataBase(String currentDbName) {
        ArrayList<Workout> workoutsList = new ArrayList<>();
        Cursor c = dataManager.getProgramDataManager().readLayoutTableCursor(currentDbName);
        //layout = new ArrayList<>();
        String muscle_str;
        WorkoutLayoutTypes type;
        WorkoutLayoutTypes innerType;
        String rep;
        String rest;
        double weight;
        ExerciseSet exerciseSet;
        PLObject.ExerciseProfile ep;
        PLObject.ExerciseProfile currentParent = null;

        //for the workoutList implementation
        ArrayList<PLObject> exArray = null;


        int workoutIndex = 0;
        if (c != null && c.moveToFirst()) {
            do {
                type = WorkoutLayoutTypes.getEnum(c.getInt(c.getColumnIndex(TYPE)));
                switch (type) {
                    case WorkoutView:
                        String wName = c.getString(c.getColumnIndex(NAME));

                        //for the workoutList implementation
                        workoutsList.add(new Workout());
                        exArray = new ArrayList<>();
                        workoutIndex = workoutsList.size()-1;
                        workoutsList.get(workoutIndex).setWorkoutName(wName);

                        /*bodyTemplate.add(new ArrayList<String>());
                        drawWorkout(layout, wName);
                        workoutsNames.add(wName);*/
                        break;
                    case BodyView:
                        String bName = c.getString(c.getColumnIndex(NAME));
                        workoutsList.get(workoutIndex).exArray.add(new PLObject.BodyText(bName));
                        /*drawBody(exArray, bName);
                        drawBody(layout, bName);*/
                        break;
                    case ExerciseProfile:
                        //   ExerciseProfile parent = null;
                        innerType = WorkoutLayoutTypes
                                .getEnum(c.getInt(c.getColumnIndex(INNER_TYPE)));

                        Muscle muscle = null;
                        muscle_str = c.getString(c.getColumnIndex(MUSCLE));
                        try {
                            muscle = Muscle.createMuscle(dataManager.getMuscleDataManager(), muscle_str);
                        } catch (Exception e) {
                            Log.d("aviv", "readLayoutFromDataBase: either null or no muscle");
                        }

                        ep = new PLObject.ExerciseProfile();
                        ep.type = type;
                        ep.isParent = true;
                        ep.setMuscle(muscle);
                        ep.setInnerType(innerType);
                        String exId = c.getString(c.getColumnIndex(EXERCISE_ID));
                        int default_int = c.getInt(c.getColumnIndex(DEFAULT_INT));
                        if (default_int == 1) {
                            ep.setExercise(dataManager.getExerciseDataManager().fetchByName(TABLE_EXERCISES_CUSTOM, exId));

                        } else {
                            if (exId != null) {
                                Beans exercise = dataManager.getExerciseDataManager().fetchByName(muscle.getMuscle_name(), exId);
                                ep.setExercise(exercise);
                            }
                        }
                        ep.comment = c.getString(c.getColumnIndex(COMMENT));
                        ep.showComment = !ep.comment.equals("");
                      /*  if (parent != null) {
                            parent.getExerciseProfiles().add(ep);
                            ep.setParent(parent);
                        }*/

                        //again for the new workoutList implementation
                        currentParent = ep;
                        exArray.add(ep);
                        break;

                    case IntraExerciseProfile:
                        innerType = WorkoutLayoutTypes
                                .getEnum(c.getInt(c.getColumnIndex(INNER_TYPE)));

                        Muscle muscle2 = null;
                        muscle_str = c.getString(c.getColumnIndex(MUSCLE));
                        try {
                            muscle2 = Muscle.createMuscle(dataManager.getMuscleDataManager(), muscle_str);
                        } catch (Exception e) {
                            Log.d("aviv", "readLayoutFromDataBase: either null or no muscle");
                        }

                        ep = new PLObject.ExerciseProfile();
                        ep.type = type;
                        ep.isParent = true;
                        ep.setMuscle(muscle2);
                        ep.setInnerType(innerType);
                        String exId2 = c.getString(c.getColumnIndex(EXERCISE_ID));
                        int default_int2 = c.getInt(c.getColumnIndex(DEFAULT_INT));
                        if (default_int2 == 1) {
                            ep.setExercise(dataManager.getExerciseDataManager().fetchByName(TABLE_EXERCISES_CUSTOM, exId2));

                        } else {
                            if (exId2 != null) {
                                Beans exercise = dataManager.getExerciseDataManager().fetchByName(muscle2.getMuscle_name(), exId2);
                                ep.setExercise(exercise);
                            }
                        }
                        ep.comment = c.getString(c.getColumnIndex(COMMENT));
                        ep.showComment = !ep.comment.equals("");
                        if (currentParent != null) {
                            currentParent.getExerciseProfiles().add(ep);
                            ep.setParent(currentParent);
                        }

                        //again for the new workoutList implementation
                        //exArray.add(ep);


                        break;
                    case SetsPLObject:

                        innerType = WorkoutLayoutTypes.getEnum(c.getInt(c.getColumnIndex(INNER_TYPE)));
                        int pos = workoutsList.get(workoutIndex).exArray.size() - 1;
                        ep = ((PLObject.ExerciseProfile) workoutsList.get(workoutIndex).exArray.get(pos));
                        PLObject.ExerciseProfile setParent = null;
                        if (ep.getParent() != null) {
                            setParent = ep.getParent();
                        } else {
                            setParent = ep;
                        }
                        rep = c.getString(c.getColumnIndex(REP_ID));
                        rest = c.getString(c.getColumnIndex(REST));
                        weight = c.getDouble(c.getColumnIndex(WEIGHT));
                        exerciseSet = new ExerciseSet();
                        exerciseSet.setRep(rep);
                        exerciseSet.setRest(rest);
                        exerciseSet.setWeight(weight);
                        PLObject.SetsPLObject setsPLObjectSet = new PLObject.SetsPLObject(setParent, exerciseSet);
                        setsPLObjectSet.setInnerType(innerType);
                        setParent.getSets().add(setsPLObjectSet);
                        setsPLObjectSet.isParent = true;
                        setsPLObjectSet.type = type;
                        break;
                    case IntraSet:
                        /**
                         * getting the last exercise profile in the list
                         * */
                        int last = workoutsList.get(workoutIndex).exArray.size() - 1;
                        ep = ((PLObject.ExerciseProfile) workoutsList.get(workoutIndex).exArray.get(last));


                        innerType = WorkoutLayoutTypes.getEnum(c.getInt(c.getColumnIndex(INNER_TYPE)));
                        rep = c.getString(c.getColumnIndex(REP_ID));
                        rest = c.getString(c.getColumnIndex(REST));
                        weight = c.getDouble(c.getColumnIndex(WEIGHT));
                        exerciseSet = new ExerciseSet();
                        exerciseSet.setRep(rep);
                        exerciseSet.setRest(rest);
                        exerciseSet.setWeight(weight);
                        PLObject.SetsPLObject intra = new PLObject.SetsPLObject();
                        intra.setExerciseSet(exerciseSet);
                        intra.setInnerType(innerType);
                        intra.type = type;

                        /**
                         * getting the last set from the exercise array
                         * */
                        int position = ep.getSets().size() - 1;


                        intra.parent = ep;
                        intra.setParent = ep.getSets().get(position);
                        ep.getSets().get(position).intraSets.add(intra);

                        break;

                    case SuperSetIntraSet:

                        int last2 = workoutsList.get(workoutIndex).exArray.size() - 1;
                        ep = ((PLObject.ExerciseProfile) workoutsList.get(workoutIndex).exArray.get(last2));


                        innerType = WorkoutLayoutTypes.getEnum(c.getInt(c.getColumnIndex(INNER_TYPE)));
                        rep = c.getString(c.getColumnIndex(REP_ID));
                        rest = c.getString(c.getColumnIndex(REST));
                        weight = c.getDouble(c.getColumnIndex(WEIGHT));
                        exerciseSet = new ExerciseSet();
                        exerciseSet.setRep(rep);
                        exerciseSet.setRest(rest);
                        exerciseSet.setWeight(weight);
                        PLObject.SetsPLObject intra2 = new PLObject.SetsPLObject();
                        intra2.setExerciseSet(exerciseSet);
                        intra2.setInnerType(innerType);
                        intra2.type = type;
                        ep.intraSets.add(intra2);
                        break;

                }

                //add the array to the workoutList
                workoutsList.get(workoutIndex).exArray = exArray;
            } while (c.moveToNext());
            return workoutsList;
        }

        return null;
    }

    //trys to instantiate a program from an existing program in db
    //returns the program database name to be used by the layout manager to fetch the program layout
    //returns the NO_PROGRAM value if no program was found in the database
    //and creates a new program
    public Program fetchProgram() {
        Program program;
        String dbName = dataManager.getPrefs().getString(CURRENT_PROGRAM_DBNAME, NO_PROGRAM);
        if (!dbName.equals(NO_PROGRAM)) {
            program = dataManager.getProgramDataManager().readProgramTable(dbName);
            if (program != null) {
                return program;
            }
        }
        return null;
    }

    private boolean saveProgramNameToSharedPreference(String dbName) {
        return dataManager.getPrefsEditor().putString(CURRENT_PROGRAM_DBNAME, dbName).commit();
    }

    public void saveProgramToDatabase(Program program) {
        dataManager.getProgramDataManager().insertData(
                DBProgramHelper.TABLE_PROGRAM_REFERENCE,
                dataManager.getProgramDataManager().getProgramContentValues(program));
        saveProgramNameToSharedPreference(program.dbName);

    }


}
