package com.strongest.savingdata.MyViews.LongClickMenu;

import android.animation.Animator;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

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

    private final Context context;
    private View back;
    private View currentView;
    private MyExpandableAdapter.MyExpandableViewHolder currentVH;
    private WorkoutLayoutTypes currentType;
    private OnWorkoutViewInterfaceClicksListener onWorkoutViewInterfaceClicksListener;
    private Button supersetBtn, setBtn, intrasetBtn;


    public LongClickMenuView(Context context) {
        super(context);
        this.context = context;
    }

    public LongClickMenuView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public void instantiate() {
        inflate(context, R.layout.layout_longclick_menu, this);
        initViews();

    }

    private void initViews() {
        findViewById(R.id.longclick_menu_back).setOnClickListener(this);
        supersetBtn = (Button) findViewById(R.id.longclick_menu_superset);
        intrasetBtn = (Button) findViewById(R.id.longclick_menu_intraSet);
        setBtn = (Button) findViewById(R.id.longclick_menu_set);
        setBtn.setOnClickListener(this);
        intrasetBtn.setOnClickListener(this);
        supersetBtn.setOnClickListener(this);
        findViewById(R.id.longclick_menu_delete).setOnClickListener(this);
    }

    private void enableDisableBtns() {
        if(currentType == WorkoutLayoutTypes.ExerciseProfile){
            enableBtns(supersetBtn, setBtn);
        }else{
            disableBtns(supersetBtn, setBtn);
        }
        if(currentType == WorkoutLayoutTypes.SetsPLObject){
           enableBtns(intrasetBtn);
        }else{
            disableBtns(intrasetBtn);
        }
    }

    public void disableBtns(View...v){
        for (View vi : v){

            vi.setAlpha(0.5f);
            vi.setEnabled(false);
        }
    }
    public void enableBtns(View...v){
        for (View vi : v){

            vi.setAlpha(1f);
            vi.setEnabled(true);
        }
    }


    public void onShowMenu(MyExpandableAdapter.MyExpandableViewHolder vh, WorkoutLayoutTypes type) {
            if (currentView != null) {
                onHideDragLayout();
            }
            currentType = type;
            currentView = vh.dragLayout;
            currentVH = vh;
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
        }
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
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.longclick_menu_back:
                onHideMenu();
                break;
            case R.id.longclick_menu_superset:
                onWorkoutViewInterfaceClicksListener.onLongClickMenuAddSuperset(currentVH);
                break;
            case R.id.longclick_menu_delete:
                onWorkoutViewInterfaceClicksListener.deleteItem(currentVH.getAdapterPosition(), false);
                break;
            case R.id.longclick_menu_intraSet:
            onWorkoutViewInterfaceClicksListener.onAddNormalIntraSet(currentVH);
        }

    }

    public void setOnWorkoutViewInterfaceClicksListener(OnWorkoutViewInterfaceClicksListener onWorkoutViewInterfaceClicksListener) {
        this.onWorkoutViewInterfaceClicksListener = onWorkoutViewInterfaceClicksListener;
    }
}
