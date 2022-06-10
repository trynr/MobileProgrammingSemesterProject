package com.ynr.keypsd.mobileprogrammingsemesterproject.BroadcastReceivers;

import static android.os.BatteryManager.BATTERY_STATUS_CHARGING;
import static android.os.BatteryManager.BATTERY_STATUS_FULL;

import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;
import android.util.Log;
import android.widget.Toast;

import com.ynr.keypsd.mobileprogrammingsemesterproject.Helpers.FirebaseDatabaseHelper;

public class OnChargedBroadcastReceiver extends android.content.BroadcastReceiver  {
    @Override
    public void onReceive(Context context, Intent intent) {
        StringBuilder sb = new StringBuilder();
        sb.append("Action: " + intent.getAction() + "\n");

        int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        boolean isCharging = status == BATTERY_STATUS_CHARGING || status == BATTERY_STATUS_FULL;

        //Toast.makeText(context, "isCharging: " + isCharging, Toast.LENGTH_SHORT).show();
        return;
        // TODO kaldır
        //if(isCharging)
          //  FirebaseDatabaseHelper.backupMemoriesInFirebaseDatabase(context);

    }
}
