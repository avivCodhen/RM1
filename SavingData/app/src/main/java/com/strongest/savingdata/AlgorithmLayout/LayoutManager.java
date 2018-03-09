package com.strongest.savingdata.AlgorithmLayout;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.strongest.savingdata.AlgorithmStats.StatsCalculatorManager;
import com.strongest.savingdata.BaseWorkout.Muscle;
import com.strongest.savingdata.BaseWorkout.ProgramTemplate;
import com.strongest.savingdata.Database.Exercise.BeansHolder;
import com.strongest.savingdata.Database.Managers.DataManager;
import com.strongest.savingdata.Database.Managers.ProgramDataManager;

import java.util.ArrayList;
import java.util.Collections;


import static com.strongest.savingdata.Database.Exercise.DBExercisesHelper.*;
import static com.strongest.savingdata.Database.Exercise.DBExercisesHelper.TYPE;
import static com.strongest.savingdata.Database.Program.DBProgramHelper.EXERCISE_ID;
import static com.strongest.savingdata.Database.Program.DBProgramHelper.INNER_TYPE;
import static com.strongest.savingdata.Database.Program.DBProgramHelper.REP_ID;
import static com.strongest.savingdata.Database.Program.DBProgramHelper.REST;
import static com.strongest.savingdata.Database.Program.DBProgramHelper.SETS;

/**
 * Created by Cohen on 10/18/2017.
 */

public class LayoutManager {

    public String getLayoutName() {
        return layoutName;
    }

    public void setLayoutName(String layoutName) {
        this.layoutName = layoutName;
    }

    public String getDbName() {
        return dbName;
    }

    public ArrayList<PLObjects> getWorkouts(boolean update) {
        if (update) {
            workouts = null;
        } else if (workouts != null) {
            return workouts;
        }
        ArrayList<PLObjects> arr = new ArrayList<>();
        for (int i = 0; i < layout.size(); i++) {
            if (layout.get(i).getType() == WorkoutLayoutTypes.WorkoutView) {
                arr.add(layout.get(i));
            }
        }
        workouts = arr;
        return workouts;
    }

    public static final String
            NEW_EXERCISE = "new_exercise",
            NEW_EXERCISE_FLIPPED = "new_exercise_fliped",
            DRAW_DIVIDER = "draw_divider",
            NEW_WORKOUT = "new_workout",
            ATTACH_SUPERSET = "attach_superset",
            ATTACH_DROPSET = "attach_dropset",
            DELETE_WORKOUT = "delete_workout",
            SWAP = "swap",
            REMOVE = "remove";


    private String dbName;
    private String layoutName;
    private int currentPosition;
    private int numOfWorkouts;
    private int numOfBodyParts;
    private int sizeOfWorkouts;
    private int numOfExercises;

    private ArrayList<ArrayList<String>> bodyTemplate = new ArrayList<>();
    private ArrayList<String> workoutsNames = new ArrayList<>();
    private ArrayList<PLObjects.BodyText> allBodys;
    public Context context;
    private ArrayList<PLObjects> layout = new ArrayList<>();
    private ProgramTemplate programTemplate;
    private ArrayList<ArrayList<PLObjects>> splitRecyclerWorkouts;
    private ArrayList<ArrayList<PLObjects>> splitCleanWorkouts;
    private ArrayList<ArrayList<PLObjects>> exercisesPerWorkouts;
    private ArrayList<PLObjects> workouts;
    // private ArrayList<PLObjects> splitCleanWorkout;
    private StatsCalculatorManager statsCalculator;

    private Learn learn;

    private DataManager dataManager;

    public LayoutManager(Context context, DataManager dataManager, ProgramTemplate programTemplate) {
        this.programTemplate = programTemplate;
        this.context = context;
        this.dataManager = dataManager;
        learn = new Learn(this);
        statsCalculator = new StatsCalculatorManager(this);
    }

    public void finish(boolean haveHolder) {
//        updateLayoutStats(haveHolder);
    }

    public LayoutManager(Context context, DataManager dataManager) {
        this.context = context;
        this.dataManager = dataManager;
        learn = new Learn(this);
        statsCalculator = new StatsCalculatorManager(this);
    }

    public static LayoutManager getDefaultLayoutManagerInstance(Context context, DataManager dataManager) {
        LayoutManager layoutManager = new LayoutManager(context, dataManager);
        layoutManager.drawWorkout(layoutManager.layout, "Workout A");
        layoutManager.drawBody(layoutManager.layout, "This Is A Title");
        layoutManager.drawExercise(null, layoutManager.layout, new BeansHolder(), WorkoutLayoutTypes.ExerciseViewLeftMargin);
        return layoutManager;
    }


    public void drawWorkout(ArrayList<PLObjects> toLayout, String workoutName) {
        ++numOfWorkouts;
        if (workoutName == null || workoutName.equals("")) {
            workoutName = ProgramTemplate.ProgramTemplateFactory.WhatsYourWorkoutName(numOfWorkouts - 1);
        }
        toLayout.add(new PLObjects.WorkoutText(numOfWorkouts - 1, workoutName));
    }

    public void drawBody(ArrayList<PLObjects> toLayout, String title) {
        ++numOfBodyParts;

        toLayout.add(new PLObjects.BodyText(numOfWorkouts - 1, numOfBodyParts, null, title));
    }

    public void drawExercise(Muscle m, ArrayList<PLObjects> toLayout, BeansHolder beansHolder, WorkoutLayoutTypes innerType) {
        ++numOfExercises;
        ArrayList<BeansHolder> list = null;

        if (beansHolder != null) {
            list = new ArrayList<>();
            list.add(beansHolder);
            list.add(beansHolder);
            list.add(beansHolder);
        }
        PLObjects.ExerciseProfile ep = new PLObjects.ExerciseProfile(m, beansHolder, list, numOfWorkouts - 1, numOfBodyParts, numOfExercises);
        /*if (layout.get(layout.size() - 1).getType() == WorkoutLayoutTypes.BodyView) {
            ep.setFirstExercise(true);
        }*/
        ep.setInnerType(innerType);
        toLayout.add(ep);
    }

   /* public void updateExercise(int position, Beans exercise) {
        ((PLObjects.ExerciseProfile) layout.get(position)).getBeansHolder().setExercise(exercise);
    }*/

  /*  public void updateRep(int position, Beans rep) {
        ((PLObjects.ExerciseProfile) layout.get(position)).getBeansHolder().setRep(rep);
    }

    public void updateMethod(int position, Beans method) {
        ((PLObjects.ExerciseProfile) layout.get(position)).getBeansHolder().setMethod(method);
    }*/

    public void createNewLayoutFromTemplate(ProgramTemplate programTemplate) {
        this.programTemplate = programTemplate;
        layout = new ArrayList<>();
        drawWorkoutFromTemplate();
        updateLayoutStats(false);
    }

    private void drawWorkoutFromTemplate() {
        for (int i = 0; i < programTemplate.getNumOfWorkouts(); i++) {
            String workoutName = programTemplate.getWorkoutsNames().get(0);
            drawWorkout(layout, workoutName);
            drawBodyFromTemplate(i);

        }
    }

    private void drawBodyFromTemplate(int workoutPosition) {
        for (int i = 0; i < programTemplate.getBodyPartsPerWorkout(workoutPosition); i++) {
            String muscle = programTemplate.getBodyTemplate().get(workoutPosition).get(i);
            Muscle m = Muscle.createMuscle(dataManager.getMuscleDataManager(), muscle);
            // StatsHolder statsHolder = statsCalculator.calculateStatsHolder(numOfBodyParts, false);
            drawBody(layout, "");
            if (layout.get(layout.size() - 1).bodyId % 0 == 2) {
                drawExerciseFromTemplate(m, WorkoutLayoutTypes.ExerciseViewLeftMargin);
            } else {
                drawExerciseFromTemplate(m, WorkoutLayoutTypes.ExerciseViewLeftMargin);

            }
        }
    }

    private void drawExerciseFromTemplate(Muscle m, WorkoutLayoutTypes innerType) {
        for (int i = 0; i < programTemplate.getBlockLength(m); i++) {
            drawExercise(m, layout, null, innerType);
        }
    }

    public void saveLayoutToDataBase(boolean update) {
        requestSplitToLayout();
        ProgramDataManager.Tables[] tables;

        if (dbName == null || dbName.equals("")) {
            String name = DataManager.generateTableName("layout");
            setLayoutName(DataManager.generateTableName(name));
            dbName = name;
        }
       /* String name = DataManager.generateTableName("layout");
        dbName = name;*/
        tables = new ProgramDataManager.Tables[]{ProgramDataManager.Tables.PROGRAM};


        dataManager.getProgramDataManager().insertTables(update, this, tables);


     /*   if (update) {
            table = dataManager.getProgramDataManager().getCurrentProgramTable();
            if (table != null)
                dataManager.getProgramDataManager().delete(table);
        } else {
            table = dataManager.getProgramDataManager().generateTableName();
        }*/
        //dataManager.getPrefsEditor().putString("program", table);

    }

    public boolean readLayoutFromDataBase(int backBy) {
        Cursor c = dataManager.getProgramDataManager().readLayoutTableCursor(backBy);
        dbName = dataManager.getProgramDataManager().getCurrentLayoutTable();
        layout = new ArrayList<>();
        Muscle muscle = null;
        String muscle_str;
        if (c != null && c.moveToFirst()) {
            do {

                switch (c.getInt(c.getColumnIndex(TYPE))) {
                    case 0:
                        bodyTemplate.add(new ArrayList<String>());
                        drawWorkout(layout, c.getString(c.getColumnIndex(NAME)));
                        workoutsNames.add(c.getString(c.getColumnIndex(NAME)));
                        break;
                    case 1:
                        // bodyTemplate.get(numOfWorkouts - 1).add(muscle.getMuscle_name());
                        drawBody(layout, c.getString(c.getColumnIndex(NAME)));
                        break;
                    case 2:
                        BeansHolder beansHolder = new BeansHolder();

                        int innerType = c.getInt(c.getColumnIndex(INNER_TYPE));
                        muscle_str = c.getString(c.getColumnIndex(MUSCLE));
                        try {
                            muscle = Muscle.createMuscle(dataManager.getMuscleDataManager(), muscle_str);
                        } catch (Exception e) {
                            Log.d("aviv", "readLayoutFromDataBase: either null or no muscle");
                        }
                        String exId = c.getString(c.getColumnIndex(EXERCISE_ID));
                        String repId = c.getString(c.getColumnIndex(REP_ID));
                        double weight = c.getDouble(c.getColumnIndex(WEIGHT));
                        String rest = c.getString(c.getColumnIndex(REST));
                        String sets = c.getString(c.getColumnIndex(SETS));

                        if (exId != null) {
                            beansHolder.setExercise(dataManager.getExerciseDataManager().fetchByName(muscle.getMuscle_name(), exId));
                        }
                        if (repId != null) {
                            beansHolder.setRep(dataManager.getExerciseDataManager().fetchByName(TABLE_REPS, repId));

                        }
                        if (rest != null) {
                            beansHolder.setRest(dataManager.getExerciseDataManager().fetchByName(TABLE_REST, rest));

                        }
                        if (sets != null) {
                            beansHolder.setSets(dataManager.getExerciseDataManager().fetchByName(TABLE_SETS, sets));
                        }
                        beansHolder.setWeight(weight);
                        drawExercise(muscle, layout, beansHolder, WorkoutLayoutTypes.getEnum(innerType));

                        break;
                }
            } while (c.moveToNext());
            //programTemplate = ProgramTemplate.ProgramTemplateFactory.createCustomProgramTemplate();
            //updateLayoutStats(true);
            setProgramTemplate(ProgramTemplate.ProgramTemplateFactory.createCustomProgramTemplate(
                    bodyTemplate, -1, -1, "", workoutsNames
            ));
            programTemplate.setModified(false);
            return true;
        }

        return false;
    }


    public void updateLayout(UpdateComponents updateComponents) {
        int workoutPosition = updateComponents.getWorkoutPosition();
        if (updateComponents != null) {

            switch (updateComponents.getUpdateType()) {
                case SWAP:
                    Collections.swap(getSplitRecyclerWorkouts(false).get(workoutPosition), updateComponents.fromPosition, updateComponents.toPosition);
                    break;
                case NEW_EXERCISE:
                    drawExercise(null,
                            getSplitRecyclerWorkouts(false).get(workoutPosition),
                            new BeansHolder(),
                            updateComponents.innerType);
                    // getSplitRecyclerWorkouts(false).get(workoutPosition).add(updateComponents.getInsertPosition(), updateComponents.getPlObject());
                    break;
                case NEW_WORKOUT:
                    getSplitRecyclerWorkouts(false).add(new ArrayList<PLObjects>());
                    drawWorkout(getWorkouts(false), "");
                    drawExercise(null, getSplitRecyclerWorkouts(false).get(getSplitRecyclerWorkouts(false).size() - 1), new BeansHolder(), WorkoutLayoutTypes.ExerciseViewLeftMargin);
                    break;
                case REMOVE:
                    getSplitRecyclerWorkouts(false).get(workoutPosition).remove(updateComponents.removePosition);
                    break;
                case DELETE_WORKOUT:
                    PLObjects workout = getWorkouts(false).get(workoutPosition);
                    getWorkouts(false).remove(workout);
                    getSplitRecyclerWorkouts(false).remove(workoutPosition);
                    break;
                case DRAW_DIVIDER:
                    drawBody(getSplitRecyclerWorkouts(false).get(workoutPosition), "New Divider...");
            }
            requestSplitToLayout();
            numOfWorkouts = 0;
            numOfBodyParts = 0;
            sizeOfWorkouts = 0;
            numOfExercises = 0;
        }
    }


    public void updateLayoutStats(boolean haveStatsHolder) {
        int bodyPos = 0;
        for (int i = 0; i < layout.size(); i++) {
            if (layout.get(i).getType() == WorkoutLayoutTypes.BodyView) {

                PLObjects.BodyText bt = (PLObjects.BodyText) layout.get(i);
                bt.setStatsHolder(statsCalculator.calculateStatsHolder(bt.getMuscle(), i, haveStatsHolder));
                bodyPos++;
            }
        }
    }

   /* public ArrayList<PLObjects> readWorkout(int position) {
        ArrayList<PLObjects> arr = new ArrayList<>();

        return arr;
    }*/

   /* private MainMuscleEnum[][] matrixListtoArray(ArrayList<ArrayList<MainMuscleEnum>> arr) {
        MainMuscleEnum[][] matrixArray = new MainMuscleEnum[arr.size()][];
        int i = 0;
        for (ArrayList list : arr) {
            matrixArray[i++] = (MainMuscleEnum[]) list.toArray(new MainMuscleEnum[list.size()]);
        }
        return matrixArray;
    }*/


    public int getLayoutSize() {
        if (layout != null)
            return layout.size();

        return 0;
    }

    public ArrayList<PLObjects> getLayout() {
        return layout;
    }

    public int getNumOfBodyParts() {
        return numOfBodyParts;
    }

    public int getNumOfWorkouts() {
        numOfWorkouts = 0;
        for (int i = 0; i < layout.size(); i++) {
            if (layout.get(i).getType() == WorkoutLayoutTypes.WorkoutView) {
                numOfWorkouts++;
            }
        }
        return numOfWorkouts;
    }

    public int getCurrentPosition() {
        return layout.size() - 1;
    }

    public void setProgramTemplate(ProgramTemplate programTemplate) {
        this.programTemplate = programTemplate;
    }

    //
    public ArrayList<PLObjects> getCleanSplitWorkout(int index) {
        return getSplitBodys().get(index);

    }

    public ArrayList<ArrayList<PLObjects>> getCleanWorkout() {
        ArrayList<ArrayList<PLObjects>> split = getSplitRecyclerWorkouts(false);
        ArrayList<ArrayList<PLObjects>> arr = new ArrayList<>();
        for (int i = 0; i < split.size(); i++) {
            arr.add(new ArrayList<PLObjects>());
            for (int j = 0; j < split.get(i).size(); j++) {
                if (split.get(i).get(j).getType() != WorkoutLayoutTypes.BodyView) {
                    arr.get(i).add(split.get(i).get(j));
                }
            }

        }
        return arr;
    }

    //must start with 1 as 0 = first workout
    public ArrayList<ArrayList<PLObjects>> getSplitRecyclerWorkouts(boolean update) {
        if (update) {
            splitRecyclerWorkouts = null;
            splitRecyclerWorkouts = new ArrayList<>();

        } else if (splitRecyclerWorkouts != null) {
            return splitRecyclerWorkouts;
        }
        ArrayList<ArrayList<PLObjects>> arr = new ArrayList<>();
        int j = 1;
        for (int i = 0; i < getNumOfWorkouts(); i++) {
            arr.add(new ArrayList<PLObjects>());
            if (layout.size() == 0) {
                return splitRecyclerWorkouts;
            }
            while (j < layout.size()) {
                if (layout.get(j).getType() == WorkoutLayoutTypes.WorkoutView) {
                    j++;
                    break;
                } else {
                    arr.get(i).add(layout.get(j));
                }
                j++;
            }

        }
        splitRecyclerWorkouts = arr;
        return splitRecyclerWorkouts;
    }


    public void requestSplitToLayout() {
        ArrayList<ArrayList<PLObjects>> arr = getSplitRecyclerWorkouts(false);
        ArrayList<PLObjects> workoutsArr = getWorkouts(false);
        layout.clear();
        //String[] workoutNames = ProgramTemplate.WhatsYourName(arr.size());
        for (int i = 0; i < arr.size(); i++) {
            layout.add(workoutsArr.get(i));
            for (int j = 0; j < arr.get(i).size(); j++) {
                layout.add(arr.get(i).get(j));
            }
        }
        /*exercisesPerWorkouts = null;
        splitRecyclerWorkouts = null;
        splitCleanWorkouts = null;*/
    }

    public ArrayList<PLObjects.BodyText> getAllBodys() {
        if (allBodys != null) {
            return allBodys;
        }
        allBodys = new ArrayList<>();
        for (int i = 0; i < layout.size(); i++) {
            if (layout.get(i).getType() == WorkoutLayoutTypes.BodyView) {
                allBodys.add((PLObjects.BodyText) layout.get(i));
            }
        }
        return allBodys;
    }

    public int getNumOfExercises() {
        numOfExercises = 0;
        int count = 0;
        for (int i = 0; i < layout.size(); i++) {
            if (layout.get(i) instanceof PLObjects.ExerciseProfile) {
                count++;
            }
        }
        numOfExercises = count;
        return numOfExercises;
    }

    //must start with 1 as 1 = body
    //gives layout of each body
    public ArrayList<ArrayList<PLObjects>> getSplitBodys() {
        splitCleanWorkouts = null;
        splitCleanWorkouts = new ArrayList<>();
        int j;
        int bodyCounter = 0;
        ArrayList<ArrayList<PLObjects>> splitWorkouts = getSplitRecyclerWorkouts(false);
        for (int i = 0; i < splitWorkouts.size(); i++) {
            j = 1;
            for (int l = 0; l < programTemplate.getBodyPartsPerWorkout(i); l++) {
                splitCleanWorkouts.add(new ArrayList<PLObjects>());
                while (j < splitWorkouts.get(i).size()) {

                    if (splitWorkouts.get(i).get(j).getType() == WorkoutLayoutTypes.BodyView) {
                        j++;
                        break;
                    } else {
                        splitCleanWorkouts.get(bodyCounter).add(splitWorkouts.get(i).get(j));


                    }
                    j++;

                }
                bodyCounter++;
            }

        }


        return splitCleanWorkouts;

    }

    public ArrayList<ArrayList<PLObjects>> getExercisesPerWorkout(boolean update) {
        if (update) {
            exercisesPerWorkouts = null;
        }
        if (exercisesPerWorkouts != null) {
            return exercisesPerWorkouts;
        } else {
            ArrayList<ArrayList<PLObjects>> arr = new ArrayList<>();
            int j = -1;
            for (int i = 0; i < layout.size(); i++) {
                if (layout.get(i).getType() == WorkoutLayoutTypes.WorkoutView) {
                    arr.add(new ArrayList<PLObjects>());
                    j++;
                    continue;
                }
                if (layout.get(i).getType() == WorkoutLayoutTypes.BodyView) {
                    continue;
                }
                arr.get(j).add(layout.get(i));

            }
            exercisesPerWorkouts = arr;
            return exercisesPerWorkouts;
        }
    }


    public ArrayList<String> bodyTemplateAsList() {
        return programTemplate.getAllBodyParts();
    }


    public StatsCalculatorManager getStatsCalculator() {
        return statsCalculator;
    }

    public ProgramTemplate getProgramTemplate() {
        return programTemplate;
    }

    public int getmuscleFrequency(int index) {
        return learn.learnBodyFrequency().get(index);
    }

    public ArrayList<String> getMusclesRoutine() {
        return learn.LearnMusclesRoutine();
    }

    public DataManager getDataManager() {
        return dataManager;
    }


    public class Learn {

        private ArrayList<PLObjects> layout;
        private ArrayList<ArrayList<String>> bodyTemplate;
        private LayoutManager pLayoutManager;

        private ArrayList<Integer> muscleFrequency;
        private ArrayList<String> muscleRoutine;

        public Learn(LayoutManager pLayoutManager) {
            this.pLayoutManager = pLayoutManager;

            this.layout = layout;
        }

        public void learn() {

            learnBodyFrequency();
        }


        //gives array of num of times the muscles are selected in the program.
        //used to determine the routine stats for statscalculator srparams for each muscle
        public ArrayList<Integer> learnBodyFrequency() {
            if (muscleFrequency != null) {
                return muscleFrequency;
            }
            muscleFrequency = new ArrayList<>();
            bodyTemplate = pLayoutManager.programTemplate.bodyTemplate;
            ArrayList<String> temp = new ArrayList<>();
            int muscleFreq = 0;
            String tempMuscle = null;
            for (int i = 0; i < bodyTemplate.size(); i++) {

                for (int k = 0; k < bodyTemplate.get(i).size(); k++) {
                    outerloop:
                    if (isInside(temp, bodyTemplate.get(i).get(k))) {
                        break outerloop;
                    }
                    muscleFreq = 0;
                    tempMuscle = bodyTemplate.get(i).get(k);
                    for (int l = 0; l < bodyTemplate.size(); l++) {
                        for (int j = 0; j < bodyTemplate.get(l).size(); j++) {
                            if (bodyTemplate.get(i).get(k) == bodyTemplate.get(i).get(j)) {
                                muscleFreq++;
                            }
                        }


                    }

                    muscleFrequency.add(muscleFreq);
                    temp.add(tempMuscle);
                }

            }
            return muscleFrequency;
        }


        private boolean isInside(ArrayList<String> arr1, String num1) {
            for (int i = 0; i < arr1.size(); i++) {
                if (arr1.get(i).equals(num1)) {
                    return true;
                }
            }
            return false;

        }

        //calculates the routine for each muscle, in case of custom template
        public ArrayList<String> LearnMusclesRoutine() {
            if (muscleRoutine != null) {
                return muscleRoutine;
            }
            if (muscleFrequency == null) {
                muscleFrequency = learnBodyFrequency();
            }
            muscleRoutine = new ArrayList<>();
            double frequency;
            double score;
            String routine;
            for (int i = 0; i < muscleFrequency.size(); i++) {
                frequency = (double) muscleFrequency.get(i);
                score = frequency / (double) programTemplate.getRoundsOfWorkoutsPerWeek();
                if (score == 1) {
                    routine = "fbw";
                } else if (score > 0.5 && score < 1) {
                    routine = "ab";
                } else if (score > 0.3 && score < 0.5) {
                    routine = "abc";
                } else {
                    routine = "abcde";
                }
                muscleRoutine.add(routine);
            }
            return muscleRoutine;
        }


    }

    public static class UpdateComponents {

        private int workoutPosition;

        private int fromPosition;
        private int toPosition;

        private int removePosition;

        private int insertPosition;

        private PLObjects plObject;
        private String updateType;
        private boolean update;
        private WorkoutLayoutTypes innerType;

        public UpdateComponents(String updateType) {

            this.updateType = updateType;
        }


        public int getWorkoutPosition() {
            return workoutPosition;
        }

        public void setWorkoutPosition(int workoutPosition) {
            this.workoutPosition = workoutPosition;
        }

        public int getInsertPosition() {
            return insertPosition;
        }

        public void setInsertPosition(int insertPosition) {
            this.insertPosition = insertPosition;
        }

        public int getRemovePosition() {
            return removePosition;
        }

        public void setRemovePosition(int removePosition) {
            this.removePosition = removePosition;
        }

        public int getToPosition() {
            return toPosition;
        }

        public void setToPosition(int toPosition) {
            this.toPosition = toPosition;
        }

        public int getFromPosition() {
            return fromPosition;
        }

        public void setFromPosition(int fromPosition) {
            this.fromPosition = fromPosition;
        }

        public String getUpdateType() {
            return updateType;
        }

        public PLObjects getPlObject() {
            return plObject;
        }

        public void setPlObject(PLObjects plObject) {
            this.plObject = plObject;
        }

        public boolean isUpdate() {
            return update;
        }

        public void setUpdate(boolean update) {
            this.update = update;
        }

        public WorkoutLayoutTypes getInnerType() {
            return innerType;
        }

        public void setInnerType(WorkoutLayoutTypes innerType) {
            this.innerType = innerType;
        }
    }
}
