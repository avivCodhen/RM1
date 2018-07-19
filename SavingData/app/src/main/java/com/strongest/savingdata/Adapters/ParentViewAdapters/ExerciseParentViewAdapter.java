package com.strongest.savingdata.Adapters.ParentViewAdapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.strongest.savingdata.AModels.AlgorithmLayout.PLObject;
import com.strongest.savingdata.BaseWorkout.Muscle;
import com.strongest.savingdata.Controllers.UiExerciseClickHandler;
import com.strongest.savingdata.R;
import com.strongest.savingdata.ViewHolders.ExerciseViewHolder;
import com.strongest.savingdata.ViewHolders.SupersetViewHolder;

public class ExerciseParentViewAdapter extends ParentView.Adapter<SupersetViewHolder>{

    private PLObject.ExerciseProfile exerciseProfile;
    private UiExerciseClickHandler uiExerciseClickHandler;

    public ExerciseParentViewAdapter(PLObject.ExerciseProfile exerciseProfile, UiExerciseClickHandler uiExerciseClickHandler){

        this.exerciseProfile = exerciseProfile;
        this.uiExerciseClickHandler = uiExerciseClickHandler;
    }

    @Override
    public SupersetViewHolder onCreateChildViewHolder(ViewGroup container) {
        LayoutInflater inflater = LayoutInflater.from(container.getContext());
        View view = inflater.inflate(R.layout.parentview_child_exercise, container, false);
        return new SupersetViewHolder(view);
    }

    @Override
    public int getCount() {
        return exerciseProfile.getExerciseProfiles().size();
    }

    @Override
    public void onBindViewHolder(SupersetViewHolder exerciseViewHolder, int position) {
        PLObject.ExerciseProfile superset = exerciseProfile.getExerciseProfiles().get(position);
        if(superset.getMuscle() != null){
            Muscle.MuscleUI mui = Muscle.provideMuscleUI(superset.getMuscle());
            exerciseViewHolder.icon.setImageResource(mui.getImage());
        }

        if(superset.getExercise() != null)
            exerciseViewHolder.name.setText(superset.getExercise().getName());

        exerciseViewHolder.edit.setOnClickListener(v ->{
            uiExerciseClickHandler.onExerciseEdit(exerciseViewHolder, superset, position);
        });

        exerciseViewHolder.delete.setOnClickListener(v ->{
            exerciseProfile.getExerciseProfiles().remove(superset);
            uiExerciseClickHandler.onRemoveSuperset(exerciseProfile);
        });


    }
}