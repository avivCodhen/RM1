package com.strongest.savingdata.Unused;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.DialogFragment;

/**
 * Created by Cohen on 6/1/2017.
 */

public abstract class BaseDialogFragment extends DialogFragment {
    public static final String PREFS = "sharedprefs";
    private SharedPreferences.Editor editor;
    public static final String DAYS_INT = "days";
    public static final String ROUTINE = "routine";
    public static final String BODY_ARRAY = "bodyarray";

    public BaseDialogFragment(){

    }

    public SharedPreferences getPrefs() {
        return getActivity().getSharedPreferences(PREFS, Context.MODE_PRIVATE);
    }

    public SharedPreferences.Editor getPrefsEditor() {
        return editor = getPrefs().edit();
    }


}
