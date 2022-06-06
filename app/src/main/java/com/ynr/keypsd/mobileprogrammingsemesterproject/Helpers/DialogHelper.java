package com.ynr.keypsd.mobileprogrammingsemesterproject.Helpers;

import android.app.Activity;
import android.content.DialogInterface;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;


public class DialogHelper {


    public static void showDialog(Activity activity,
                           String message,
                           String positiveButtonMessage,
                           String negativeButtonMessage,
                           DialogInterface.OnClickListener onPositiveButtonClickedListener,
                           DialogInterface.OnClickListener onNegativeButtonClickedListener){

        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
        alertDialogBuilder
                .setMessage(message)
                .setCancelable(true)
                .setPositiveButton(positiveButtonMessage, onPositiveButtonClickedListener)
                .setNegativeButton(negativeButtonMessage, onNegativeButtonClickedListener);
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


}
