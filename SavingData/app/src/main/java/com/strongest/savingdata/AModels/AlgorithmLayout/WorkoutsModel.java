package com.strongest.savingdata.AModels.AlgorithmLayout;

import android.content.Context;

import com.strongest.savingdata.AModels.AlgorithmLayout.PLObject.ExerciseProfile;
import com.strongest.savingdata.AModels.WorkoutItemAdapter;
import com.strongest.savingdata.AModels.WorkoutItemAdapterFactory;
import com.strongest.savingdata.AService.WorkoutsService;
import com.strongest.savingdata.Architecture;
import com.strongest.savingdata.BaseWorkout.Program;
import com.strongest.savingdata.BaseWorkout.ProgramTemplate;
import com.strongest.savingdata.Database.Exercise.ExerciseSet;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Queue;


import javax.inject.Inject;

import static com.strongest.savingdata.AModels.AlgorithmLayout.WorkoutsModel.Actions.NewWorkout;
import static com.strongest.savingdata.AModels.AlgorithmLayout.WorkoutsModel.ListModifier.DoTasks.Child;
import static com.strongest.savingdata.AModels.AlgorithmLayout.WorkoutsModel.ListModifier.DoTasks.DoNew;
import static com.strongest.savingdata.AModels.AlgorithmLayout.WorkoutsModel.ListModifier.DoTasks.Duplicate;
import static com.strongest.savingdata.AModels.AlgorithmLayout.WorkoutsModel.ListModifier.DoTasks.Remove;

/**
 * Created by Cohen on 10/18/2017.
 */

public class WorkoutsModel implements Architecture.model {


    private WorkoutsService workoutsService;

    public WorkoutsService getWorkoutsService() {
        return workoutsService;
    }

    @Override
    public void removeItem(Workout w) {

    }

    @Override
    public void addNewItem(Workout w, Actions a) {

        switch (a) {
            case NewWorkout:

                break;
        }
    }

    public enum Actions {
        Delete, Swap, Add, Duplicate, NewExercise, Child, MultiDelete, NewWorkout, Advanced, NewDivider
    }

    /*  public String getLayoutName() {
          return layoutName;
      }

      public void setLayoutName(String layoutName) {
          this.layoutName = layoutName;
      }

      public String getDbName() {
          return dbName;
      }
  */
/*
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
*/
/*
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
            REMOVE = "remove",
            SWAP_WORKOUTS = "swap_workouts";
*/
    public static String[] intraWorkoutsLetters = new String[]{
            "A",
            "B",
            "C",
            "D",
            "E",
            "F",
            "G", "H", "I", "J", "K", "L", "M", "N", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y"
    };

    //  public String dbName;
//    private String layoutName;
    //  private int currentPosition;
    private int numOfWorkouts;
    //   private int numOfBodyParts;
    //   private int sizeOfWorkouts;
    private int numOfExercises;

    // private ArrayList<ArrayList<String>> bodyTemplate = new ArrayList<>();
    //   private ArrayList<String> workoutsNames = new ArrayList<>();
    private ArrayList<PLObject.BodyText> allBodys;
    public Context context;
    private ArrayList<PLObject> layout = new ArrayList<>();
    private ProgramTemplate programTemplate;
    private ArrayList<ArrayList<PLObject>> splitRecyclerWorkouts;
    private ArrayList<ArrayList<PLObject>> splitCleanWorkouts;
    private ArrayList<ArrayList<PLObject>> exercisesPerWorkouts;
    // private ArrayList<PLObject> workouts;

    //this is a new(June 2018) implementation, for making a list of workouts
    //   private ArrayList<Workout> workoutsList = new ArrayList<>();

 /*   public ArrayList<Workout> getWorkoutsList() {
        return workoutsList;
    }
*/
    // private ArrayList<PLObjects> splitCleanWorkout;
    //  private StatsCalculatorManager statsCalculator;

    //  public LayoutManagerHelper mLayoutManagerHelper;
    //  private Learn learn;

    //   private DataManager dataManager;

    /*    public WorkoutsModel(Context context, DataManager dataManager) {
            this.context = context;
            this.dataManager = dataManager;
            //  learn = new Learn(this);
            statsCalculator = new StatsCalculatorManager(this);
            mLayoutManagerHelper = new LayoutManagerHelper();

        }*/
    @Inject
    public WorkoutsModel(WorkoutsService workoutsService) {
        this.workoutsService = workoutsService;
    }


    public ArrayList<Workout> createDefaultWorkoutsList() {
        ArrayList<Workout> workoutArrayList = new ArrayList<>();
        Workout w = new Workout();
        w.workoutName = "Workout 1";
        w.exArray.add(new PLObject.ExerciseProfile());
        workoutArrayList.add(w);
        return workoutArrayList;
    }

    public Program provideProgram() {
        Program p = workoutsService.fetchProgram();
        if (p == null) {
            p = createProgram();
            workoutsService.saveProgramToDatabase(p);
        }
        return p;
    }

    /**
     * requests an existing workout list from workoutservice.
     * in any case of error(probably no list is stored), it will create
     * a default workoutlist
     * it uses a recursion to make sure the new default workout is saved in the database
     */
    public ArrayList<Workout> provideWorktoutsList(String dbName) {
        ArrayList<Workout> list = workoutsService.readLayoutFromDataBase(dbName);
        if (list == null) {
            list = createDefaultWorkoutsList();
            workoutsService.saveLayoutToDataBase(false, list, dbName);
            //provideWorktoutsList(dbName);
        }
        return list;
    }

    //creates a new program with a special UDBname for the program
    //
    private Program createProgram() {
        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.US).format(new Date());
        String table = date.replace("-", "_");
        String currentTime = new SimpleDateFormat("HH:mm:ss").format(new Date());
        String time = "_" + currentTime.replace(":", "_");
        String results = "program" + table + time;
        return new Program("My Program", currentTime, date, results);

    }

    /**
     * this is the main method of the workoutsmodel
     * to handle every action provided by the activity
     */
    public boolean validateActions(ArrayList<Workout> list, int currentPosition, Actions a) {
        int results = WorkoutsModelValidator.getWorkoutsValidator().validateList(list);

        if (results == WorkoutsModelValidator.NULL) {
            throw new NullPointerException(
                    "List is null you idiot. How can this happen? workoutsMode.validateActions");
        }

        if (results == WorkoutsModelValidator.LIST_EMPTY && a != NewWorkout) {
            addNewWorkout(list);
            return validateActions(list, currentPosition, a);
        }

        Workout w = list.get(currentPosition);
        boolean toUpdate = false;

        return toUpdate;

    }


    public boolean addNewWorkout(ArrayList<Workout> list) {
        int results = WorkoutsModelValidator.getWorkoutsValidator().validateList(list);
        if (results != WorkoutsModelValidator.NULL) {

            Workout w = new Workout();
            w.exArray.add(new ExerciseProfile());
            w.workoutName = "Workout " + (list.size() + 1);
            list.add(w);
            return true;
        }
        return false;
    }

    public Workout exerciseToList(ExerciseProfile ep) {
        int results = WorkoutsModelValidator.getWorkoutsValidator().validateExercise(ep);

        if (results == WorkoutsModelValidator.NULL_SET) {
            ep.setSets(new ArrayList<>());
        }

        if (results == WorkoutsModelValidator.NO_SET) {
            ep.getSets().add(new PLObject.SetsPLObject());
        }


            Workout w = new Workout();

            //add all of the supersets to the list
            for (ExerciseProfile superset : ep.getExerciseProfiles()){
                w.exArray.add(superset);
            }

            //add each set to the list
            for (PLObject.SetsPLObject s : ep.getSets()) {
                w.exArray.add(s);

                //add superset(s) for each set
                for(ExerciseProfile e : ep.getExerciseProfiles()){
                    w.exArray.add(e);
                }

                //add each child (if any)
                for (PLObject.IntraSetPLObject i : s.getIntraSets()){
                    w.exArray.add(i);
                }


            return w;
        }

        throw  new IllegalArgumentException(
                "Even when trying to adverse all of the problem, exerciseList still failed. Idiot.");

    }

    public ArrayList<PLObject> expandExercise(ExerciseProfile ep){
        ArrayList<PLObject> list = new ArrayList<>();
        list.add(ep);
        for (ExerciseProfile e : ep.getExerciseProfiles()){
            list.add(e);
        }
        return list;
    }


  /*  public void drawWorkout(ArrayList<PLObject> toLayout, String workoutName) {
        ++numOfWorkouts;
        if (workoutName == null || workoutName.equals("")) {
            workoutName = ProgramTemplate.ProgramTemplateFactory.WhatsYourWorkoutName(numOfWorkouts - 1);
        }
        toLayout.add(new PLObject.WorkoutPLObject(numOfWorkouts - 1, workoutName));
    }*/

  /*  public void drawBody(ArrayList<PLObject> toLayout, String title) {
        toLayout.add(new PLObject.BodyText(numOfWorkouts - 1, numOfBodyParts, null, title));
    }*/

    /*  public ExerciseProfile drawExercise(Muscle m, ArrayList<PLObject> toLayout, WorkoutLayoutTypes innerType,
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
  */
    public PLObject.SetsPLObject createSetsPlObject(ExerciseProfile parent, ExerciseSet exerciseSet, WorkoutLayoutTypes innerType) {
        PLObject.SetsPLObject setsPLObject = new PLObject.SetsPLObject(parent, exerciseSet);
        setsPLObject.setInnerType(innerType);
        return setsPLObject;

    }

  /*  public void saveLayoutToDataBase(final boolean update) {
        new Thread(new Runnable() {
            @Override
            public void run() {
          //      requestSplitToLayout();
                //  dataManager.getProgramDataManager().insertTables(update, WorkoutsModel.this);
            }
        }).start();
    }
*/
   /* public boolean readLayoutFromDataBase(String currentDbName) {
        Cursor c = dataManager.getProgramDataManager().readLayoutTableCursor(currentDbName);
        dbName = currentDbName;
        layout = new ArrayList<>();
        String muscle_str;
        WorkoutLayoutTypes type;
        WorkoutLayoutTypes innerType;
        String rep;
        String rest;
        double weight;
        ExerciseSet exerciseSet;
        ExerciseProfile ep;

        //for the workoutList implementation
        ArrayList<PLObject> exArray = new ArrayList<>();


        int workoutIndex = 0;
        if (c != null && c.moveToFirst()) {
            do {
                type = WorkoutLayoutTypes.getEnum(c.getInt(c.getColumnIndex(TYPE)));
                switch (type) {
                    case WorkoutView:
                        String wName = c.getString(c.getColumnIndex(NAME));

                        //for the workoutList implementation
                        workoutsList.add(new Workout());
                        workoutsList.get(workoutIndex).setWorkoutName(wName);

                        bodyTemplate.add(new ArrayList<String>());
                        drawWorkout(layout, wName);
                        workoutsNames.add(wName);
                        break;
                    case BodyView:
                        String bName = c.getString(c.getColumnIndex(NAME));
                        drawBody(exArray, bName);
                        drawBody(layout, bName);
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

                        ep = drawExercise(muscle, layout, innerType, false);
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
                      *//*  if (parent != null) {
                            parent.getExerciseProfiles().add(ep);
                            ep.setParent(parent);
                        }*//*

                        //again for the new workoutList implementation
                        exArray.add(ep);
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
                                int position = ep.getSets().size() - 1;

                                PLObject.IntraSetPLObject setsPLObjectIntraSet = new PLObject.IntraSetPLObject(
                                        ep, exerciseSet, innerType, ep.getSets().get(position));
                                ep.getSets().get(position).getIntraSets().add(setsPLObjectIntraSet);
                                break;

                        }

                }

                //add the array to the workoutList
                workoutsList.get(workoutIndex).exArray = exArray;
            } while (c.moveToNext());
            mLayoutManagerHelper.reArrangeFathers(layout);
            initRecyclerMatrixLayout();
            initWorkouts();
            return true;
        }

        return false;
    }
*/
/*

    public void updateLayout(UpdateComponents updateComponents) {
        int workoutPosition = updateComponents.getWorkoutPosition();
        if (updateComponents.getLayout() != null) {
            getSplitRecyclerWorkouts().set(workoutPosition, updateComponents.getLayout());
        }
        if (updateComponents != null) {

            switch (updateComponents.getUpdateType()) {
                case DELETE_EXERCISE:
                    getSplitRecyclerWorkouts().get(workoutPosition).remove(updateComponents.getRemovePosition());
                    break;
                case SWAP:
                    Collections.swap(getSplitRecyclerWorkouts().get(workoutPosition), updateComponents.fromPosition, updateComponents.toPosition);
                    break;
                case NEW_EXERCISE:
                    //   getSplitRecyclerWorkouts().set(workoutPosition, updateComponents.getLayout());
                    ExerciseProfile epCopy = (ExerciseProfile) updateComponents.getPlObject();
                    int position = updateComponents.getToPosition();
                    if (epCopy != null) {
                        getSplitRecyclerWorkouts().get(workoutPosition).add(updateComponents.getToPosition(), epCopy);
                    } else {
                        drawExercise(null,
                                getSplitRecyclerWorkouts().get(workoutPosition),
                                updateComponents.innerType, true);
                    }

                    // initRecyclerMatrixLayout(false).get(workoutPosition).add(updateComponents.getInsertPosition(), updateComponents.getPlObject());
                    break;
                case NEW_SUPERSET:
                    ExerciseProfile ep = (ExerciseProfile) updateComponents.getPlObject();
                    getSplitRecyclerWorkouts().get(workoutPosition).add(updateComponents.toPosition, ep);
                    break;
                case NEW_WORKOUT:
                    getSplitRecyclerWorkouts().add(new ArrayList<PLObject>());
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
                    break;
                case SWAP_WORKOUTS:
                    Collections.swap(getSplitRecyclerWorkouts(), updateComponents.fromPosition, updateComponents.toPosition);
                    Collections.swap(getWorkouts(), updateComponents.fromPosition, updateComponents.toPosition);
                    break;
            }


            onLayoutChange();
            numOfWorkouts = 0;
            numOfBodyParts = 0;
            sizeOfWorkouts = 0;
            numOfExercises = 0;
        }
    }

*/

 /*   private void onLayoutChange() {
        requestSplitToLayout();
        initRecyclerMatrixLayout();
        initWorkouts();
    }
*/

   /* public void updateLayoutStats(boolean haveStatsHolder) {
        int bodyPos = 0;
        for (int i = 0; i < layout.size(); i++) {
            if (layout.get(i).getType() == WorkoutLayoutTypes.BodyView) {

                PLObject.BodyText bt = (PLObject.BodyText) layout.get(i);
                bt.setStatsHolder(statsCalculator.calculateStatsHolder(bt.getMuscle(), i, haveStatsHolder));
                bodyPos++;
            }
        }
    }
*/
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

/*

    public int getLayoutSize() {
        if (layout != null)
            return layout.size();

        return 0;
    }
*/

/*

    public ArrayList<PLObject> getLayout() {
        return layout;
    }

    */
/* public int getNumOfBodyParts() {
         return numOfBodyParts;
     }
 *//*

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
*/


/*    public void requestSplitToLayout() {
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
    }*/


    /*   public StatsCalculatorManager getStatsCalculator() {
           return statsCalculator;
       }

       public ProgramTemplate getProgramTemplate() {
           return programTemplate;
       }

       public DataManager getDataManager() {
           return dataManager;
       }

   */
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

    /**
     * this class provides an implementation of clean modification of the workoutlist
     * it provides methods for editing the list, as well as calling the workout's
     * observer.
     */

    public static class ListModifier {


        protected enum DoTasks {
            DoNew, Remove, Duplicate, Child
        }

        private Lister workoutList;
        private WorkoutItemAdapterFactory adapterFactory;
        private WorkoutItemAdapter workoutItemAdapters;
        private Modifier modifier;
        private Modifier mModifier;
        private Queue<DoTasks> doTasks = new LinkedList<>();
        private int positionTo = -1;
        private Queue<PLObject> removeObjects = new LinkedList<>();
        private PLObject parent;

        private static ListModifier listModifier;

        private ListModifier(Lister workoutList) {
            this.workoutList = workoutList;
            this.modifier = modifier;
        }

        private ListModifier(Lister workoutList, WorkoutItemAdapter workoutItemAdapters) {

            this.workoutList = workoutList;
            this.workoutItemAdapters = workoutItemAdapters;
        }

        private ListModifier(Lister workoutList, WorkoutItemAdapterFactory adapterFactory) {

            this.workoutList = workoutList;
            this.adapterFactory = adapterFactory;
        }

        public static ListModifier OnWith(Lister workoutList, WorkoutItemAdapter workoutItemAdapter) {
            if (listModifier == null) {
                listModifier = new ListModifier(workoutList, workoutItemAdapter);
            }
            return listModifier;
        }

        public static ListModifier OnWith(Lister workoutList, WorkoutItemAdapterFactory adapterFactory) {
            if (listModifier == null) {
                listModifier = new ListModifier(workoutList, adapterFactory);
            }
            return listModifier;
        }

        public ListModifier doAddNew(int positionTo) {
            listModifier.positionTo = positionTo;
            listModifier.doTasks.add(DoNew);
            return listModifier;
        }

        public ListModifier doDuplicate(int start) {
            listModifier.positionTo = start;
            listModifier.doTasks.add(Duplicate);
            return listModifier;
        }

        public ListModifier doChild(PLObject parent) {
            listModifier.parent = parent;
            listModifier.doTasks.add(Child);
            return listModifier;
        }

        public ListModifier doRemove(PLObject p, int count) {
            listModifier.removeObjects.add(p);
            listModifier.doTasks.add(Remove);
            return listModifier;
        }


        public void applyWith(WorkoutItemAdapter.ItemAdapter adapter) {
            listModifier.modifier = new Modifier();
            listModifier.modifier.adapter = adapter;
            listModifier.modifier.workoutList = workoutList;
            listModifier.modifier.workoutItemAdapter = workoutItemAdapters;
            listModifier.modifier.adapterFactory = adapterFactory;
            listModifier.modifier.listModifier = listModifier;
            apply();
            listModifier = null;

        }

        private void apply() {

            for (DoTasks d : doTasks) {
                switch (d) {
                    case DoNew:
                        modifier.addNew(positionTo);
                        break;
                    case Remove:
                        Iterator<PLObject> it = removeObjects.iterator();
                        while (it.hasNext()) {
                            PLObject p = it.next();
                            adapterOrFactory(p);
                            modifier.remove(p);

                        }
                        break;
                    case Duplicate:
                        modifier.duplicate(positionTo);
                        break;
                    case Child:
                        adapterOrFactory(parent);
                        modifier.child(parent);
                        break;
                }
            }
        }

        private void adapterOrFactory(PLObject p){
            if (workoutItemAdapters == null) {
                if (adapterFactory == null) {
                    throw new IllegalArgumentException("apply: Please provide an adapter or factory ");
                } else {
                    modifier.workoutItemAdapter = adapterFactory.create(p.getClass());
                }
            }
        }

      /*  public void andNotify() {
            if (workoutList.getObserver() != null)
                workoutList.getObserver().onChange(null);
        }*/

        public static class Modifier {
            Lister workoutList;
            ListModifier listModifier;
            private WorkoutItemAdapter workoutItemAdapter;
            private WorkoutItemAdapter.ItemAdapter adapter;
            private WorkoutItemAdapterFactory adapterFactory;


            private Modifier() {

            }


            public ListModifier addNew(int positionTo) {
                ArrayList list = new ArrayList();
                list.add(workoutItemAdapter.insert());
                workoutList.getList().addAll(positionTo,
                        workoutItemAdapter.onInsert(positionTo, list));
                workoutItemAdapter.notifyInserted(
                        positionTo,
                        list.size(),
                        adapter);
                return listModifier;
            }

            public <T> ListModifier remove(PLObject p) {
                int pos = workoutList.getList().indexOf(p);
                workoutList.getList().remove(p);
                workoutItemAdapter.remove(pos, p);
                workoutItemAdapter.notifyRemoved(pos, listModifier.removeObjects.size(), adapter);

                return listModifier;
            }

            public <T> ListModifier duplicate(int position) {
                PLObject pl = (PLObject) workoutList.getList().get(position);
                PLObject clone = workoutItemAdapter.onDuplicate(pl);
                workoutList.getList().add(position + 1, clone);
                workoutItemAdapter.notifyDuplicate(position + 1, adapter);
                return listModifier;
            }

            public <T>ListModifier child(PLObject parent){
                //calls the adapter and asks for a child created by the client
                PLObject child = workoutItemAdapter.onChild(parent);

                //gets the parent's position in the list
                int parentPosition = workoutList.getList().indexOf(parent);

                //requests a specific position for the child to be inserted
                //zero means the child be added to the top in the parent's group
                int position = workoutItemAdapter.onAddingChildToGroup(parent, child);
                int finalPos = position+parentPosition;
                //adds the child to the list
                workoutList.getList().add(finalPos, child);

                //notify implemented by the client
                workoutItemAdapter.notifyChild(finalPos, adapter);
                return listModifier;
            }
        }

        public static class Factory {

        }
    }
}
