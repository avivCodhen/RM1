package com.strongest.savingdata.Adapters.WorkoutAdapter;

import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.strongest.savingdata.Adapters.MainAdapter;
import com.strongest.savingdata.Adapters.MyExpandableAdapter;
import com.strongest.savingdata.ViewHolders.ExerciseViewHolder;

/**
 * Created by Cohen on 10/27/2017.
 */

public class DragAndSwipeCallback extends ItemTouchHelper.Callback

{

    private MainAdapter mainAdapter;
    private MyExpandableAdapter workoutAdapter;
    private boolean onTheMove;

    public DragAndSwipeCallback(MainAdapter adapter) {
        mainAdapter = adapter;
    }

    public DragAndSwipeCallback(MyExpandableAdapter adapter) {
        workoutAdapter = adapter;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return false;
    }


    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        int swipeFlags;
        if (viewHolder.getLayoutPosition() != 0 && viewHolder instanceof MainAdapter.WorkoutViewHolder) {
            swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
        } else {
            swipeFlags = 0;
        }


        return makeMovementFlags(dragFlags, swipeFlags);
    }


    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                          RecyclerView.ViewHolder target) {
        if (mainAdapter != null) {
            mainAdapter.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        } else {
            workoutAdapter.onItemMove(viewHolder, target);
        }
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        if(mainAdapter != null){
            mainAdapter.onItemDismiss(viewHolder.getAdapterPosition());
        }else{
        }
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder,
                                  int actionState) {
        // We only want the active item
        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE ) {
            if (viewHolder instanceof MainAdapter.WorkoutViewHolder) {
                    MainAdapter.WorkoutViewHolder itemViewHolder = (MainAdapter.WorkoutViewHolder) viewHolder;
                 //   itemViewHolder.onItemSelected();
            }else if(viewHolder instanceof ExerciseViewHolder){
                ExerciseViewHolder itemViewHolder = (ExerciseViewHolder) viewHolder;
               // itemViewHolder.onItemSelected();
            }
        }
        super.onSelectedChanged(viewHolder, actionState);
    }

    @Override
    public void clearView(RecyclerView recyclerView,
                          RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);

        if (viewHolder instanceof MainAdapter.WorkoutViewHolder) {
            MainAdapter.WorkoutViewHolder itemViewHolder = (MainAdapter.WorkoutViewHolder) viewHolder;
         //   itemViewHolder.onItemClear();
        }else if(viewHolder instanceof ExerciseViewHolder){
            ExerciseViewHolder itemViewHolder = (ExerciseViewHolder) viewHolder;
           // itemViewHolder.onItemClear();
        }
    }

}
