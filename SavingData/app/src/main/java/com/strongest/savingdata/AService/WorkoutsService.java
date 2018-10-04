package com.strongest.savingdata.AService;

import android.arch.lifecycle.MutableLiveData;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.strongest.savingdata.AModels.workoutModel.PLObject;
import com.strongest.savingdata.AModels.workoutModel.Workout;
import com.strongest.savingdata.AModels.workoutModel.WorkoutBro;
import com.strongest.savingdata.AModels.workoutModel.WorkoutHolder;
import com.strongest.savingdata.AModels.workoutModel.WorkoutLayoutTypes;
import com.strongest.savingdata.Adapters.WorkoutItemAdapters.ExerciseItemAdapter;
import com.strongest.savingdata.BaseWorkout.Muscle;
import com.strongest.savingdata.Controllers.CallBacks;
import com.strongest.savingdata.Database.Exercise.Beans;
import com.strongest.savingdata.Database.Exercise.ExerciseSet;
import com.strongest.savingdata.Database.Managers.DataManager;
import com.strongest.savingdata.Database.Program.DBWorkoutHelper;
import com.strongest.savingdata.Utils.FireBaseUtils;

import java.util.ArrayList;
import java.util.List;

import static com.strongest.savingdata.Database.Exercise.DBExercisesHelper.DEFAULT_INT;
import static com.strongest.savingdata.Database.Exercise.DBExercisesHelper.MUSCLE;
import static com.strongest.savingdata.Database.Exercise.DBExercisesHelper.NAME;
import static com.strongest.savingdata.Database.Exercise.DBExercisesHelper.TABLE_EXERCISES_CUSTOM;
import static com.strongest.savingdata.Database.Exercise.DBExercisesHelper.TYPE;
import static com.strongest.savingdata.Database.Exercise.DBExercisesHelper.WEIGHT;
import static com.strongest.savingdata.Database.Program.DBWorkoutHelper.COMMENT;
import static com.strongest.savingdata.Database.Program.DBWorkoutHelper.EXERCISE_ID;
import static com.strongest.savingdata.Database.Program.DBWorkoutHelper.INNER_TYPE;
import static com.strongest.savingdata.Database.Program.DBWorkoutHelper.REP_ID;
import static com.strongest.savingdata.Database.Program.DBWorkoutHelper.REST;
import static com.strongest.savingdata.Database.Program.DBWorkoutHelper.TITLE;

public class WorkoutsService {


    public enum CMD {
        INIT, NEW, SWITCH
    }

    public static final String NO_PROGRAM = "false";
    public static String CURRENT_PROGRAM_DBNAME = "current_program_dbname";
    public static final String CURRENT_WORKOUT = "current_workouts";
    private final SharedPreferences sharedPreferences;
    private final SharedPreferences.Editor editor;
    private UserService userService;
    private Handler handler = new Handler();

    private static final String TAG = "workoutservice";
    private DataManager dataManager;

    public DataManager getDataManager() {
        return dataManager;
    }

    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;

    public WorkoutsService(DataManager dataManager, SharedPreferences sharedPreferences,
                           SharedPreferences.Editor editor, UserService userService) {
        this.sharedPreferences = sharedPreferences;
        this.editor = editor;
        this.userService = userService;
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase
                .getInstance()
                .getReference()
                .child(FireBaseUtils.FIRE_BASE_REFERENCE_WORKOUTS);

        this.dataManager = dataManager;
    }

    public void provideWorktoutsList(MutableLiveData<ArrayList<Workout>> mutableLiveDataWorkout, CMD cmd) {
        String programUID = sharedPreferences.getString(ProgramService.CURRENT_PROGRAM, "");
        ArrayList<Workout> list = null;
        if (programUID.equals("")) {
            return;
        }

        if (cmd == CMD.NEW) {
            list = createDefaultWorkoutsList(programUID);
            mutableLiveDataWorkout.setValue(list);
            return;
        }

        if (cmd == CMD.INIT) {
            if (programUID.equals(getCurrentWorkoutUID())) {
                list = readLayoutFromDataBase(DBWorkoutHelper.TABLE_WORKOUTS);
            }
            if (list != null) {
                mutableLiveDataWorkout.setValue(list);
            } else {
                getWorkoutsFromFireBase(programUID, resultList -> {
                    if (resultList != null) {
                        mutableLiveDataWorkout.setValue((ArrayList<Workout>) resultList);
                    } else {
                        mutableLiveDataWorkout.setValue(createDefaultWorkoutsList(programUID));

                    }
                });
            }
        }
        if (cmd == CMD.SWITCH) {
            getWorkoutsFromFireBase(programUID, (returnedList) -> {
                mutableLiveDataWorkout.setValue((ArrayList<Workout>) returnedList);
            });
        }

    }

    public ArrayList<Workout> createDefaultWorkoutsList(String programUID) {
        ArrayList<Workout> workoutArrayList = new ArrayList<>();
        for (int i = 0; i < 1; i++) {
            Workout w = new Workout();
            w.workoutName = "Workout " + (i + 1);
            if (i == 1) {
                w.exArray.add(new ExerciseItemAdapter().insert());
            }
            workoutArrayList.add(w);
        }

        saveLayoutToDataBase(programUID, false, workoutArrayList, null);
        //mutableLiveDataWorkout.setValue(workoutArrayList);
        return workoutArrayList;
    }

    private boolean isWorkoutMatchProgramUID() {
        return sharedPreferences.getString(CURRENT_WORKOUT, "no")
                .equals(
                        sharedPreferences.getString(ProgramService.CURRENT_PROGRAM, " nope")
                );
    }

    private String getCurrentWorkoutUID() {
        return sharedPreferences.getString(CURRENT_WORKOUT, "");
    }

    private boolean saveProgramNameToSharedPreference(String programUID) {
        return editor.putString(CURRENT_WORKOUT, programUID).commit();
    }


    public void saveWorkoutsToFireBase(String programUID, ArrayList<Workout> workouts) {
        Log.d(TAG, "saveWorkoutsToFireBase: ");
        if (programUID.equals("")) {
            throw new IllegalArgumentException("there must be an program UID");
        }
        if (userService.isUserLoggedIn()) {

            List<WorkoutBro> list = new ArrayList<>();
            for (Workout w : workouts) {
                WorkoutBro workoutBro = new WorkoutBro();
                workoutBro.setExArray(exerciseToPLObject((ArrayList) w.exArray));
                workoutBro.setProgramUid(programUID);
                workoutBro.setWorkoutName(w.workoutName);
                //String key = databaseReference.push().getKey();
                //w.setProgramUid(programUID);
                list.add(workoutBro);
            }

            WorkoutHolder wor = new WorkoutHolder();
            wor.setWorkouts(list);
            try {

                databaseReference
                        .child(programUID)
                        .setValue(wor)
                        .addOnCompleteListener(task -> {

                            Log.d("aviv", "saveWorkoutsToFireBase: workout saved!");
                        });

            } catch (Exception e) {
                throw new IllegalArgumentException(e.toString());
            }
        }
    }

    public void getWorkoutsFromFireBase(String programUID, CallBacks.OnFinish onFinish) {
        databaseReference.child(programUID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d(TAG, "onDataChange: ");
                if (dataSnapshot.getValue() != null) {
                    WorkoutHolder wor = dataSnapshot.getValue(WorkoutHolder.class);
                    saveLayoutToDataBase(programUID, true, workoutBroParser(wor), (intResult) -> {
                        ArrayList<Workout> list = readLayoutFromDataBase("");

                        onFinish.onFinish(list);

                    });

                } else {
                    // createDefaultWorkoutsList(mutableLiveDataWorkout, programUID);
                    onFinish.onFinish(createDefaultWorkoutsList(programUID));
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Log.d("aviv", "onCancelled: didn't retrieve");
            }
        });

    }

    private ArrayList<Workout> workoutBroParser(WorkoutHolder workoutHolder) {
        ArrayList<Workout> list = new ArrayList<>();
        for (WorkoutBro wor : workoutHolder.getWorkouts()) {
            Workout w = new Workout();
            w.setProgramUid(wor.getProgramUid());
            w.setExArray(exerciseToPLObject(wor.getExArray()));
            w.setWorkoutName(wor.getWorkoutName());
            list.add(w);
        }
        return list;
    }

    public static ArrayList<PLObject> exerciseToPLObject(List<PLObject.ExerciseProfile> to) {
        ArrayList<PLObject> list = new ArrayList<>();
        if (to == null) {
            to = new ArrayList<>();
        }
        list.addAll(to);
        return list;
    }

    public void duplicateWorkouts(String originalKey, String duplicatedKey, CallBacks.OnFinish onFinish) {
        getWorkoutsFromFireBase(originalKey, list -> {
            saveLayoutToDataBase(duplicatedKey, false, (ArrayList<Workout>) list, noResult -> {
                onFinish.onFinish(null);
            });
        });
    }

    public void saveCurrentWorkouts(boolean b, ArrayList<Workout> value, CallBacks.OnFinish o) {
        saveLayoutToDataBase(getCurrentWorkoutUID(), b, value, o);
    }


    public boolean saveLayoutToDataBase(String programUID, final boolean update, ArrayList<Workout> layout,
                                        CallBacks.OnFinish onFinish) {
        Log.d(TAG, "saveLayoutToDataBase: ");
        saveWorkoutsToFireBase(programUID, layout);
        saveProgramNameToSharedPreference(programUID);
        new Thread(new Runnable() {
            @Override
            public void run() {
                dataManager.getWorkoutDataManager().insertTables(update, DBWorkoutHelper.TABLE_WORKOUTS, layout);
                if (onFinish != null) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            onFinish.onFinish(1);
                        }
                    });
                }

            }
        }).start();
        return true;
    }


    public void deleteWorkout(String key) {
        databaseReference.child(key).setValue(null);
    }

    public ArrayList<Workout> readLayoutFromDataBase(String currentDbName) {
        Log.d(TAG, "readLayoutFromDataBase: ");
        ArrayList<Workout> workoutsList = new ArrayList<>();
        Cursor c = dataManager.getWorkoutDataManager().readLayoutTableCursor(DBWorkoutHelper.TABLE_WORKOUTS);
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
        ArrayList<PLObject.ExerciseProfile> exArray = null;


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
                        workoutIndex = workoutsList.size() - 1;
                        workoutsList.get(workoutIndex).setWorkoutName(wName);

                        /*bodyTemplate.add(new ArrayList<String>());
                        drawWorkout(layout, wName);
                        workoutsNames.add(wName);*/
                        break;
                    case BodyView:
                        String bName = c.getString(c.getColumnIndex(NAME));
                        PLObject.ExerciseProfile bodyText = PLObject.ExerciseProfile.getBodyTextInstance();
                        bodyText.setTitle(c.getString(c.getColumnIndex(TITLE)));
                        exArray.add(bodyText);

                        //TODO: FIX THIS!!!
                        //workoutsList.get(workoutIndex).exArray.add(new PLObject.BodyText(bName));
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
                            currentParent.exerciseProfiles.add(ep);
                            //ep.setParent(currentParent);
                        }

                        //again for the new workoutList implementation
                        //exArray.add(ep);


                        break;
                    case SetsPLObject:

                        innerType = WorkoutLayoutTypes.getEnum(c.getInt(c.getColumnIndex(INNER_TYPE)));
                        int pos = workoutsList.get(workoutIndex).exArray.size() - 1;
                        ep = ((PLObject.ExerciseProfile) workoutsList.get(workoutIndex).exArray.get(pos));
                        //  PLObject.ExerciseProfile setParent = null;
                       /* if (ep.getParent() != null) {
                            setParent = ep.getParent();
                        } else {
                            setParent = ep;
                        }*/
                        rep = c.getString(c.getColumnIndex(REP_ID));
                        rest = c.getString(c.getColumnIndex(REST));
                        weight = c.getDouble(c.getColumnIndex(WEIGHT));
                        exerciseSet = new ExerciseSet();
                        exerciseSet.setRep(rep);
                        exerciseSet.setRest(rest);
                        exerciseSet.setWeight(weight);
                        PLObject.SetsPLObject setsPLObjectSet = new PLObject.SetsPLObject(exerciseSet);
                        setsPLObjectSet.setInnerType(innerType);
                        ep.getSets().add(setsPLObjectSet);
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

                        int position = ep.getSets().size() - 1;
                        ep.getSets().get(position).superSets.add(intra2);
                        break;

                }

                //add the array to the workoutList
                workoutsList.get(workoutIndex).exArray = exerciseToPLObject(exArray);
            } while (c.moveToNext());
            return workoutsList;
        }

        return null;
    }



  /*  //trys to instantiate a program from an existing program in db
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
    }*/


    //deprecated

    /*public void saveProgramToDatabase(Program program) {
        dataManager.getProgramDataManager().insertData(
                DBProgramHelper.TABLE_PROGRAM_REFERENCE,
                dataManager.getProgramDataManager().getProgramContentValues(program));
        saveProgramNameToSharedPreference(program.getDbName());

    }*/


}
