package com.strongest.savingdata.AlgorithmLayout;

import android.content.Context;
import android.support.annotation.Nullable;

import com.strongest.savingdata.AlgorithmStats.StatsHolder;

import com.strongest.savingdata.BaseWorkout.Muscle;
import com.strongest.savingdata.Database.Exercise.BeansHolder;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;


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
        private Muscle muscle;
        private boolean loaded;
        private boolean selected = false;
        private ArrayList<BeansHolder> beansHolders;
        private int exerciseProfileId;
        private boolean firstExercise;

        //private char id;
        public ExerciseProfile(ArrayList<BeansHolder> beansHolders, Muscle muscle, int workoutId, int bodyId, int exerciseProfileId) {
            this.beansHolders = beansHolders;
            this.exerciseProfileId = exerciseProfileId;
            this.workoutId = workoutId;
            this.bodyId = bodyId;
            //this.beansHolder = beansHolder;
            this.muscle = muscle;
            type = WorkoutLayoutTypes.ExerciseView;

        }

      /*  public void setBeansHolder(com.strongest.savingdata.Database.Exercise.BeansHolder beansHolder) {
            *//*exerciseProfileView.setExercise(beansHolder.getExercise());
            exerciseProfileView.setReps(beansHolder.getRep());
            exerciseProfileView.setMethod(beansHolder.getMethod());
            exerciseProfileView.setSuperset(beansHolder.getSuperset());*//*
            this.beansHolder = beansHolder;
        }*/

        public void setMuscle(Muscle muscle) {
            this.muscle = muscle;
        }


        public Muscle getMuscle() {
            return muscle;
        }

      /*  public ExerciseProfileView getExerciseProfileView() {
            return exerciseProfileView;
        }
*/
      /*  public BeansHolder getBeansHolder() {
            return beansHolder;
        }*/

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


}
