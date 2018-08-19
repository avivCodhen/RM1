package com.strongest.savingdata.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.strongest.savingdata.R;
import com.strongest.savingdata.AModels.UserModel.User;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RegisterActivity extends BaseActivity {

    private EditText emailED, passED, confirmED;

    static final String PREFS_FILE_NAME = "myPrefs";
    private SharedPreferences settings; // read
    private SharedPreferences.Editor editor; // write

    private String email, pass;

    private static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    @BindView(R.id.register_activity_progressbar)
    public ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        emailED = (EditText) findViewById(R.id.register_activity_emailED);
        passED = (EditText) findViewById(R.id.register_activity_passwordED);
        confirmED = (EditText) findViewById(R.id.register_activity_confirmED);


        settings = getSharedPreferences(PREFS_FILE_NAME, MODE_PRIVATE);
        editor = settings.edit();

    }

    public void register(View v) {

        if (vaildate()) {

            editor.putString("EMAIL", email);
            editor.putString("PASS", pass);
            editor.commit();


            //TODO:put name textview
            userService.registerUser(progressBar, email,pass,"", user->{

                userService.saveUserToServer((User) user, (success->{
                    if((Integer)success == 1){
                        startActivity(new Intent(this, HomeActivity.class));
                    }
                }));
            });

        } else {
            return;
        }
    }

    public void back(View v) {
        finish();
    }

    private boolean vaildate() {
        boolean valid = true;
        email = emailED.getText().toString().trim();
        pass = passED.getText().toString();
        String confirmPass = confirmED.getText().toString();
        if (email == null || email.isEmpty()) {
            valid = false;
            emailED.setError("You must enter email address");
        }
        if (pass == null || pass.isEmpty()) {
            passED.setError("You must enter password");
            valid = false;
        }
        if (!validEmail(email)) {
            emailED.setError("Email address is incorrect");
            valid = false;
        }
        if (!confirmPass.equals(pass) || confirmPass.isEmpty()) {
            confirmED.setError("Password does not match");
            valid = false;
        }
        return valid;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);

    }

    public static boolean validEmail(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                return true;
        }
        return false;
    }
}
