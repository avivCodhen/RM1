package com.strongest.savingdata.AModels.workoutModel;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.InputType;
import android.util.TypedValue;
import android.widget.EditText;

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

    public static AlertDialog getInputAlertDialog(Context context, final OnLayoutManagerDialogPress callback, final String text, final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final EditText input = new EditText(context);
        input.setHint(text);
        input.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input)
                .setCancelable(true)
                .setMessage("Change item text")
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        callback.onLMDialogOkPressed(input.getText().toString(), position);

                    }
                }).setNegativeButton("Cancel", null).show();


    AlertDialog alert = builder.create();
    return alert;
    }

}
