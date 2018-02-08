package com.strongest.savingdata.createProgramFragments.Unused;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.strongest.savingdata.MyViews.WorkoutView.OnAddOrDeleteListener;
import com.strongest.savingdata.R;

public class AddOrDeleteFragment extends DialogFragment implements View.OnClickListener {

    private int pos;
    public static final String ARGS = "args";
    public static final String POS = "pos";
    private OnAddOrDeleteListener addOrDeleteListener;

    public AddOrDeleteFragment() {
    }


    public static AddOrDeleteFragment newInstance(int i) {

        AddOrDeleteFragment fragment = new AddOrDeleteFragment();
        Bundle args = new Bundle();
        args.putInt(POS, i);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            pos = getArguments().getInt(POS);
        }else{

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Drawable d = new ColorDrawable(Color.BLACK);
        d.setAlpha(100);

        getDialog().getWindow().setBackgroundDrawable(d);
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();

        wlp.gravity = Gravity.RIGHT;
        wlp.height = WindowManager.LayoutParams.MATCH_PARENT;
        wlp.width = WindowManager.LayoutParams.MATCH_PARENT/4;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);

        return inflater.inflate(R.layout.fragment_interact_dialog, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    private void initView(View view) {
        view.findViewById(R.id.interact_remove_btn).setOnClickListener(this);
        view.findViewById(R.id.interact_add_btn).setOnClickListener(this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onClick(View v) {
        int arg = -1;
        switch(v.getId()){
            case R.id.interact_remove_btn:
                arg = 1;
                break;
            case R.id.interact_add_btn:
                arg = 0;
                break;
        }
        pos = getArguments().getInt(POS);
     /*   Intent i = new Intent();
       i.putExtra(ARGS, arg);
        i.putExtra(POS, pos);*/
        addOrDeleteListener.addOrDeleteResults(arg, pos);
        //getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, i);
        dismiss();
    }

    public void setAddOrDeleteListener(OnAddOrDeleteListener addOrDeleteListener) {
        this.addOrDeleteListener = addOrDeleteListener;
    }
}
