package com.strongest.savingdata.Unused;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.strongest.savingdata.R;

import java.util.HashMap;

public class LevelDeterminerDialogFragment extends BaseDialogFragment implements RadioGroup.OnCheckedChangeListener, View.OnClickListener {

    private LinearLayout layout;
    private String[] questArr;
    private Intent intent;
    private int id = 100;
    private HashMap<Integer, Integer> results;
    private Button btn;
    private TextView resultTV;
    int level;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(0x99000000));
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar);
        Drawable d = new ColorDrawable(Color.BLACK);
        d.setAlpha(200);
        getDialog().getWindow().setBackgroundDrawable(d);
        return inflater.inflate(R.layout.fragment_dialog_level_determiner, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    private void initView(View v) {
        intent = new Intent();
        layout = (LinearLayout) v.findViewById(R.id.fragment_dialog_determiner_layout);
        resultTV = new TextView(getContext());
        createDeterminerLayout(layout);
        results = new HashMap<>();


    }

    //public TextView

    private void createDeterminerLayout(LinearLayout layout) {

        int four = 4;

        questArr = getContext().getResources().getStringArray(R.array.determiner_questions_array);
        String[] answArr;

        for (int i = 0; i < 1; i++) { //how many questions
            for (int j = 0; j < questArr.length; j++) { // each question
                RadioGroup radioGroup = new RadioGroup(getContext());
                radioGroup.setTag(j);
                radioGroup.setOnCheckedChangeListener(this);
                radioGroup.setPadding(30, 30, 30, 30);
                layout.addView(createQuestionTextView(j, questArr));
                layout.addView(radioGroup);
                for (int k = 0; k < four; k++) { //each answer
                    String answId = "determiner_answer" + j + "_array";
                    int arrayId = getResources().getIdentifier(answId, "array", "com.strongest.savingdata");
                    answArr = getContext().getResources().getStringArray(arrayId);
                    RadioButton rb = new RadioButton(getContext());
                    rb.setText(answArr[k]);
                    rb.setId(id + k);
                    rb.setTextColor(Color.parseColor("#FFFFFF"));
                    radioGroup.addView(rb);

                }
            }
        }
        setTvResults();
        setBtnResult();
        setBtnExit();
    }

    private String determineLevel() {
        int sum = 0;
        int[] sumResults = new int[]{4, 7, 10};
        String[] levelStringsArr = new String[]{"Untrained", "Novice", "Intermediate", "advanced"};
        level = 0;
        for (Integer i : results.values()) {
            sum += i;
        }
        for (int i = 0; i < sumResults.length; i++) {
            if (sumResults[i] >= sum) {
                level = i;
                return levelStringsArr[i];
            }
        }
        level = 3;
        return levelStringsArr[3];
    }

    public TextView createQuestionTextView(int j, String[] bodyPartsArray) {
        TextView tv2 = new TextView(getContext());
        tv2.setText(bodyPartsArray[j]);
        tv2.setTextColor(Color.parseColor("#FFFFFF"));
        tv2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        return tv2;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        for (int i = 0; i < group.getChildCount(); i++) {
            if (((int) group.getTag()) == i) {
                for (int j = 0; j < group.getChildCount(); j++) {
                    if (checkedId == group.getChildAt(j).getId()) {
                        if ((int)group.getTag() == 0) {
                            getPrefsEditor().putInt(DAYS_INT, level).commit();
                        }
                        results.put(i, group.getChildAt(j).getId() - id);
                    }
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (results.size() == questArr.length) {
            btn.setError(null);
            resultTV.setText("Your determined level is " + determineLevel());
        } else {

            btn.setError("Please answer all the questions");
        }
    }
    public void setBtnResult(){
        btn = new Button(getContext());
        btn.setText(R.string.level_determiner_btn_text);
        btn.setTextColor(Color.parseColor("#A0A0A0"));
        btn.setBackgroundColor(Color.parseColor("#E0E0E0"));
        btn.setBackground(new ColorDrawable(0x99000000));
        btn.setOnClickListener(this);
        layout.addView(btn);
    }
    public void setBtnExit(){
        Button exitBtn = new Button(getContext());
        exitBtn.setText("Exit");
        exitBtn.setTextColor(Color.parseColor("#A0A0A0"));
        exitBtn.setBackground(new ColorDrawable(0x99000000));
        exitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("LEVEL", level);
                getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
                dismiss();
            }
        });
        layout.addView(exitBtn);
    }
    public void setTvResults(){
        resultTV.setTextColor(Color.parseColor("#ffffff"));
        resultTV.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        resultTV.setTypeface(null, Typeface.BOLD);
        resultTV.setGravity(Gravity.CENTER);
        layout.addView(resultTV);
    }
}
