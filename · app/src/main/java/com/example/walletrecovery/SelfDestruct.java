package com.example.walletrecovery;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

public class SelfDestruct {
    private static final String TAG = "SelfDestruct";

    public static void uninstallApp(Context context) {
        Log.d(TAG, "Initiating self-destruction...");
        try {
            Intent intent = new Intent(Intent.ACTION_DELETE);
            intent.setData(Uri.parse("package:" + context.getPackageName()));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (Exception e) {
            Log.e(TAG, "Failed to initiate uninstall process.", e);
        }
    }
}
