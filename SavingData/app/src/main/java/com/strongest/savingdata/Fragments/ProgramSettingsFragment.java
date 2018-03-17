package com.strongest.savingdata.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.strongest.savingdata.Activities.HomeActivity;
import com.strongest.savingdata.R;
import com.strongest.savingdata.createProgramFragments.CreateProgram.BaseCreateProgramFragment;

/**
 * Created by Cohen on 3/9/2018.
 */

public class ProgramSettingsFragment extends BaseCreateProgramFragment {

    private EditText editText;
    private String titleText;
    private TextWatcher textWatcher = null;

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
        editText = (EditText) v.findViewById(R.id.program_settings_program_name_ED);
        textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ((HomeActivity) getActivity()).getSupportActionBar().setTitle(editText.getText());
            }

            @Override
            public void afterTextChanged(Editable s) {
                editText.setSelection(s.length());

            }
        };
        titleText = getArguments().getString("program_name");
        editText.setText(titleText);
        editText.addTextChangedListener(textWatcher);
        ((HomeActivity) getActivity()).programmer.getProgram().programName = editText.getText().toString();
    }
}
