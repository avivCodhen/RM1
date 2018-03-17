package com.strongest.savingdata.Database.Progress;

import com.strongest.savingdata.AlgorithmLayout.PLObjects;
import com.strongest.savingdata.Database.Exercise.Sets;

/**
 * Created by Cohen on 1/1/2018.
 */

public class ProgressActions {

    public enum ProgressActionsType{
        Inject, Delete, Swap, Change;
    }

    public ProgressActionsType type;
    private int exerciseProfileId;
    public ProgressActions(int exerciseProfileId){

        this.exerciseProfileId = exerciseProfileId;
    }

    public void setType(ProgressActionsType type) {
        this.type = type;
    }

    public ProgressActionsType getType() {
        return type;
    }

    public static class Inject extends ProgressActions{
        private int position;
        private PLObjects.ExerciseProfile exerciseProfile;

        public Inject(int position, PLObjects.ExerciseProfile exerciseProfile, int exerciseProfileId) {
            super(exerciseProfileId);
            this.position = position;
            this.exerciseProfile = exerciseProfile;
            this.type = ProgressActionsType.Inject;
        }
    }

    public static class Delete extends ProgressActions{
        private int position;

        public Delete(int position, int exerciseProfileId) {
            super(exerciseProfileId);
            this.position = position;
            this.type = ProgressActionsType.Delete;
        }
    }

    public static class Change extends ProgressActions{
        private int position;
        private Sets sets;

        public Change(int exerciseProfileId, int position, Sets sets) {
            super(exerciseProfileId);
            this.position = position;
            this.sets = sets;
        }
    }

    public static class Swap extends ProgressActions{
        private int fromPosition;
        private int ToPosition;
        public Swap(int exerciseProfileId, int fromPosition, int toPosition) {
            super(exerciseProfileId);
            this.fromPosition = fromPosition;
            ToPosition = toPosition;
        }
    }

}
