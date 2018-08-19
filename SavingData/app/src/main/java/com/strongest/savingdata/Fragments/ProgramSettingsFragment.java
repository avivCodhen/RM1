package com.strongest.savingdata.Fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.strongest.savingdata.AModels.workoutModel.Workout;
import com.strongest.savingdata.AViewModels.WorkoutsViewModel;
import com.strongest.savingdata.Activities.HomeActivity;
import com.strongest.savingdata.Adapters.MainAdapter;
import com.strongest.savingdata.Adapters.WorkoutAdapter.DragAndSwipeCallback;
import com.strongest.savingdata.Adapters.WorkoutAdapter.OnDragListener;
import com.strongest.savingdata.AModels.workoutModel.WorkoutsModel;
import com.strongest.savingdata.AModels.workoutModel.LayoutManagerAlertdialog;
import com.strongest.savingdata.AModels.workoutModel.OnLayoutManagerDialogPress;
import com.strongest.savingdata.MyViews.WorkoutView.OnProgramToolsActionListener;
import com.strongest.savingdata.R;

import java.util.ArrayList;

/**
 * Created by Cohen on 3/9/2018.
 */

public class ProgramSettingsFragment extends BaseFragment implements OnDragListener,
        OnLayoutManagerDialogPress {

    private EditText editText;
    private String titleText;
    private TextWatcher textWatcher = null;
    private RecyclerView mRecyclerView;
    ArrayList<Workout> list;
    private WorkoutsViewModel workoutViewModel;
    private ItemTouchHelper itemTouchHelper;
    private TextView toolbarTitle;
    private ImageView toolbarIv;
    private WorkoutsModel workoutsModel;
    private OnProgramToolsActionListener onProgramToolsActionListener;
    private OnProgramSettingsChange onProgramSettingChange;


    public static ProgramSettingsFragment getInstance(OnProgramToolsActionListener onProgramToolsActionListener){
        ProgramSettingsFragment f = new ProgramSettingsFragment();
        f.setOnProgramToolsActionListener(onProgramToolsActionListener);
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_program_settings, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        workoutViewModel = ViewModelProviders.of(getActivity()).get(WorkoutsViewModel.class);
        list = workoutViewModel.getWorkoutsList().getValue();
        initViews(view);

    }

    private void initViews(View v) {
        ( (TextView)v.findViewById(R.id.tool_bar_title)).setText("Advanced Program Settings");

        mRecyclerView = (RecyclerView) v.findViewById(R.id.program_settings_recyclerview);
        RecyclerView.LayoutManager lm = new LinearLayoutManager(getContext());

        MainAdapter adapter = new MainAdapter(getContext(),list, this, null);
        adapter.setOnProgramChangeListener(onProgramSettingChange);
        ItemTouchHelper.Callback callback = new DragAndSwipeCallback(adapter);
        itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);


        mRecyclerView.setLayoutManager(lm);
        mRecyclerView.setAdapter(adapter);

        toolbarIv = (ImageView) v.findViewById(R.id.toolbar_back);
        toolbarIv.setOnClickListener((view)-> getFragmentManager().popBackStack());
        toolbarTitle = (TextView) v.findViewById(R.id.tool_bar_title);
        editText = (EditText) v.findViewById(R.id.program_settings_program_name_ED);
        textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ((HomeActivity) getActivity()).getSupportActionBar().setTitle(editText.getText());
                toolbarTitle.setText(editText.getText());
            }

            @Override
            public void afterTextChanged(Editable s) {
                editText.setSelection(s.length());
                toolbarTitle.setText(editText.getText());

                //TODO: fix this
                //  ((HomeActivity) getActivity()).programmer.getProgram().programName = editText.getText().toString();

            }
        };

        //TODO: fix this
        //titleText = workoutViewModel.getProgram().getValue().getProgramName();
        editText.setText(titleText);
        editText.addTextChangedListener(textWatcher);
        v.findViewById(R.id.program_title_edit_iv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutManagerAlertdialog.getInputAlertDialog(getContext(), ProgramSettingsFragment.this, editText.getText().toString(), -1);
            }
        });
    }

    @Override

    public void startDrag(RecyclerView.ViewHolder viewHolder) {
        itemTouchHelper.startDrag(viewHolder);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        onProgramSettingChange = (OnProgramSettingsChange) context;

    }
    public void setOnProgramToolsActionListener(OnProgramToolsActionListener onProgramToolsActionListener) {
        this.onProgramToolsActionListener = onProgramToolsActionListener;
    }

    @Override
    public void onLMDialogOkPressed(int viewHolderPosition) {

    }

    @Override
    public void onLMDialogOkPressed(String input, int position) {
        editText.setText(input);
        toolbarTitle.setText(editText.getText());

        //TODO: fix this
        //workoutViewModel.getProgram().getValue().setProgramName(input);
       /* ((HomeActivity) getActivity()).programmer.getProgram().programName = editText.getText().toString();
        ((HomeActivity) getActivity()).dataManager.getProgramDataManager().updateProgramName(input,
               layoutManager.getDbName() );
*/
    }


    public interface OnProgramSettingsChange{
        void notifyAdapter();
    }
}
