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

        int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        boolean isCharging = status == BATTERY_STATUS_CHARGING;

        if(isCharging)
            FirebaseDatabaseHelper.backupMemoriesInFirebaseDatabase(context);

    }
}
