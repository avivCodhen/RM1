package com.strongest.savingdata.Activities;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.strongest.savingdata.AModels.UserModel.User;
import com.strongest.savingdata.AModels.programModel.Program;
import com.strongest.savingdata.Handlers.MaterialDialogHandler;
import com.strongest.savingdata.MyViews.SaveExitToolBar;
import com.strongest.savingdata.MyViews.SmartProgressBar;
import com.strongest.savingdata.R;
import com.strongest.savingdata.Utils.MyUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShareProgramActivity extends BaseActivity {

    String programUID;
    TextWatcher textWatcher;

    @BindView(R.id.shared_program_username)
    TextView userNameTV;

    @BindView(R.id.share_program_ET)
    EditText emailET;

    @BindView(R.id.share_program_btn)
    Button shareBtn;

    @BindView(R.id.share_program_toolbar)
    SaveExitToolBar saveExitToolBar;

    @BindView(R.id.shared_smartprogressbar)
    SmartProgressBar smartProgressBar;

    @BindView(R.id.success_tv)
    TextView successTV;

    @BindView(R.id.share_program_activity_title_tv)
    TextView titleTV;
    User toUser;

    Program p;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_program);
        ButterKnife.bind(this);
        programUID = getIntent().getStringExtra("programuid");
        p = (Program) getIntent().getSerializableExtra("program");
        titleTV.setText(p.getProgramName());
        saveExitToolBar.instantiate();
        saveExitToolBar.showCancel(false).showBack(true);
        saveExitToolBar.setOptionalText("Share Program");
        saveExitToolBar.setSaveButton(v -> finish());


        if (!MyUtils.isNetworkConnected(this)) {
            MaterialDialogHandler.get()
                    .defaultBuilder(this, "No Internet Connection", "OK")
                    .addContent("Without internet connection, we cannot fetch your data.")
                    .hideNegativeButton()
                    .buildDialog().show();
        }

        textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                userService.findUserByEmail(editable.toString(),
                        user -> {
                            if (user == null) {
                                userNameTV.setText("No user found");
                                shareBtn.setEnabled(false);
                                shareBtn.setAlpha(0.5f);


                            } else {
                                toUser = (User) user;
                                userNameTV.setText(((User) user).getName());
                                shareBtn.setEnabled(true);
                                shareBtn.setAlpha(1f);
                            }
                        });
            }
        };
        emailET.addTextChangedListener(textWatcher);


        shareBtn.setOnClickListener(v -> {
            if (toUser != null) {
                smartProgressBar.setText("Sharing program with " + toUser.getName() + "...")
                        .show();
                programService.shareProgramWithUser(this, p, toUser);
                shareBtn.setAlpha(0.5f);
                shareBtn.setEnabled(false);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        smartProgressBar.hide();
                        successTV.setText("Successfully shared with " + toUser.getName() + "!");
                        successTV.setVisibility(View.VISIBLE);
                        shareBtn.setAlpha(1);
                        shareBtn.setEnabled(true);

                    }
                }, 2000);
//                smartProgressBar.hide();

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
}
