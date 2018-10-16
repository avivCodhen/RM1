package com.strongest.savingdata.Fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.strongest.savingdata.AModels.programModel.Program;
import com.strongest.savingdata.AModels.workoutModel.Workout;
import com.strongest.savingdata.AViewModels.WorkoutsViewModel;
import com.strongest.savingdata.Activities.HomeActivity;
import com.strongest.savingdata.Adapters.MainAdapter;
import com.strongest.savingdata.Adapters.WorkoutAdapter.DragAndSwipeCallback;
import com.strongest.savingdata.Adapters.WorkoutAdapter.OnDragListener;
import com.strongest.savingdata.AModels.workoutModel.WorkoutsModel;
import com.strongest.savingdata.AModels.workoutModel.OnLayoutManagerDialogPress;
import com.strongest.savingdata.Handlers.MaterialDialogHandler;
import com.strongest.savingdata.MyViews.SaveExitToolBar;
import com.strongest.savingdata.MyViews.WorkoutView.OnProgramToolsActionListener;
import com.strongest.savingdata.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Cohen on 3/9/2018.
 */

public class ProgramSettingsFragment extends BaseFragment implements OnDragListener,
        OnLayoutManagerDialogPress, MainAdapter.OnProgramSettingsChange {

    private EditText titleTV;
    private String titleText;
    private TextWatcher textWatcher = null;
    private RecyclerView mRecyclerView;
    ArrayList<Workout> list;
    private WorkoutsViewModel workoutViewModel;
    private ItemTouchHelper itemTouchHelper;
    private OnProgramToolsActionListener onProgramToolsActionListener;
    Program p;

    @BindView(R.id.program_settings_toolbar)
    SaveExitToolBar toolBar;
    MainAdapter adapter;

    @BindView(R.id.newWorkoutFab)
    FloatingActionButton addWorkoutFab;


    public static ProgramSettingsFragment getInstance(OnProgramToolsActionListener onProgramToolsActionListener) {
        ProgramSettingsFragment f = new ProgramSettingsFragment();
        f.setOnProgramToolsActionListener(onProgramToolsActionListener);
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_program_settings, container, false);
        ButterKnife.bind(this, v);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        workoutViewModel = ViewModelProviders.of(getActivity()).get(WorkoutsViewModel.class);
        list = workoutViewModel.getWorkoutsList().getValue();
        initViews(view);

    }

    private void initViews(View v) {
        p = ((HomeActivity) getActivity()).program;
        titleTV = v.findViewById(R.id.program_settings_program_name_ED);
        titleTV.setText(p.getProgramName());
        toolBar.instantiate()
                .setOptionalText("Advanced Program Settings")
                .setSaveButton(view -> {
                    getFragmentManager().popBackStack();
                    closeKeyBoard();
                })
                .showCancel(false)
                .showBack(true);

        mRecyclerView = (RecyclerView) v.findViewById(R.id.program_settings_recyclerview);
        RecyclerView.LayoutManager lm = new LinearLayoutManager(getContext());

        adapter = new MainAdapter(getContext(), list, this, null);
        adapter.setOnProgramChangeListener(this);
        ItemTouchHelper.Callback callback = new DragAndSwipeCallback(adapter);
        itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);

        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        mRecyclerView.setLayoutManager(lm);
        mRecyclerView.setAdapter(adapter);

        titleTV.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE) {
                    titleTV.setText(textView.getText().toString());
                    p.setProgramName(textView.getText().toString());
                    ((HomeActivity) getActivity()).programViewModel.updateProgram(p);
                    closeKeyBoard();
                    return true;
                }
                return false;
            }
        });

        addWorkoutFab.setOnClickListener(fab -> {
            Workout w = new Workout();
            w.setWorkoutName("Workout " + (list.size() + 1));
            list.add(w);
            adapter.notifyItemInserted(list.size() - 1);
            notifyhomeActivityAdapter();
        });
    }

    @Override

    public void startDrag(RecyclerView.ViewHolder viewHolder) {
        itemTouchHelper.startDrag(viewHolder);
    }

    public void setOnProgramToolsActionListener(OnProgramToolsActionListener
                                                        onProgramToolsActionListener) {
        this.onProgramToolsActionListener = onProgramToolsActionListener;
    }

    @Override
    public void onLMDialogOkPressed(int viewHolderPosition) {

    }

    @Override
    public void onLMDialogOkPressed(String input, int position) {
        titleTV.setText(input);

        //TODO: fix this
        //workoutViewModel.getProgram().getValue().setProgramName(input);
       /* ((HomeActivity) getActivity()).programmer.getProgram().programName = titleTV.getText().toString();
        ((HomeActivity) getActivity()).dataManager.getProgramDataManager().updateProgramName(input,
               layoutManager.getDbName() );
*/
    }

    @Override
    public void notifyAdapter() {
        closeKeyBoard();
        notifyhomeActivityAdapter();
        adapter.notifyDataSetChanged();

    }

    private void notifyhomeActivityAdapter() {
        ((HomeActivity) getActivity()).mAdapter.notifyDataSetChanged();

    }


}
