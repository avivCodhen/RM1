package com.strongest.savingdata;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TimingLogger;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.strongest.savingdata.AlgorithmProgress.ProgressDialogFragment;
import com.strongest.savingdata.MyViews.CreateCustomBeansView.NumberChooseManager;
import com.strongest.savingdata.MyViews.CreateCustomBeansView.PyramidNumberChooseView;
import com.strongest.savingdata.Dialogs.CreateRepsDialog;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TestActivity extends AppCompatActivity implements View.OnClickListener {

    Button btn;
    PyramidNumberChooseView view;

    @BindView(R.id.test_tv)
    TextView tv;
    private LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ButterKnife.bind(this);
       btn = (Button) findViewById(R.id.btn_test);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimingLogger t = new TimingLogger("aviv","dialog");
                CreateRepsDialog d = new CreateRepsDialog();
                ProgressDialogFragment ds = new ProgressDialogFragment();
                //ds.show(getSuFragmentManager(), "sdsd");
                d.show(getSupportFragmentManager(), "workplease");
                t.addSplit("after show");
                t.dumpToLog();
            }
        });

//      tv = (TextView) view.findViewById(R.id.test_tv);

    }

    public TextView getTv() {
        return tv;
    }

    @Override
    public void onClick(View v) {

    }
}
