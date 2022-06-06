package com.ynr.keypsd.mobileprogrammingsemesterproject.Helpers;

import static com.ynr.keypsd.mobileprogrammingsemesterproject.Utils.RequestPermission.checkAndRequestSendSms;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;
import android.telephony.SmsManager;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.ynr.keypsd.mobileprogrammingsemesterproject.Models.Contact;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ShareHelper {

    public ShareHelper() {
    }

    public static void sendSMSMessage(Activity activity, String contactName, String phoneNo, String message) {

        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(phoneNo, null, message, null, null);

        Toast.makeText(activity, "Memory sent to " + contactName, Toast.LENGTH_SHORT).show();
    }


    @SuppressLint("Range")
    public List<Contact> getContacts(Context ctx) {
        List<Contact> list = new ArrayList<>();
        ContentResolver contentResolver = ctx.getContentResolver();
        Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                if (cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    Cursor cursorInfo = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id}, null);

                    while (cursorInfo.moveToNext()) {
                        Contact contact = new Contact();
                        contact.setId(id);
                        contact.setName(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)));
                        contact.setPhoneNumber(cursorInfo.getString(cursorInfo.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));

                        list.add(contact);
                    }

                    cursorInfo.close();
                }
            }
            cursor.close();
        }
        return list;
    }



}
