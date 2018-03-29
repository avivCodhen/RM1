package com.strongest.savingdata.AlgorithmLayout;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.strongest.savingdata.Adapters.MyExpandableAdapter;
import com.strongest.savingdata.AlgorithmLayout.PLObject.ExerciseProfile;
import com.strongest.savingdata.AlgorithmStats.StatsCalculatorManager;
import com.strongest.savingdata.BaseWorkout.Muscle;
import com.strongest.savingdata.BaseWorkout.ProgramTemplate;
import com.strongest.savingdata.Database.Exercise.ExerciseSet;
import com.strongest.savingdata.Database.Managers.DataManager;
import com.strongest.savingdata.Fragments.ExerciseChooseFragment;
import com.strongest.savingdata.Fragments.SetsChooseSingleFragment;
import com.strongest.savingdata.MyViews.LongClickMenu.LongClickMenuView;

import java.util.ArrayList;
import java.util.Collections;


import static com.strongest.savingdata.AlgorithmLayout.WorkoutLayoutTypes.IntraExerciseProfile;
import static com.strongest.savingdata.AlgorithmLayout.WorkoutLayoutTypes.IntraSet;
import static com.strongest.savingdata.AlgorithmLayout.WorkoutLayoutTypes.IntraSetNormal;
import static com.strongest.savingdata.AlgorithmLayout.WorkoutLayoutTypes.SetsPLObject;
import static com.strongest.savingdata.AlgorithmLayout.WorkoutLayoutTypes.SuperSetIntraSet;
import static com.strongest.savingdata.Database.Exercise.DBExercisesHelper.*;
import static com.strongest.savingdata.Database.Exercise.DBExercisesHelper.TYPE;
import static com.strongest.savingdata.Database.Program.DBProgramHelper.EXERCISE_ID;
import static com.strongest.savingdata.Database.Program.DBProgramHelper.INNER_TYPE;
import static com.strongest.savingdata.Database.Program.DBProgramHelper.REP_ID;
import static com.strongest.savingdata.Database.Program.DBProgramHelper.REST;

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

    public ArrayList<PLObject> getWorkouts() {
        return workouts;
    }

    private ArrayList<PLObject> initWorkouts() {
        ArrayList<PLObject> arr = new ArrayList<>();
        for (int i = 0; i < layout.size(); i++) {
            if (layout.get(i).getType() == WorkoutLayoutTypes.WorkoutView) {
                arr.add(layout.get(i));
            }
        }
        workouts = arr;
        return workouts;
    }

    public static final String
            DELETE_EXERCISE = "delete_exercise",
            NEW_EXERCISE = "new_exercise",
            NEW_SUPERSET = "new_superset",
            NEW_EXERCISE_FLIPPED = "new_exercise_fliped",
            DRAW_DIVIDER = "draw_divider",
            NEW_WORKOUT = "new_workout",
            ATTACH_SUPERSET = "attach_superset",
            ATTACH_DROPSET = "attach_dropset",
            DELETE_WORKOUT = "delete_workout",
            SWAP = "swap",
            REMOVE = "remove";

    public static String[] intraWorkoutsLetters = new String[]{
            "A",
            "B",
            "C",
            "D",
            "E",
            "F",
            "G", "H", "I", "J", "K", "L", "M", "N", "P"
    };

    public String dbName;
    private String layoutName;
    private int currentPosition;
    private int numOfWorkouts;
    private int numOfBodyParts;
    private int sizeOfWorkouts;
    private int numOfExercises;

    private ArrayList<ArrayList<String>> bodyTemplate = new ArrayList<>();
    private ArrayList<String> workoutsNames = new ArrayList<>();
    private ArrayList<PLObject.BodyText> allBodys;
    public Context context;
    private ArrayList<PLObject> layout = new ArrayList<>();
    private ProgramTemplate programTemplate;
    private ArrayList<ArrayList<PLObject>> splitRecyclerWorkouts;
    private ArrayList<ArrayList<PLObject>> splitCleanWorkouts;
    private ArrayList<ArrayList<PLObject>> exercisesPerWorkouts;
    private ArrayList<PLObject> workouts;
    // private ArrayList<PLObjects> splitCleanWorkout;
    private StatsCalculatorManager statsCalculator;

    public LayoutManagerHelper mLayoutManagerHelper;
    private Learn learn;

    private DataManager dataManager;

/*    public LayoutManager(Context context, DataManager dataManager, ProgramTemplate programTemplate) {
        this.programTemplate = programTemplate;
        this.context = context;
        this.dataManager = dataManager;
        learn = new Learn(this);
        statsCalculator = new StatsCalculatorManager(this);
    }*/

    public void finish() {
        initRecyclerMatrixLayout();
        initWorkouts();
    }

    public LayoutManager(Context context, DataManager dataManager) {
        this.context = context;
        this.dataManager = dataManager;
        learn = new Learn(this);
        statsCalculator = new StatsCalculatorManager(this);
        mLayoutManagerHelper = new LayoutManagerHelper();

    }

    public static LayoutManager getDefaultLayoutManagerInstance(Context context, DataManager dataManager) {
        LayoutManager layoutManager = new LayoutManager(context, dataManager);
        layoutManager.drawWorkout(layoutManager.layout, "Workout A");
        layoutManager.drawBody(layoutManager.layout, "This Is A Title");
        layoutManager.drawExercise(null, layoutManager.layout, WorkoutLayoutTypes.ExerciseProfile, true);
        layoutManager.initRecyclerMatrixLayout();
        layoutManager.initWorkouts();
        return layoutManager;
    }

/*
    public ArrayList<PLObjects> expandedLayout(ExerciseProfile ep){
        ArrayList<PLObjects> miniLayout = new ArrayList<>();
        for (int i = 0; i < ep.getSets().size(); i++) {
            for (int j = 0; j < ; j++) {

            }
        }
    }
*/


    public void drawWorkout(ArrayList<PLObject> toLayout, String workoutName) {
        ++numOfWorkouts;
        if (workoutName == null || workoutName.equals("")) {
            workoutName = ProgramTemplate.ProgramTemplateFactory.WhatsYourWorkoutName(numOfWorkouts - 1);
        }
        toLayout.add(new PLObject.WorkoutText(numOfWorkouts - 1, workoutName));
    }

    public void drawBody(ArrayList<PLObject> toLayout, String title) {
        toLayout.add(new PLObject.BodyText(numOfWorkouts - 1, numOfBodyParts, null, title));
    }

    public ExerciseProfile drawExercise(Muscle m, ArrayList<PLObject> toLayout, WorkoutLayoutTypes innerType,
                                        boolean isNew) {
        if (toLayout == this.layout) {
            ++numOfExercises;
        }
        ArrayList<ArrayList<ExerciseSet>> setsList = new ArrayList<>();
        ExerciseSet set = new ExerciseSet();
        ExerciseProfile ep = new ExerciseProfile(m, numOfWorkouts - 1, numOfBodyParts, numOfExercises);
        if (isNew) {
            PLObject.SetsPLObject setsPLObject = new PLObject.SetsPLObject(ep, set);
            setsPLObject.setInnerType(SetsPLObject);
            ep.getSets().add(setsPLObject);
        }
        ep.setInnerType(innerType);
        if (toLayout != null) {
            toLayout.add(ep);
        }
        return ep;
    }

    public PLObject.SetsPLObject createSetsPlObject(ExerciseProfile parent, ExerciseSet exerciseSet, WorkoutLayoutTypes innerType) {
        PLObject.SetsPLObject setsPLObject = new PLObject.SetsPLObject(parent, exerciseSet);
        setsPLObject.setInnerType(innerType);
        return setsPLObject;

    }

    public void saveLayoutToDataBase(boolean update) {
        requestSplitToLayout();
        dataManager.getProgramDataManager().insertTables(update, this);
    }

    public boolean readLayoutFromDataBase(String currentDbName) {
        Cursor c = dataManager.getProgramDataManager().readLayoutTableCursor(currentDbName);
        dbName = currentDbName;
        layout = new ArrayList<>();
        Muscle muscle = null;
        String muscle_str;
        WorkoutLayoutTypes type;
        WorkoutLayoutTypes innerType;
        String rep;
        String rest;
        double weight;
        ExerciseSet exerciseSet;
        ExerciseProfile ep;

        if (c != null && c.moveToFirst()) {
            do {
                type = WorkoutLayoutTypes.getEnum(c.getInt(c.getColumnIndex(TYPE)));
                switch (type) {
                    case WorkoutView:
                        bodyTemplate.add(new ArrayList<String>());
                        drawWorkout(layout, c.getString(c.getColumnIndex(NAME)));
                        workoutsNames.add(c.getString(c.getColumnIndex(NAME)));
                        break;
                    case BodyView:
                        // bodyTemplate.get(numOfWorkouts - 1).add(muscle.getMuscle_name());
                        drawBody(layout, c.getString(c.getColumnIndex(NAME)));
                        break;
                    case ExerciseProfile:
                     //   ExerciseProfile parent = null;
                        innerType = WorkoutLayoutTypes
                                .getEnum(c.getInt(c.getColumnIndex(INNER_TYPE)));

                        muscle_str = c.getString(c.getColumnIndex(MUSCLE));
                        try {
                            muscle = Muscle.createMuscle(dataManager.getMuscleDataManager(), muscle_str);
                        } catch (Exception e) {
                            Log.d("aviv", "readLayoutFromDataBase: either null or no muscle");
                        }

                     /*   if (innerType == IntraExerciseProfile) {
                            if (((ExerciseProfile) layout.get(layout.size() - 1)).getParent() != null) {
                                parent = ((ExerciseProfile) layout.get(layout.size() - 1)).getParent();
                            } else {
                                parent = ((ExerciseProfile) layout.get(layout.size() - 1));

                            }
                        }*/
                        ep = drawExercise(muscle, layout, innerType, false);

                        String exId = c.getString(c.getColumnIndex(EXERCISE_ID));
                        if (exId != null) {
                            ep.setExercise(dataManager.getExerciseDataManager().fetchByName(muscle.getMuscle_name(), exId));
                        }
                      /*  if (parent != null) {
                            parent.getExerciseProfiles().add(ep);
                            ep.setParent(parent);
                        }*/
                        break;
                    case SetsPLObject:

                        innerType = WorkoutLayoutTypes.getEnum(c.getInt(c.getColumnIndex(INNER_TYPE)));
                        ep = ((ExerciseProfile) layout.get(layout.size() - 1));
                        ExerciseProfile setParent = null;
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
                        break;
                    case IntraSet:
                        ep = ((ExerciseProfile) layout.get(layout.size() - 1));
                        innerType = WorkoutLayoutTypes.getEnum(c.getInt(c.getColumnIndex(INNER_TYPE)));
                        switch (innerType) {

                            case SuperSetIntraSet:
                                //exerciseprofile is a superset

                                rep = c.getString(c.getColumnIndex(REP_ID));
                                rest = c.getString(c.getColumnIndex(REST));
                                weight = c.getDouble(c.getColumnIndex(WEIGHT));
                                exerciseSet = new ExerciseSet();
                                exerciseSet.setRep(rep);
                                exerciseSet.setRest(rest);
                                exerciseSet.setWeight(weight);
                                PLObject.IntraSetPLObject intra = new PLObject.IntraSetPLObject(
                                        ep,
                                        exerciseSet,
                                        innerType, null);
                                ep.getIntraSets().add(intra);
                                break;

                            case IntraSetNormal:
                                //exerciseprofile is normal
                                rep = c.getString(c.getColumnIndex(REP_ID));
                                rest = c.getString(c.getColumnIndex(REST));
                                weight = c.getDouble(c.getColumnIndex(WEIGHT));
                                exerciseSet = new ExerciseSet();
                                exerciseSet.setRep(rep);
                                exerciseSet.setRest(rest);
                                exerciseSet.setWeight(weight);
                                int position = ep.getSets().size()-1;

                                PLObject.IntraSetPLObject setsPLObjectIntraSet = new PLObject.IntraSetPLObject(
                                        ep, exerciseSet, innerType, ep.getSets().get(position));
                                ep.getSets().get(position).getIntraSets().add(setsPLObjectIntraSet);
                                break;

                        }

                }
            } while (c.moveToNext());
            mLayoutManagerHelper.reArrangeFathers(layout);
            initRecyclerMatrixLayout();
            initWorkouts();
            return true;
        }

        return false;
    }


    public void updateLayout(UpdateComponents updateComponents) {
        int workoutPosition = updateComponents.getWorkoutPosition();
        if (updateComponents != null) {

            switch (updateComponents.getUpdateType()) {
                case DELETE_EXERCISE:
                    for (int i = 0; i < ((ExerciseProfile) updateComponents.getPlObject()).getExerciseProfiles().size() + 1; i++) {
                        getSplitRecyclerWorkouts().get(workoutPosition).remove(updateComponents.getRemovePosition());
                    }
                case SWAP:
                    Collections.swap(getSplitRecyclerWorkouts().get(workoutPosition), updateComponents.fromPosition, updateComponents.toPosition);
                    break;
                case NEW_EXERCISE:
                    getSplitRecyclerWorkouts().set(workoutPosition, updateComponents.getLayout());
                    drawExercise(null,
                            getSplitRecyclerWorkouts().get(workoutPosition),
                            updateComponents.innerType, true);
                    // initRecyclerMatrixLayout(false).get(workoutPosition).add(updateComponents.getInsertPosition(), updateComponents.getPlObject());
                    break;
                case NEW_SUPERSET:

                case NEW_WORKOUT:
                    initRecyclerMatrixLayout().add(new ArrayList<PLObject>());
                    drawWorkout(getWorkouts(), "");
                    drawExercise(null, getSplitRecyclerWorkouts().get(getSplitRecyclerWorkouts().size() - 1),
                            WorkoutLayoutTypes.ExerciseProfile, true);
                    break;
                case REMOVE:
                    getSplitRecyclerWorkouts().get(workoutPosition).remove(updateComponents.removePosition);
                    break;
                case DELETE_WORKOUT:
                    PLObject workout = getWorkouts().get(workoutPosition);
                    getWorkouts().remove(workout);
                    getSplitRecyclerWorkouts().remove(workoutPosition);
                    break;
                case DRAW_DIVIDER:
                    drawBody(getSplitRecyclerWorkouts().get(workoutPosition), "New Divider...");
            }
            onLayoutChange();
            numOfWorkouts = 0;
            numOfBodyParts = 0;
            sizeOfWorkouts = 0;
            numOfExercises = 0;
        }
    }

    private void onLayoutChange() {
        requestSplitToLayout();
        initRecyclerMatrixLayout();
        initWorkouts();
    }


    public void updateLayoutStats(boolean haveStatsHolder) {
        int bodyPos = 0;
        for (int i = 0; i < layout.size(); i++) {
            if (layout.get(i).getType() == WorkoutLayoutTypes.BodyView) {

                PLObject.BodyText bt = (PLObject.BodyText) layout.get(i);
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

    public ArrayList<PLObject> getLayout() {
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
    public ArrayList<PLObject> getCleanSplitWorkout(int index) {
        return getSplitBodys().get(index);

    }

    public ArrayList<ArrayList<PLObject>> getCleanWorkout() {
        ArrayList<ArrayList<PLObject>> split = getSplitRecyclerWorkouts();
        ArrayList<ArrayList<PLObject>> arr = new ArrayList<>();
        for (int i = 0; i < split.size(); i++) {
            arr.add(new ArrayList<PLObject>());
            for (int j = 0; j < split.get(i).size(); j++) {
                if (split.get(i).get(j).getType() != WorkoutLayoutTypes.BodyView) {
                    arr.get(i).add(split.get(i).get(j));
                }
            }

        }
        return arr;
    }

    public ArrayList<ArrayList<PLObject>> getSplitRecyclerWorkouts() {
        return splitRecyclerWorkouts;
    }

    //must start with 1 as 0 = first workout
    private ArrayList<ArrayList<PLObject>> initRecyclerMatrixLayout() {
        ArrayList<ArrayList<PLObject>> arr = new ArrayList<>();
        int j = 1;
        for (int i = 0; i < getNumOfWorkouts(); i++) {
            arr.add(new ArrayList<PLObject>());
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
        ArrayList<ArrayList<PLObject>> arr = getSplitRecyclerWorkouts();
        ArrayList<PLObject> workoutsArr = getWorkouts();
        layout.clear();
        //String[] workoutNames = ProgramTemplate.WhatsYourName(arr.size());
        for (int i = 0; i < arr.size(); i++) {
            layout.add(workoutsArr.get(i));
            for (int j = 0; j < arr.get(i).size(); j++) {
                layout.add(arr.get(i).get(j));
            }
        }

    }

    public ArrayList<PLObject.BodyText> getAllBodys() {
        if (allBodys != null) {
            return allBodys;
        }
        allBodys = new ArrayList<>();
        for (int i = 0; i < layout.size(); i++) {
            if (layout.get(i).getType() == WorkoutLayoutTypes.BodyView) {
                allBodys.add((PLObject.BodyText) layout.get(i));
            }
        }
        return allBodys;
    }

    public int getNumOfExercises() {
        numOfExercises = 0;
        int count = 0;
        for (int i = 0; i < layout.size(); i++) {
            if (layout.get(i) instanceof ExerciseProfile) {
                count++;
            }
        }
        numOfExercises = count;
        return numOfExercises;
    }

    //must start with 1 as 1 = body
    //gives layout of each body
    public ArrayList<ArrayList<PLObject>> getSplitBodys() {
        splitCleanWorkouts = null;
        splitCleanWorkouts = new ArrayList<>();
        int j;
        int bodyCounter = 0;
        ArrayList<ArrayList<PLObject>> splitWorkouts = getSplitRecyclerWorkouts();
        for (int i = 0; i < splitWorkouts.size(); i++) {
            j = 1;
            for (int l = 0; l < programTemplate.getBodyPartsPerWorkout(i); l++) {
                splitCleanWorkouts.add(new ArrayList<PLObject>());
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

    public ArrayList<ArrayList<PLObject>> getExercisesPerWorkout(boolean update) {
        if (update) {
            exercisesPerWorkouts = null;
        }
        if (exercisesPerWorkouts != null) {
            return exercisesPerWorkouts;
        } else {
            ArrayList<ArrayList<PLObject>> arr = new ArrayList<>();
            int j = -1;
            for (int i = 0; i < layout.size(); i++) {
                if (layout.get(i).getType() == WorkoutLayoutTypes.WorkoutView) {
                    arr.add(new ArrayList<PLObject>());
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

        private ArrayList<PLObject> layout;
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

        private PLObject plObject;
        private String updateType;
        private boolean update;
        private ArrayList<PLObject> layout;
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

        public PLObject getPlObject() {
            return plObject;
        }

        public void setPlObject(PLObject plObject) {
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

        public ArrayList<PLObject> getLayout() {
            return layout;
        }

        public void setLayout(ArrayList<PLObject> layout) {
            this.layout = layout;
        }
    }

    public class LayoutManagerHelper {

        private MyExpandableAdapter adapter;
        private ArrayList<PLObject> layout;

        public void setUpWithLayout(MyExpandableAdapter adapter, ArrayList<PLObject> layout) {
            this.adapter = adapter;
            this.layout = layout;
        }

        private LayoutManagerHelper(MyExpandableAdapter adapter) {

            this.adapter = adapter;
        }

        private LayoutManagerHelper() {

        }

        public void onChange(String change, PLObject plObject) {
            int position = findPLObjectPosition(plObject, layout);
            if (position != -1) {
                adapter.notifyItemChanged(position);
            }
        }
/*

        public void onItemChange(){
            adapter.notifyItemChanged(0);
        }
*/

        public void onExerciseSetChange() {
            adapter.notifyItemChanged(0);
        }

        public int findPLObjectPosition(PLObject plObject, ArrayList<PLObject> layout) {
            for (int i = 0; i < layout.size(); i++) {
                PLObject current = layout.get(i);
                if (current == plObject) {
                    return i;
                }
            }
            return -1;
        }

        public int countExerciseRangeChange(int position, ArrayList<PLObject> layout) {
            int count = 1;
            for (int i = position + 1; i < layout.size(); i++) {
                if (layout.get(i).getType() != WorkoutLayoutTypes.ExerciseProfile &&
                        layout.get(i).getInnerType() != WorkoutLayoutTypes.IntraExerciseProfile) {
                    count++;
                }
            }
            return count;
        }

        public int findSetPosition(PLObject.SetsPLObject setsPLObject) {
            for (int i = 0; i < setsPLObject.getParent().getSets().size(); i++) {
                PLObject.SetsPLObject temp = setsPLObject.getParent().getSets().get(i);
                if (setsPLObject == temp) {
                    return i;
                }
            }
            return -1;
        }

        public int findIntraSetPosition(PLObject.IntraSetPLObject intraPLObject) {
            if (intraPLObject.innerType == WorkoutLayoutTypes.SuperSetIntraSet) {
                for (int i = 0; i < intraPLObject.getParent().getIntraSets().size(); i++) {
                    if (intraPLObject.getParent().getIntraSets().get(i) == intraPLObject) {
                        return i;
                    }
                }
            } else if (intraPLObject.innerType == WorkoutLayoutTypes.IntraSet) {
                for (int i = 0; i < intraPLObject.getParentSet().getIntraSets().size(); i++) {
                    PLObject.IntraSetPLObject temp = intraPLObject.getParentSet().getIntraSets().get(i);
                    if (intraPLObject == temp) {
                        return i;
                    }
                }
            }
            return -1;
        }

        public int findExercisePosition(ExerciseProfile ep, ArrayList<PLObject> layout){
            int count=1;
            for (int i = 0; i < layout.size(); i++) {
                if(layout.get(i) != ep){
                    WorkoutLayoutTypes type = layout.get(i).getType();
                    WorkoutLayoutTypes innerType = layout.get(i).getInnerType();
                    if( type== WorkoutLayoutTypes.ExerciseProfile && innerType != IntraExerciseProfile){
                        count++;
                    }
                }else{
                    return count;
                }
            }
            return -1;
        }

        public String writeTitle(PLObject plObject){
            PLObject.ExerciseProfile exerciseProfile;
            PLObject.SetsPLObject setsPLObject;
            PLObject.IntraSetPLObject intraSetPLObject;
            String title = "";
            if (plObject instanceof PLObject.ExerciseProfile) {
                exerciseProfile = (PLObject.ExerciseProfile) plObject;
                if (exerciseProfile.innerType == WorkoutLayoutTypes.IntraExerciseProfile) {
                    title = "Exercise "
                            + exerciseProfile.getParent().rawPosition
                            + ", " + "Superset " + exerciseProfile.getTag();
                }else{
                    title = "Exercise "+exerciseProfile.rawPosition;
                }
                return title;

            } else if (plObject instanceof PLObject.SetsPLObject) {
                setsPLObject = (PLObject.SetsPLObject) plObject;
                exerciseProfile = setsPLObject.getParent();
                String set = findSetPosition(setsPLObject) == 0 ? "First set" : "Set "+(findSetPosition(setsPLObject)+1);
                String exerciseName = exerciseProfile.getExercise() != null? ", "+ exerciseProfile.getExercise().getName()
                        :"";
                title = set+ exerciseName ;
                return title;

            } else if (plObject instanceof PLObject.IntraSetPLObject) {
                intraSetPLObject = (PLObject.IntraSetPLObject) plObject;

                if(intraSetPLObject.innerType == WorkoutLayoutTypes.SuperSetIntraSet){
                    exerciseProfile = intraSetPLObject.getParent();
                    String exerciseName = exerciseProfile.getExercise() != null? ", "+exerciseProfile.getExercise().getName() : "";
                    title = "Intra-Set "+exerciseProfile.getTag()+", "+"set "+(1+findIntraSetPosition(intraSetPLObject))
                            +exerciseName;
                }else{
                    exerciseProfile = intraSetPLObject.getParentSet().getParent();
                    String exerciseName = exerciseProfile.getExercise() != null? ", "+ exerciseProfile.getExercise().getName() : "";
                    title = "Intra-Set "
                            +findIntraSetPosition(intraSetPLObject)
                            +", "+"set "+(findSetPosition(intraSetPLObject.getParentSet())+1)
                            +exerciseName;
                }
            }
            return title;
        }

        public void reArrangeFathers( ArrayList<PLObject> layout) {
            for (int i = 0; i < layout.size() ; i++) {
                if(layout.get(i).innerType == WorkoutLayoutTypes.IntraExerciseProfile){
                    if(i-1 < 0 || layout.get(i-1).innerType == WorkoutLayoutTypes.ExerciseProfile){
                        ((ExerciseProfile) layout.get(i)).setParent(null);
                    }
                }
                if(layout.get(i).getType() == WorkoutLayoutTypes.ExerciseProfile){
                    ExerciseProfile parent = (ExerciseProfile) layout.get(i);
                    parent.getExerciseProfiles().clear();
                    if(layout.size() > i+1 && layout.get(i+1).innerType == IntraExerciseProfile){

                        innerloop:
                        for (int j = i+1; j <layout.size() ; j++) {
                            if(layout.get(j).innerType == IntraExerciseProfile){
                                ExerciseProfile superset = (ExerciseProfile) layout.get(j);
                                parent.getExerciseProfiles().add(superset);
                                superset.setParent(parent);
                                i++;
                            }else{
                                break innerloop;
                            }

                        }
                    }
                }
            }
        }

        public ArrayList<ExerciseProfile> getAllExerciseProfiles(ArrayList<PLObject> layout){
            ArrayList<ExerciseProfile> arr = new ArrayList<>();
            for (int i = 0; i < layout.size(); i++) {
                if(layout.get(i).getType()==WorkoutLayoutTypes.ExerciseProfile){
                    arr.add((ExerciseProfile) layout.get(i));
                }
            }
            return arr;
        }

        public WorkoutLayoutTypes findPLObjectDefaultType(PLObject plObject){
            WorkoutLayoutTypes type = null;
            if(plObject.getType() == WorkoutLayoutTypes.ExerciseProfile){
                ExerciseProfile ep =(ExerciseProfile) plObject;
                if(ep.innerType == WorkoutLayoutTypes.IntraExerciseProfile){
                    type = WorkoutLayoutTypes.IntraExerciseProfile;
                }else{
                    type = WorkoutLayoutTypes.ExerciseProfile;
                }
            }
            if(plObject.getType() == SetsPLObject){
                    type = SetsPLObject;
                }if(plObject.getType() == IntraSet){
                    if(plObject.innerType == WorkoutLayoutTypes.SuperSetIntraSet){
                        type = WorkoutLayoutTypes.SuperSetIntraSet;
                    }else{
                        type = IntraSetNormal;
                    }
                }
                return type;
            }



        public void attachSupersetIntraSetsByParent(ExerciseProfile superset){
            int sets = superset.getParent().getSets().size();
            int intraSets = superset.getIntraSets().size();
            int length =  sets-intraSets;
            if(length > 0){
                for (int i = 0; i <length ; i++) {
                    superset.getIntraSets().add(new PLObject.IntraSetPLObject(superset,new ExerciseSet(),SuperSetIntraSet, null));
                }
            }

        }


        }



}
