package com.strongest.savingdata.MyViews.CreateCustomBeansView;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.strongest.savingdata.Database.Exercise.Beans;
import com.strongest.savingdata.R;


/**
 * Created by Cohen on 1/18/2018.
 */

public class SingleNumberChooseView extends LinearLayout implements NumberChoose {


    private TextView plusTV, minusTV;
    private TextView numberTV;
    private NumberChooseManager mNumberChooseManager;
    private Context context;
    private int intProgress = 1;
    private double doubleProgress= 1;
    private boolean toDouble;

    public SingleNumberChooseView(Context context) {
        super(context);

        this.context = context;
    }

    public SingleNumberChooseView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        this.context = context;
    }

    private void initViews() {
        plusTV = (TextView) this.findViewById(R.id.simple_number_choose_plus);
        plusTV.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                updateNumber(true);
            }
        });
        numberTV = (TextView) this.findViewById(R.id.simple_number_choose_number);
        numberTV.setText("6");
        minusTV = (TextView) this.findViewById(R.id.simple_number_choose_minus);
        minusTV.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                updateNumber(false);
            }
        });
    }

    public void updateNumber(boolean action) {

        if(toDouble){
            updatedoubleNumber(action);
        }else{
            updateIntNumber(action);
        }

    }

    public void updateIntNumber(boolean action) {
        String number = numberTV.getText().toString();

        int num = Integer.parseInt(number);

        if (mNumberChooseManager.hasOnRangeControlCallBack()) {
            if (mNumberChooseManager.getOnRangeNumberChooseControl().isLowerSmaller(this, action)) {
                return;
            }
        }

        if (action) {

            num+= intProgress;

        } else {


            if (num > 1) {
                num-= intProgress;
            } else {
                return;

            }
        }
        numberTV.setText(String.valueOf(num));

    }

    public void updatedoubleNumber(boolean action) {
        String number = getText();

        double num = getDoubleNum();

        if (mNumberChooseManager.hasOnRangeControlCallBack()) {
            if (mNumberChooseManager.getOnRangeNumberChooseControl().isLowerSmaller(this, action)) {
                return;
            }
        }

        if (action) {

            num+= doubleProgress;

        } else {


            if (num > 1) {
                num-= doubleProgress;
            } else {
                return;

            }
        }
        numberTV.setText(String.valueOf(num));

    }



    public void setText(String number) {
        numberTV.setText(number);
    }

    public String getText() {
        return numberTV.getText().toString();
    }

    public int getIntNum() {
        return Integer.parseInt(getText());
    }
    public double getDoubleNum(){
        return Double.parseDouble(getText());
    }

    @Override
    public void setUpWithNumberChooseManager(NumberChooseManager numberChooseManager) {
        this.mNumberChooseManager = numberChooseManager;
        inflate(context, R.layout.choose_number_single, this);
        initViews();
    }

    @Override
    public Beans createRepBeans() {
        return mNumberChooseManager.createBeans(this, NumberChooseManager.Type.SingleRep, getText());
    }

    @Override
    public View getView() {
        return this;
    }

    @Override
    public boolean hasManager() {
        return mNumberChooseManager != null;
    }

    public void setIntProgress(int intProgress) {
        this.intProgress = intProgress;
    }

    public void setToDouble(boolean toDouble, double progress) {
        this.toDouble = toDouble;
        this.doubleProgress = progress;
    }

    public TextView getNumberTV() {
        return numberTV;
    }

    @Override
    public void setEnabled(boolean enabled) {
        //super.setEnabled(enabled);
        plusTV.setEnabled(enabled);
        minusTV.setEnabled(enabled);
        if(!enabled){
            plusTV.setAlpha(0.5f);
            minusTV.setAlpha(0.5f);
        }else{
            plusTV.setAlpha(1f);
            minusTV.setAlpha(1f);
        }
    }
}
