package com.strongest.savingdata.Handlers;

import android.content.Context;
import android.view.View;
import android.widget.CompoundButton;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.strongest.savingdata.R;

public class MaterialDialogHandler {

    private MaterialDialog.Builder builder;
    private MaterialDialog dialog;

    public static MaterialDialogHandler get() {
        return new MaterialDialogHandler();
    }


    public MaterialDialogHandler defaultDeleteBuilder(Context context,
                                                      String title,

                                                      String positiveText) {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(context)
                .title(title)
                .content("Content deleted cannot be retrieved.")
                .positiveText(positiveText)
                .negativeText("Cancel");

        this.builder = builder;
        return this;
    }
    public MaterialDialogHandler defaultBuilder(Context context,
                                                      String title,
                                                      String positiveText) {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(context)
                .title(title)
                .positiveText(positiveText)
                .negativeText("Cancel");

        this.builder = builder;
        return this;
    }
    public MaterialDialogHandler addContent(String content){
        if (builder != null){
            builder.content(content);
        }
        return this;
    }

    public MaterialDialogHandler hideNegativeButton(){
        if (builder != null){
            builder.negativeText("");
        }
        return this;
    }


    public MaterialDialogHandler addCheckBox(CompoundButton.OnCheckedChangeListener onCheckedChangeListener) {
        builder.checkBoxPromptRes(R.string.log_mode_checkbox, false, onCheckedChangeListener);
        return this;

    }

    public MaterialDialogHandler addInput(String text, MaterialDialog.InputCallback inputCallback) {
        builder.input("", text, inputCallback);
        return this;

    }

    public MaterialDialogHandler buildDialog() {
        guard(builder,"builder");
        this.dialog = builder.build();
        return this;
    }

    public MaterialDialogHandler addPositiveActionFunc(View.OnClickListener onClickListener, boolean dismissOnAction) {
        guard(dialog,"dialog");
        this.dialog.getActionButton(DialogAction.POSITIVE).setOnClickListener(v->{
            onClickListener.onClick(v);
            if(dismissOnAction){
                dialog.dismiss();
            }
        });
        return this;
    }

    public void show(){
        guard(dialog, "dialog");
        dialog.show();
    }

    private void guard(Object o, String s){
        if(o == null){
            throw new IllegalArgumentException(s+ "cannot be null");
        }
    }
}
