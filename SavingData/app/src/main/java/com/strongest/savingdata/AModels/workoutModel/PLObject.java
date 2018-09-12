package com.strongest.savingdata.AModels.workoutModel;

import android.content.Context;


import com.strongest.savingdata.BaseWorkout.Muscle;
import com.strongest.savingdata.Database.Exercise.Beans;
import com.strongest.savingdata.Database.Exercise.ExerciseSet;

import java.io.Serializable;
import java.util.ArrayList;


/**
 * Created by Cohen on 10/18/2017.
 */

public class PLObject implements Serializable {


    public WorkoutLayoutTypes type;
    public int workoutId;
    public int bodyId;
    public boolean expand;
    public boolean more;
    public boolean editMode;
    public WorkoutLayoutTypes innerType;
    public boolean isParent;


    public WorkoutLayoutTypes getInnerType() {
        return innerType;
    }

    public void setInnerType(WorkoutLayoutTypes innerType) {
        this.innerType = innerType;
    }

    public void setEditMode(boolean editMode) {
        this.editMode = editMode;
    }

    public boolean isEditMode() {
        return editMode;
    }

    public void setMore(boolean more) {
        this.more = more;
    }

    public boolean isMore() {
        return more;
    }

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

    public static class WorkoutPLObject extends PLObject {

        private Context context;
        private String workoutName;

        public WorkoutPLObject(int id, String workoutName) {
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

    public static class BodyText extends PLObject {

        //  private static final WorkoutLayoutTypes type = WorkoutLayoutTypes.BodyView;
        private Context context;
        private Muscle muscle;
        private String title;

        public BodyText(int workoutId, int bodyId, String title) {
            this.workoutId = workoutId;
            this.bodyId = bodyId;
            this.context = context;
            type = WorkoutLayoutTypes.BodyView;
            this.muscle = muscle;
            this.title = title;

        }


        public BodyText(){
            type = WorkoutLayoutTypes.BodyView;
        }

        public BodyText(String title){
            this.title = title;
            type = WorkoutLayoutTypes.BodyView;
        }


        public Muscle getMuscle() {
            return muscle;
        }

        public void setBodyPart(Muscle muscle) {
            this.muscle = muscle;
        }


        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }

    public static class ExerciseProfile extends PLObject {

        public String comment = "";
        private ArrayList<SetsPLObject> sets = new ArrayList<>();
        public ArrayList<SetsPLObject> intraSets = new ArrayList<>();
        public ArrayList<ExerciseProfile> exerciseProfiles = new ArrayList<>();
        private int exerciseProfileId;
        private Muscle muscle;
        public Beans exercise;
        private String tag;
        //private ExerciseProfile parent;
        private boolean shadowExpand;
        public int rawPosition;
        public boolean showComment;
        private int defaultInt;
        public ExerciseProfile(Muscle muscle, int workoutId, int bodyId, int exerciseProfileId) {
            //    this.mSets = mSets;
            this.sets = sets;
            this.exerciseProfileId = exerciseProfileId;
            this.workoutId = workoutId;
            this.bodyId = bodyId;
            this.muscle = muscle;
            type = WorkoutLayoutTypes.ExerciseProfile;

        }

        public ExerciseProfile(ExerciseProfile ep){
            this.type = ep.type;
            this.exercise = ep.exercise;
            this.muscle = ep.muscle;
            //this.parent = ep.parent;
            this.tag = ep.tag;
            this.comment = ep.comment;
            this.defaultInt = ep.getDefaultInt();
            this.sets = ep.sets;
            this.isParent = ep.isParent;
            this.exerciseProfiles = ep.exerciseProfiles;
        }
        public ExerciseProfile(){
            type = WorkoutLayoutTypes.ExerciseProfile;

        }

        public WorkoutLayoutTypes getInnerType() {
            return innerType;
        }

        public void setInnerType(WorkoutLayoutTypes innerType) {
            this.innerType = innerType;
        }

        public ArrayList<SetsPLObject> getSets() {
            return sets;
        }

        public void setSets(ArrayList<SetsPLObject> sets) {
            this.sets = sets;
        }

        public Muscle getMuscle() {
            return muscle;
        }

        public void setMuscle(Muscle muscle) {
            this.muscle = muscle;
        }


        public String getTag() {
            return tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }

        public Beans getExercise() {
            return exercise;
        }

        public void setExercise(Beans exercise) {
            this.exercise = exercise;
            if(exercise != null){
                defaultInt = exercise.getDefault_int();
            }
        }

      /*  public ExerciseProfile getParent() {
            return parent;
        }*/

       /* public void setParent(ExerciseProfile parent) {
            this.parent = parent;
        }*/

        public boolean isShadowExpand() {
            return shadowExpand;
        }

        public void setShadowExpand(boolean shadowExpand) {
            this.shadowExpand = shadowExpand;
        }

        public int getDefaultInt() {
            return defaultInt;
        }
    }

    public static class SetsPLObject extends PLObject {

        private ExerciseSet ExerciseSet = new ExerciseSet();
        public ArrayList<SetsPLObject> intraSets = new ArrayList<>();
        public ArrayList<SetsPLObject> superSets = new ArrayList<>();
       // public ExerciseProfile parent;
        public SetsPLObject setParent;
        public String tag;
        public String title;
        public WorkoutLayoutTypes innerType;
        private String ExerciseName;


        public SetsPLObject( ExerciseSet ExerciseSet) {
            this.ExerciseSet = ExerciseSet;
            type = WorkoutLayoutTypes.SetsPLObject;
            //parent = father;
        }

        //this is necessary for duplication
        public SetsPLObject(SetsPLObject setsPLObject){

            this.ExerciseSet = setsPLObject.ExerciseSet;
            this.type = setsPLObject.type;
            this.intraSets = setsPLObject.intraSets;
            this.isParent = setsPLObject.isParent;
            this.title = setsPLObject.title;
            this.innerType = setsPLObject.innerType;
        }

        public ArrayList<SetsPLObject> getIntraSets() {
            return intraSets;
        }

        public ArrayList<SetsPLObject> getSuperSets() {
            return superSets;
        }

        public SetsPLObject(){
            type = WorkoutLayoutTypes.SetsPLObject;
        }

        public WorkoutLayoutTypes getInnerType() {
            return this.innerType;
        }

        public void setInnerType(WorkoutLayoutTypes innerType) {
            this.innerType = innerType;
        }

        public ExerciseSet getExerciseSet() {
            return ExerciseSet;
        }

        public void setExerciseSet(ExerciseSet exerciseSet) {
            this.ExerciseSet = exerciseSet;
        }

        public String getExerciseName() {
            return ExerciseName;
        }

        public void setExerciseName(String exerciseName) {
            ExerciseName = exerciseName;
        }
    }

    public static class ExerciseStats extends PLObject{

        public ExerciseProfile exerciseProfile;

        public ExerciseStats(ExerciseProfile exerciseProfile){
            this.exerciseProfile = exerciseProfile;
            this.type = WorkoutLayoutTypes.ExerciseStats;
        }
    }


    public static class IntraSetPLObject extends PLObject {

        private ExerciseProfile parent;
        private ExerciseSet exerciseSet;
        private SetsPLObject parentSet;
        public IntraSetPLObject(ExerciseProfile parent, ExerciseSet exerciseSet, WorkoutLayoutTypes innerType, SetsPLObject parentSet){
            this.innerType = innerType;
            this.parent = parent;
            this.exerciseSet = exerciseSet;
            this.parentSet = parentSet;
            type = WorkoutLayoutTypes.IntraSet;
        }

        public IntraSetPLObject(){

        }

        public ExerciseProfile getParent() {
            return parent;
        }

        public void setParent(ExerciseProfile parent) {
            this.parent = parent;
        }

        public ExerciseSet getExerciseSet() {
            return exerciseSet;
        }

        public void setExerciseSet(ExerciseSet exerciseSet) {
            this.exerciseSet = exerciseSet;
        }

        public WorkoutLayoutTypes getInnerType() {
            return innerType;
        }

        public void setInnerType(WorkoutLayoutTypes innerType) {
            this.innerType = innerType;
        }

        public SetsPLObject getParentSet() {
            return parentSet;
        }

        public void setParentSet(SetsPLObject parentSet) {
            this.parentSet = parentSet;
        }
    }


    public static class ProgressPLObject extends PLObject {


        public ProgressPLObject(ExerciseSet ExerciseSet) {

            type = WorkoutLayoutTypes.SetsPLObject;
        }

    }

    public static class AddExercise extends PLObject {

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

    public static class Superset extends PLObject {

        private Muscle m;

        public Superset(Muscle m) {
            this.m = m;
            type = WorkoutLayoutTypes.AddExercise;
        }

        public Muscle getM() {
            return m;
        }
    }

    public static class MoreMenu extends PLObject {

        public MoreMenu(){
            type = WorkoutLayoutTypes.More;
        }
    }




}
