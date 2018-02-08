package com.strongest.savingdata.tabFragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Cohen on 6/23/2017.
 */

public class BaseTabs extends Fragment {


    public static final String ROUTINE = "routine";
    public static final String PREFS = "sharedprefs";
    private SharedPreferences.Editor editor;

    public SharedPreferences getPrefs() {
        return getContext().getSharedPreferences(PREFS, Context.MODE_PRIVATE);
    }

    public ActionBar getActionBar() {
        return ((AppCompatActivity) getActivity()).getSupportActionBar();
    }

    public SharedPreferences.Editor getPrefsEditor() {
        return editor = getPrefs().edit();
    }

}
