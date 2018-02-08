package com.strongest.savingdata.createProgramFragments.Unused;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.strongest.savingdata.Unused.AlertPopUp;
import com.strongest.savingdata.R;
import com.strongest.savingdata.createProgramFragments.CreateProgram.BaseCreateProgramFragment;
import com.strongest.savingdata.createProgramFragments.CreateProgram.RoutinesFragment;

/**
 * Created by Cohen on 4/27/2017.
 */

public class DaysFragment extends BaseCreateProgramFragment implements RadioGroup.OnCheckedChangeListener {

    private RadioGroup radioGroup;

    private int days;
    private MenuItem item;
    private boolean move = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        setHasOptionsMenu(true);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setTitle(R.string.title_frequency);

        return inflater.inflate(R.layout.fragment_days, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }


    private void initView(View view) {

        radioGroup = (RadioGroup) view.findViewById(R.id.fragment_days_radioGroup);
        radioGroup.setOnCheckedChangeListener(this);
     //   recommendDays();

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.next_button, menu);

    }

   /* public void recommendDays() {
        for (int i = 0; i <radioGroup.getChildCount() ; i++) {
            if (getPrefs().getInt(LEVEL_INT, -1) == 0) {
                setEnableRadioButton(3);
            }
            if (getPrefs().getInt(LEVEL_INT, -1) == 1) {
                setEnableRadioButton(3);
            }
            if (getPrefs().getInt(LEVEL_INT, -1) == 3) {
                setEnableRadioButton(0);
            }
        }
    }*/

    public void setEnableRadioButton(int num){
        RadioButton rb = (RadioButton) radioGroup.getChildAt(num);
        rb.setEnabled(false);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        this.item = item;
        int id = R.id.action_next;
        super.onOptionsItemSelected(item);
        if (item.getItemId() == id) {
            if (radioGroup.getCheckedRadioButtonId() == -1) {
                AlertPopUp alert = new AlertPopUp(getContext());
                alert.createAlert(getString(R.string.error_frequency), false);
                return false;
            } else {
                RoutinesFragment fragment = new RoutinesFragment();
                getPrefsEditor().putInt(DAYS_INT, days).commit();
                switchFragment(fragment, null);
                return true;
            }

        }

        return false;
    }


    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {

       // days = ProgHelper.determineInt(checkedId, daysButnArray);
        move = true;
        // returnOption();


       /* RoutinesFragment fragment = new RoutinesFragment();
        getPrefsEditor().putInt(DAYS_INT, days).commit();
        switchFragment(fragment);*/
    }

    public void returnOption() {
        onOptionsItemSelected(item);
    }
}
