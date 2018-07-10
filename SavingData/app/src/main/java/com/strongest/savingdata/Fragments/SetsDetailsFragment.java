package com.strongest.savingdata.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import com.strongest.savingdata.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SetsDetailsFragment extends BaseFragment {


    @BindView(R.id.reps)
    TextView reps;
    @BindView(R.id.rest)
    TextView rest;
    @BindView(R.id.weight)
    TextView weight;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        postponeEnterTransition();
        View v = inflater.inflate(R.layout.fragment_sets_detail, container, false);
        ButterKnife.bind(this, v);
        v.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                startPostponedEnterTransition();
                return true;
            }
        });
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        reps.setText("8");
    }
}
