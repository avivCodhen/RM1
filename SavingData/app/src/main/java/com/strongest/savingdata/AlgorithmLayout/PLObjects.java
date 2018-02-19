package com.strongest.savingdata.AlgorithmLayout;

import android.content.Context;
import android.support.annotation.Nullable;

import com.strongest.savingdata.AlgorithmStats.StatsHolder;

import com.strongest.savingdata.BaseWorkout.Muscle;
import com.strongest.savingdata.Database.Exercise.BeansHolder;

import java.util.ArrayList;


/**
 * Created by Cohen on 10/18/2017.
 */

public class PLObjects implements Aba {


    public WorkoutLayoutTypes type;
    public int workoutId;
    public int bodyId;
    public boolean expand;

    public boolean isExpand() {
        return expand;
    }

    public void setExpand(boolean expand) {
        this.expand = expand;
    }

    public int getBodyId() {
        return bodyId;
    }

    public int getWorkoutId() {
        return workoutId;
    }

    public WorkoutLayoutTypes getType() {
        return type;
    }

    public static class WorkoutText extends PLObjects {

        private Context context;
        private String workoutName;

        public WorkoutText(int id, String workoutName) {
            workoutId = id;
            this.context = context;
            this.workoutName = workoutName;
            type = WorkoutLayoutTypes.WorkoutView;
        }


        public void setWorkoutName(String workoutName) {
            this.workoutName = workoutName;
        }

        public String getWorkoutName() {
            return workoutName;
        }
    }

    public static class BodyText extends PLObjects {

        //  private static final WorkoutLayoutTypes type = WorkoutLayoutTypes.BodyView;
        private Context context;
        private Muscle muscle;
        private StatsHolder statsHolder;

        public BodyText(Muscle muscle, int workoutId, int bodyId, @Nullable StatsHolder statsHolder) {
            this.workoutId = workoutId;
            this.bodyId = bodyId;
            this.context = context;
            type = WorkoutLayoutTypes.BodyView;
            this.muscle = muscle;
            this.statsHolder = statsHolder;

        }


        public Muscle getMuscle() {
            return muscle;
        }

        public void setBodyPart(Muscle muscle) {
            this.muscle = muscle;
        }

        public void setStatsHolder(StatsHolder statsHolder) {
            this.statsHolder = statsHolder;
        }

        public StatsHolder getStatsHolder() {
            return statsHolder;
        }

    }

    public static class ExerciseProfile extends PLObjects {

        // private final WorkoutLayoutTypes type = WorkoutLayoutTypes.ExerciseView;
        // private BeansHolder beansHolder;
        //private Muscle muscle;
        private boolean loaded;
        private boolean selected = false;
        private BeansHolder mBeansHolder;
        private ArrayList<BeansHolder> beansHolders;
        private int exerciseProfileId;
        private boolean firstExercise;
        private boolean hasBeansHolders;
        private WorkoutLayoutTypes innerType;

        //private char id;
        public ExerciseProfile(BeansHolder mBeansHolder, ArrayList<BeansHolder> beansHolders, int workoutId, int bodyId, int exerciseProfileId) {
            this.mBeansHolder = mBeansHolder;
            this.beansHolders = beansHolders;
            this.exerciseProfileId = exerciseProfileId;
            this.workoutId = workoutId;
            this.bodyId = bodyId;
            //this.beansHolder = beansHolder;
            type = WorkoutLayoutTypes.ExerciseView;

        }

      /*  public void setBeansHolder(com.strongest.savingdata.Database.Exercise.BeansHolder beansHolder) {
            *//*exerciseProfileView.setExercise(beansHolder.getExercise());
            exerciseProfileView.setReps(beansHolder.getRep());
            exerciseProfileView.setMethod(beansHolder.getMethod());
            exerciseProfileView.setSuperset(beansHolder.getSuperset());*//*
            this.beansHolder = beansHolder;
        }*/

    /*    public void setMuscle(Muscle muscle) {
            this.muscle = muscle;
        }


        public Muscle getMuscle() {
            return muscle;
        }*/

      /*  public ExerciseProfileView getExerciseProfileView() {
            return exerciseProfileView;
        }
*/
      /*  public BeansHolder getBeansHolder() {
            return beansHolder;
        }*/

        public WorkoutLayoutTypes getInnerType() {
            return innerType;
        }

        public void setInnerType(WorkoutLayoutTypes innerType) {
            this.innerType = innerType;
        }

        public boolean isSelected() {
            return selected;
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
        }

        public int getExerciseProfileId() {
            return exerciseProfileId;
        }

        public boolean isFirstExercise() {
            return firstExercise;
        }

        public void setFirstExercise(boolean firstExercise) {
            this.firstExercise = firstExercise;
        }

        public ArrayList<BeansHolder> getBeansHolders() {
            return beansHolders;
        }

        public void setBeansHolders(ArrayList<BeansHolder> beansHolders) {
            this.beansHolders = beansHolders;
        }

        public BeansHolder getmBeansHolder() {
            return mBeansHolder;
        }

        public void setmBeansHolder(BeansHolder mBeansHolder) {
            this.mBeansHolder = mBeansHolder;
        }

        public boolean isHasBeansHolders() {
            return mBeansHolder != null;
        }
    }

    public static class BeansHolderPLObject extends PLObjects implements Ben {

        private BeansHolder beansHolder;

        public BeansHolderPLObject(BeansHolder beansHolder) {
            this.beansHolder = beansHolder;
            type = WorkoutLayoutTypes.BeansHolderPLObject;
        }


        public BeansHolder getBeansHolder() {
            return beansHolder;
        }

        public void setBeansHolder(BeansHolder beansHolder) {
            this.beansHolder = beansHolder;
        }
    }

    public static class ProgressPLObject extends PLObjects {


        public ProgressPLObject(BeansHolder beansHolder) {

            type = WorkoutLayoutTypes.BeansHolderPLObject;
        }

    }

    public static class AddExercise extends PLObjects {

        private Muscle m;

        public AddExercise(Muscle m) {
            this.m = m;
            type = WorkoutLayoutTypes.AddExercise;
        }

        public Muscle getM() {
            return m;
        }
    }

  /*  public static class Method extends ExerciseProfile {

        private String method;
        private Muscle m;

        public Method(String method, ArrayList<BeansHolder> beansHolders, Muscle m, int workoutId, int bodyId, int exerciseProfileId) {
            super(beansHolders, m, workoutId, bodyId, exerciseProfileId);
            this.method = method;
            type = WorkoutLayoutTypes.Method;
        }

        public Muscle getM() {
            return m;
        }

        public String getMethod() {
            return method;
        }

        public void setMethod(String method) {
            this.method = method;
        }
    }*/

    public static class Superset extends PLObjects {

        private Muscle m;

        public Superset(Muscle m) {
            this.m = m;
            type = WorkoutLayoutTypes.AddExercise;
        }

        public Muscle getM() {
            return m;
        }
    }


}
