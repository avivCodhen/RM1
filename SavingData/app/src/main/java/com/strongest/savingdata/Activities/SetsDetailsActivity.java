package com.strongest.savingdata.Activities;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.strongest.savingdata.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SetsDetailsActivity extends AppCompatActivity {

    @BindView(R.id.activity_sets_toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sets_details);
        ButterKnife.bind(this);

        setUpToolbar();

    }

    private void setUpToolbar() {
        setSupportActionBar(toolbar);
        getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.background_color));
    }
}
