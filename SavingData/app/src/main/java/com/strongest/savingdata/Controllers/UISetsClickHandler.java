package com.strongest.savingdata.Controllers;

import com.strongest.savingdata.AModels.workoutModel.PLObject;
import com.strongest.savingdata.Adapters.MyExpandableAdapter;

public interface UISetsClickHandler {

    void onSetsClick(MyExpandableAdapter.MyExpandableViewHolder vh, PLObject plObject);
    void onSetsLongClick(PLObject plObject, MyExpandableAdapter.MyExpandableViewHolder vh);
    void onAddingIntraSet(PLObject.SetsPLObject setsPLObject, int position);
    void onRemoveIntraSet(PLObject.SetsPLObject setsPLObject, int position);
    void onDuplicate(PLObject.SetsPLObject setsPLObject);

}
