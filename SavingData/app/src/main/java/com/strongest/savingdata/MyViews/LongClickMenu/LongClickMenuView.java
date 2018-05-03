package com.strongest.savingdata.MyViews.LongClickMenu;

import android.animation.Animator;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.shehabic.droppy.DroppyClickCallbackInterface;
import com.shehabic.droppy.DroppyMenuItem;
import com.shehabic.droppy.DroppyMenuPopup;
import com.strongest.savingdata.Adapters.MyExpandableAdapter;
import com.strongest.savingdata.AlgorithmLayout.PLObject;
import com.strongest.savingdata.AlgorithmLayout.WorkoutLayoutTypes;
import com.strongest.savingdata.MyViews.WorkoutView.OnWorkoutViewInterfaceClicksListener;
import com.strongest.savingdata.R;

/**
 * Created by Cohen on 3/27/2018.
 */

public class LongClickMenuView extends LinearLayout implements View.OnClickListener {
    public static final String EXERCISE = "exercise", SET = "set",
            INTRA_SET = "intra_set", INTRA_SUPERSET = "intra_superset", INTRA_EXERCISE = "intra_exercise";

    public boolean isOn() {
        return isOn;
    }

    public enum Action {
        Delete, Back, Superset, Intraset, Set, Duplicate;
    }

    private final Context context;
    private View back;
    private View currentView;
    private MyExpandableAdapter.MyExpandableViewHolder currentVH;
    private WorkoutLayoutTypes currentType;
    private OnWorkoutViewInterfaceClicksListener onWorkoutViewInterfaceClicksListener;
    private Button supersetBtn, setBtn, intrasetBtn;
    private ImageView more;
    private int viewPosition;
    private OnLongClickMenuActionListener listener;
    private boolean deleteMode = false;
    private boolean isOn;

    public LongClickMenuView(Context context) {
        super(context);
        this.context = context;
    }

    public LongClickMenuView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public void instantiate(OnLongClickMenuActionListener callback) {
        this.listener = callback;
        inflate(context, R.layout.layout_longclick_menu, this);
        initViews();

    }

    private void initViews() {
        findViewById(R.id.longclick_menu_back).setOnClickListener(this);
        supersetBtn = (Button) findViewById(R.id.longclick_menu_superset);
        intrasetBtn = (Button) findViewById(R.id.longclick_menu_intraSet);
        more = (ImageView) findViewById(R.id.longclick_menu_more);
        //more.setOnClickListener(this);
        setBtn = (Button) findViewById(R.id.longclick_menu_set);
        setBtn.setOnClickListener(this);
        intrasetBtn.setOnClickListener(this);
        supersetBtn.setOnClickListener(this);
        findViewById(R.id.longclick_menu_delete).setOnClickListener(this);
        initDropMenu();
    }

    private void enableDisableBtns() {
        if (deleteMode) {
            disableBtns(supersetBtn, setBtn, intrasetBtn, more);
        }else{
            enableBtns(more);
        }
        if (currentType == WorkoutLayoutTypes.ExerciseProfile) {
            enableBtns(supersetBtn, setBtn);
        } else {
            disableBtns(supersetBtn, setBtn);
        }
        if (currentType == WorkoutLayoutTypes.SetsPLObject) {
            enableBtns(intrasetBtn);
        } else {
            disableBtns(intrasetBtn);
        }
    }

    public void disableBtns(View... v) {
        for (View vi : v) {

            vi.setAlpha(0.5f);
            vi.setEnabled(false);
        }
    }

    public void enableBtns(View... v) {
        for (View vi : v) {

            vi.setAlpha(1f);
            vi.setEnabled(true);
        }
    }


    public void onShowMenu(MyExpandableAdapter.MyExpandableViewHolder vh, WorkoutLayoutTypes type, boolean deleteMode) {
        if (currentView != null) {
            onHideDragLayout();
        }
        isOn = true;
        this.deleteMode = deleteMode;
        currentType = type;
        currentView = vh.dragLayout;
        currentVH = vh;
        viewPosition = vh.getAdapterPosition();
        setVisibility(VISIBLE);

        currentView.setVisibility(VISIBLE);

        currentView.animate().alpha(0.8f).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });


        animate().alpha(1f).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        enableDisableBtns();


    }

    public void onHideDragLayout() {
        if (currentView != null) {
            currentView.setVisibility(GONE);
            if(currentVH instanceof MyExpandableAdapter.ExerciseViewHolder)
            ((MyExpandableAdapter.ExerciseViewHolder) currentVH).flipView.flipTheView();
        }
        isOn = false;
    }

    public void onHideMenu() {
        animate().alpha(0).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                setVisibility(GONE);


            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        if (currentView != null) {
            currentView.animate().alpha(0f).setListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    currentView.setVisibility(GONE);
                    currentView = null;
                    currentType = null;
                    currentVH = null;
                    deleteMode = false;
                    enableDisableBtns();
                    isOn = false;
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
        }
        if (currentType == WorkoutLayoutTypes.ExerciseProfile || currentType == WorkoutLayoutTypes.IntraExerciseProfile) {
            ((MyExpandableAdapter.ExerciseViewHolder) currentVH).flipView.flipTheView();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.longclick_menu_back:
                listener.onAction(Action.Back, currentVH, currentType);
                break;
            case R.id.longclick_menu_superset:
                listener.onAction(Action.Superset, currentVH, currentType);

                break;
            case R.id.longclick_menu_delete:
                listener.onAction(Action.Delete, currentVH, currentType);
                break;
            case R.id.longclick_menu_intraSet:
                listener.onAction(Action.Intraset, currentVH, currentType);

                break;
            case R.id.longclick_menu_set:
                listener.onAction(Action.Set, currentVH, currentType);

                break;
        }

    }

    public void initDropMenu() {

        DroppyMenuPopup.Builder droppyBuilder = new DroppyMenuPopup.Builder(context, more);

// Add normal items (text only)
        droppyBuilder.addMenuItem(new DroppyMenuItem("Duplicate"))
                .addSeparator();


// Set Callback handler
        droppyBuilder.setOnClick(new DroppyClickCallbackInterface() {
            @Override
            public void call(View v, int id) {
                listener.onAction(Action.Duplicate, currentVH, currentType);
            }
        });

        DroppyMenuPopup droppyMenu = droppyBuilder.build();

    }

    public void setOnWorkoutViewInterfaceClicksListener(OnWorkoutViewInterfaceClicksListener onWorkoutViewInterfaceClicksListener) {
        this.onWorkoutViewInterfaceClicksListener = onWorkoutViewInterfaceClicksListener;
    }

    public int getViewPosition() {
        return viewPosition;
    }
}
