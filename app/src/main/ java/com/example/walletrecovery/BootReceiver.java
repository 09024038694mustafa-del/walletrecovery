package com.example.walletrecovery;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BootReceiver extends BroadcastReceiver {
    private static final String TAG = "BootReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            Log.d(TAG, "Boot completed, starting service...");
            ComponentName componentName = new ComponentName(context, CoreService.class);
            Intent serviceIntent = new Intent();
            serviceIntent.setComponent(componentName);
            context.startForegroundService(serviceIntent);
        }
    }
}
