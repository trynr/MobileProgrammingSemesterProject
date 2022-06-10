package com.ynr.keypsd.mobileprogrammingsemesterproject.Popups;

import static com.ynr.keypsd.mobileprogrammingsemesterproject.Utils.Constants.GENERAL_PASSWORD;
import static com.ynr.keypsd.mobileprogrammingsemesterproject.Utils.Constants.SP_NAME;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatImageView;

import com.ynr.keypsd.mobileprogrammingsemesterproject.R;

public class SetPasswordPopup implements View.OnClickListener {

    private SharedPreferences mPrefs;
    private SharedPreferences.Editor prefsEditor;

    private Activity activity;
    private Dialog dialog;
    private AppCompatImageView closeIcon;
    private EditText oldPasswordEt;
    private EditText newPasswordEt;
    private TextView errorMessageTv;
    private Button setGeneralPasswordButton;
    private Button removeGeneralPasswordButton;
    private boolean passwordPresent;
    private String currentPassword;

    public SetPasswordPopup(Activity activity, Dialog dialog) {
        this.activity = activity;
        this.dialog = dialog;
        dialog.setContentView(R.layout.popup_set_password);
        define();

        if(!passwordPresent){
            // Password hiç set edilmedi
            oldPasswordEt.setVisibility(View.GONE);
            removeGeneralPasswordButton.setVisibility(View.GONE);
        }

    }

    private void define(){
        closeIcon = dialog.findViewById(R.id.close_icon_password);
        oldPasswordEt = dialog.findViewById(R.id.et_old_password);
        newPasswordEt = dialog.findViewById(R.id.et_new_password);
        errorMessageTv = dialog.findViewById(R.id.error_message_tv);
        setGeneralPasswordButton = dialog.findViewById(R.id.set_general_password_button);
        removeGeneralPasswordButton = dialog.findViewById(R.id.remove_general_password_button);

        closeIcon.setOnClickListener(this);
        setGeneralPasswordButton.setOnClickListener(this);
        removeGeneralPasswordButton.setOnClickListener(this);

        mPrefs = activity.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        prefsEditor = mPrefs.edit();
        currentPassword = mPrefs.getString(GENERAL_PASSWORD, "");
        passwordPresent = !currentPassword.equals("");


    }

    public void show() {
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.close_icon_password){
            dialog.dismiss();
        }
        else if(view.getId() == R.id.set_general_password_button){

            String oldPassword = oldPasswordEt.getText().toString();
            String newPassword = newPasswordEt.getText().toString();

            if(passwordPresent && !oldPassword.equals(currentPassword)){
                errorMessageTv.setVisibility(View.VISIBLE);
                errorMessageTv.setText("Old password is not correct.");
            }
            else if(newPassword.equals("")){
                errorMessageTv.setVisibility(View.VISIBLE);
                errorMessageTv.setText("Password cannot be empty.");
            }
            else{ // Success
                errorMessageTv.setVisibility(View.GONE);
                updatePassword(newPassword);
                dialog.dismiss();
            }

        }
        else if(view.getId() == R.id.remove_general_password_button){
            String oldPassword = oldPasswordEt.getText().toString();

            if(passwordPresent && !oldPassword.equals(currentPassword)){
                errorMessageTv.setVisibility(View.VISIBLE);
                errorMessageTv.setText("Password is not correct.");
            }
            else{
                errorMessageTv.setVisibility(View.GONE);
                updatePassword("");
                dialog.dismiss();
            }

        }

    }

    private void updatePassword(String newPassword) {
        prefsEditor.putString(GENERAL_PASSWORD, newPassword);
        prefsEditor.commit();
    }


}
