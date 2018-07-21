package com.strongest.savingdata.MyViews.LongClickMenu;

import android.animation.Animator;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.strongest.savingdata.AModels.AlgorithmLayout.WorkoutsModel.Actions;
import com.strongest.savingdata.AModels.AlgorithmLayout.PLObject;
import com.strongest.savingdata.Adapters.MyExpandableAdapter;
import com.strongest.savingdata.AModels.AlgorithmLayout.WorkoutLayoutTypes;
import com.strongest.savingdata.Controllers.Architecture;
import com.strongest.savingdata.MyViews.WorkoutView.OnWorkoutViewInterfaceClicksListener;
import com.strongest.savingdata.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Cohen on 3/27/2018.
 */

public class LongClickMenuView extends LinearLayout implements View.OnClickListener {
    public static final String EXERCISE = "exercise", SET = "set",
            INTRA_SET = "intra_set", INTRA_SUPERSET = "intra_superset", INTRA_EXERCISE = "intra_exercise";

    public boolean isOn() {
        return isOn;
    }


    @BindView(R.id.longclick_menu_back)
    ImageView backBtn;
    @BindView(R.id.longclick_menu_delete)
    ImageView delete;
    @BindView(R.id.longclick_menu_duplicate)
    ImageView duplicate;
    @BindView(R.id.longclick_menu_child)
    ImageView child;
    @BindView(R.id.longclick_menu_selected)
    TextView selectedNum;

    private final Context context;
    private ArrayList<MyExpandableAdapter.MyExpandableViewHolder> highlightedViews = new ArrayList<>();
    private MyExpandableAdapter.MyExpandableViewHolder currentVH;
    private WorkoutLayoutTypes currentType;
    private OnWorkoutViewInterfaceClicksListener onWorkoutViewInterfaceClicksListener;
    private ArrayList<PLObject> selectedPLObjects = new ArrayList<>();

    //    private ImageView more;
    private int viewPosition;
    private Architecture.view.LongClickView listener;
    private boolean isOn;


    public ArrayList<PLObject> getSelectedPLObjects() {
        return selectedPLObjects;
    }

    public ArrayList<MyExpandableAdapter.MyExpandableViewHolder> getHighlightedViews() {
        return highlightedViews;
    }

    public LongClickMenuView(Context context) {
        super(context);
        this.context = context;
    }

    public LongClickMenuView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public void instantiate(Architecture.view.LongClickView callback) {
        this.listener = callback;
        inflate(context, R.layout.layout_longclick_menu, this);
        ButterKnife.bind(this);
        initViews();

    }

    private void initViews() {
        findViewById(R.id.longclick_menu_back).setOnClickListener(this);
        /*supersetBtn =  findViewById(R.id.longclick_menu_superset);
        intrasetBtn = findViewById(R.id.longclick_menu_intraSet);*/
        //  more =  findViewById(R.id.longclick_menu_more);
        //more.setOnClickListener(this);
        /*setBtn =  findViewById(R.id.longclick_menu_set);
        setBtn.setOnClickListener(this);
        intrasetBtn.setOnClickListener(this);
        supersetBtn.setOnClickListener(this);*/
        findViewById(R.id.longclick_menu_delete).setOnClickListener(this);
        initDropMenu();
    }

    private void enableDisableBtns() {
        if (highlightedViews.size() > 1) {
            disableBtns(duplicate, child);
        } else if (!selectedPLObjects.get(0).isParent) {
            enableBtns(duplicate);
            disableBtns(child);
        } else {
            enableBtns(duplicate, child);
        }
        /*else{
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
        }*/
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


    public void onShowMenu(MyExpandableAdapter.MyExpandableViewHolder vh, PLObject plObject) {
        isOn = true;
        selectedPLObjects.add(plObject);
        View hView = vh.getMainLayout();
        //hView.setVisibility(VISIBLE);
        hView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.longClickBackground));
        highlightedViews.add(vh);

        currentVH = vh;
        viewPosition = vh.getAdapterPosition();
        setVisibility(VISIBLE);
        selectedNum.setText(highlightedViews.size() + "");
        /*hView.animate().alpha(0.8f)*//*.setListener(new Animator.AnimatorListener() {
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
        })*//*;
         */

        animate().alpha(1f)/*.setListener(new Animator.AnimatorListener() {
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
        })*/;
        setAlpha(1f);
        setVisibility(VISIBLE);
        enableDisableBtns();


    }

    /* public void onHideDragLayout() {
         if (highlightedViews != null) {
             highlightedViews.setVisibility(GONE);
             if(currentVH instanceof MyExpandableAdapter.ExerciseViewHolder)
             ((MyExpandableAdapter.ExerciseViewHolder) currentVH).flipView.flipTheView();
         }
         isOn = false;
     }
 */
    public void onHideMenu() {
        animate().alpha(0).setListener(new Animator.AnimatorListener() {
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
        if (highlightedViews.size() != 0) {
            removeVisibility();
            highlightedViews.clear();
            currentType = null;
            currentVH = null;
            isOn = false;
            selectedPLObjects.clear();
        }
        setVisibility(GONE);


          /*  highlightedViews.animate().alpha(0f).setListener(new Animator.AnimatorListener() {
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
            });*/

      /*  if (currentType == WorkoutLayoutTypes.ExerciseProfile || currentType == WorkoutLayoutTypes.IntraExerciseProfile) {
            ((MyExpandableAdapter.ExerciseViewHolder) currentVH).flipView.flipTheView();
        }*/
    }

    @OnClick({R.id.longclick_menu_back,
            R.id.longclick_menu_delete,
            R.id.longclick_menu_duplicate,
            R.id.longclick_menu_child,
            R.id.longclick_menu_selected
    })
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.longclick_menu_back:
                onHideMenu();
                break;
            case R.id.longclick_menu_duplicate:
                listener.onLongClickAction(this, Actions.Duplicate);

                break;
            case R.id.longclick_menu_delete:
                Actions a = highlightedViews.size() > 1 ? Actions.MultiDelete : Actions.Delete;
                listener.onLongClickAction(this, a);
                break;
            case R.id.longclick_menu_child:
                listener.onLongClickAction(this, Actions.Child);

                break;
            case R.id.longclick_menu_selected:
                //TODO: need to change the callback
                break;
        }

    }

    public void removeVisibility() {
        for (int i = 0; i < highlightedViews.size(); i++) {
            highlightedViews.get(i).getMainLayout().setBackgroundColor(
                    ContextCompat.getColor(getContext(), R.color.colorPrimary)
            );
        }
    }

    public void initDropMenu() {

      /*  DroppyMenuPopup.Builder droppyBuilder = new DroppyMenuPopup.Builder(context, more);

// Add normal items (text only)
        droppyBuilder.addMenuItem(new DroppyMenuItem("Duplicate"))
                .addSeparator();


// Set Callback handler
        droppyBuilder.setOnClick(new DroppyClickCallbackInterface() {
            @Override
            public void call(View v, int id) {
                listener.onLongClickAction(Action.Duplicate, currentVH, currentType);
            }
        });

        DroppyMenuPopup droppyMenu = droppyBuilder.build();*/

    }

    public void setOnWorkoutViewInterfaceClicksListener(OnWorkoutViewInterfaceClicksListener onWorkoutViewInterfaceClicksListener) {
        this.onWorkoutViewInterfaceClicksListener = onWorkoutViewInterfaceClicksListener;
    }

    public int getViewPosition() {
        return viewPosition;
    }
}
