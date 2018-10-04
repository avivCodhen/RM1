package com.strongest.savingdata.ViewHolders;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.strongest.savingdata.Adapters.MyExpandableAdapter;
import com.strongest.savingdata.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IntraSetViewHolder extends MyExpandableAdapter.MyExpandableViewHolder {


    @BindView(R.id.recycler_view_exercise_details_sets_tv)
    public ImageView intraSetTag;
    @BindView(R.id.exerciseView_repsTV)
    public TextView reps;

    @BindView(R.id.exerciseView_weightTV)
    public TextView weight;

    @BindView(R.id.superset_tv)
    public TextView supersetTv;

    @BindView(R.id.sets_delete)
    public ImageView deleteIV;

    @BindView(R.id.exerciseView_restTV)
    public TextView rest;

    @BindView(R.id.intra_sets_main_layout)
    public ViewGroup mainLayout;

    @BindView(R.id.recycler_view_set_card)
    public ViewGroup card;

    @BindView(R.id.edit_layout)
    public ViewGroup editContainer;

    @BindView(R.id.edit_container_tv)
    public TextView editContainerTV;

    @BindView(R.id.reps_rest_weight_layout)
    public ViewGroup dataLayout;

    @BindView(R.id.superset_tv_tag)
    public TextView supersetTag;

    public IntraSetViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    @Override
    public View getMainLayout() {
        return mainLayout;
    }

    @Override
    public void onItemSelected() {

    }

    @Override
    public void onItemClear() {

    }
}
