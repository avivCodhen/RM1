package com.strongest.savingdata.BaseWorkout;

import android.content.Context;

import com.strongest.savingdata.R;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Cohen on 8/5/2017.
 */

//****
//stores the default values for the workouts template.
//values can be assigned to create a custom workout
//stores the number of exercises per small and big body part
//
//****
public class ProgramTemplate implements Serializable {


    public static int returnComplex() {
        return complex;
    }

    private boolean modified = true;
    private String templateName;
    private String dbName;
    private boolean custom;
    public static int complex;
    private ArrayList<String> programWorkoutsNames;
    private String[] templateWorkoutsNames;
    private int bodyPartsPerWorkout;
    private String[] stringWorkouts;
    public Muscle muscle;
    //    private Routine routine;
    private int defaultBigParts = 2; // how many exercises per big body part
    private int defaultSmallParts = 1; //how many exercises per small body part
    public ArrayList<ArrayList<String>> bodyTemplate; //template of bodyparts
    private int roundsPerWeek;
    private int recommendedWorkoutsPerWeek;
    private String dateCreated;

    public ProgramTemplate(ArrayList<ArrayList<String>> bodyTemplate) {
        //    this.context = context;
        this.bodyTemplate = bodyTemplate;
    }

    private ProgramTemplate() {
        dbName = new SimpleDateFormat("yyyy-MM-dd-hh-", Locale.US).format(new Date());
    }

    public int getRoundsOfWorkoutsPerWeek() {
        return roundsPerWeek;
    }

    public void setRoundsPerWeek(int roundsPerWeek) {
        this.roundsPerWeek = roundsPerWeek;
    }

   /* public ProgramTemplate(Context context) {
        this.context = context;
    }


    public void setContext(Context context) {
        this.context = context;
    }
*/
//    private Context context;

/*    public int getAllBodyPartsLength() {
        int sum = 0;
        for (int i = 0; i < getBodyTemplate().size(); i++) {
            sum += getBodyTemplate().get(i).size();
        }
        return sum;
    }*/

    public ArrayList<String> getAllBodyParts() {
        ArrayList<String> arr = new ArrayList();
        for (int i = 0; i < getBodyTemplate().size(); i++) {
            for (int j = 0; j < getBodyTemplate().get(i).size(); j++) {
                arr.add(getBodyTemplate().get(i).get(j));
            }
        }
        return arr;
    }

//    public Routine getRoutine() {
//        return routine;
//    }
//
//    public void setRoutine(Routine routine) {
//        this.routine = routine;
//    }

    public String[] getStringWorkouts() {
        return stringWorkouts;
    }

    public void setStringWorkouts(String[] stringWorkouts, Context c) {
        this.stringWorkouts = c.getResources().getStringArray(R.array.workouts);
    }

    public void setDefaultBigParts(int defaultBigParts) {
        this.defaultBigParts = defaultBigParts;
    }

    public void setDefaultSmallParts(int defaultSmallParts) {
        this.defaultSmallParts = defaultSmallParts;
    }

    public void setBodyPartsPerWorkout(int bodyPartsPerWorkout) {
        this.bodyPartsPerWorkout = bodyPartsPerWorkout;
    }

   /* public String[] getWorkoutsNames() {
        if (programWorkoutsNames == null) {
            initProgramWorkoutsNames();
            return programWorkoutsNames;
        } else {
            return programWorkoutsNames;
        }
    }*/

    public int getBodyPartsPerWorkout(int position) {
        return bodyTemplate.get(position).size();
    }

    public boolean isCustom() {
        return custom;
    }

    public void setCustom(boolean custom) {
        this.custom = custom;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public int getRecommendedWorkoutsPerWeek() {
        return recommendedWorkoutsPerWeek;
    }

    public void setRecommendedWorkoutsPerWeek(int recommendedWorkoutsPerWeek) {
        this.recommendedWorkoutsPerWeek = recommendedWorkoutsPerWeek;
    }
    // private StatsBeanProcessor stats;


    public int getNumOfWorkouts() {
        return getBodyTemplate().size();
    }
    //private Workout[] workouts;
    //private static Workout workout;

/*
    private String[] initProgramWorkoutsNames() {
        String[] stringWorkouts = {"A", "B", "C", "D", "E", "F", "G"};
        programWorkoutsNames = new String[bodyTemplate.length];
        for (int i = 0; i < programWorkoutsNames.length; i++) {
            programWorkoutsNames[i] = stringWorkouts[i];
        }
        return programWorkoutsNames;

    }
*/

    public Muscle getMuscle() {
        return muscle;
    }

    public void setBodyPart(Muscle muscle) {
        this.muscle = muscle;
    }


    public int getDefaultSmallParts() {
        return defaultSmallParts;
    }

    public int getDefaultBigParts() {
        return defaultBigParts;
    }

    public String getName() {
        return getClass().getEnclosingClass().getSimpleName();
    }

    public ArrayList<ArrayList<String>> getBodyTemplate() {
        return bodyTemplate;
    }

    public void setBodyTemplate(ArrayList<ArrayList<String>> bodyTemplate) {
        this.bodyTemplate = bodyTemplate;
    }


//    public static ProgramTemplate getWorkout(Routine r, Context context) {
//        switch (r) {
//            case FBW:
//                return new FBW(context);
//            case AB:
//                return new AB(context);
//            case ABC:
//                return new ABC(context);
//            case ABCDE:
//                return new ABCDE(context);
//        }
//        return null;
//    }


    public int getBlockLength(Muscle muscle) {
        if (muscle.getMuscle_size() == "big") {
            return this.getDefaultBigParts();
        } else {
            return this.getDefaultSmallParts();
        }
    }


    public ArrayList<String> getWorkoutsNames() {
        return programWorkoutsNames;
    }

    public void setWorkoutsNames(ArrayList<String> programWorkoutsNames) {
        this.programWorkoutsNames = programWorkoutsNames;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public boolean isModified() {
        return modified;
    }

    public void setModified(boolean modified) {
        this.modified = modified;
    }

    public String getDbName() {
        return dbName;
    }


    /**
     * static workouts with hardcoded configurations
     */

    public static class ProgramTemplateFactory {

        public static ArrayList<String> WhatsYourName(int numOfWorkouts) {
            String[] stringWorkouts = {"A", "B", "C", "D", "E", "F", "G"};
            String[] workouts = new String[numOfWorkouts];
            for (int i = 0; i < numOfWorkouts; i++) {
                workouts[i] = stringWorkouts[i];
            }
            return new ArrayList<String>(Arrays.asList(workouts));
        }

        public static String WhatsYourWorkoutName(int workout){
            String[] stringWorkouts = {"A", "B", "C", "D", "E", "F", "G"};
            return stringWorkouts[workout];
        }


        public static ArrayList<ArrayList<String>> matrixArrayToList(String[][] t) {
            ArrayList<ArrayList<String>> arr = new ArrayList<>();
            for (String[] str_arr : t) {
                ArrayList<String> a =  new ArrayList<>(Arrays.asList((str_arr)));
                arr.add(a);
            }
            return arr;
        }

        public static ProgramTemplate getStaticTemplates(String templateName) {
            ProgramTemplate p = new ProgramTemplate();
            String[][] bodyTemplate;
            switch (templateName) {
                case "fbw":
                    bodyTemplate = new String[][]{
                            {
                                    "chest", "back", "legs", "shoulders", "triceps", "biceps"
                            }
                    };
                    p.setBodyTemplate(matrixArrayToList(bodyTemplate));
                    p.setDefaultBigParts(1);
                    p.setDefaultSmallParts(1);
                    p.setRoundsPerWeek(3);
                    break;
                case "ab":
                    bodyTemplate = new String[][]{
                            {
                                    "chest", "shoulders", "triceps"
                            },
                            {
                                    "back", "legs", "biceps"
                            }
                    };
                    p.setBodyTemplate(matrixArrayToList(bodyTemplate));
                    p.setDefaultBigParts(2);
                    p.setDefaultSmallParts(1);
                    p.setRoundsPerWeek(2);
                    break;
                case "abc":
                    bodyTemplate = new String[][]{
                            {
                                    "chest", "biceps"
                            },
                            {
                                    "back", "triceps"
                            }
                            ,
                            {
                                    "legs", "shoulders"
                            }
                    };
                    p.setBodyTemplate(matrixArrayToList(bodyTemplate));
                    p.setDefaultBigParts(3);
                    p.setDefaultSmallParts(2);
                    p.setRoundsPerWeek(2);
                    p.setRecommendedWorkoutsPerWeek(5);
                    break;
                case "abcde":
                    bodyTemplate = new String[][]{
                            {
                                    "chest"
                            },
                            {
                                    "back"
                            }
                            ,
                            {
                                    "shoulders"
                            },
                            {
                                    "arms"
                            },
                            {
                                    "legs"
                            }
                    };
                    p.setBodyTemplate(matrixArrayToList(bodyTemplate));
                    p.setDefaultBigParts(4);
                    p.setDefaultSmallParts(3);
                    p.setRoundsPerWeek(1);
                    p.setRecommendedWorkoutsPerWeek(5);
                    break;
            }
            p.setDateCreated(new SimpleDateFormat("yyyy-MM-dd-hh", Locale.US).format(new Date()));
            p.setWorkoutsNames(WhatsYourName(p.getNumOfWorkouts()));
            p.setDateCreated(new SimpleDateFormat("yyyy-MM-dd-hh", Locale.US).format(new Date()));
            p.setTemplateName(templateName);

            return p;
        }

        public static ProgramTemplate createCustomProgramTemplate(
                ArrayList<ArrayList<String>> bodyTemplate,
                int roundsPerWeek,
                int recommendedWorkoutsPerWeek,
                String templateName,
                ArrayList<String> workoutNames
        ) {

            ProgramTemplate p = new ProgramTemplate();
            p.setBodyTemplate(bodyTemplate);
            p.setRoundsPerWeek(roundsPerWeek);
            p.setRecommendedWorkoutsPerWeek(recommendedWorkoutsPerWeek);
            p.setTemplateName(templateName);
            p.setDateCreated(new SimpleDateFormat("yyyy-MM-dd-hh-", Locale.US).format(new Date()));

            if (workoutNames.equals("")) {
                p.setTemplateName(p.getDateCreated());
            } else {
                p.setTemplateName(templateName);
            }
            if (workoutNames == null) {
                p.setWorkoutsNames(WhatsYourName(p.getNumOfWorkouts()));
            } else {
                p.setWorkoutsNames(workoutNames);
            }
            return p;
        }

    }


}


/* public static class FBW extends ProgramTemplate {


        public FBW(Context context) {
            super(context);
            initWorkout();
        }

        private void initWorkout() {


            setBodyTemplate(
                    new MainMuscleEnum[][]{
                            {CHEST, BACK, LEGS, SHOULDERS, TRICEPS, BICEPS}
                    }
            );


        }
    }


    public static class AB extends ProgramTemplate {

        public AB(Context context) {
            super(context);
            initWorkout();
        }

        private void initWorkout() {
            setRoutine(Routine.AB);
            setDefaultBigParts(2);
            setDefaultSmallParts(1);
            setRoundsPerWeek(3);
            setBodyTemplate(
                    new MainMuscleEnum[][]{
                            {CHEST, SHOULDERS, TRICEPS},
                            {BACK, LEGS, BICEPS}

                    });
        }

    }

    public static class ABC extends ProgramTemplate {

        public ABC(Context context) {
            super(context);
            initWorkout();
        }

        private void initWorkout() {
            setRoutine(Routine.ABC);
            setDefaultBigParts(3);
            setDefaultSmallParts(3);
            setRoundsPerWeek(5);
            setBodyTemplate(
                    new MainMuscleEnum[][]{
                            {CHEST, BICEPS},
                            {BACK, TRICEPS},
                            {LEGS, SHOULDERS}
                    });
        }
    }

    public static class ABCDE extends ProgramTemplate {

        public ABCDE(Context context) {
            super(context);
            initWorkout();
        }

        private void initWorkout() {
            setRoutine(Routine.ABCDE);
            setDefaultBigParts(4);
            setDefaultSmallParts(3);
            setRoundsPerWeek(6);
            setBodyTemplate(
                    new MainMuscleEnum[][]{
                            {CHEST},
                            {BACK},
                            {SHOULDERS},
                            {ARMS},
                            {LEGS},

                    });
        }
    }

    *//**
 * this method takes a 1d list, and creates a 2d array for the bodytemplate
 * to be set.
 *//*
    public void setTemplateFromList(ArrayList<PLObjects> arr) {
        int workouts = 0;
        int body = 0;
        for (PLObjects pLObj : arr) {
            if (pLObj.getType() == WorkoutLayoutTypes.WorkoutView) {
                workouts++;
            } else {
                body++;
            }
        }
        MainMuscleEnum[][] muscles_ar = new MainMuscleEnum[workouts][];
        ArrayList<MainMuscleEnum> list;
        int j = -1;
        int k = 0;
        int length = arr.size();
        list = new ArrayList();

        for (int i = 0; i < length; i++) {
            if (arr.get(0).getType() == WorkoutLayoutTypes.WorkoutView) {
                if (i != 0) {
                    muscles_ar[j] = list.toArray(new MainMuscleEnum[list.size()]);
                    list = new ArrayList();
                }
                arr.remove(0);
                j++;
                k = 0;
            } else {
                list.add(((PLObjects.BodyText) arr.get(0)).getMuscle().getMuscleEnum());
                arr.remove(0);
            }

        }
        muscles_ar[j] = list.toArray(new MainMuscleEnum[list.size()]);
        this.bodyTemplate = muscles_ar;
    }*/