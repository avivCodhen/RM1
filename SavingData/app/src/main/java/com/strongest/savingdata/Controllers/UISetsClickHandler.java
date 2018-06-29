package com.strongest.savingdata.Controllers;

import com.strongest.savingdata.AModels.AlgorithmLayout.PLObject;
import com.strongest.savingdata.Adapters.MyExpandableAdapter;

public interface UISetsClickHandler {

    void onSetsClick(PLObject plObject);
    void onSetsLongClick(PLObject plObject, MyExpandableAdapter.MyExpandableViewHolder vh);
}
