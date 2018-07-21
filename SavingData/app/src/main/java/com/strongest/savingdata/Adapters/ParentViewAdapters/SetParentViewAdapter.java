package com.strongest.savingdata.Adapters.ParentViewAdapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.strongest.savingdata.AModels.AlgorithmLayout.PLObject;
import com.strongest.savingdata.Controllers.UISetsClickHandler;
import com.strongest.savingdata.R;
import com.strongest.savingdata.ViewHolders.IntraSetViewHolder;

public class SetParentViewAdapter extends ParentView.Adapter<IntraSetViewHolder> {

    private PLObject.SetsPLObject setsPLObject;
    private UISetsClickHandler uiSetsClickHandler;

    public SetParentViewAdapter(PLObject.SetsPLObject setsPLObject, UISetsClickHandler uiSetsClickHandler){
        this.setsPLObject = setsPLObject;
        this.uiSetsClickHandler = uiSetsClickHandler;
    }

    @Override
    public IntraSetViewHolder onCreateChildViewHolder(ViewGroup container) {
        LayoutInflater inflater = LayoutInflater.from(container.getContext());
        View v = inflater.inflate(R.layout.recycler_view_intra_set,container, false);
         return new IntraSetViewHolder(v);
    }

    @Override
    public int getCount() {
        return setsPLObject.intraSets.size();
    }

    @Override
    public void onBindViewHolder(IntraSetViewHolder intraSetViewHolder, int position) {

    }

}