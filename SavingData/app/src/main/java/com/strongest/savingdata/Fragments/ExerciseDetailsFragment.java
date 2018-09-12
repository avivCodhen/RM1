package com.strongest.savingdata.Fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.strongest.savingdata.AModels.workoutModel.PLObject;
import com.strongest.savingdata.AModels.workoutModel.Workout;
import com.strongest.savingdata.AModels.workoutModel.WorkoutLayoutTypes;
import com.strongest.savingdata.AModels.workoutModel.WorkoutsModel;
import com.strongest.savingdata.AModels.ExerciseModel;
import com.strongest.savingdata.AViewModels.SelectedExerciseViewModel;
import com.strongest.savingdata.AViewModels.SelectedSetViewModel;
import com.strongest.savingdata.AViewModels.WorkoutsViewModel;
import com.strongest.savingdata.Activities.ExerciseDetailsActivity;
import com.strongest.savingdata.Adapters.MyExpandableAdapter;
import com.strongest.savingdata.Adapters.WorkoutItemAdapters.SetsItemAdapter;
import com.strongest.savingdata.Animations.MyJavaAnimator;
import com.strongest.savingdata.BaseWorkout.Muscle;
import com.strongest.savingdata.Controllers.Architecture;
import com.strongest.savingdata.Controllers.OnExerciseInfo;
import com.strongest.savingdata.Controllers.UISetsClickHandler;
import com.strongest.savingdata.Database.Exercise.Beans;
import com.strongest.savingdata.Database.LogDataManager;
import com.strongest.savingdata.Dialogs.LogModeDialog;
import com.strongest.savingdata.MyViews.LongClickMenu.LongClickMenuView;
import com.strongest.savingdata.R;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;


public class ExerciseDetailsFragment extends BaseFragment implements
        Architecture.view.LongClickView, UISetsClickHandler, OnExerciseInfo {

    private static final String DONT_DISPLAY_CHECKBOX = "dont_display_checkbox";
    private static final String EXERCISE_POSITION = "exercise_position";
    @BindView(R.id.textview_title)
    TextView textview_title;
    @BindView(R.id.stats)
    View stats;

    @BindView(R.id.toolbar_add_set_btn)
    ImageView toolbarAddSet;

    @BindView(R.id.details_fragment_recyclerview)
    RecyclerView recyclerView;
    @BindView(R.id.fragment_details_recycler_exercises)
    RecyclerView exerciseRecycler;
    @BindView(R.id.exercise_details_activity_toolbar)
    Toolbar toolbar;

    MyExpandableAdapter adapter;
    MyExpandableAdapter exerciseAdapter;
    private Workout workout = new Workout();

    @BindView(R.id.testIv)
    CircleImageView icon;

    @BindView(R.id.fragment_details_longclick)
    LongClickMenuView longClickMenuView;

    @BindView(R.id.exercise_info)
    View exerciseInfo;

    @BindView(R.id.youtube_expand_layout)
    ExpandableLayout el;

    @BindView(R.id.logBtn)
    Button logBtn;

    @BindView(R.id.saveToLogBtn)
    Button saveToLogBtn;

    private SelectedExerciseViewModel selectedExerciseViewModel;
    private WorkoutsViewModel workoutsViewModel;
    private SetsItemAdapter setsItemAdapter;

    /**
     * this is needs to be a clone version of the selected exerciseProfile
     * based upon saving or canceling, it will either replace the exerciseprofile
     * if the client saved, or the old exerciseprofile will keep it's data
     */
    private PLObject.ExerciseProfile exerciseProfile;
    private String parentFragmentId;
    private LinearLayoutManager setsLayoutManager;
    private LogDataManager logDataManager;

    Animation slideRight, slideTop;

    private Handler handler = new Handler();

    private int exercisePosition;

    public static ExerciseDetailsFragment getInstance(String fragment, int position) {
        ExerciseDetailsFragment f = new ExerciseDetailsFragment();
        Bundle b = new Bundle();
        b.putString(FRAGMENT_TAG, fragment);
        b.putInt(EXERCISE_POSITION, position);
        f.setArguments(b);
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_details, container, false);
        ButterKnife.bind(this, view);
        //postponeEnterTransition();
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            parentFragmentId = getArguments().getString(FRAGMENT_TAG);
            exercisePosition = getArguments().getInt(EXERCISE_POSITION);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(FRAGMENT_TAG, parentFragmentId);
        outState.putInt(EXERCISE_POSITION, exercisePosition);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        icon.setTransitionName("q1");

        if (savedInstanceState != null) {
            parentFragmentId = savedInstanceState.getString(FRAGMENT_TAG);
            exercisePosition = savedInstanceState.getInt(EXERCISE_POSITION);

        }

        selectedExerciseViewModel = ViewModelProviders.of(getActivity()).get(parentFragmentId, SelectedExerciseViewModel.class);
        logDataManager = new LogDataManager(getContext());
        //instantiating the cloned exercise profile
        getExercise();
        if (exerciseProfile == null) {
            getFragmentManager().popBackStack();
        }
        setsItemAdapter = new SetsItemAdapter(exerciseProfile);
        initToolbar();
        initAdapters();
        initRecycler();
        getList();
        workoutsViewModel = ViewModelProviders.of(getActivity()).get(WorkoutsViewModel.class);
        //workout = selectedExerciseViewModel.getExerciseAsList().getValue();

        longClickMenuView.instantiate(this);


        if (exerciseProfile.getMuscle() != null) {
            Muscle.MuscleUI mui = Muscle.provideMuscleUI(exerciseProfile.getMuscle());
            icon.setImageResource(mui.getImage());
        }
        //startPostponedEnterTransition();


        slideRight = AnimationUtils.loadAnimation(getContext(),
                R.anim.slide_in_right);
        slideTop = AnimationUtils.loadAnimation(getContext(),
                R.anim.slide_in_up);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                stats.setVisibility(View.VISIBLE);
                stats.startAnimation(slideRight);
                exerciseRecycler.setVisibility(View.VISIBLE);
                exerciseRecycler.startAnimation(slideRight);
                recyclerView.setVisibility(View.VISIBLE);
                recyclerView.startAnimation(slideTop);
            }
        }, 100);


        exerciseInfo.setOnClickListener(info -> {
            transitionToExerciseDetailsActivity(exerciseProfile.getExercise());
        });

        logBtn.setOnClickListener(btnClicked -> {

            LogModeDialog l = LogModeDialog.getInstacen(exerciseProfile);
            l.show(getFragmentManager(), "dialog");
           /* if (!el.isExpanded()) {
                showEnterLogModeDialog();
            } else {
                el.collapse();
                exitLogMode();
            }*/
        });


        saveToLogBtn.setOnClickListener(saveLog -> {
            logDataManager.insert(exerciseProfile);
        });


    }

    private void initRecycler() {

        setsLayoutManager = new LinearLayoutManager(getContext());
        LinearLayoutManager lm2 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(setsLayoutManager);
        exerciseRecycler.setLayoutManager(lm2);
        //recyclerView.setNestedScrollingEnabled(false);

        recyclerView.setAdapter(adapter);
        exerciseRecycler.setAdapter(exerciseAdapter);
        recyclerView.setAlpha(0f);

    }


    private void initToolbar() {

        toolbar.setNavigationIcon(R.drawable.icon_back_white);
        if (exerciseProfile.getExercise() != null) {
            textview_title.setText(exerciseProfile.getExercise().getName());

        }
        if (exerciseProfile.getMuscle() != null) {
            Muscle.MuscleUI mui = Muscle.provideMuscleUI(exerciseProfile.getMuscle());
            icon.setImageResource(mui.getImage());
        }
        toolbar.setTitle("");

        toolbarAddSet.setOnClickListener(toolBarAddIcon -> {
            selectedExerciseViewModel.getParentWorkout().getExerciseObserver().onChange(exerciseProfile, exercisePosition);
            WorkoutsModel.ListModifier.OnWith(workout, setsItemAdapter)
                    .doAddNew(workout.exArray.size()).applyWith(adapter, null, null);
            recyclerView.scrollToPosition(workout.exArray.size() - 1);

        });

        toolbar.setNavigationOnClickListener(navBack -> {
            getFragmentManager().popBackStack();
        });

    }

    private void initAdapters() {
        //workout = workoutsViewModel.workoutsModel.exerciseToList(ep);


        exerciseAdapter = new MyExpandableAdapter(
               new ArrayList<>(),
                getContext());
        exerciseAdapter.setLeanLayout(true);
        adapter = new MyExpandableAdapter(
                workout.exArray,
                getContext());
        adapter.setUiSetsClickHandler(this);
        adapter.setOnExerciseInfo(this);
        adapter.setSupersets(exerciseProfile.exerciseProfiles);
        exerciseAdapter.setOnExerciseInfo(this);
        adapter.setLongClickMenuView(longClickMenuView);

    }

    private void transitionToExerciseDetailsActivity(Beans exercise) {
        Intent i = new Intent(getContext(), ExerciseDetailsActivity.class);
        i.putExtra("exercise", exercise);
       /* Pair<View, String>[] pairs = new Pair[2];

        pairs[0] = Pair.create(icon, "icon");
        pairs[1] = Pair.create(textview_title, "title");
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(getActivity(), pairs);
        startActivity(i,options.toBundle());*/
        startActivity(i);
    }


    @Override
    public void onLongClickAction(LongClickMenuView longClickMenuView, WorkoutsModel.Actions action) {

        WorkoutsModel.ListModifier listModifier = WorkoutsModel.ListModifier.OnWith(workout, setsItemAdapter);
        switch (action) {

            case Delete:

                listModifier.doRemove(longClickMenuView.getSelectedPLObjects().get(0), 1)
                        .applyWith(adapter, null, null);
                adapter.notifyItemRangeChanged(0, adapter.getExArray().size());
                longClickMenuView.onHideMenu();
                break;
            case MultiDelete:
                for (PLObject plObject : longClickMenuView.getSelectedPLObjects()) {
                    listModifier.doRemove(plObject, 1);
                }
                longClickMenuView.onHideMenu();

                listModifier.applyWith(adapter, null, null);
                adapter.notifyItemRangeChanged(0, adapter.getExArray().size());
                break;
            case Duplicate:
                //int position = workout.exArray.indexOf(longClickMenuView.getSelectedPLObjects().get(0));
                listModifier.doDuplicate(longClickMenuView.getSelectedPLObjects().get(0)).applyWith(adapter, null, null);
                break;

            /*case Child:
                listModifier.doChild(longClickMenuView.getSelectedPLObjects().get(0))
                        .applyWith(adapter);
                break;*/


        }
        selectedExerciseViewModel.getParentWorkout().getExerciseObserver().onChange(exerciseProfile, exercisePosition);

        if (workout.exArray.size() == 0) {
            longClickMenuView.onHideMenu();
        }
       /* if(exerciseAdapter.getExArray().size() != 0){
            exerciseAdapter.notifyItemChanged(0);
        }else{
            throw new IllegalArgumentException("No exercise? Exercise list is 0? What the fuck");
        }*/
    }


    @Override
    public void onSetsClick(MyExpandableAdapter.MyExpandableViewHolder vh, PLObject plObject) {
        SelectedSetViewModel selectedSetViewModel = ViewModelProviders.of(getActivity()).get(SelectedSetViewModel.class);
        selectedSetViewModel.select((PLObject.SetsPLObject) plObject);
        selectedSetViewModel.getSelectedExerciseSet().removeObservers(this);
        selectedSetViewModel.getSelectedExerciseSet().observe(this, (exerciseSet) -> {
            adapter.notifyDataSetChanged();
        });

        SetsChooseSingleFragment f = SetsChooseSingleFragment.getInstance();
        addFragmentChild(getFragmentManager(), f);


        /*setsItemAdapter.onChild((PLObject.SetsPLObject) plObject);
        adapter.notifyItemChanged(vh.getAdapterPosition());*/
    }

    @Override
    public void onSetsLongClick(PLObject plObject, MyExpandableAdapter.MyExpandableViewHolder vh) {
        longClickMenuView.onShowMenu(vh, plObject);
    }

    @Override
    public void onAddingIntraSet(PLObject.SetsPLObject setsPLObject, int position) {
        PLObject.SetsPLObject intraSet = new PLObject.SetsPLObject();
        intraSet.type = WorkoutLayoutTypes.IntraSet;
        intraSet.innerType = WorkoutLayoutTypes.IntraSet;
        setsPLObject.intraSets.add(intraSet);
        adapter.notifyItemChanged(workout.exArray.indexOf(setsPLObject));
    }

    @Override
    public void onRemoveIntraSet(PLObject.SetsPLObject setsPLObject, int intraSetPosition) {
        setsPLObject.intraSets.remove(intraSetPosition);
        adapter.notifyItemChanged(workout.exArray.indexOf(setsPLObject));
    }

    @Override
    public void transitionToExerciseInfo(Beans exercise) {
        transitionToExerciseDetailsActivity(exercise);
    }

    private void showEnterLogModeDialog() {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(getContext())
                .title("Log Mode")
                .content("On entering Log Mode, any changes you make will not apply to the program." +
                        " To save, click Save To Program. ")
                .positiveText("Enter")
                .negativeText("Cancel");

        if (!workoutsViewModel.getDataManager().getPrefs().getString(DONT_DISPLAY_CHECKBOX, "").equals(DONT_DISPLAY_CHECKBOX)) {
            builder.checkBoxPromptRes(R.string.log_mode_checkbox, false, (compoundButton, b) -> {
                if (b == true)
                    workoutsViewModel.getDataManager().getPrefsEditor().putString(DONT_DISPLAY_CHECKBOX, DONT_DISPLAY_CHECKBOX);
            });

        }

        MaterialDialog dialog = builder.build();
        dialog.getActionButton(DialogAction.POSITIVE).setOnClickListener((positive) -> {
            el.toggle();
            enterLogMode();
            dialog.dismiss();
        });
        dialog.show();

    }

    private void enterLogMode() {
        exerciseProfile = new PLObject.ExerciseProfile(selectedExerciseViewModel.getSelectedExercise().getValue());
        getList();
    }

    private void exitLogMode() {
        getExercise();
        getList();
    }

    private void getExercise() {
        exerciseProfile = selectedExerciseViewModel.getSelectedExercise().getValue();

    }

    private void getList() {
        ExerciseModel.exerciseToWorkout(setsItemAdapter, exerciseProfile, w -> {
            workout = (Workout) w;
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            handler.post(new Runnable() {
                @Override
                public void run() {
                    adapter.setExArray(workout.getExArray());
                    adapter.notifyDataSetChanged();
                    MyJavaAnimator.fadeIn(recyclerView);

                }
            });

        });

        exerciseAdapter.setExArray(ExerciseModel.expandExerciseSupersets(exerciseProfile));
        exerciseAdapter.notifyDataSetChanged();
    }

}