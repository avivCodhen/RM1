package com.strongest.savingdata.Fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.strongest.savingdata.AModels.UserModel.User;
import com.strongest.savingdata.Activities.HomeActivity;
import com.strongest.savingdata.Activities.RegisterActivity;
import com.strongest.savingdata.MyViews.SaveExitToolBar;
import com.strongest.savingdata.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RegisterFragment extends BaseFragment implements View.OnClickListener {


    @BindView(R.id.register_fragment_emailED)
    EditText emailED;

    @BindView(R.id.register_fragment_passwordED)
    EditText passED;

    @BindView(R.id.register_fragment_confirmED)
    EditText confirmED;

    @BindView(R.id.saveExitToolbar)
    SaveExitToolBar saveExitToolBar;

    @BindView(R.id.register_fragment_progressbar)
    public ProgressBar progressBar;

    String email;
    String pass;


    private static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_register, container, false);
        ButterKnife.bind(this, v);
        return v;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        saveExitToolBar.instantiate();
        saveExitToolBar.showBack(true);
        saveExitToolBar.showCancel(false);
        saveExitToolBar.noElevation();
        saveExitToolBar.setSaveButton(backBtn -> {
            getFragmentManager().popBackStack();
        });
    }


    public void register(View v) {

        if (vaildate()) {

            //TODO:put name textview
            userService.registerUser(progressBar, email, pass, "", user -> {

                userService.saveUserToServer((User) user, (success -> {
                    if ((Integer) success == 1) {

                        userService.logInUser(email, pass, progressBar, result -> {
                            getActivity().finish();
                        });
                    }

                }));
            });

        } else {
            return;
        }
    }

    @Override
    public void onClick(View view) {

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

    public static boolean validEmail(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }

}
