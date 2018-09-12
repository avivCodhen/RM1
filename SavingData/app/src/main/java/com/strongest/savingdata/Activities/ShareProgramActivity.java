package com.strongest.savingdata.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.strongest.savingdata.AModels.UserModel.User;
import com.strongest.savingdata.R;

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

    User toUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_program);
        ButterKnife.bind(this);
        programUID = getIntent().getStringExtra("programuid");

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
                        user ->{
                            toUser = (User) user;
                            userNameTV.setText(((User) user).getName());
                        });
            }
        };
        emailET.addTextChangedListener(textWatcher);


        shareBtn.setOnClickListener(v ->{
            programService.shareProgramWithUser(programUID, toUser);
        });
    }
}
