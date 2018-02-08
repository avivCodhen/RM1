package com.strongest.savingdata.Unused;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * Created by Cohen on 5/2/2017.
 */


public class AlertPopUp extends AlertDialog.Builder {
    private Context context;

    public AlertPopUp(Context context) {
        super(context);
        this.context = context;
    }

    public void createAlert(String text, boolean neg){
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
        .setTitle("Error")
        .setMessage(text)
        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                return;
            }
        });
        if(neg) {

            setNegativeButton("", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    return;
                }
            });
        }
       final AlertDialog alert = builder.create();
        alert.show();
    }

    public void createCustomeAlert(String text){
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle("Error")
                .setMessage(text);

        final AlertDialog alert = builder.create();
        alert.show();
    }
}
