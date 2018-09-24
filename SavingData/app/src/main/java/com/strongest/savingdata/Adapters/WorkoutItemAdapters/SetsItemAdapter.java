package com.strongest.savingdata.Adapters.WorkoutItemAdapters;

import com.strongest.savingdata.AModels.workoutModel.PLObject;
import com.strongest.savingdata.AModels.workoutModel.PLObject.SetsPLObject;
import com.strongest.savingdata.AModels.workoutModel.WorkoutLayoutTypes;
import com.strongest.savingdata.AModels.ExerciseModel;
import com.strongest.savingdata.Utils.MyUtils;

import java.io.IOException;
import java.util.ArrayList;

public class SetsItemAdapter implements WorkoutItemAdapter<SetsPLObject> {

    private PLObject.ExerciseProfile exerciseProfile;

    public SetsItemAdapter() {

    }

    public SetsItemAdapter(PLObject.ExerciseProfile exerciseProfile) {

        this.exerciseProfile = exerciseProfile;

    }

    @Override
    public SetsPLObject insert() {
        SetsPLObject set = new SetsPLObject();
        if (exerciseProfile != null) {
            //set.parent = exerciseProfile;
            exerciseProfile.getSets().add(set);
            ExerciseModel.injectSupersetExercise(exerciseProfile, set);
        }


        set.setInnerType(WorkoutLayoutTypes.SetsPLObject);

        set.isParent = true;
        return set;
    }

    @Override
    public ArrayList<SetsPLObject> onInsert(int pos, ArrayList<SetsPLObject> itemsHolder) {
        if (exerciseProfile != null) {
            SetsPLObject setsPLObject = itemsHolder.get(0);
           /* int i = 0;
            for (PLObject.ExerciseProfile s : exerciseProfile.getExerciseProfiles()){
                SetsPLObject child = s.intraSets.get(i++);
                setsPLObject.intraSets.add(child);
                itemsHolder.add(child);
            }*/
        }
        return itemsHolder;
    }

    @Override
    public boolean notifyInserted(int start, int count, ItemAdapter adapter) {
        adapter.adapterNotifyItemInserted(start);
        return true;
    }

    @Override
    public void remove(int position, SetsPLObject removedItem) {
        if (removedItem.type == WorkoutLayoutTypes.IntraSet) {
            removedItem.setParent.intraSets.remove(removedItem);
        }
        if (exerciseProfile != null) {
            exerciseProfile.getSets().remove(removedItem);
        }
    }

    @Override
    public boolean notifyRemoved(int from, int count, ItemAdapter adapter) {
        adapter.adapterNotifyItemRemoved(from);
        return true;
    }

    @Override
    public boolean onDestroy() {
        return false;
    }

    @Override
    public SetsPLObject onDuplicate(int position, SetsPLObject clone) {
        SetsPLObject setsPLObject;
        try {
            setsPLObject = MyUtils.clone(clone);
            exerciseProfile.getSets().add(setsPLObject);
            return setsPLObject;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        throw new IllegalArgumentException("object cannot be cloned");
    }

    @Override
    public int addingDuplicateTo(SetsPLObject parent) {
        return 1;
    }

    @Override
    public boolean notifyDuplicate(int position, ItemAdapter adapter) {
        adapter.adapterNotifyItemInserted(position);
        return true;
    }

    @Override
    public SetsPLObject onChild(SetsPLObject parent) {

        SetsPLObject child = new SetsPLObject();
        child.type = WorkoutLayoutTypes.IntraSet;
        child.setParent = parent;
        child.setInnerType(WorkoutLayoutTypes.IntraSetNormal);
        parent.intraSets.add(child);
        return child;
    }

    @Override
    public int onAddingChildToGroup(SetsPLObject parent, SetsPLObject child) {
        return parent.intraSets.size();
    }

    @Override
    public boolean notifyChild(int position, ItemAdapter adapter) {
        adapter.adapterNotifyItemInserted(position);
        return true;
    }

    @Override
    public SetsPLObject replace(SetsPLObject toReplace) {
        return null;
    }

    @Override
    public boolean notifyReplaced(int positionReplaced, ItemAdapter adapter) {
        return false;
    }
}
