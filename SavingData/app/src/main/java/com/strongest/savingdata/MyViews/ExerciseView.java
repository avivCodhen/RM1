package com.strongest.savingdata.MyViews;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.strongest.savingdata.R;

import java.io.Serializable;

/**
 * Created by Cohen on 5/9/2017.
 */

@Deprecated
public class ExerciseView extends LinearLayout implements Serializable{

    private Context context;
    private LinearLayout layout;

    private TextView exerciseTV, supersetTV, accsTV, setsTV, repTV, methodTV, ovrTV;
    private String baseMuscle;
    private int order;

    public ExerciseView(Context context) {
        super(context);
        this.context = context;

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layout = (LinearLayout) inflater.inflate(R.layout.recycler_view_exercises_left_margin, this, true);

        setTextViewID();
        setDefaultTextViewText();
    }

    public ExerciseView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layout = (LinearLayout) inflater.inflate(R.layout.recycler_view_exercises_left_margin, this, true);

        setTextViewID();
        setDefaultTextViewText();


    }

    private void setTextViewID() {
        setsTV = (TextView) findViewById(R.id.exerciseView_setsTV);
        repTV = (TextView) findViewById(R.id.exerciseView_repsTV);
        methodTV = (TextView) findViewById(R.id.exerciseView_methodTV);
        ovrTV = (TextView) findViewById(R.id.exerciseView_weightTV);
        exerciseTV = (TextView) findViewById(R.id.exerciseView_nameTV);
    }

    private void setDefaultTextViewText() {

        setsTV.setText("3");
        ovrTV.setText("OVR");


    }

    public String getOvrTV() {
        return ovrTV.getText().toString();
    }

    public void setOvrTV(String s) {
        ovrTV.setText(s);
    }

    public String getMethodTV() {
        return methodTV.getText().toString();
    }

    public void setMethodTV(String s) {
        methodTV.setText(s);
    }

    public String getRepTV() {
        return repTV.getText().toString();
    }

    public void setRepTV(String s) {
        repTV.setText(s);
    }

    public String getSetsTV() {
        return setsTV.getText().toString();
    }

    public void setSetsTV(String s) {
        setsTV.setText(s);
    }

    public String getExerciseTV() {
        return exerciseTV.getText().toString();
    }

    public void setExerciseTV(String s) {
        exerciseTV.setText(s);
    }

    public String getBaseMuscle() {
        return baseMuscle;
    }

    public void setBaseMuscle(String baseMuscle) {
        this.baseMuscle = baseMuscle;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public TextView getSupersetTV() {
        return supersetTV;
    }

    public void setSupersetTV(TextView supersetTV) {
        this.supersetTV = supersetTV;
    }
}
