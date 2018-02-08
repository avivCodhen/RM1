package com.strongest.savingdata.createProgramFragments.Create;

import android.app.Dialog;
import android.app.DialogFragment;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.strongest.savingdata.R;

/**
 * Created by Cohen on 5/28/2017.
 */

public class CreateDialogFragment extends android.support.v4.app.DialogFragment implements View.OnClickListener {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(0x00000000));
        return inflater.inflate(R.layout.fragment_dialog_create, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        initView(view);
    }

    private void initView(View v) {
        v.findViewById(R.id.fragment_dialog_create_floatingBtn).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        dismiss();
    }
}
