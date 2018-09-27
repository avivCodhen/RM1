package com.strongest.savingdata.Fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.transition.Fade;
import android.support.transition.Transition;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.strongest.savingdata.AViewModels.SelectedExerciseViewModel;
import com.strongest.savingdata.AViewModels.WorkoutsViewModel;
import com.strongest.savingdata.Activities.HomeActivity;
import com.strongest.savingdata.Adapters.ExerciseListAdapter;
import com.strongest.savingdata.Adapters.GridViewMusclesAdapter;
import com.strongest.savingdata.Adapters.OnExerciseListAdapterClickListener;
import com.strongest.savingdata.Adapters.OnGridViewMuscleAdapterClickListener;
import com.strongest.savingdata.AModels.workoutModel.PLObject;
import com.strongest.savingdata.Animations.MyJavaAnimator;
import com.strongest.savingdata.BaseWorkout.Muscle;
import com.strongest.savingdata.Database.Exercise.Beans;
import com.strongest.savingdata.Database.Exercise.DBExercisesHelper;
import com.strongest.savingdata.Database.Managers.DataManager;
import com.strongest.savingdata.Fragments.Choose.ExerciseSearchSuggestion;
import com.strongest.savingdata.Fragments.Choose.OnExerciseSetChange;
import com.strongest.savingdata.Handlers.FloatingSearchViewHandler;
import com.strongest.savingdata.Handlers.YoutubeHandler;
import com.strongest.savingdata.MyViews.SaveExitToolBar;
import com.strongest.savingdata.MyViews.SmartEmptyView;
import com.strongest.savingdata.R;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Cohen on 3/13/2018.
 */

public class ExerciseEditFragment extends BaseFragment implements
        OnExerciseListAdapterClickListener, OnGridViewMuscleAdapterClickListener {

    public static final String FRAGMENT_EDIT_EXERCISE = "exerciseeditfragment";
    private OnExerciseSetChange onExerciseSetChange;

    @BindView(R.id.muscle_tv)
    TextView mMuscleText;

    @BindView(R.id.choose_change_Tv)
    TextView mChangeMuscleTV;

    @BindView(R.id.choose_cancel_Tv)
    TextView mCancelMuscleTV;

    @BindView(R.id.exercise_edit_fragment_saveExitToolbar)
    SaveExitToolBar saveExitToolBar;

    @BindView(R.id.expandable)
    ExpandableLayout mExpandable;
    @BindView(R.id.fragment_choose_exercise_gridview)
    GridView mGridView;
    private GridViewMusclesAdapter mGridViewAdapter;

    @BindView(R.id.choose_recyclerview)
    RecyclerView recyclerView;

    @BindView(R.id.choose_exercise_comment_cb)
    CheckBox commentCB;

    @BindView(R.id.choose_exercise_comment_et)
    EditText commentET;

    @BindView(R.id.exercise_choose_search_view)
    FloatingSearchView mSearchView;


    @BindView(R.id.youtube_fragment)
    FrameLayout youtubeLayout;

    @BindView(R.id.youtube_player_wrapper)
    ViewGroup youtubeWrapper;


    @BindView(R.id.exercise_layout)
    RelativeLayout listWrapper;


    @BindView(R.id.my_exercise_smart_empty_view)
    SmartEmptyView myExerciseSmartView;

    @BindView(R.id.change_btn)
    Button changeBtn;

    @BindView(R.id.no_exercise_wrapper)
    ViewGroup noExerciseWrapper;

    public static final String EXERCISE_PROFILE = "exercise_profile";
    public ArrayList<Beans> exerciseBeans = new ArrayList<>();
    private PLObject.ExerciseProfile exerciseProfile;
    public ExerciseListAdapter mAdapter;


    private DataManager dataManager;
    private int selectedIndex = -1;
    private ArrayList<Beans> allExercisesList;
    private ArrayList<ExerciseSearchSuggestion> allExercisesListString;
    private WorkoutsViewModel workoutsViewModel;
    private SelectedExerciseViewModel selectedExerciseViewModel;

    YoutubeHandler youtubeHandler;

    private Beans selectedExercise;
    private Muscle selectedMuscle;
    private String fragmentId;
    private View mainView;

    int x;
    int y;

    boolean clickedOnce;
    public static ExerciseEditFragment newInstance(String fragmentId) {
        ExerciseEditFragment f = new ExerciseEditFragment();
        Bundle bundle = new Bundle();
        bundle.putString(FRAGMENT_TAG, fragmentId);
        f.setArguments(bundle);
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            fragmentId = getArguments().getString(FRAGMENT_TAG);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_choose_exercise, container, false);
        ButterKnife.bind(this, v);
        x = v.getRight();
        y = v.getBottom();
        v.setVisibility(View.INVISIBLE);

        mainView = v;
        return v;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(FRAGMENT_TAG, fragmentId);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (savedInstanceState != null) {
            fragmentId = savedInstanceState.getString(FRAGMENT_TAG);
        }
        workoutsViewModel = ViewModelProviders.of(getActivity()).get(WorkoutsViewModel.class);
        selectedExerciseViewModel = ViewModelProviders.of(getActivity()).get(fragmentId, SelectedExerciseViewModel.class);
        dataManager = workoutsViewModel.getDataManager();
        exerciseProfile = selectedExerciseViewModel.getSelectedExercise().getValue();
        selectedExercise = exerciseProfile.exercise;
        selectedMuscle = exerciseProfile.getMuscle();

        youtubeHandler = YoutubeHandler
                .getHandler(getContext())
                .init(R.id.youtube_fragment, getChildFragmentManager());


        initViews(view);
    }

    private void initViews(View v) {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                MyJavaAnimator.animateRevealShowParams(v, true, R.color.colorAccent, x, y, null);

            }
        }, 100);
        initToolbar();
        int height = ((HomeActivity) getActivity()).getScreenHeight();
        mGridViewAdapter = new GridViewMusclesAdapter(height, getContext(), dataManager, this);
        mGridView.setAdapter(mGridViewAdapter);

        if (exerciseProfile.getMuscle() != null) {
            mMuscleText.setText(exerciseProfile.getMuscle().getMuscle_display());
            initExerciseBeans(exerciseProfile.getMuscle());
        }


        DividerItemDecoration itemDecor = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecor);
        RecyclerView.LayoutManager lm = new LinearLayoutManager(getContext());
        int selectedIndex = getSelectedIndex();
        mAdapter = new ExerciseListAdapter(selectedIndex,
                getContext(),
                this,
                onExerciseSetChange,
                exerciseBeans);
        recyclerView.setLayoutManager(lm);
        recyclerView.setAdapter(mAdapter);

        /**
         * the if checks if there was a match between the exercises from the current exercise
         * it uses the setExercise function to for further instructions
         * */
        if (selectedIndex != -1) {
            setExercise(exerciseBeans.get(selectedIndex));
        }

        /**
         * these two onclicks are used to toggle between showing a "change"
         * and showing a "cancel" on the gridview's button toggle
         * */
        mChangeMuscleTV.setOnClickListener(v12 -> {
            mExpandable.toggle();
            mChangeMuscleTV.setVisibility(View.GONE);
            mCancelMuscleTV.setVisibility(View.VISIBLE);
        });
        mCancelMuscleTV.setOnClickListener(v1 -> {
            mCancelMuscleTV.setVisibility(View.GONE);
            mChangeMuscleTV.setVisibility(View.VISIBLE);
            mExpandable.toggle();
        });


        changeBtn.setOnClickListener(changeBtn->{
            if(!clickedOnce){
                mChangeMuscleTV.callOnClick();
            }
            clickedOnce = true;
        });
        /**
         * this is used to show the user's custom exercises
         * */
        v.findViewById(R.id.choose_change_user_tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showUserCustomExercises();
            }
        });

        /**
         * handles all the required actions for the search view
         * including fetching data. the callback is used to provide the clicked
         * suggestion
         * */
        FloatingSearchViewHandler.getHandler(mSearchView).from(dataManager).handleExercises((b) -> {
            mSearchView.setSearchFocused(false);
            onMuscleChange(new GridViewMusclesAdapter.MusclesContentHolder(-1, b.muscle.getMuscle_display(), b.muscle));
            setExercise(b);
        });


        /**
         * test porpuses only
         * */
        v.findViewById(R.id.hide_player_edit_exercise_fragment).setOnClickListener(view -> {
            float distance = 0;
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) listWrapper.getLayoutParams();

            if (((TextView) view).getText().equals("Show Player")) {
                distance = TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP, 260,
                        getResources().getDisplayMetrics()
                );
                ((TextView) view).setText("Hide Player");
            } else {
                distance = TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP, 55,
                        getResources().getDisplayMetrics());
                ((TextView) view).setText("Show Player");

            }

            float finalDistance = distance;
            Animation a = new Animation() {

                @Override
                protected void applyTransformation(float interpolatedTime, Transformation t) {
                    params.topMargin = (int) (finalDistance * interpolatedTime);
                    listWrapper.setLayoutParams(params);
                }
            };
            a.setDuration(500); // in ms
            listWrapper.startAnimation(a);

        });

    }

    /**
     * this custom toolbar allows to customize the save, cancel
     * exercise name, and search button
     */
    private void initToolbar() {

        saveExitToolBar.instantiate()
        .noElevation();
        if (selectedExercise != null) {
            saveExitToolBar.setOptionalText(exerciseProfile.getExercise().getName());
        }

        saveExitToolBar.setSaveButton(saveBtn -> {
            exerciseProfile.setMuscle(selectedMuscle);
            exerciseProfile.setExercise(selectedExercise);
            int exercisePos = selectedExerciseViewModel.getSelectedExercisePosition();
            selectedExerciseViewModel.getParentWorkout().getExerciseObserver().onChange(exerciseProfile, exercisePos);
            workoutsViewModel.saveLayoutToDataBase();
            MyJavaAnimator.animateRevealShowParams(mainView, false, R.color.background_color, x, y, r -> {
                getFragmentManager().popBackStack();

            });
        });

        saveExitToolBar.setCancelButton(v -> {
            this.setExitTransition(new Fade());

            MyJavaAnimator.animateRevealShowParams(mainView, false, R.color.background_color, x, y, r -> {
                getFragmentManager().popBackStack();

            });

        });
    }


    private int getSelectedIndex() {
        if (selectedExercise != null) {
            for (int i = 0; i < exerciseBeans.size(); i++) {
                if (selectedExercise.getName().equals(exerciseBeans.get(i).getName())) {
                    selectedIndex = i;
                    return selectedIndex;
                }
            }
        }
        selectedIndex = -1;
        return selectedIndex;
    }
/*

    private void initComment(View v) {
        commentET.setText(exerciseProfile.comment);
        commentCB.setOnCheckedChangeListener((buttonView, isChecked) -> {
            commentET.setEnabled(isChecked);
            exerciseProfile.showComment = isChecked;
            if (isChecked) {
                exerciseProfile.comment = commentET.getText().toString();
            }
        });

        commentCB.setChecked(exerciseProfile.showComment);
        commentET.setEnabled(commentCB.isChecked());
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                exerciseProfile.comment = commentET.getText().toString();
            }

            @Override
            public void afterTextChanged(Editable s) {
                commentET.setSelection(s.length());
                exerciseProfile.comment = commentET.getText().toString();

            }
        };
        commentET.addTextChangedListener(textWatcher);
    }
*/

    /**
     * this function takes the muscle from the clicked exercise(if it has any)
     * and fetchs the exercise corresponding to the muscle
     * it also sorts the list
     */
    private void initExerciseBeans(Muscle m) {
        exerciseBeans = (ArrayList<Beans>)
                dataManager.getExerciseDataManager().
                        readByTable(m.getMuscle_name());
        exerciseBeans = Beans.sortByAccessory(exerciseBeans);
    }

    private void showUserCustomExercises() {
        exerciseBeans = (ArrayList<Beans>) dataManager.getExerciseDataManager().readByTable(DBExercisesHelper.TABLE_EXERCISES_CUSTOM);
        mAdapter.setExerciseBeans(exerciseBeans);


        collapseExpandableLayout();
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onMuscleChange(GridViewMusclesAdapter.MusclesContentHolder mch) {
        mCancelMuscleTV.setVisibility(View.GONE);
        mChangeMuscleTV.setVisibility(View.VISIBLE);
        initExerciseBeans(mch.m);
        mAdapter.setExerciseBeans(exerciseBeans);
        selectedMuscle = mch.m;
        selectedExercise = null;
        mMuscleText.setText(mch.text);
        mAdapter.setSelectedIndex(-1);
        mAdapter.notifyDataSetChanged();

    }

    @Override
    public void collapseExpandableLayout() {
        mExpandable.collapse();
    }

    /**
     * called when an exercise is available or changed
     * <p>
     * applies the selectedExercise value to the clicked exercise row in the recyclerview
     * it hides the youtube default text layout
     * makes the youtube layout visible to be displayed
     * sets the selected muscle to the exercise's corresponding muscle
     * applies the index of the selected exercise via getSelectedIndex
     * calls the youtubeHandler to initialize the player
     * changes the right top tv to the exercise's name
     * update the adapter
     */
    @Override
    public void setExercise(Beans beans) {
        selectedExercise = beans;
        if (beans != null) {

            //hides the default text that overlays the youtube player
            noExerciseWrapper.setVisibility(View.GONE);
            //makes the youtube player visible
            youtubeLayout.setVisibility(View.VISIBLE);

            selectedMuscle = beans.muscle;

            //updates the selected index in the dapter
            mAdapter.setSelectedIndex(getSelectedIndex());

            //request to search and present on the youtube player
            youtubeHandler.searchOnYoutube(beans.getName());

            //sets the toolbar exercise text
            saveExitToolBar.setOptionalText(selectedExercise.getName());
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        youtubeHandler.stop();
    }
}
