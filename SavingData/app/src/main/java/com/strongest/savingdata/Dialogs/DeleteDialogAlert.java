package com.strongest.savingdata.Dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;

import com.strongest.savingdata.Adapters.MyExpandableAdapter;
import com.strongest.savingdata.ViewHolders.ExerciseViewHolder;

/**
 * Created by Cohen on 3/20/2018.
 */

public class DeleteDialogAlert extends AlertDialog{

    AlertDialog.Builder alertDialogBuilder;
    public boolean delete = false;

    protected DeleteDialogAlert(Context context) {
        super(context);
        alertDialogBuilder = new Builder(context);
    }

    public void shootAlertDialog(RecyclerView.ViewHolder vh){

     //   alertDialogBuilder.setTitle();

        // set dialog message
        String object = "";
        if(vh instanceof ExerciseViewHolder){
            object = "exercise";
        }else if(vh instanceof MyExpandableAdapter.SetsViewHolder){
            object = "set";
        }
        alertDialogBuilder
                .setMessage("Delete this "+object+"?")
                .setCancelable(false)
                .setPositiveButton("OK",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        delete = true;
                        dialog.cancel();
                    }
                })
                .setNegativeButton("CANCEL",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        delete = false;
                        dialog.cancel();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();

    }

}
