package com.strongest.savingdata.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.strongest.savingdata.Adapters.WorkoutAdapter.ItemTouchHelperAdapter;
import com.strongest.savingdata.Adapters.WorkoutAdapter.OnDragListener;
import com.strongest.savingdata.Adapters.WorkoutAdapter.ScrollToPositionListener;
import com.strongest.savingdata.AlgorithmLayout.LayoutManagerAlertdialog;
import com.strongest.savingdata.AlgorithmLayout.OnLayoutManagerDialogPress;
import com.strongest.savingdata.AlgorithmLayout.PLObject;
import com.strongest.savingdata.Fragments.OnProgramSettingChange;
import com.strongest.savingdata.Fragments.ProgramSettingsFragment;
import com.strongest.savingdata.MyViews.WorkoutTextView;
import com.strongest.savingdata.R;
import com.wajahatkarim3.easyflipview.EasyFlipView;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Cohen on 10/27/2017.
 */

public class MainAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements ItemTouchHelperAdapter {


    private Context context;
    private ArrayList<PLObject> list;
    private OnDragListener onDragListener;
    private ScrollToPositionListener scrollListener;
    private OnProgramSettingChange onProgramChangeListener;

    public MainAdapter(Context context, ArrayList<PLObject> list, OnDragListener onDragListener,
                       ScrollToPositionListener scrollListener) {
        this.context = context;
        this.list = list;
        this.onDragListener = onDragListener;
        this.scrollListener = scrollListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.recyclerview_settings_workout, parent, false);

        return new WorkoutViewHolder(v);
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        configWorkout((WorkoutViewHolder) holder, position);
    }

    private void configWorkout(WorkoutViewHolder holder, int position) {
        holder.editText.setText(((PLObject.WorkoutPLObject) list.get(position)).getWorkoutName());
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setOnProgramChangeListener(OnProgramSettingChange onProgramChangeListener) {
        this.onProgramChangeListener = onProgramChangeListener;
    }


    public class WorkoutViewHolder extends RecyclerView.ViewHolder implements OnLayoutManagerDialogPress {
        private EditText editText;
        private ImageView delete, drag, edit;
        private WorkoutViewHolder _this = this;

        public WorkoutViewHolder(View itemView) {
            super(itemView);
            editText = (EditText) itemView.findViewById(R.id.recyclerview_workout_tv);
            drag = (ImageView) itemView.findViewById(R.id.recyclerview_workout_drag_iv);
            delete = (ImageView) itemView.findViewById(R.id.recyclerview_workout_delete_iv);
            edit = (ImageView) itemView.findViewById(R.id.recycler_view_workouts_edit_iv);


            drag.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    onDragListener.startDrag(_this);
                    return true;
                }
            });

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LayoutManagerAlertdialog.getAlertDialog(context, WorkoutViewHolder.this, getAdapterPosition());

                }
            });

            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LayoutManagerAlertdialog.getInputAlertDialog(context, WorkoutViewHolder.this, editText.getText().toString(), getAdapterPosition());
                }
            });
        }


        @Override
        public void onLMDialogOkPressed(int viewHolderPosition) {
            onProgramChangeListener.onDelete(getAdapterPosition());
            list.remove(viewHolderPosition);
            notifyItemRemoved(getAdapterPosition());
        }

        @Override
        public void onLMDialogOkPressed(String input, int position) {
            editText.setText(input);
            ((PLObject.WorkoutPLObject) list.get(getAdapterPosition())).setWorkoutName(input);
            onProgramChangeListener.onEdit(getAdapterPosition());
        }


    }

    public boolean onItemMove(int fromPosition, int toPosition) {

        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(list, i, i + 1);
                onProgramChangeListener.onSwap(i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(list, i, i - 1);
                onProgramChangeListener.onSwap(i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    @Override
    public boolean onItemMove(RecyclerView.ViewHolder fromVh, RecyclerView.ViewHolder toVh) {
        return false;
    }

    @Override
    public void onItemDismiss(int position) {

    }

}
