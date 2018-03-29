package com.strongest.savingdata.AlgorithmLayout;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.strongest.savingdata.MyViews.WorkoutView.OnWorkoutViewInterfaceClicksListener;

/**
 * Created by Cohen on 3/29/2018.
 */

public class LayoutManagerAlertdialog {

    public static AlertDialog getAlertDialog(Context context, final OnLayoutManagerDialogPress callback, final int position){
        return  new AlertDialog.Builder(context)
                .setCancelable(true)
                .setMessage("Delete this item?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        callback.onLMDialogOkPressed(position);

                    }
                }).setNegativeButton("No", null).show();
    }

}
