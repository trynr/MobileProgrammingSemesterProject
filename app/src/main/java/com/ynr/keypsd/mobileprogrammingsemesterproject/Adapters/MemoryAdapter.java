package com.ynr.keypsd.mobileprogrammingsemesterproject.Adapters;

import static com.ynr.keypsd.mobileprogrammingsemesterproject.Helpers.DateHelper.createDateString;
import static com.ynr.keypsd.mobileprogrammingsemesterproject.Utils.Constants.GENERAL_PASSWORD;
import static com.ynr.keypsd.mobileprogrammingsemesterproject.Utils.Constants.SP_NAME;
import static com.ynr.keypsd.mobileprogrammingsemesterproject.Utils.RequestPermission.checkAndRequestReadContacts;
import static com.ynr.keypsd.mobileprogrammingsemesterproject.Utils.RequestPermission.checkAndRequestSendSms;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.ynr.keypsd.mobileprogrammingsemesterproject.Activities.AddOrUpdateMemoryActivity;
import com.ynr.keypsd.mobileprogrammingsemesterproject.Helpers.SqliteDatabaseHelper;
import com.ynr.keypsd.mobileprogrammingsemesterproject.Helpers.ShareHelper;
import com.ynr.keypsd.mobileprogrammingsemesterproject.Models.Contact;
import com.ynr.keypsd.mobileprogrammingsemesterproject.Models.Memory;
import com.ynr.keypsd.mobileprogrammingsemesterproject.Popups.ContactsPopup;
import com.ynr.keypsd.mobileprogrammingsemesterproject.Popups.MemoryPasswordPopup;
import com.ynr.keypsd.mobileprogrammingsemesterproject.R;

import java.util.Date;
import java.util.List;

public class MemoryAdapter extends RecyclerView.Adapter<MemoryAdapter.MyViewHolder> {

    public static String MEMORY_OBJECT = "MEMORY_OBJECT";

    private SharedPreferences mPrefs;
    private SharedPreferences.Editor prefsEditor;
    Activity activity;
    Context context;
    List<Memory> memoryList;
    Dialog dialog;
    SqliteDatabaseHelper databaseHelper;
    ShareHelper shareHelper;

    public MemoryAdapter(Activity activity,
                         Context context,
                         List<Memory> memoryList,
                         Dialog dialog,
                         SqliteDatabaseHelper databaseHelper) {

        this.activity = activity;
        this.context = context;
        this.memoryList = memoryList;
        this.dialog = dialog;
        this.databaseHelper = databaseHelper;
        mPrefs = activity.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        prefsEditor = mPrefs.edit();
        shareHelper = new ShareHelper();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.adapter_memory_item, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Memory memory = memoryList.get(position);

        Date date = memory.getDate();
        int day = date.getDate() + 1;
        int month = date.getMonth() + 1;
        int year = date.getYear();
        holder.dateTv.setText(createDateString(day, month, year));

        holder.titleTv.setText(memory.getTitle());
        holder.mainTextTv.setText(memory.getMainText());

        holder.editMemoryIcon.setOnClickListener(view -> {
            // Önce özel şifre var mı diye bak. Varsa onu al şifre olarak.
            // Yoksa genel şifreye bak. Varsa onu al şifre olarak.
            // İkisi de yoksa aç.

            String specialPassword = memory.getPassword();
            String generalPassword = mPrefs.getString(GENERAL_PASSWORD, "");
            MemoryPasswordPopup memoryPasswordPopup;

            if(!specialPassword.equals("")){
                memoryPasswordPopup = new MemoryPasswordPopup(activity, dialog, specialPassword, memory);
                memoryPasswordPopup.show();
                return;
            }
            if(!generalPassword.equals("")){
                memoryPasswordPopup = new MemoryPasswordPopup(activity, dialog, generalPassword, memory);
                memoryPasswordPopup.show();
                return;
            }

            Intent intent = new Intent(activity, AddOrUpdateMemoryActivity.class);

            intent.putExtra(MEMORY_OBJECT, memory);

            activity.startActivity(intent);
        });

        holder.deleteMemoryIcon.setOnClickListener(view -> {
            final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
            alertDialogBuilder
                    .setMessage("Do you want to delete this memory?")
                    .setCancelable(true)
                    .setPositiveButton("Yes", (dialog, id) -> {
                        memoryList.remove(position);
                        notifyDataSetChanged();
                        databaseHelper.deleteMemory(memory.getId());
                    })
                    .setNegativeButton("No", (dialog, id) -> dialog.dismiss());
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        });

        holder.shareIcon.setOnClickListener(view -> {

            if(checkAndRequestReadContacts(activity) && checkAndRequestSendSms(activity)){
                List<Contact> contacts = shareHelper.getContacts(context);
                ContactsPopup contactsPopup = new ContactsPopup(activity, dialog, memory, contacts);
                contactsPopup.show();
            }

        });

    }

    @Override
    public int getItemCount() {
        return memoryList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView dateTv;
        TextView titleTv;
        TextView mainTextTv;
        AppCompatImageView editMemoryIcon;
        AppCompatImageView deleteMemoryIcon;
        AppCompatImageView shareIcon;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            dateTv = itemView.findViewById(R.id.dateTv);
            titleTv = itemView.findViewById(R.id.titleTv);
            mainTextTv = itemView.findViewById(R.id.mainTextTv);
            editMemoryIcon = itemView.findViewById(R.id.editMemoryIcon);
            deleteMemoryIcon = itemView.findViewById(R.id.deleteMemoryIcon);
            shareIcon = itemView.findViewById(R.id.shareIcon);
        }
    }

}