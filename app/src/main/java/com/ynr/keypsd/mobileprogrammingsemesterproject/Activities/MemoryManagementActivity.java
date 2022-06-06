package com.ynr.keypsd.mobileprogrammingsemesterproject.Activities;

import static com.ynr.keypsd.mobileprogrammingsemesterproject.Utils.Constants.SP_NAME;
import static com.ynr.keypsd.mobileprogrammingsemesterproject.Utils.Constants.USER_ID;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.ynr.keypsd.mobileprogrammingsemesterproject.Adapters.MemoryAdapter;
import com.ynr.keypsd.mobileprogrammingsemesterproject.BroadcastReceivers.OnChargedBroadcastReceiver;
import com.ynr.keypsd.mobileprogrammingsemesterproject.Helpers.SqliteDatabaseHelper;
import com.ynr.keypsd.mobileprogrammingsemesterproject.Models.Memory;
import com.ynr.keypsd.mobileprogrammingsemesterproject.Popups.SetPasswordPopup;
import com.ynr.keypsd.mobileprogrammingsemesterproject.R;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MemoryManagementActivity extends AppCompatActivity implements View.OnClickListener {

    RecyclerView memoriesRecyclerView;
    Button addMemoryButton;

    List<Memory> memoryList;
    Dialog dialog;
    SqliteDatabaseHelper databaseHelper;
    SharedPreferences mPrefs;
    SharedPreferences.Editor prefsEditor;
    OnChargedBroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memory_management);

        databaseHelper = new SqliteDatabaseHelper(MemoryManagementActivity.this);
        databaseHelper.createTable();
        define();
        memoryList = databaseHelper.getMemoriesFromDatabase();
        setAdapterToRecyclerView(memoryList);
        setUserId();
        registerBroadcastReceiver();

    }

    private void registerBroadcastReceiver(){
        broadcastReceiver = new OnChargedBroadcastReceiver();

        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(broadcastReceiver, filter);
    }

    private void setAdapterToRecyclerView(List<Memory> memoryList) {
        MemoryAdapter memoryAdapter = new MemoryAdapter(MemoryManagementActivity.this,
                                                getApplicationContext(),
                                                memoryList,
                                                dialog,
                                                databaseHelper
                                        );
        memoriesRecyclerView.setAdapter(memoryAdapter);
        memoriesRecyclerView.setLayoutManager(new LinearLayoutManager(MemoryManagementActivity.this));
    }

    private void define(){
        memoriesRecyclerView = findViewById(R.id.memoriesRecyclerView);
        addMemoryButton = findViewById(R.id.addMemoryButton);

        addMemoryButton.setOnClickListener(this);
        memoryList = new ArrayList<>();
        mPrefs = getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        prefsEditor = mPrefs.edit();
        dialog = new Dialog(MemoryManagementActivity.this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_memory_management, menu);

        MenuItem keyItem = menu.findItem(R.id.password_for_all_button);
        keyItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                SetPasswordPopup setPasswordPopup = new SetPasswordPopup(
                        MemoryManagementActivity.this,
                               dialog);
                setPasswordPopup.show();

                return false;
            }
        });

        return true;
    }

    private void setUserId() {
        String userID = mPrefs.getString(USER_ID, "");
        if(!userID.equals(""))
            return;

        prefsEditor.putString(USER_ID, UUID.randomUUID().toString());
        prefsEditor.commit();
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.addMemoryButton){
            Intent intent = new Intent(MemoryManagementActivity.this, AddOrUpdateMemoryActivity.class);
            startActivity(intent);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }
}