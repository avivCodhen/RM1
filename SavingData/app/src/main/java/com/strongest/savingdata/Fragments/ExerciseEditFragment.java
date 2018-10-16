package com.strongest.savingdata.Fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.transition.Fade;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.TransitionInflater;
import android.transition.TransitionSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
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

    /* @BindView(R.id.muscle_tv)
     TextView mMuscleText;
 */
    @BindView(R.id.choose_change_Tv)
    TextView mChangeMuscleTV;

    /*@BindView(R.id.choose_cancel_Tv)
    TextView mCancelMuscleTV;
*/
    @BindView(R.id.exercise_edit_fragment_saveExitToolbar)
    SaveExitToolBar saveExitToolBar;

    @BindView(R.id.expandable)
    ExpandableLayout mExpandable;
    @BindView(R.id.fragment_choose_exercise_gridview)
    GridView mGridView;
    private GridViewMusclesAdapter mGridViewAdapter;

    @BindView(R.id.choose_recyclerview)
    RecyclerView recyclerView;

    /*@BindView(R.id.choose_exercise_comment_cb)
    CheckBox commentCB;

    @BindView(R.id.choose_exercise_comment_et)
    EditText commentET;
*/

    @BindView(R.id.edit_exercise_arrow_iv)
    ImageView muscleArrow;

    @BindView(R.id.choose_exercise_muscle_icon)
    ImageView muscleIcon;

    @BindView(R.id._choose_exercise_muscle_name_tv)
    TextView muscleTV;

    @BindView(R.id.player_btn)
    FloatingActionButton fabPlayerBtn;


    @BindView(R.id.exercise_choose_search_view)
    FloatingSearchView mSearchView;


    @BindView(R.id.youtube_fragment)
    FrameLayout youtubeLayout;

    @BindView(R.id.youtube_player_wrapper)
    ExpandableLayout youtubeWrapperEL;


    @BindView(R.id.exercise_layout)
    RelativeLayout listWrapper;


    @BindView(R.id.my_exercise_smart_empty_view)
    SmartEmptyView myExerciseSmartView;

    /*@BindView(R.id.change_btn)
    Button changeBtn;
*/
    @BindView(R.id.no_exercise_wrapper)
    ViewGroup noExerciseWrapper;

    @BindView(R.id.no_exercise_tv)
    TextView customExerciseTV;

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

    private boolean customExercise;

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
        guard(exerciseProfile);
        selectedExercise = exerciseProfile.exercise;
        selectedMuscle = exerciseProfile.getMuscle();

        youtubeHandler = YoutubeHandler
                .getHandler(getContext())
                .init(R.id.youtube_fragment, getChildFragmentManager());


        initViews(view);
    }

    private void initViews(View v) {

        initToolbar();
        int height = ((HomeActivity) getActivity()).getScreenHeight();
        mGridViewAdapter = new GridViewMusclesAdapter(height, getContext(), dataManager, this);
        mGridView.setAdapter(mGridViewAdapter);

        if (exerciseProfile.getMuscle() != null) {
            muscleTV.setText(exerciseProfile.getMuscle().getMuscle_display());
            initExerciseBeans(exerciseProfile.getMuscle());
        }


        DividerItemDecoration itemDecor = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecor);
        RecyclerView.LayoutManager lm = new LinearLayoutManager(getContext());
        int selectedIndex = getSelectedIndex();

        if (selectedIndex != -1) {
            recyclerView.scrollToPosition(selectedIndex);
        }
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

        if (exerciseProfile.getMuscle() != null) {
            Muscle.MuscleUI mui = Muscle.provideMuscleUI(exerciseProfile.getMuscle());
            muscleIcon.setImageResource(mui.getImage());
        }

        /**
         * these two onclicks are used to toggle between showing a "change"
         * and showing a "cancel" on the gridview's button toggle
         * */
        mChangeMuscleTV.setOnClickListener(v12 -> {
            mExpandable.toggle();
            if (mExpandable.isExpanded()) {
                MyJavaAnimator.rotateView(muscleArrow, 360, 180);
            } else {
                MyJavaAnimator.rotateView(muscleArrow, 180, 360);

            }
        });

        /*changeBtn.setOnClickListener(changeBtn -> {
            if (!clickedOnce) {
                mChangeMuscleTV.callOnClick();
            }
            clickedOnce = true;
        });*/
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
            Muscle.MuscleUI mui = Muscle.provideMuscleUI(b.getMuscle());
            onMuscleChange(new GridViewMusclesAdapter.MusclesContentHolder(mui.getImage(), b.muscle.getMuscle_display(), b.muscle));
            setExercise(b);
        });

        fabPlayerBtn.setOnClickListener(fab -> {
            youtubeWrapperEL.toggle();
            if (youtubeWrapperEL.isExpanded()) {
                fabPlayerBtn.setImageResource(R.drawable.sort_up_white_96dp);
            } else {
                fabPlayerBtn.setImageResource(R.drawable.sort_down_white_96dp);
            }
        });
    }

    /**
     * this custom toolbar allows to customize the save, cancel
     * exercise name, and search button
     */
    private void initToolbar() {

        saveExitToolBar.instantiate()
                .noElevation()
                .showNext(true)
                .nextFunc(v -> {
                    exerciseProfile.setMuscle(selectedMuscle);
                    exerciseProfile.setExercise(selectedExercise);
                    int exercisePos = selectedExerciseViewModel.getSelectedExercisePosition();
                    selectedExerciseViewModel.getParentWorkout().getExerciseObserver().onChange(exerciseProfile, exercisePos);
                    selectedExerciseViewModel = ViewModelProviders.of(getActivity()).get(fragmentId, SelectedExerciseViewModel.class);
                    selectedExerciseViewModel.select(exerciseProfile);
                    selectedExerciseViewModel.setParentWorkout(selectedExerciseViewModel.getParentWorkout());
                    Fragment nextFragment = ExerciseDetailsFragment.getInstance(fragmentId, selectedExerciseViewModel.getSelectedExercisePosition());

                    addFragmentChild(getFragmentManager(), nextFragment, fragmentId);
                });
        if (selectedExercise != null) {
            saveExitToolBar.setOptionalText(exerciseProfile.getExercise().getName());
        }
        if (exerciseProfile.getExercise() == null) {
            saveExitToolBar.disableNextBtn();
        }

        saveExitToolBar.setSaveButton(saveBtn -> {
            exerciseProfile.setMuscle(selectedMuscle);
            exerciseProfile.setExercise(selectedExercise);
            int exercisePos = selectedExerciseViewModel.getSelectedExercisePosition();
            selectedExerciseViewModel.getParentWorkout().getExerciseObserver().onChange(exerciseProfile, exercisePos);
            workoutsViewModel.saveLayoutToDataBase();
            getFragmentManager().popBackStack();

        });

       /* saveExitToolBar.setCancelButton(v -> {
            this.setExitTransition(new Fade());
            exitRevealAnimation(r -> getFragmentManager().popBackStack());
        });*/


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
        customExercise = false;
    }

    private void showUserCustomExercises() {
        exerciseBeans = (ArrayList<Beans>) dataManager.getExerciseDataManager().readByTable(DBExercisesHelper.TABLE_EXERCISES_CUSTOM);
        mAdapter.setExerciseBeans(exerciseBeans);
        collapseExpandableLayout();
        customExercise = true;
        notifyAdapter();

    }

    private void needToShowCustomExerciseTV() {
        if (exerciseBeans.size() == 0 && customExercise) {
            customExerciseTV.setVisibility(View.VISIBLE);
        } else {
            customExerciseTV.setVisibility(View.GONE);
        }
    }

    @Override
    public void onMuscleChange(GridViewMusclesAdapter.MusclesContentHolder mch) {
        MyJavaAnimator.rotateView(muscleArrow, 180, 360);
        muscleIcon.setImageResource(mch.icon);
        muscleTV.setText(mch.text);
        initExerciseBeans(mch.m);
        mAdapter.setExerciseBeans(exerciseBeans);
        selectedMuscle = mch.m;
        selectedExercise = null;
        mAdapter.setSelectedIndex(-1);
        notifyAdapter();
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
            saveExitToolBar.enableNextBtn();
            //hides the default text that overlays the youtube player
            noExerciseWrapper.setVisibility(View.GONE);
            //makes the youtube player visible
            youtubeLayout.setVisibility(View.VISIBLE);

            //selectedMuscle = beans.muscle;

            //updates the selected index in the dapter
            mAdapter.setSelectedIndex(getSelectedIndex());

            //request to search and present on the youtube player
            youtubeHandler.searchOnYoutube(beans.getName());

            //sets the toolbar exercise text
            saveExitToolBar.setOptionalText(selectedExercise.getName());
            mAdapter.notifyDataSetChanged();
            recyclerView.scrollToPosition(selectedIndex);
        }
    }

    private void notifyAdapter() {
        mAdapter.notifyDataSetChanged();
        needToShowCustomExerciseTV();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        youtubeHandler.stop();
    }
}
