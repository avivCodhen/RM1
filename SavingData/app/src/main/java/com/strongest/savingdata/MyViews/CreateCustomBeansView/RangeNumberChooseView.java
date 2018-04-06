package com.strongest.savingdata.MyViews.CreateCustomBeansView;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import com.strongest.savingdata.Database.Exercise.Beans;
import com.strongest.savingdata.R;

/**
 * Created by Cohen on 1/18/2018.
 */

public class RangeNumberChooseView extends LinearLayout implements CompoundButton.OnCheckedChangeListener,
        OnNumberInject {

    private Context context;

    private SingleNumberChooseView lowerNumView, higherNumView;
    private NumberChooseManager mNumberChooseManager;
    private CheckBox checkBox;
    public String rep;

    public SingleNumberChooseView getLowerNumView() {
        return lowerNumView;
    }

    public SingleNumberChooseView getHigherNumView() {
        return higherNumView;
    }

    public CheckBox getCheckBox() {
        return checkBox;
    }

    public RangeNumberChooseView(Context context) {
        super(context);
        this.context = context;
    }

    public RangeNumberChooseView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public void initViews() {
        checkBox = (CheckBox) findViewById(R.id.choose_number_range_checkbox);
        checkBox.setOnCheckedChangeListener(this);
        //mNumberChooseManager.setOnRangeNumberChooseControl(this);
        lowerNumView = (SingleNumberChooseView) findViewById(R.id.lower_number_choose_view);
        lowerNumView.setUpWithNumberChooseManager(mNumberChooseManager, this);
        higherNumView = (SingleNumberChooseView) findViewById(R.id.higher_number_choose_view);
        higherNumView.setUpWithNumberChooseManager(mNumberChooseManager, this);
        higherNumView.setText("10");
        higherNumView.setEnabled(false);
        String repetition = mNumberChooseManager.getExerciseSet().getRep();
        initRep(repetition);


    }

    public void initRep(String repetition){
        if(repetition.contains("-")){
            String[] reps = repetition.split("-");
            lowerNumView.setText(reps[0]);
            higherNumView.setText(reps[1]);
            rep = lowerNumView.getText() + "-"+higherNumView.getText();
            checkBox.setChecked(true);
        }else{
            lowerNumView.setText(repetition);
            rep = lowerNumView.getText();
        }
    }

   /* @Override
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
*/


    public void setUpWithNumberChooseManager(NumberChooseManager numberChooseManager) {
        this.mNumberChooseManager = numberChooseManager;
        inflate(context, R.layout.choose_number_range, this);
        initViews();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            higherNumView.setEnabled(true);
            rep = String.valueOf(String.valueOf(lowerNumView.getText() + "-" + higherNumView.getText()));
            mNumberChooseManager.injectRep(rep);
        } else {
            higherNumView.setEnabled(false);
            rep = String.valueOf(lowerNumView.getText());
            mNumberChooseManager.injectRep(rep);
        }

    }

    @Override
    public void onInject(String num) {
        if (checkBox.isChecked()) {
            rep = String.valueOf(String.valueOf(lowerNumView.getText() + "-" + higherNumView.getText()));
        } else {
            rep = String.valueOf(lowerNumView.getText());
        }
        mNumberChooseManager.injectRep(rep);
    }
}
