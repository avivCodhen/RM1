package com.strongest.savingdata.MyViews.WorkoutView;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.strongest.savingdata.Animations.MyJavaAnimator;
import com.strongest.savingdata.R;

import net.cachapa.expandablelayout.ExpandableLayout;

import static com.strongest.savingdata.AlgorithmLayout.LayoutManager.*;
import static com.strongest.savingdata.AlgorithmLayout.LayoutManager.ATTACH_DROPSET;
import static com.strongest.savingdata.AlgorithmLayout.LayoutManager.ATTACH_SUPERSET;

/**
 * Created by Cohen on 2/17/2018.
 */

public class ProgramToolsView extends LinearLayout {

    private Context context;


    private View openProgramToolsIV;
    private ExpandableLayout openProgramToolsEL, showExpandedToolsButton;
    private View addExercise;
    private View addExerciseSplit;
    private View attachSuperset;
    private View attachDropset;
    private View addWorkout;
    private View deleteWorkout;

    private WorkoutViewModes mWorkoutViewModes;

    private OnProgramToolsActionListener onProgramToolsActionListener;
    private boolean expandedToolsButtonOn;
    private boolean programToolsOn;

    public ProgramToolsView(Context context) {
        super(context);
        this.context = context;
        initViews();
    }

    public ProgramToolsView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initViews();
    }

    private void initViews() {
        inflate(context, R.layout.layout_program_tools_view, this);
        initProgramToolsViews();
        mWorkoutViewModes = new WorkoutViewModes();


    }

    private void initProgramToolsViews() {
        showExpandedToolsButton = (ExpandableLayout) findViewById(R.id.workout_view_show_program_tools_expand);
        addExercise = findViewById(R.id.workout_view_add_exercise);
        addExercise.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onProgramToolsActionListener != null) {
                    onProgramToolsActionListener.onProgramToolsAction(NEW_EXERCISE);
                }
            }
        });
        addExerciseSplit = findViewById(R.id.workout_view_add_exercise_split);
        addExerciseSplit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onProgramToolsActionListener != null) {
                    onProgramToolsActionListener.onProgramToolsAction(NEW_EXERCISE_FLIPPED);
                }
            }
        });
        attachSuperset = findViewById(R.id.workout_view_attach_superset);
        attachSuperset.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onProgramToolsActionListener != null) {
                    onProgramToolsActionListener.onProgramToolsAction(ATTACH_SUPERSET);
                }
            }
        });
        attachDropset = findViewById(R.id.workout_view_attach_dropset);
        attachDropset.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onProgramToolsActionListener != null) {
                    onProgramToolsActionListener.onProgramToolsAction(ATTACH_DROPSET);
                }
            }
        });
        addWorkout = findViewById(R.id.workout_view_add_workout);
        addWorkout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onProgramToolsActionListener != null) {
                    onProgramToolsActionListener.onProgramToolsAction(NEW_WORKOUT);
                }
            }
        });
        deleteWorkout = findViewById(R.id.workout_view_delete_workout);
        deleteWorkout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onProgramToolsActionListener != null) {
                    onProgramToolsActionListener.onProgramToolsAction(DELETE_WORKOUT);
                }
            }
        });
        openProgramToolsEL = (ExpandableLayout) findViewById(R.id.workout_view_program_tools_expandable);
        openProgramToolsIV = findViewById(R.id.workout_view_open_program_tools);
        openProgramToolsIV.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                openProgramToolsEL.toggle();
                if (openProgramToolsEL.isExpanded()) {
                    MyJavaAnimator.rotateView(openProgramToolsIV, 180, 225);
                    MyJavaAnimator.fadeIn(addExercise,
                            addExerciseSplit,
                            attachSuperset,
                            attachDropset,
                            addWorkout,
                            deleteWorkout);

                } else {
                    MyJavaAnimator.rotateView(openProgramToolsIV, 225, 180);

                    MyJavaAnimator.fadeOut(addExercise,
                            addExerciseSplit,
                            attachSuperset,
                            attachDropset,
                            addWorkout,
                            deleteWorkout);
                }

            }
        });

    }

    public void toggleMode(boolean changeMode) {
        if(changeMode){
            if (mWorkoutViewModes.isEdit()) {
                mWorkoutViewModes.setEdit(false);
            } else {
                mWorkoutViewModes.setEdit(true);

            }
        }

        toggleShowToolsButton();

    }

    public void showAll() {
        showExpandedToolsButton.expand();
        openProgramToolsEL.expand();
    }

    private void toggleShowToolsButton() {
        if (mWorkoutViewModes.isEdit()) {
          //expand(showExpandedToolsButton);
            showExpandedToolsButton.expand();
        } else {
            //collapse(openProgramToolsEL);
            //collapse(showExpandedToolsButton);

            openProgramToolsEL.collapse();
            showExpandedToolsButton.collapse();
            MyJavaAnimator.rotateView(openProgramToolsIV, 225, 180);
        }
    }

    public void setOnProgramToolsActionListener(OnProgramToolsActionListener onProgramToolsActionListener) {
        this.onProgramToolsActionListener = onProgramToolsActionListener;
    }

   /* private void expand(ExpandableLayout el){
        if(el == showExpandedToolsButton){
         expandedToolsButtonOn = true;
        }
        if(el == openProgramToolsEL){
            programToolsOn = true;
        }
        el.expand();
    }

    private void collapse(ExpandableLayout el){
        if(el == showExpandedToolsButton){
            expandedToolsButtonOn = false;
        }
        if(el == openProgramToolsEL){
            programToolsOn = false;
        }
        el.collapse();
    }*/

    public void getState(){
       // expandedToolsButtonOn == true ? expand(showExpandedToolsButton) : coll;
    }

    public void setmWorkoutViewModes(WorkoutViewModes mWorkoutViewModes) {
        this.mWorkoutViewModes = mWorkoutViewModes;
    }

    public WorkoutViewModes getmWorkoutViewModes() {
        return mWorkoutViewModes;
    }

    public class WorkoutViewModes{

        private boolean edit;
        private boolean progress;
        private boolean none;
        private boolean showAnimation;
        public WorkoutViewModes(){

        }

        public boolean isShowAnimation() {
            return showAnimation;
        }

        public void setShowAnimation(boolean showAnimation) {
            this.showAnimation = showAnimation;
        }

        public boolean isEdit() {
            return edit;
        }

        public void setEdit(boolean edit) {
            this.edit = edit;
        }

        public boolean isProgress() {
            return progress;
        }

        public void setProgress(boolean progress) {
            this.progress = progress;
        }

        public boolean isNone() {
            return none;
        }

        public void setNone(boolean none) {
            this.none = none;
        }
    }

}
