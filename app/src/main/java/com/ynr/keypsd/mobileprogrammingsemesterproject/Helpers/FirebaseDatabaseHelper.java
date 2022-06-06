package com.ynr.keypsd.mobileprogrammingsemesterproject.Helpers;

import static com.ynr.keypsd.mobileprogrammingsemesterproject.Utils.Constants.ALREADY_IN_FIREBASE;
import static com.ynr.keypsd.mobileprogrammingsemesterproject.Utils.Constants.SP_NAME;
import static com.ynr.keypsd.mobileprogrammingsemesterproject.Utils.Constants.USER_ID;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ynr.keypsd.mobileprogrammingsemesterproject.Models.Memory;

import java.util.List;
import java.util.UUID;

public class FirebaseDatabaseHelper {

    public static void backupMemoriesInFirebaseDatabase(Context context) {

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        SharedPreferences mPrefs = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        List<Memory> memories = new SqliteDatabaseHelper(context).getMemoriesFromDatabase();

        String userID = mPrefs.getString(USER_ID, "");
        boolean alreadyInFirebase = mPrefs.getBoolean(ALREADY_IN_FIREBASE, false);
        if(userID.equals(""))
            return;
        if(alreadyInFirebase)
            return;

        mDatabase.child("Memories").child(userID).setValue(memories).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(context, "Memories backup succesful.", Toast.LENGTH_LONG).show();
                prefsEditor.putBoolean(ALREADY_IN_FIREBASE, true);
                prefsEditor.commit();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "Error backing up memories: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }
}
