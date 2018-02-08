package com.strongest.savingdata.createProgramFragments.Unused;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.strongest.savingdata.Unused.AlertPopUp;
import com.strongest.savingdata.R;
import com.strongest.savingdata.createProgramFragments.CreateProgram.BaseCreateProgramFragment;

/**
 * Created by Cohen on 4/27/2017.
 */

public class LevelsFragment extends BaseCreateProgramFragment {
    private TextView descriptionTV;
    private RadioGroup radioGroup;
    private int baseLevel;
    private static final int[] buttonLevelsArray = new int[]{R.id.level_radio_untrained, R.id.level_radio_novice
            , R.id.level_radio_intermediate, R.id.level_radio_advanced};


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        getActionBar().setTitle(R.string.title_estimate);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        return inflater.inflate(R.layout.fragment_levels, container, false);


    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.next_button, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        int id = R.id.action_next;
        if (item.getItemId() == id) {

            if (radioGroup.getCheckedRadioButtonId() == -1) {
                AlertPopUp alert = new AlertPopUp(getContext());
                alert.createAlert(getString(R.string.error_estimate), false);
                return false;
            } else {
                passDataAndSwitch();
                return true;
            }
        }


        return false;
    }

    private void passDataAndSwitch() {

        DaysFragment fragment = new DaysFragment();
        switchFragment(fragment, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    private void initView(View v) {

        descriptionTV = (TextView) v.findViewById(R.id.levels_and_days_levels_description_TV);

    }

}
