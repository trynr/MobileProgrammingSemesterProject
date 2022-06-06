package com.ynr.keypsd.mobileprogrammingsemesterproject.Adapters;

import static com.ynr.keypsd.mobileprogrammingsemesterproject.Utils.RequestPermission.checkAndRequestSendSms;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.ynr.keypsd.mobileprogrammingsemesterproject.Helpers.DialogHelper;
import com.ynr.keypsd.mobileprogrammingsemesterproject.Helpers.ShareHelper;
import com.ynr.keypsd.mobileprogrammingsemesterproject.Models.Contact;
import com.ynr.keypsd.mobileprogrammingsemesterproject.Models.Memory;
import com.ynr.keypsd.mobileprogrammingsemesterproject.R;

import java.util.List;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.MyViewHolder> {

    Activity activity;
    Context context;
    Dialog dialog;
    Memory memory;
    List<Contact> contacts;

    public ContactsAdapter(Activity activity, Context context, Dialog dialog, Memory memory, List<Contact> contacts) {
        this.activity = activity;
        this.context = context;
        this.dialog = dialog;
        this.memory = memory;
        this.contacts = contacts;
    }

    @NonNull
    @Override
    public ContactsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.adapter_contact_item, parent, false);

        return new ContactsAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactsAdapter.MyViewHolder holder, int position) {
        Contact contact = contacts.get(position);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DialogHelper.showDialog(activity,
                        String.format("Do you want to send this memory %s to %s?", memory.getTitle(), contact.getName()),
                        "Yes",
                        "No",
                        (dialogInterface, i) -> {
                            String memoryMessage = memory.getTitle() + "\n\n" + memory.getMainText();

                            ShareHelper.sendSMSMessage(activity, contact.getName(), contact.getPhoneNumber(), memoryMessage);
                        },
                        (dialogInterface, i) -> {
                            dialog.dismiss();
                        }
                );

            }
        });

        holder.nameTv.setText(contact.getName());
        holder.phoneTv.setText(contact.getPhoneNumber());
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView nameTv;
        TextView phoneTv;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTv = itemView.findViewById(R.id.nameTv);
            phoneTv = itemView.findViewById(R.id.phoneTv);
        }
    }
}
