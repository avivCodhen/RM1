package com.strongest.savingdata.createProgramFragments.Create;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.strongest.savingdata.AlgorithmStats.ProgressStats;
import com.strongest.savingdata.Database.Exercise.Beans;
import com.strongest.savingdata.AlgorithmLayout.PLObjects;
import com.strongest.savingdata.AlgorithmLayout.WorkoutLayoutTypes;
import com.strongest.savingdata.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Cohen on 6/24/2017.
 */

@Deprecated
public class CreateAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    // private final int TITLE = 0, BODY = 1, EXERCISE = 2;
    private Context context;
    private View.OnClickListener listener;
    private List<PLObjects> programLayout;
    private boolean showStats;
    private int[][] stats;
    private ProgressStats progressProgressStats;
    private OnPositionViewListener lonListener;
    //    private Handler handler = new Handler();
    private boolean loadStats = true;


    public CreateAdapter(ArrayList<PLObjects> programLayout, View.OnClickListener listener, Context context,
                         OnPositionViewListener lonListener) {
        this.context = context;
        this.lonListener = lonListener;
        this.listener = listener;
        this.programLayout = programLayout;
        this.showStats = showStats;
        this.stats = stats;
        this.progressProgressStats = progressProgressStats;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == WorkoutLayoutTypes.WorkoutView.ordinal()) {
            View v1 = inflater.inflate(R.layout.recycler_view_workouts, parent, false);
            holder = new WorkoutViewHolder(v1);
        } else if (viewType == WorkoutLayoutTypes.BodyView.ordinal()) {
            View v2 = inflater.inflate(R.layout.recyclerview_body_parts, parent, false);
            holder = new MuscleViewHolder(v2);
        } else {
            View v3 = inflater.inflate(R.layout.recycler_view_exercises, parent, false);
            holder = new ExerciseProfileViewHolder(v3);
        }

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == WorkoutLayoutTypes.WorkoutView.ordinal()) {
            WorkoutViewHolder vh1 = (WorkoutViewHolder) holder;
            configureViewHolder1(vh1, position);
        } else if (holder.getItemViewType() == WorkoutLayoutTypes.BodyView.ordinal()) {
            MuscleViewHolder vh2 = (MuscleViewHolder) holder;
            configureViewHolder2(vh2, position);
        } else {
            ExerciseProfileViewHolder vh3 = (ExerciseProfileViewHolder) holder;
            configureViewHolder3(vh3, position);
        }
    }

    private void configureViewHolder3(ExerciseProfileViewHolder vh3, int position) {

     /*   vh3.layout.setOnClickListener(listener);
        PLObjects.ExerciseProfile exerciseProfile = (PLObjects.ExerciseProfile) programLayout.get(position);
        // ExerciseProfileView epv = exerciseProfile.getExerciseProfileView();
        if (exerciseProfile.getBeansHolder() != null && exerciseProfile.getBeansHolder().isLoaded()) {
            vh3.name.setText(exerciseProfile.getBeansHolder().getExercise().getName());
            vh3.reps.setText(exerciseProfile.getBeansHolder().getRep().getName());
            vh3.sets.setText(exerciseProfile.getBeansHolder().getSets()+"");
            vh3.rest.setText(exerciseProfile.getBeansHolder().getRest().getName());

            if (exerciseProfile.getBeansHolder().getMethod() != null) {
                vh3.method.setText(exerciseProfile.getBeansHolder().getMethod().getName());
            } else {
                vh3.method.setText("");
            }
        } else {
            vh3.name.setText("");
            vh3.reps.setText("");
            vh3.method.setText("");
            vh3.sets.setText("");
            vh3.rest.setText("");
        }
        vh3.layout.setTag(position);
*/
    }

    private boolean isBean(Beans b) {
        if (b == null) {
            return false;
        }
        return true;
    }

    private void configureViewHolder2(final MuscleViewHolder vh2, int position) {
        final PLObjects.BodyText bodyText = (PLObjects.BodyText) programLayout.get(position);
        vh2.bodyTv.setText(bodyText.getMuscle().getMuscle_display());


        vh2.damage.setMax(bodyText.getStatsHolder().getProgressStats().getMaxDamage());
        vh2.mechanical.setMax(bodyText.getStatsHolder().getProgressStats().getMaxMechanical());
        vh2.metabolic.setMax(bodyText.getStatsHolder().getProgressStats().getMaxMetabolic());

        vh2.damage.setProgress(bodyText.getStatsHolder().getDamage());
        vh2.mechanical.setProgress(bodyText.getStatsHolder().getMechanical());
        vh2.metabolic.setProgress(bodyText.getStatsHolder().getMetabolic());

        vh2.damage.setProgressDrawable(ContextCompat.getDrawable(context, determineColorForDamage(vh2.damage, bodyText)));
        vh2.mechanical.setProgressDrawable(ContextCompat.getDrawable(context, determineColorForMechanical(vh2.mechanical, bodyText)));
        vh2.metabolic.setProgressDrawable(ContextCompat.getDrawable(context, determineColorForMetabolic(vh2.metabolic, bodyText)));

       /* Log.d("aviv", "damage fragment_manager: "+ vh2.damage.getProgress());
        Log.d("aviv", "mechanical fragment_manager: "+ vh2.mechanical.getProgress());
        Log.d("aviv", "metabolic fragment_manager: "+ vh2.metabolic.getProgress());
        Log.d("aviv", "damage max: "+ vh2.damage.getMax());
        Log.d("aviv", "mechanical max fragment_manager: "+ vh2.mechanical.getMax());
        Log.d("aviv", "metabolic max fragment_manager: "+ vh2.metabolic.getMax());*/
    }

    private int determineColorForDamage(ProgressBar pb, PLObjects.BodyText b) {
        float p = pb.getProgress();


        if (p >= b.getStatsHolder().getProgressStats().getMaxDamage()) {
            return R.drawable.progress_bar_drawable_max;
        }
        if (p <= b.getStatsHolder().getProgressStats().getMinDamage()) {
            return R.drawable.progress_bar_drawable_min;
        } else {
            return R.drawable.progress_bar_drawable_correct;
        }
    }

    private int determineColorForMechanical(ProgressBar pb, PLObjects.BodyText b) {
        float p = pb.getProgress();

        if (p >= b.getStatsHolder().getProgressStats().getMaxMechanical()) {
            return R.drawable.progress_bar_drawable_max;
        }
        if (p <= b.getStatsHolder().getProgressStats().getMinMechanical()) {
            return R.drawable.progress_bar_drawable_min;
        } else {
            return R.drawable.progress_bar_drawable_correct;
        }
    }

    private int determineColorForMetabolic(ProgressBar pb, PLObjects.BodyText b) {
        float p = pb.getProgress();


        if (p >= b.getStatsHolder().getProgressStats().getMaxMetabolic()) {
            return R.drawable.progress_bar_drawable_max;
        }
        if (p <= b.getStatsHolder().getProgressStats().getMinMetabolic()) {
            return R.drawable.progress_bar_drawable_min;
        } else {
            return R.drawable.progress_bar_drawable_correct;
        }
    }


    private void configureViewHolder1(WorkoutViewHolder vh1, final int position) {


        PLObjects.WorkoutText workoutText = (PLObjects.WorkoutText) programLayout.get(position);
        vh1.workoutTv.setText(workoutText.getWorkoutName());


    }

    public void setLoadStats(boolean loadStats) {
        this.loadStats = loadStats;
    }


    private class ExerciseProfileViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {
        private ImageView delete;

        private TextView name;
        private TextView sets;
        private TextView rest;
        private TextView reps;
        private TextView method;
        private TextView ovr;
        private ImageView icon;

        private RelativeLayout layout;

        public ExerciseProfileViewHolder(View itemView) {
            super(itemView);
            layout = (RelativeLayout) itemView.findViewById(R.id.exerciseView_layout);
            layout.setOnLongClickListener(this);

            //delete = (ImageView) itemView.findViewById(R.id.recycler_view_exercise_delete);

            name = (TextView) itemView.findViewById(R.id.exerciseView_nameTV);
            sets = (TextView) itemView.findViewById(R.id.exerciseView_setsTV);
            reps = (TextView) itemView.findViewById(R.id.exerciseView_repsTV);
            method = (TextView) itemView.findViewById(R.id.exerciseView_methodTV);
            ovr = (TextView) itemView.findViewById(R.id.exerciseView_weightTV);
            rest = (TextView) itemView.findViewById(R.id.exerciseView_restTV);

        }

        @Override
        public boolean onLongClick(View v) {
            lonListener.longClick(v, this.getLayoutPosition());
            return true;
        }
    }


    private class WorkoutViewHolder extends RecyclerView.ViewHolder {
        private TextView workoutTv;

        public WorkoutViewHolder(View itemView) {
            super(itemView);
            workoutTv = (TextView) itemView.findViewById(R.id.recycler_view_workouts_tv);

        }
    }

    @Override
    public int getItemViewType(int position) {
        if (programLayout.get(position).getType() == WorkoutLayoutTypes.WorkoutView) {
            //   Log.d("aviv", "workoutView: "+ WorkoutLayoutTypes.WorkoutView.ordinal());
            return WorkoutLayoutTypes.WorkoutView.ordinal();
        } else if (programLayout.get(position).getType() == WorkoutLayoutTypes.BodyView) {
            //       Log.d("aviv", "bodyView: " +WorkoutLayoutTypes.BodyView.ordinal());

            return WorkoutLayoutTypes.BodyView.ordinal();
        }
        //      Log.d("aviv", "exerciseView: " +WorkoutLayoutTypes.ExerciseView.ordinal() );

        return WorkoutLayoutTypes.ExerciseView.ordinal();
    }

    @Override
    public int getItemCount() {
        return programLayout.size();
    }


    private class MuscleViewHolder extends RecyclerView.ViewHolder {
        private TextView bodyTv;
        private ProgressBar damage, mechanical;
        // private IconRoundCornerProgressBar metabolic;
        private ProgressBar metabolic;
        private MuscleViewHolder vh;

        public MuscleViewHolder(View itemView) {
            super(itemView);
            vh = this;
            damage = (ProgressBar) itemView.findViewById(R.id.stats_damage_progress);
            //metabolic = (IconRoundCornerProgressBar) itemView.findViewById(R.id.stats_metabolic_progress);
            metabolic = (ProgressBar) itemView.findViewById(R.id.stats_metabolic_progress);
            mechanical = (ProgressBar) itemView.findViewById(R.id.stats_mechanical_progress);
            bodyTv = (TextView) itemView.findViewById(R.id.recycler_view_body_parts_TV);


            if (showStats) {

            }
            // loadFromGenerator(damage, metabolic, mechanical);
        }

        private class DetailsViewHolder extends RecyclerView.ViewHolder {

            public DetailsViewHolder(View itemView) {
                super(itemView);
            }
        }


    }
}

