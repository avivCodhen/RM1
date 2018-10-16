package com.strongest.savingdata.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.strongest.savingdata.AModels.workoutModel.PLObject;
import com.strongest.savingdata.AModels.workoutModel.WorkoutLayoutTypes;
import com.strongest.savingdata.BaseWorkout.Muscle;
import com.strongest.savingdata.BaseWorkout.Muscle.MuscleUI;
import com.strongest.savingdata.Controllers.OnExerciseInfo;
import com.strongest.savingdata.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class ExerciseRecyclerAdapter extends RecyclerView.Adapter<ExerciseRecyclerAdapter.ViewHolder> {

    private ArrayList<PLObject.ExerciseProfile> list;
    private Context context;
    private OnExerciseInfo onExerciseInfo;

    public ExerciseRecyclerAdapter(Context context, ArrayList<PLObject.ExerciseProfile> list, OnExerciseInfo onExerciseInfo) {
        this.context = context;

        this.list = list;
        this.onExerciseInfo = onExerciseInfo;
    }

    public void setList(ArrayList<PLObject.ExerciseProfile> list) {
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_lean_exercise, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        PLObject.ExerciseProfile exerciseProfile = list.get(position);

        MuscleUI mui;
        if (exerciseProfile.getMuscle() != null) {
            mui = Muscle.provideMuscleUI(exerciseProfile.getMuscle());
            holder.icon.setImageResource(mui.getImage());
        }
        if (exerciseProfile.getExercise() != null) {

            holder.name.setText(exerciseProfile.getExercise().getName());
        }

        holder.itemView.setOnClickListener(v -> {

                onExerciseInfo.transitionToExerciseInfo(exerciseProfile);
        });


        if (exerciseProfile.type == WorkoutLayoutTypes.IntraExerciseProfile) {
            holder.tag.setText("Superset");
            holder.childTag.setVisibility(View.VISIBLE);
        } else {
            holder.tag.setText("Exercise");
            holder.childTag.setVisibility(View.GONE);
        }


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        @BindView(R.id.ex1)
        public CircleImageView icon;

        @BindView(R.id.exerciseView_nameTV)
        public TextView name;

        /*@BindView(R.id.exercise_info)
        public View exerciseInfo;
*/
        @BindView(R.id.child_arrow_iv)
        ImageView childTag;

        @BindView(R.id.exercise_lean_tag_tv)
        TextView tag;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
