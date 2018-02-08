package com.strongest.savingdata.MyViews.CreateCustomBeansView;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.strongest.savingdata.Database.Exercise.Beans;
import com.strongest.savingdata.R;

/**
 * Created by Cohen on 1/18/2018.
 */

public class RangeNumberChooseView extends LinearLayout implements NumberChoose, OnRangeNumberChooseControl{

    private Context context;

    private SingleNumberChooseView lowerNumView, higherNumView;
    private NumberChooseManager mNumberChooseManager;

    public RangeNumberChooseView(Context context) {
        super(context);
        this.context = context;
    }

    public RangeNumberChooseView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public void initViews() {
        mNumberChooseManager.setOnRangeNumberChooseControl(this);
        lowerNumView = (SingleNumberChooseView) findViewById(R.id.lower_number_choose_view);
        lowerNumView.setUpWithNumberChooseManager(mNumberChooseManager);
        higherNumView = (SingleNumberChooseView) findViewById(R.id.higher_number_choose_view);
        higherNumView.setUpWithNumberChooseManager(mNumberChooseManager);
        higherNumView.setText("10");
    }

    @Override
    public boolean isLowerSmaller(SingleNumberChooseView view, boolean action) {
        switch (view.getId()){
            case R.id.lower_number_choose_view:
                if(action){
                    if((lowerNumView.getIntNum() - higherNumView.getIntNum()) == -1){
                        return true;
                    }
                }
                break;
            case R.id.higher_number_choose_view:
                if(!action){
                    if((higherNumView.getIntNum() - lowerNumView.getIntNum()) == +1){
                        return true;
                    }
                }
                break;
        }
        return false;
    }

    @Override
    public void setUpWithNumberChooseManager(NumberChooseManager numberChooseManager) {
        this.mNumberChooseManager = numberChooseManager;
        inflate(context, R.layout.choose_number_range, this);
        initViews();
    }

    @Override
    public Beans createRepBeans() {
        String range = lowerNumView.getText()+"-"+higherNumView.getText();
        return mNumberChooseManager.createBeans(this, NumberChooseManager.Type.RangeRep, range);
    }

    @Override
    public View getView( ) {
        return this;
    }

    @Override
    public boolean hasManager() {
        return mNumberChooseManager != null;
    }
}
