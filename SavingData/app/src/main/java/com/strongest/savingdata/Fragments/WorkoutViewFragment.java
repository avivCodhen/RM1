package com.strongest.savingdata.Fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.transition.Fade;
import android.transition.TransitionInflater;
import android.transition.TransitionSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.strongest.savingdata.AModels.AlgorithmLayout.Workout;
import com.strongest.savingdata.AViewModels.SelectedExerciseViewModel;
import com.strongest.savingdata.AViewModels.WorkoutsViewModel;
import com.strongest.savingdata.AViewModels.WorkoutsViewModelFactory;
import com.strongest.savingdata.Activities.HomeActivity;
import com.strongest.savingdata.Adapters.MyExpandableAdapter;
import com.strongest.savingdata.Adapters.WorkoutAdapter.DragAndSwipeCallback;
import com.strongest.savingdata.Adapters.WorkoutAdapter.MyExpandableLinearLayoutManager;
import com.strongest.savingdata.Adapters.WorkoutAdapter.ScrollToPositionListener;
import com.strongest.savingdata.AModels.AlgorithmLayout.OnLayoutManagerDialogPress;
import com.strongest.savingdata.AModels.AlgorithmLayout.PLObject;
import com.strongest.savingdata.AModels.AlgorithmLayout.PLObject.ExerciseProfile;
import com.strongest.savingdata.Adapters.WorkoutItemAdapters.ExerciseItemAdapter;
import com.strongest.savingdata.Controllers.UiExerciseClickHandler;
import com.strongest.savingdata.MyViews.LongClickMenu.LongClickMenuView;
import com.strongest.savingdata.MyViews.WorkoutView.OnEnterExitChooseFragment;
import com.strongest.savingdata.MyViews.WorkoutView.OnUpdateLayoutStatsListener;
import com.strongest.savingdata.R;
import com.strongest.savingdata.ViewHolders.ExerciseViewHolder;

import java.util.ArrayList;

import javax.inject.Inject;


public class WorkoutViewFragment extends BaseFragment implements com.strongest.savingdata.Adapters.WorkoutAdapter.OnDragListener,
        ScrollToPositionListener,

        OnLayoutManagerDialogPress, UiExerciseClickHandler {


    private RecyclerView recycler;
    private MyExpandableAdapter adapter;
    private ItemTouchHelper touchHelper;
    //private ArrayList<Workout> exArray;
    private ArrayList<PLObject> exArray;
    private Workout workout;
    private OnUpdateLayoutStatsListener onUpdateLayoutStatsListener;
    private MyExpandableLinearLayoutManager lm;
    public WorkoutViewFragmentListener workoutViewFragmentListener;

    @Inject
    WorkoutsViewModelFactory workoutsViewModelFactory;
    private WorkoutsViewModel workoutsViewModel;
    //gaining access to provide animations upon destroy
    private View mainView;

    private OnEnterExitChooseFragment onEnterExitChooseFragment;
    //private LayoutManagerOperator layoutManagerOperator;
    private int tag;

    private SelectedExerciseViewModel selectedExerciseViewModel;
    private LongClickMenuView longclickMenuView;


    public static WorkoutViewFragment getInstance(int tag) {
        WorkoutViewFragment frag = new WorkoutViewFragment();
        Bundle b = new Bundle();
        b.putInt(FRAGMENT_TAG, tag);
        frag.setArguments(b);
        return frag;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            tag = getArguments().getInt(FRAGMENT_TAG);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.workoutview_list, container, false);

        return mainView;

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(FRAGMENT_TAG, tag);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (savedInstanceState != null) {
            tag = savedInstanceState.getInt(FRAGMENT_TAG);

        }
        longclickMenuView = ((HomeActivity) getActivity()).longClickMenuView;
        workoutsViewModel = ViewModelProviders.of(getActivity()).get(WorkoutsViewModel.class);
        exArray = workoutsViewModel.getWorkoutsList().getValue().get(tag).exArray;
        workout = workoutsViewModel.getWorkoutsList().getValue().get(tag);
        workout.registerObserver((listModifier) -> {

            listModifier.applyWith(adapter, null, null);

        });

        initViews(view);
    }

    private void initViews(View view) {
        recycler = view.findViewById(R.id.workoutview_list_recycler);
        initAdapter();
        configurateRecycler();

    }

    private void initAdapter() {
        adapter = new MyExpandableAdapter(
                exArray,
                getContext()
        );
        adapter.setUiExerciseClickHandler(this);
        adapter.setLongClickMenuView(longclickMenuView);
    }

    private void configurateRecycler() {
        lm = new MyExpandableLinearLayoutManager(getContext());
        recycler.setLayoutManager(lm);
        recycler.setAdapter(adapter);
        adapter.setOnDragListener(this);
        adapter.setScrollListener(this);
        //   adapter.setHelper(layoutManagerHelper);
        ItemTouchHelper.Callback callback = new DragAndSwipeCallback(adapter);
        touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recycler);
    }


   /* public MyExpandableAdapter getAdapter() {
        return adapter;
    }*/


    @Override
    public void scrollToPosition(int position, boolean enableScroll, boolean lastVisible) {
        if (lastVisible) {
            ExerciseProfile ep = (ExerciseProfile) exArray.get(position);
            if (position == lm.findLastCompletelyVisibleItemPosition()) {
                lm.scrollToPosition(position + ep.getSets().size());
            }
        } else if (enableScroll) {
            lm.setScrollEnabled(true);
            if (position != -1) {
                lm.scrollToPosition(position);
            }
        } else {
            lm.scrollToPositionWithOffset(position, 0);
            lm.setScrollEnabled(false);
        }
    }

    @Override
    public void startDrag(RecyclerView.ViewHolder viewHolder) {
        touchHelper.startDrag(viewHolder);
        if (onUpdateLayoutStatsListener != null) {
                /*LayoutManager.UpdateComponents uc = new LayoutManager.UpdateComponents(LayoutManager.SWAP);
                onUpdateLayoutStatsListener.updateLayout(uc);*/
        }

    }


    @Override
    public void onLMDialogOkPressed(int viewHolderPosition) {

    }

    @Override
    public void onLMDialogOkPressed(String input, int position) {

    }

    @Override
    public void onExerciseEdit(int position, ExerciseProfile exerciseProfile) {
        selectedExerciseViewModel = ViewModelProviders
                .of(getActivity())
                .get(String.valueOf(tag), SelectedExerciseViewModel.class);
        selectedExerciseViewModel.setSelectedExercisePosition(position);
        selectedExerciseViewModel.select((ExerciseProfile) exerciseProfile);
        selectedExerciseViewModel.setParentWorkout(workout);
       /* selectedExerciseViewModel.getSelectedExercise().observe(this, (ep) -> {
            int pos = exArray.indexOf(ep);
            if(pos == -1){
                adapter.notifyItemChanged(position);

            }else{
                adapter.notifyItemChanged(pos);

            }
        });*/

        workout.registerExerciseObserver((ep, pos) -> {
            adapter.notifyItemChanged(pos);
        });
        ExerciseEditFragment f = ExerciseEditFragment.newInstance(String.valueOf(tag));
        addFragmentChild(getFragmentManager(), f);
    }

    /**
     * this is the callback that upon clicking on exercise container
     * it transists to the ExerciseDetailsFragment/Activity
     */
    @Override
    public void onExerciseDetails(ExerciseViewHolder vh, ExerciseProfile exerciseProfile) {
        // final int selectedPosition = workout.exArray.indexOf(exerciseProfile);

        //this is an object that has observer that applies changes to this fragment's list
        workout.registerExerciseObserver((changedEp, pos) -> {
            //  adapter.notifyItemChanged(selectedExerciseViewModel.getSelectedExercisePosition());

            adapter.notifyItemChanged(pos);
        });


        /**
         * selectedExerciseViewModel provides the ExerciseProfile for the fragment
         * */
        selectedExerciseViewModel = ViewModelProviders.of(getActivity()).get(String.valueOf(tag), SelectedExerciseViewModel.class);
        selectedExerciseViewModel.select(exerciseProfile);
        selectedExerciseViewModel.setParentWorkout(workout);

        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        Fragment nextFragment = ExerciseDetailsFragment.getInstance(String.valueOf(tag), vh.getAdapterPosition());
        //Fragment previousFragment = getSupportFragmentManager().getFragments().get(tag);
        // 1. Exit for Previous Fragment
        Fade exitFade = new Fade();
        exitFade.setDuration(200);
        this.setExitTransition(exitFade);

        // 2. Shared Elements Transition
        TransitionSet enterTransitionSet = new TransitionSet();
        enterTransitionSet.addTransition(TransitionInflater.from(getContext()).inflateTransition(android.R.transition.move));
        enterTransitionSet.setDuration(200);
        enterTransitionSet.setStartDelay(200);
        nextFragment.setSharedElementEnterTransition(enterTransitionSet);

        // 3. Enter Transition for New Fragment
        Fade enterFade = new Fade();
        enterFade.setStartDelay(200);
        enterFade.setDuration(200);
        nextFragment.setEnterTransition(enterFade);

        /**
         * icon is the imageview for the shared element
         * */

        vh.icon.setTransitionName("q");
        fragmentTransaction.addSharedElement(vh.icon, "q");
        fragmentTransaction.replace(R.id.activity_home_framelayout, nextFragment);
        fragmentTransaction.addToBackStack(String.valueOf(tag));
        fragmentTransaction.commitAllowingStateLoss();

        //((HomeActivity) getActivity()).navigateToExerciseDetailsActivity(exerciseProfile, vh);

    }

    @Override
    public void onExerciseStartDrag(RecyclerView.ViewHolder vh) {
        touchHelper.startDrag(vh);
    }

    @Override
    public void onLongClick(PLObject plObject, MyExpandableAdapter.MyExpandableViewHolder vh) {
        workoutViewFragmentListener.onLongClick(plObject, vh);
    }

    @Override
    public void onAddSuperset(RecyclerView.ViewHolder vh, PLObject.ExerciseProfile exerciseProfile) {
        int position = vh.getAdapterPosition();
        ExerciseItemAdapter exerciseItemAdapter = new ExerciseItemAdapter();
        exerciseItemAdapter.onChild(exerciseProfile);
        adapter.notifyItemChanged(position);

    }

    @Override
    public void onRemoveSuperset(ExerciseProfile exerciseProfile) {
        adapter.notifyItemChanged(exArray.indexOf(exerciseProfile));
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            workoutViewFragmentListener = (WorkoutViewFragmentListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(e.toString());
        }
    }

    @Override
    public void onDetach() {

        super.onDetach();

    }

    public interface WorkoutViewFragmentListener {

        void onLongClick(PLObject plObject, MyExpandableAdapter.MyExpandableViewHolder vh);

        void onProgramToolsClick();
    }

}