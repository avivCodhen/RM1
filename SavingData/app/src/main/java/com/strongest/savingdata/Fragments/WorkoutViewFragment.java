package com.strongest.savingdata.Fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.transition.Explode;
import android.support.transition.Transition;
import android.support.transition.TransitionSet;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.strongest.savingdata.AModels.AlgorithmLayout.Workout;
import com.strongest.savingdata.AModels.AlgorithmLayout.WorkoutsModel;
import com.strongest.savingdata.AViewModels.SelectedExerciseViewModel;
import com.strongest.savingdata.AViewModels.WorkoutsViewModel;
import com.strongest.savingdata.AViewModels.WorkoutsViewModelFactory;
import com.strongest.savingdata.Activities.HomeActivity;
import com.strongest.savingdata.Adapters.MyExpandableAdapter;
import com.strongest.savingdata.Adapters.WorkoutAdapter.DragAndSwipeCallback;
import com.strongest.savingdata.Adapters.WorkoutAdapter.MyExpandableLinearLayoutManager;
import com.strongest.savingdata.Adapters.WorkoutAdapter.OnExerciseProfileEditClick;
import com.strongest.savingdata.Adapters.WorkoutAdapter.ScrollToPositionListener;
import com.strongest.savingdata.AModels.AlgorithmLayout.OnLayoutManagerDialogPress;
import com.strongest.savingdata.AModels.AlgorithmLayout.PLObject;
import com.strongest.savingdata.AModels.AlgorithmLayout.PLObject.ExerciseProfile;
import com.strongest.savingdata.AModels.AlgorithmLayout.WorkoutLayoutTypes;
import com.strongest.savingdata.Controllers.SingleWorkout.UIWorkoutClickHandler;
import com.strongest.savingdata.Animations.MyJavaAnimator;
import com.strongest.savingdata.MyViews.WorkoutView.Choose.ChooseDialogFragment;
import com.strongest.savingdata.MyViews.WorkoutView.OnEnterExitChooseFragment;
import com.strongest.savingdata.MyViews.WorkoutView.OnExerciseChangeListener;
import com.strongest.savingdata.MyViews.WorkoutView.OnUpdateLayoutStatsListener;
import com.strongest.savingdata.R;

import java.util.ArrayList;

import javax.inject.Inject;


public class WorkoutViewFragment extends BaseFragment implements com.strongest.savingdata.Adapters.WorkoutAdapter.OnDragListener,
        ScrollToPositionListener,
        OnExerciseProfileEditClick, OnExerciseChangeListener,
        OnLayoutManagerDialogPress, UIWorkoutClickHandler {


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
        workoutsViewModel = ViewModelProviders.of(getActivity()).get(WorkoutsViewModel.class);
        exArray = workoutsViewModel.getWorkoutsList().getValue().get(tag).exArray;
        workout = workoutsViewModel.getWorkoutsList().getValue().get(tag);
        workout.registerObserver((listModifier) -> {
            listModifier.applyWith(adapter);
        });
        initViews(view);
    }

    private void initViews(View view) {
        recycler = view.findViewById(R.id.workoutview_list_recycler);
        //DividerItemDecoration itemDecor = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        initAdapter();
        configurateRecycler();

    }

    private void initAdapter() {
        adapter = new MyExpandableAdapter(
                exArray,
                getContext(),
                this,
                this,
                null,
                this
        );
        adapter.setOnExerciseProfileEditClick(this);
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

   /* private void addRow(int position, WorkoutLayoutTypes innerType) {


        if (onUpdateLayoutStatsListener != null) {
            WorkoutsModel.UpdateComponents uc = new WorkoutsModel.UpdateComponents(NEW_EXERCISE);
            uc.setInsertPosition(position);
            uc.setInnerType(innerType);
            exArray = onUpdateLayoutStatsListener.updateLayout(uc);
        }
        adapter.myNotifyItemInserted(exArray, position);
        scrollToPosition(position, true, false);
    }*/

    public void addBodyView(int position, WorkoutsModel.UpdateComponents upd) {
        if (onUpdateLayoutStatsListener != null) {
            upd.setInsertPosition(position);
            exArray = onUpdateLayoutStatsListener.updateLayout(upd);
        }
        adapter.myNotifyItemInserted(exArray, position);
        scrollToPosition(position, true, false);
    }

    public void setOnEnterExitChooseFragment(OnEnterExitChooseFragment onEnterExitChooseFragment) {
        this.onEnterExitChooseFragment = onEnterExitChooseFragment;
    }

    public void setExArray(ArrayList<PLObject> exArray) {
        this.exArray = exArray;
    }


    public void setOnUpdateLayoutStatsListener(OnUpdateLayoutStatsListener onUpdateLayoutStatsListener) {
        this.onUpdateLayoutStatsListener = onUpdateLayoutStatsListener;
    }


    @Override
    public void onDetach() {
        //provides a "fadeout" effect upon destroy(for deleting mainly)
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                MyJavaAnimator.fadeOut(mainView);
            }
        });
        super.onDetach();

    }

    @Override
    public void onEditExerciseClick(boolean enter, RecyclerView.ViewHolder vh, int position, PLObject plObject) {
        if (!enter) {
            // onEnterExitChooseFragment.onExitChooseFragment(vh, true);
            //getFragmentManager().popBackStack();
        } else {

            onEnterExitChooseFragment.onEnterChooseFragment(vh.getAdapterPosition());
        }
    }

    //updates the item that has been changed in the choosedialogfragment
    @Override
    public void onExerciseChange(int position, String change) {
        adapter.notifyItemChanged(position);
    }

    public void deleteTransition() {
        MyJavaAnimator.fadeOut(mainView);
    }


  /*  @Override
    public void duplicateExercise(RecyclerView.ViewHolder vh) {
        int position = vh.getAdapterPosition();
        ExerciseProfile ep = (ExerciseProfile) exArray.get(position);
        ExerciseProfile newCopy = new ExerciseProfile(
                ep.getMuscle(),
                0,
                0,
                0
        );

        //copies the sets
        for (int i = 0; i < ep.getSets().size(); i++) {
            newCopy.getSets().add(new PLObject.SetsPLObject(newCopy, new ExerciseSet(ep.getSets().get(i).getExerciseSet())));
        }

        // copies the intrasets
        for (int i = 0; i < ep.getSets().size(); i++) {
            for (int j = 0; j < ep.getSets().get(i).getIntraSets().size(); j++) {
                PLObject.SetsPLObject copyParentSet = newCopy.getSets().get(i);
                copyParentSet.getIntraSets().add(new PLObject.IntraSetPLObject(
                        newCopy,
                        new ExerciseSet(ep.getSets().get(i).getIntraSets().get(j).getExerciseSet()),
                        WorkoutLayoutTypes.IntraSetNormal,
                        copyParentSet
                ));
            }

        }

        LayoutManager.UpdateComponents updateComponents = new LayoutManager.UpdateComponents(NEW_EXERCISE);
        int toPosition = position;
        if (ep.isExpand()) {
            toPosition += LayoutManagerHelper.calcBlockLength(ep);
        } else {
            toPosition += (ep.getExerciseProfiles().size());
        }
        updateComponents.setToPosition(toPosition);
        updateComponents.setPlObject(newCopy);
        updateComponents.setLayout(exArray);
        newCopy.setExercise(ep.getExercise());
        newCopy.setInnerType(ep.getInnerType());
        exArray = onUpdateLayoutStatsListener.updateLayout(updateComponents);
        adapter.setExArray(exArray);
        adapter.notifyItemInserted(toPosition);
    }*/

  /*  @Override
    public void duplicateItem(RecyclerView.ViewHolder vh, WorkoutLayoutTypes type) {
        switch (type) {
            case SetsPLObject:
                onSetsDoubleClick(vh);
                break;
            case ExerciseProfile:
                duplicateExercise(vh);
                break;
            case IntraSetNormal:
                onAddNormalIntraSet(vh);
                break;
        }

    }*/


    @Override
    public void onLMDialogOkPressed(int viewHolderPosition) {

    }

    @Override
    public void onLMDialogOkPressed(String input, int position) {

    }

    @Override
    public void onExerciseEdit(RecyclerView.ViewHolder vh, PLObject plObject, int position) {
        int oldPosition = lm.findFirstVisibleItemPosition();

        //TODO: create a viewmodel for the details fragment
        //TODO: call the details fragment


        ChooseDialogFragment f = ChooseDialogFragment.getInstance(this, oldPosition, position, plObject);
        addFragmentChild(getFragmentManager(), f);

        Rect rect = new Rect();
        vh.itemView.getGlobalVisibleRect(rect);
        rect.top = rect.bottom;
        Explode explode = new Explode();
        explode.setEpicenterCallback(new Transition.EpicenterCallback() {
            @Override
            public Rect onGetEpicenter(@NonNull Transition transition) {
                return rect;
            }
        });
        TransitionSet set = new TransitionSet();
        set.setPropagation(null);
        set.addTransition(explode);

        android.support.transition.TransitionManager.beginDelayedTransition(recycler, set);
        recycler.setAdapter(null);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                recycler.setAdapter(adapter);
            }
        }, 1000);
        //hides the item for extra transition animation
        //new Handler().postDelayed(() -> vh.itemView.setAlpha(0f), 200);
    }

    @Override
    public void onExerciseDetails(ExerciseProfile exerciseProfile) {
        SelectedExerciseViewModel selectedExerciseViewModel = ViewModelProviders
                .of(getActivity())
                .get(SelectedExerciseViewModel.class);
        selectedExerciseViewModel.select(exerciseProfile);
        ((HomeActivity) getActivity()).addFragmentToHomeActivity(new ExerciseDetailsFragment(),"details");

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
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            workoutViewFragmentListener = (WorkoutViewFragmentListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(e.toString());
        }
    }

    public interface WorkoutViewFragmentListener {

        void onLongClick(PLObject plObject, MyExpandableAdapter.MyExpandableViewHolder vh);

        void onProgramToolsClick();
    }

}