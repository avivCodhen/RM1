package com.strongest.savingdata.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.strongest.savingdata.AlgorithmLayout.ExerciseProfileView;
import com.strongest.savingdata.R;

import java.util.ArrayList;

/**
 * Created by Cohen on 10/4/2017.
 */

public class ProgressAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<ExerciseProfileView> layout;

    public ProgressAdapter(ArrayList<ExerciseProfileView> layout) {
        this.layout = layout;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.recycler_view_progress_fragment, parent, false);
        holder = new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder v = (ViewHolder) holder;
        configurateViewHolder(v, position);
    }

    private void configurateViewHolder(ViewHolder holder, int position) {

        holder.exercise.setText(layout.get(position).getExercise().getName());
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        //private ImageView exercise;
        private TextView exercise, cMax, cFReps, aMax, AFReps;

        public ViewHolder(View v) {
            super(v);
            exercise = (TextView) v.findViewById(R.id.progress_recycler_view_exercise_image);
            cMax = (TextView) v.findViewById(R.id.progress_fragment_currentMax);
            cFReps = (TextView) v.findViewById(R.id.progress_fragment_current_reps);
            aMax = (TextView) v.findViewById(R.id.progress_fragment_all_time_Max);
            AFReps = (TextView) v.findViewById(R.id.progress_fragment_all_time_reps);
        }
    }

    @Override
    public int getItemCount() {
        return layout.size();
    }
}
