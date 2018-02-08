package com.strongest.savingdata.MyViews.CreateCustomBeansView;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.strongest.savingdata.Database.Exercise.Beans;
import com.strongest.savingdata.R;

import java.util.ArrayList;

/**
 * Created by Cohen on 1/18/2018.
 */

public class PyramidNumberChooseView extends LinearLayout implements NumberChoose {
    private NumberChooseManager mNumberChooseManager;
    private Context context;
    private Button addSetBtn;
    private ArrayList<PyramidLayoutHolder> pyramidLayoutHolders = new ArrayList<>();

    private LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT);

    //private LinearLayout mInnerView;
    private LinearLayout mInnerView;
    //private ScrollView mInnerView;

    public PyramidNumberChooseView(Context context) {
        super(context);
        this.context = context;
    }

    public PyramidNumberChooseView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public void initViews() {
        //mInnerView = (LinearLayout) findViewById(R.id.choose_number_pyramid_linear_layout);
        mInnerView = (LinearLayout) findViewById(R.id.choose_number_pyramid_innerlayout);
        //mInnerView = (ScrollView) findViewById(R.id.choose_number_pyramid_sc);
        addSetBtn = (Button) findViewById(R.id.choose_number_pyramid_add_set_btn);
        addSetBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                addSingleChooseLayout();
            }
        });
        addSingleChooseLayout();
    }

    private void addSingleChooseLayout() {
        PyramidLayoutHolder p = new PyramidLayoutHolder(context, mNumberChooseManager);
        //  p.getSingleNumberChooseView().setLayoutParams(lp);
        p.setSetsNumber(pyramidLayoutHolders.size() + 1);
        //this.setLayoutParams(lp);
        // mInnerView.setLayoutParams(lp);
        mInnerView.addView(p.getSets());
        mInnerView.addView(p.getSingleNumberChooseView());
        pyramidLayoutHolders.add(p);
    }

    @Override
    public boolean hasManager() {
        return mNumberChooseManager != null;
    }

    @Override
    public void setUpWithNumberChooseManager(NumberChooseManager numberChooseManager) {
        this.mNumberChooseManager = numberChooseManager;
        inflate(context, R.layout.choose_number_pyramid, this);
        initViews();
    }

    @Override
    public Beans createRepBeans() {
        String pyramid = "";
        for (int i = 0; i < pyramidLayoutHolders.size(); i++) {
            pyramid += pyramidLayoutHolders.get(i).getSingleNumberChooseView().getText() + ", ";
        }
        pyramid = pyramid.substring(0,pyramid.length()-2);
        return mNumberChooseManager.createBeans(this, NumberChooseManager.Type.PyramidRep, pyramid);
    }

    @Override
    public View getView() {
        return this;
    }


    private class PyramidLayoutHolder {

        private SingleNumberChooseView singleNumberChooseView;
        private Context context;
        private TextView sets;

        public PyramidLayoutHolder(Context context, NumberChooseManager numberChooseManager) {
            singleNumberChooseView = new SingleNumberChooseView(context);
            singleNumberChooseView.setUpWithNumberChooseManager(numberChooseManager);
            this.context = context;
            initsetsTV();
        }

        private void initsetsTV() {
            sets = new TextView(this.context);
            LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                sets.setTextAlignment(TEXT_ALIGNMENT_CENTER);
            }
            sets.setLayoutParams(lp);
            sets.setText("Set: ");

        }

        public String getSetsText() {
            return sets.getText().toString();
        }

        public TextView getSets() {
            return sets;
        }

        public void setSetsNumber(int num) {
            sets.setText(getSetsText() + num);
        }

        public SingleNumberChooseView getSingleNumberChooseView() {
            return singleNumberChooseView;
        }
    }
}
