package com.strongest.savingdata.AlgorithmGenerator;

import android.content.Context;
import android.util.Log;

import com.strongest.savingdata.AlgorithmLayout.LayoutManager;
import com.strongest.savingdata.AlgorithmLayout.WorkoutLayoutTypes;
import com.strongest.savingdata.BaseWorkout.Muscle;
import com.strongest.savingdata.BaseWorkout.ProgramTemplate;
import com.strongest.savingdata.Database.Exercise.Beans;
import com.strongest.savingdata.Database.Exercise.ExerciseSet;
import com.strongest.savingdata.Database.Managers.DataManager;
import com.strongest.savingdata.AlgorithmLayout.PLObject;

import java.util.ArrayList;
import java.util.Random;

import static com.strongest.savingdata.AlgorithmLayout.WorkoutLayoutTypes.*;
import static com.strongest.savingdata.Database.Exercise.DBExercisesHelper.TABLE_EXERCISES_GENERATOR;
import static com.strongest.savingdata.Database.Exercise.DBExercisesHelper.TABLE_REPS;
import static com.strongest.savingdata.Database.Exercise.DBExercisesHelper.TABLE_REST;
import static com.strongest.savingdata.Database.Exercise.DBExercisesHelper.TABLE_SETS;

/**
 * Created by Cohen on 7/4/2017.
 */

public class Generator {

    public static final String LOAD_EXERCISE_LEVEL = "exercise_level";
    public static final String LOAD_INTENSITY = "intensity";
    public static final String LOAD_VOLUME = "volume";
    public static final String LOAD_COMPLEXITY = "complex";
    public static final String ROUTINE = "complex";

    private static final String TAG = "aviv";
    private Context context;
    private ProgramTemplate programTemplate;

    private Load load; //recieves exercise level, volume, reps and complexity
    private String[] bodyParts; //body parts of the specific routine
    private ArrayList<PLObject> exArray; //stores the layout array
    private ArrayList<Object> newExArray; // loads the new generated layout array
    int numOfExercises;

    private DataManager dm;

    private BParams[][] permMethodBlock;
    private BParams[][] permRepBlock;

    private ArrayList<ArrayList<Beans>> reps;
    private ArrayList<ArrayList<Beans>> methods;

    private boolean first = false;
    //private Programmer programmer;
    private LayoutManager layoutManager;
    GeneratorTemplateManager generatorTemplateManager;

    //level_beginner represents exerciseLevel level two.

    public static final int ONE_EXERCISE = 1, TWO_EXERCISE = 2, THREE_EXERCISE = 3, FOUR_EXERCISE = 4,
            FIVE_EXERCISE = 5;
    public static final int LEVEL_BEGINNER = 1, LEVEL_BALANCED = 2, LEVEL_TWO = 3;

    //public final int THREE = 1, FOUR =


    private static final int NO_GENERATE = 0;

    public Generator(Context context, ProgramTemplate programTemplate, DataManager dm) {
        this.dm = dm;
        this.context = context;
        this.programTemplate = programTemplate;
        this.load = getSharedPrefsLoad();
    }

    private Load getSharedPrefsLoad() {
        Load l = new Load(
                dm.getPrefs().getInt(LOAD_EXERCISE_LEVEL, 0),
               /* dm.getPrefs().getInt(LOAD_VOLUME, 0)*/ 0,
                dm.getPrefs().getInt(LOAD_INTENSITY, 0),
                dm.getPrefs().getInt(LOAD_COMPLEXITY, 0)
        );
        return l;
    }

    public LayoutManager generate() {
        //  int counter = 0;
        // newExArray = new ArrayList<>();
        // ExercisesDataManager dm = new ExercisesDataManager(context);
        // programLayoutManager = new ProgramLayoutManager(context);
        // programManager = new ProgramManager(context, workout.getRoutine());
        // programManager.createProgramLayout();
        // exArray = programManager.getProgramLayoutManager().getLayout();
        // int length = workout.getAllBodyPartsLength();
        layoutManager = new LayoutManager(context, dm, programTemplate);
        fillProgram();
        //   programManager.setProgramLayoutManager(programLayoutManager);
        // programManager.updateRecyclerLayout(newExArray);
        // programManager.setStats();

       // layoutManager.finish(true);
        return layoutManager;
    }

    private void fillProgram() {
        reps = new ArrayList<>();
        methods = new ArrayList<>();
        for (int i = 0; i < programTemplate.getNumOfWorkouts(); i++) {
            String workoutName = programTemplate.getWorkoutsNames().get(i);
            layoutManager.drawWorkout(layoutManager.getLayout(), workoutName);
            first = true;
            fillWorkout(i);
        }
    }

    private void fillWorkout(int currentWorkout) {
        for (int i = 0; i < programTemplate.getBodyTemplate().get(currentWorkout).size(); i++) {
            String muscle = programTemplate.getBodyTemplate().get(currentWorkout).get(i);
            Muscle muscle1 = Muscle.createMuscle(dm.getMuscleDataManager(), muscle);
            layoutManager.drawBody(layoutManager.getLayout(), muscle1.getMuscle_display());
            fillBlock(muscle1, i);
        }
    }


    //loads the correct body part (tag)
    private void fillBlock(Muscle currentMuscle, int currentBody) {

        //programTemplate.setBodyPart(currentMuscle);
        int numOfExercises = currentMuscle.getMuscle_size().equals("big") ? programTemplate.getDefaultBigParts()
                :
                programTemplate.getDefaultSmallParts();

        if (numOfExercises > 5) {
            Log.d("aviv", "fillBlock: numOfExercises is more than 6 bro...");
            return;
        }
        generatorTemplateManager = new GeneratorTemplateManager(numOfExercises
                , currentMuscle, load);
        BParams[][] permExerciseBlock = generatorTemplateManager.getExerciseTemplate();
        if (first) {
            permRepBlock = generatorTemplateManager.getRepsTemplate();
            //permMethodBlock = generatorTemplateManager.getMethoTemplate();
        }

        for (int i = 0; i < numOfExercises; i++) {
            LoadExercises(i, currentMuscle, permExerciseBlock[i], permRepBlock[i], currentBody);
        }
        first = false;
    }


    private void LoadExercises(int pos, Muscle muscle, BParams[] exerciseParams, BParams[] repsParams, int currentBody) {
        Beans exerciseBean;
        Beans repBean;
        Beans methodBean;
        Beans restBean;
        DataManager dm = new DataManager(context);
        ArrayList<Beans> exercises = (ArrayList<Beans>) dm.getExerciseDataManager().readByBParams(TABLE_EXERCISES_GENERATOR,
                exerciseParams);
     /*   ArrayList<Beans> reps;
        ArrayList<Beans> methods;*/
        if (first) {
            reps.add((ArrayList<Beans>) dm.getExerciseDataManager().readByBParams(TABLE_REPS, repsParams));
            //  methods.add((ArrayList<Beans>) dm.getExerciseDataManager().readByBParams(TABLE_METHODS, methodParams));
        }
        BParams[] restParams = generatorTemplateManager.getRestTemplate(reps.get(pos).get(0).getIntensity());
        ArrayList<Beans> rests = (ArrayList<Beans>) dm.getExerciseDataManager().readByBParams(TABLE_REST, restParams);
        ArrayList<Beans> sets = (ArrayList<Beans>) dm.getExerciseDataManager().readByTable(TABLE_SETS);
        int exR = random(exercises.size());
        int repR = random(reps.get(pos).size());
        int metR = 0;
        int restR = random(rests.size());
        exerciseBean = exercises.get(exR);
        repBean = reps.get(pos).get(repR);
        restBean = rests.get(restR);
       /* boolean isMethod = methods.get(pos).size() != 0;
        if (isMethod) {
            metR = random(methods.get(pos).size());
            methodBean = methods.get(pos).get(metR);
        } else {
            methodBean = null;
        }*/
        WorkoutLayoutTypes innerType = ExerciseProfile;
       /* if (layoutManager.getLayout().size() != 0) {
            if (layoutManager.getLayout().get(layoutManager.getLayoutSize() - 1).getType() == ExerciseProfile) {
                if (layoutManager.getLayout().get(layoutManager.getLayoutSize() - 1).getBodyId() % 2 != 0) {
                    innerType = ExerciseViewLeftMargin;

                } else {
                    innerType = ExerciseViewRightMargin;

                }
            } else {
                innerType = ExerciseViewLeftMargin;
            }
        } else {
            innerType = ExerciseViewLeftMargin;
        }
*/
        injectExercise(sets.get(2), muscle, exerciseBean, repBean, restBean, innerType);
    }

    //saves the generated exercise to the new array
    private void injectExercise(Beans sets, Muscle muscle, Beans exercise, Beans reps, Beans rest,
                                WorkoutLayoutTypes type) {
        ExerciseSet beansHolder = new ExerciseSet();
        beansHolder.setExercise(exercise);
       // beansHolder.setRep(reps);
        // beansHolder.setMethod(method);
        beansHolder.setSets(sets);
       // beansHolder.setRest(rest);
        beansHolder.setLoaded(true);

        beansHolder.setSuperset(new Beans());
        //layoutManager.drawExercise(muscle,layoutManager.getLayout(),type);
        dm.getExerciseDataManager().removeByName(TABLE_EXERCISES_GENERATOR, exercise.getName());

        //     p.setRepsId(reps.getWorkoutId());


       /* if (method != null) {
            p.setMethod(method);
            p.setMethodId(method.getWorkoutId());
        }*/
        // p.setExPosition(exercise.getWorkoutId());
        // newExArray.add(p);
        // exArray.remove(0);


    }

    /*private String restTimeTypeCalc(Beans reps) {
        String[] rest_ar = context.getResources().getStringArray(R.array.rest_arr);
        switch (reps.getIntensity()){
            case 0:
                return rest_ar[];
        }
    }*/

    public int random(int limit) {
        Random r = new Random();
        if (limit != 0) {
            return r.nextInt(limit);
        } else {
            return 0;
        }

    }

    /*private BParams[]loadTemplates(BodyPart currentBodyPart){
        ExerciseTemplate exerciseTemplate = new ExerciseTemplate(currentBodyPart, load);
        BParams[][] blockTemplate = exerciseTemplate.getTemplate(numOfExercises);
    }*/

    public ArrayList<Object> getNewExArray() {
        return newExArray;
    }


    //loads the correct type of exercise level and body part
  /*  private void loadExercise(ExerciseData exData) {

        ExerciseTemplate et = new ExerciseTemplate(new BodyPart.Arms(context),new Load(0,0,0,0));
        if (true) {

            combinedArray = exData.initCombinedArrays(load.getExerciseLevel());
            switch (numOfExercises) {
                case 0:
                    return;
                case ONE_EXERCISE:
                    combinedArray.get(0).addAll(Arrays.asList(exData.singleExerciseArray(load.getExerciseLevel())));
                    addSmallPartsExercise(numOfExercises, combinedArray.get(0));
                    return;
                default:
                    Log.d(TAG, "loadExercise: ");
                //    int[] template = et.getTemplate(numOfExercises);
                    //addBigPartsExercise(combinedArray, template);
            }

        } else if (tag.equals("Arms")) {
          //  int[] template = ExerciseTemplate.ARMS_WORKOUT;
            //addArmsPartExercise(template);
        } else {
            //adds small body parts exercises
            ArrayList<ExerciseBean> arr = exData.getAllTypes();
            addSmallPartsExercise(numOfExercises, arr);
    }
        }*/


    //calls inject exercise with big body parts
   /* private void addBigPartsExercise(List<List<ExerciseBean>> arr, int[] t) {
        int j = 0;
        for (int i : t) {
            int r = random(arr.get(i).size());
            injectExercise(arr.get(i).get(r));
            arr.get(i).remove(r);
            j++;
        }

    }

    //calls inject exercise with small body parts
    private void addSmallPartsExercise(int num, List<ExerciseBean> arr) {
        if (num == 0)
            return;

        int r = random(arr.size());
        ExerciseBean exercise = null;
        exercise = arr.get(r);
        arr.remove(r);
        injectExercise(exercise);
        addSmallPartsExercise(num - 1, arr);
    }
*/

   /* public class Load {

        private int exerciseLevel;
        private int exerciseVolume;
        private int intensity;
        private int complexity;

        public Load(int exerciseLevel, int exerciseVolume, int intensity, int complexity) {
            this.exerciseLevel = exerciseLevel;
            this.exerciseVolume = exerciseVolume;
            this.intensity = intensity;
            this.complexity = complexity;
        }

        public int getExerciseVolume() {
            return exerciseVolume;
        }

        public int getExerciseLevel() {
            return exerciseLevel;
        }

        public int getIntensity() {
            return intensity;
        }

        public int getComplexity() {
            return complexity;
        }
    }

*/
}
