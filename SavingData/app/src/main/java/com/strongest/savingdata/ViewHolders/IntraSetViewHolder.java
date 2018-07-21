package com.strongest.savingdata.ViewHolders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.strongest.savingdata.Adapters.MyExpandableAdapter;
import com.strongest.savingdata.R;

import butterknife.BindView;

public class IntraSetViewHolder extends MyExpandableAdapter.MyExpandableViewHolder{


    @BindView(R.id.exerciseView_repsTV)
    public TextView reps;

    @BindView(R.id.sets_settings)
    public ImageView edit;

    @BindView(R.id.exerciseView_weightTV)
    public TextView weight;


    @BindView(R.id.exerciseView_restTV)
    public TextView rest;

    public IntraSetViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public View getMainLayout() {
        return null;
    }
}
