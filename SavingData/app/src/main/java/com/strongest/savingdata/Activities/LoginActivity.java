package com.strongest.savingdata.Activities;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.View;

import com.strongest.savingdata.R;
import com.strongest.savingdata.Adapters.SwipeAdapter;

@Deprecated
public class LoginActivity extends FragmentActivity implements View.OnClickListener {

    private ViewPager viewPager;
    private TabLayout tabLayout;
    private SwipeAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        int[] images = {
                R.drawable.logo,
                R.drawable.one,
                R.drawable.two,
                R.drawable.three,
                R.drawable.four
        };

        String[] info = new String[]{
                getString(R.string.swipe),
                getString(R.string.swipe_details),
                getString(R.string.swipe_create),
                getString(R.string.swipe_improve),
                getString(R.string.swipe_progress)
        };

        int imageView = R.id.swipe_layout_imageView;
        int textView = R.id.swipe_layout_textView;

        findViewById(R.id.login_activity_registerBtn).setOnClickListener(this);
        findViewById(R.id.login_activity_logInBtn).setOnClickListener(this);
        viewPager = (ViewPager) findViewById(R.id.login_activity_viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabDots);
        adapter = new SwipeAdapter(this, images, info);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager, true);


    }



    @Override
    public void onClick(View v) {

        switch(v.getId()) {

            case R.id.login_activity_registerBtn:
                Intent intent = new Intent(this, RegisterActivity.class);
                startActivity(intent);
                break;
        }

        }

}
