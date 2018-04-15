package com.strongest.savingdata.Adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.strongest.savingdata.BaseWorkout.Muscle;
import com.strongest.savingdata.Database.Exercise.Beans;
import com.strongest.savingdata.MyViews.WorkoutView.Choose.OnExerciseSetChange;
import com.strongest.savingdata.R;

import java.util.ArrayList;

public class ExerciseListAdapter extends RecyclerView.Adapter<ExerciseListAdapter.ViewHolder> {

    private Context context;
    private OnExerciseListAdapterClickListener onExerciseListAdapterClickListener;
    private OnExerciseSetChange onExerciseSetChange;
    private ArrayList<Beans> exerciseBeans;


    public ExerciseListAdapter(Context context, OnExerciseListAdapterClickListener onExerciseListAdapterClickListener,
                               OnExerciseSetChange onExerciseSetChange, ArrayList<Beans> exerciseBeans) {

        this.context = context;
        this.onExerciseListAdapterClickListener = onExerciseListAdapterClickListener;
        this.onExerciseSetChange = onExerciseSetChange;
        this.exerciseBeans = exerciseBeans;
    }

    @Override
    public ExerciseListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.recycler_view_exercise_choose, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ExerciseListAdapter.ViewHolder holder, final int position) {

        holder.exercise.setText(exerciseBeans.get(position).getName());
        if (exerciseBeans.get(position).getMuscles() != null) {
            String muscle = "";
            String[] muscles = Muscle.parseMuscles(exerciseBeans.get(position).getMuscles());
            int index = 0;
            for (String m : muscles) {
                muscle += m;
                if (index == 0 || index != muscle.length() && index != 0) {
                    muscle += ", ";
                }
                index++;
            }
            muscle = muscle.substring(0, muscle.length() - 2);
            holder.muscles.setText(muscle);
            holder.type.setText(exerciseBeans.get(position).getType());
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onExerciseListAdapterClickListener.setExercise(exerciseBeans.get(position));
                holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorAccent));
                onExerciseSetChange.notifyExerciseSetChange();
            }
        });

    }

    @Override
    public int getItemCount() {
        return exerciseBeans.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView exercise, muscles, type;

        public ViewHolder(View itemView) {
            super(itemView);
            exercise = (TextView) itemView.findViewById(R.id.exercise_tv);
            muscles = (TextView) itemView.findViewById(R.id.exercise_muscles_tv);
            type = (TextView) itemView.findViewById(R.id.choose_type);
        }
    }


    public void setExerciseBeans(ArrayList<Beans> exerciseBeans) {
        this.exerciseBeans = exerciseBeans;
    }
}
