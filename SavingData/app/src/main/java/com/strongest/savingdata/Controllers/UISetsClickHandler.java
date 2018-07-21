package com.strongest.savingdata.Controllers;

import com.strongest.savingdata.AModels.AlgorithmLayout.PLObject;
import com.strongest.savingdata.Adapters.MyExpandableAdapter;

public interface UISetsClickHandler {

    void onSetsClick(MyExpandableAdapter.SetsViewHolder vh, PLObject plObject);
    void onSetsLongClick(PLObject plObject, MyExpandableAdapter.MyExpandableViewHolder vh);
    void onAddingIntraSet(PLObject.SetsPLObject setsPLObject, int position);
    void onRemoveIntraSet(PLObject.SetsPLObject setsPLObject);

}
