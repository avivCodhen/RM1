package com.strongest.savingdata;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.strongest.savingdata.MyViews.CreateCustomBeansView.PyramidNumberChooseView;

public class TestActivity extends AppCompatActivity implements View.OnClickListener {

    Button btn;
    PyramidNumberChooseView view;

    private LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

//      tv = (TextView) view.findViewById(R.id.test_tv);

    }


    @Override
    public void onClick(View v) {

    }
}
