package com.strongest.savingdata.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

import com.strongest.savingdata.Activities.BaseActivity;
import com.strongest.savingdata.Activities.HomeActivity;
import com.strongest.savingdata.Adapters.MainAdapter;
import com.strongest.savingdata.Adapters.WorkoutAdapter.DragAndSwipeCallback;
import com.strongest.savingdata.Adapters.WorkoutAdapter.OnDragListener;
import com.strongest.savingdata.AlgorithmLayout.LayoutManager;
import com.strongest.savingdata.AlgorithmLayout.LayoutManagerAlertdialog;
import com.strongest.savingdata.AlgorithmLayout.OnLayoutManagerDialogPress;
import com.strongest.savingdata.AlgorithmLayout.PLObject;
import com.strongest.savingdata.MyViews.WorkoutView.OnProgramToolsActionListener;
import com.strongest.savingdata.R;
import com.strongest.savingdata.createProgramFragments.CreateProgram.BaseCreateProgramFragment;

import java.util.ArrayList;

/**
 * Created by Cohen on 3/9/2018.
 */

public class ProgramSettingsFragment extends BaseCreateProgramFragment implements OnDragListener,
        OnLayoutManagerDialogPress, OnProgramSettingChange {

    private EditText editText;
    private String titleText;
    private TextWatcher textWatcher = null;
    private RecyclerView mRecyclerView;
    ArrayList<PLObject> list = new ArrayList<>();
    private ItemTouchHelper itemTouchHelper;
    private TextView toolbarTitle;
    private ImageView toolbarIv;
    private LayoutManager layoutManager;
    private OnProgramToolsActionListener onProgramToolsActionListener;


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
        initViews(view);
        getActionBar().show();
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initViews(View v) {
        layoutManager = ((BaseActivity) getActivity()).getProgrammer().layoutManager;
        mRecyclerView = (RecyclerView) v.findViewById(R.id.program_settings_recyclerview);
        RecyclerView.LayoutManager lm = new LinearLayoutManager(getContext());

        for (int i = 0; i < layoutManager.getWorkouts().size(); i++) {
            list.add(layoutManager.getWorkouts().get(i));
        }
        MainAdapter adapter = new MainAdapter(getContext(),list, this, null);
        adapter.setOnProgramChangeListener(this);
        ItemTouchHelper.Callback callback = new DragAndSwipeCallback(adapter);
        itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);


        mRecyclerView.setLayoutManager(lm);
        mRecyclerView.setAdapter(adapter);

        toolbarIv = (ImageView) v.findViewById(R.id.toolbar_back);
        toolbarIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  onProgramToolsActionListener.onProgramToolsAction("notify");
                getFragmentManager().popBackStack();
            }
        });
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
                ((HomeActivity) getActivity()).programmer.getProgram().programName = editText.getText().toString();

            }
        };

        titleText = ((BaseActivity) getActivity()).getProgrammer().getProgram().programName;
        editText.setText(titleText);
        editText.addTextChangedListener(textWatcher);
        v.findViewById(R.id.program_title_edit_iv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutManagerAlertdialog.getInputAlertDialog(getContext(), ProgramSettingsFragment.this, editText.getText().toString());
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
    }

    @Override
    public void onDelete(int position) {
        LayoutManager.UpdateComponents updateComponents = new LayoutManager.UpdateComponents(LayoutManager.DELETE_WORKOUT);
        updateComponents.setWorkoutPosition(position);
        layoutManager.updateLayout(updateComponents);
        onProgramToolsActionListener.onProgramToolsAction("notify", null);
    }

    @Override
    public void onSwap(int fromPosition, int toPosition) {
        LayoutManager.UpdateComponents updateComponents = new LayoutManager.UpdateComponents(LayoutManager.SWAP_WORKOUTS);
        updateComponents.setFromPosition(fromPosition);
        updateComponents.setToPosition(toPosition);
        layoutManager.updateLayout(updateComponents);
        onProgramToolsActionListener.onProgramToolsAction("notify", updateComponents);

    }


    @Override
    public void onEdit(int position) {
        onProgramToolsActionListener.onProgramToolsAction("notify", null);
    }

    public void setOnProgramToolsActionListener(OnProgramToolsActionListener onProgramToolsActionListener) {
        this.onProgramToolsActionListener = onProgramToolsActionListener;
    }

    @Override
    public void onLMDialogOkPressed(int viewHolderPosition) {

    }

    @Override
    public void onLMDialogOkPressed(String input) {
        editText.setText(input);
        toolbarTitle.setText(editText.getText());
        ((HomeActivity) getActivity()).programmer.getProgram().programName = editText.getText().toString();
        ((HomeActivity) getActivity()).dataManager.getProgramDataManager().updateProgramName(input,
               layoutManager.getDbName() );

    }
}
