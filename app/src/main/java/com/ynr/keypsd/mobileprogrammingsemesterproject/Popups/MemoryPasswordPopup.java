package com.ynr.keypsd.mobileprogrammingsemesterproject.Popups;

import static com.ynr.keypsd.mobileprogrammingsemesterproject.Adapters.MemoryAdapter.MEMORY_OBJECT;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatImageView;

import com.ynr.keypsd.mobileprogrammingsemesterproject.Activities.AddOrUpdateMemoryActivity;
import com.ynr.keypsd.mobileprogrammingsemesterproject.Models.Memory;
import com.ynr.keypsd.mobileprogrammingsemesterproject.R;

public class MemoryPasswordPopup implements View.OnClickListener {

    Activity activity;
    Dialog dialog;
    String memoryPassword;
    AppCompatImageView closeIcon;
    EditText etPassword;
    TextView errorMessageTv;
    Button openMemoryButton;
    Memory memory;

    public MemoryPasswordPopup(Activity activity, Dialog dialog, String memoryPassword, Memory memory) {
        this.activity = activity;
        this.dialog = dialog;
        this.memoryPassword = memoryPassword;
        this.memory = memory;
        dialog.setContentView(R.layout.popup_memory_password);
        define();

    }

    private void define() {
        closeIcon = dialog.findViewById(R.id.close_icon);
        etPassword = dialog.findViewById(R.id.et_password);
        errorMessageTv = dialog.findViewById(R.id.error_message_tv);
        openMemoryButton = dialog.findViewById(R.id.open_memory_button);

        closeIcon.setOnClickListener(this);
        openMemoryButton.setOnClickListener(this);

    }

    public void show() {
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.close_icon){
            dialog.dismiss();
        }
        else if(view.getId() == R.id.open_memory_button){
            String inputPassword = etPassword.getText().toString();
            if(inputPassword.equals("")){
                errorMessageTv.setVisibility(View.VISIBLE);
                errorMessageTv.setText("Password field can\'t be empty.");
            }
            else if(!inputPassword.equals(memoryPassword)){
                errorMessageTv.setVisibility(View.VISIBLE);
                errorMessageTv.setText("Wrong password.");
            }
            else{
                errorMessageTv.setVisibility(View.INVISIBLE);

                Intent intent = new Intent(activity, AddOrUpdateMemoryActivity.class);

                intent.putExtra(MEMORY_OBJECT, memory);

                activity.startActivity(intent);
            }

        }
    }
}
