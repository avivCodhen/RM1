package com.strongest.savingdata.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.strongest.savingdata.Database.Managers.DataManager;
import com.strongest.savingdata.R;

import tslamic.fancybg.FancyBackground;

public class LevelActivity extends AppCompatActivity {

    private DataManager dataManager;
    private SeekBar seekBar;
    private TextView tv;


    private Button saveBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level);
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.activity_level_layout);
        dataManager = new DataManager(this);
        if (dataManager.getPrefs().getInt(DataManager.LEVEL_INT, -1) != -1){
            startActivity(new Intent(LevelActivity.this, HomeActivity.class));
            finish();
        }
        FancyBackground.on(layout)
                .set(R.drawable.level_background_one, R.drawable.level_background_two,
                        R.drawable.level_background_three)
                .inAnimation(R.anim.fad_in)
                .outAnimation(R.anim.fad_out)
                .interval(3000)

                .start();

        seekBar = (SeekBar) findViewById(R.id.activity_level_seekBar);
        seekBar.setProgress(1);
        seekBar.callOnClick();
        tv = (TextView) findViewById(R.id.activity_level_textview);
        saveBtn = (Button) findViewById(R.id.activity_level_save);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataManager.getPrefsEditor().putInt(DataManager.LEVEL_INT, seekBar.getProgress()).commit();
                startActivity(new Intent(LevelActivity.this, HomeActivity.class));
                finish();
            }
        });
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                String text = "";
                switch(progress){
                    case 0:
                        text = "Beginner";
                        break;
                    case 1:
                        text = "Novice";
                        break;
                    case 2:
                        text = "Intermediate";
                        break;
                    case 3:
                        text = "Advanced";
                        break;
                }
                tv.setText(text);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }
}
