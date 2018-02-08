package com.strongest.savingdata.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import com.strongest.savingdata.R;

public class EnterActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText emailED, passED;
    private CheckBox rememberCB;
  //  private Button btnLogIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter);

        rememberCB = (CheckBox) findViewById(R.id.enter_activity_checkBox);
        emailED = (EditText) findViewById(R.id.enter_activity_emailED);
        passED = (EditText) findViewById(R.id.enter_activity_passwordED);
        findViewById(R.id.enter_activiy_logIn).setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }
}
