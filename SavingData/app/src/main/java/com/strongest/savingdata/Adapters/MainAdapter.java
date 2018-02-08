package com.strongest.savingdata.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.strongest.savingdata.Adapters.WorkoutAdapter.ItemTouchHelperAdapter;
import com.strongest.savingdata.Adapters.WorkoutAdapter.ItemTouchHelperListener;
import com.strongest.savingdata.Adapters.WorkoutAdapter.OnDragListener;
import com.strongest.savingdata.Adapters.WorkoutAdapter.ScrollToPositionListener;
import com.strongest.savingdata.AlgorithmLayout.PLObjects;
import com.strongest.savingdata.AlgorithmLayout.PLObjects.BodyText;
import com.strongest.savingdata.AlgorithmLayout.PLObjects.WorkoutText;
import com.strongest.savingdata.AlgorithmLayout.WorkoutLayoutTypes;
import com.strongest.savingdata.R;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Cohen on 10/27/2017.
 */

public class MainAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements ItemTouchHelperAdapter {


    private final int WORKOUT = 0;
    private final int MUSCLE = 1;
    private Context context;
    private ArrayList<PLObjects> list;
    private OnDragListener onDragListener;
    private ScrollToPositionListener scrollListener;

    public MainAdapter(Context context, ArrayList<PLObjects> list, OnDragListener onDragListener,
                       ScrollToPositionListener scrollListener) {
        this.context = context;
        this.list = list;
        this.onDragListener = onDragListener;
        this.scrollListener = scrollListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        RecyclerView.ViewHolder viewHolder;
        View v;
        switch (viewType) {
            case WORKOUT:
                v = inflater.inflate(R.layout.template_workout, parent, false);
                viewHolder = new WorkoutViewHolder(v);
                return viewHolder;
            case MUSCLE:
                v = inflater.inflate(R.layout.template_muscle, parent, false);
                viewHolder = new MuscleViewHolder(v);
                return viewHolder;
            default:
                return null;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (list.get(position).getType() == WorkoutLayoutTypes.WorkoutView) {
            return WorkoutLayoutTypes.WorkoutView.ordinal();
        } else if (list.get(position).getType() == WorkoutLayoutTypes.BodyView) {
            return WorkoutLayoutTypes.BodyView.ordinal();
        }
        return -1;
    }

    private class WorkoutViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;
        public WorkoutViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.template_workout_text_view);
        }
    }

    public static class MuscleViewHolder extends RecyclerView.ViewHolder implements ItemTouchHelperListener {
        private TextView textView;
        private ImageView navigator;

        public MuscleViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.template_muscle_text_view);
            navigator = (ImageView) itemView.findViewById(R.id.template_muscle_nav_image_view);
        }

        @Override
        public void onItemSelected() {
            navigator.setImageResource(R.drawable.ball_green_24px);
        }

        @Override
        public void onItemClear() {
            navigator.setImageResource(R.drawable.ball_red_24px);

        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder.getItemViewType() == WORKOUT){
            WorkoutViewHolder vh = (WorkoutViewHolder) holder;
            configWorkout(vh, position);
        }
        if(holder.getItemViewType() == MUSCLE){
            MuscleViewHolder vh = (MuscleViewHolder) holder;
            configBody(vh, position);
        }
    }

    private void configWorkout(WorkoutViewHolder holder, int position) {
       WorkoutText workText = (WorkoutText) list.get(position);
        workText.setWorkoutName(workoutName(position));
      holder.textView.setText(workoutName(position));
    }

    private void configBody(final MuscleViewHolder holder, int position) {
        BodyText bodyText = (BodyText) list.get(position);
        holder.textView.setText(bodyText.getMuscle().getMuscle_display());

        holder.navigator.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                onDragListener.startDrag(holder);
                return true;
            }
        });
    }

    public String workoutName(int position){
        int counter = 0;
        for (int i = position; i > 0 ; i--) {
            if(list.get(i).getType() == WorkoutLayoutTypes.WorkoutView){
                counter++;
            }
        }
        String workoutName = "Workout "+ counter;
        return workoutName;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        if(toPosition == 0 || fromPosition == 0){
            return false;
        }
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(list, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(list, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    @Override
    public void onItemDismiss(int position) {
        if(list.get(position).getType() == WorkoutLayoutTypes.WorkoutView){
            WorkoutText workText =(WorkoutText) list.get(position);
            workText.setWorkoutName(workoutName(position));
            notifyDataSetChanged();
        }
         list.remove(position);
        notifyItemRemoved(position);
    }

}
