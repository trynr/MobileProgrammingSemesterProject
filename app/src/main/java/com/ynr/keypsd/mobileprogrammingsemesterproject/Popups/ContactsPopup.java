package com.ynr.keypsd.mobileprogrammingsemesterproject.Popups;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ynr.keypsd.mobileprogrammingsemesterproject.Activities.MemoryManagementActivity;
import com.ynr.keypsd.mobileprogrammingsemesterproject.Adapters.ContactsAdapter;
import com.ynr.keypsd.mobileprogrammingsemesterproject.Models.Contact;
import com.ynr.keypsd.mobileprogrammingsemesterproject.Models.Memory;
import com.ynr.keypsd.mobileprogrammingsemesterproject.R;

import java.util.List;

public class ContactsPopup {

    private Activity activity;
    private Dialog dialog;
    private Memory memory;
    private List<Contact> contacts;

    RecyclerView contactsRecyclerView;

    public ContactsPopup(Activity activity, Dialog dialog, Memory memory, List<Contact> contacts) {
        this.activity = activity;
        this.dialog = dialog;
        this.memory = memory;
        this.contacts = contacts;
        dialog.setContentView(R.layout.popup_contacts);
        define();
        setAdapterToRecyclerView();
    }

    private void setAdapterToRecyclerView() {
        ContactsAdapter contactsAdapter = new ContactsAdapter(activity, activity, dialog, memory, contacts);
        contactsRecyclerView.setAdapter(contactsAdapter);
        contactsRecyclerView.setLayoutManager(new LinearLayoutManager(activity));
    }

    private void define(){
        contactsRecyclerView = dialog.findViewById(R.id.contactsRecyclerView);
    }

    public void show() {
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }


}
