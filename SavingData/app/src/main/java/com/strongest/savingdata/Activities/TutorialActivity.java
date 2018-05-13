package com.strongest.savingdata.Activities;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.strongest.savingdata.Adapters.OnSingleChoiceAdapterOnclickListener;
import com.strongest.savingdata.Adapters.SwipeAdapter;
import com.strongest.savingdata.MyViews.MyViewPager;
import com.strongest.savingdata.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.droidsonroids.gif.GifImageView;


public class TutorialActivity extends BaseActivity implements OnSingleChoiceAdapterOnclickListener{


    @BindView(R.id.activity_tutorial_viewpager)
    MyViewPager viewPager;

    public static final String FIRSTVISIT = "firstvisit";
    private SwipeAdapter swipeAdapter;
    private int[] images = {
            R.drawable.dc_gif,
            R.drawable.lc_gif
    };
    private String[] texts = {
      "You can Double Tap on a set to multiply it.",
      "Long click on an item to explore more options!"
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(dataManager.getPrefs().getString(FIRSTVISIT, FIRSTVISIT).equals("no")){
            startActivity(new Intent(this, HomeActivity.class));
        }
        setContentView(R.layout.activity_tutorial);
        ButterKnife.bind(this);
        swipeAdapter = new SwipeAdapter(this, images, texts);
        swipeAdapter.setListener(this);
        viewPager.setAdapter(swipeAdapter);

    }

    @Override
    public void onClick(String object, String type) {
        if(object.equals("0")){
            viewPager.setCurrentItem(1);
        }else{
            dataManager.getPrefsEditor().putString(FIRSTVISIT, "no").commit();
            startActivity(new Intent(this, HomeActivity.class));

        }
    }

    @Override
    public void onLongclick(String object, String type) {

    }
}
