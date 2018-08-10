package com.pax.paylauncher.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.view.View;

import com.pax.paylauncher.App;

import static android.content.Context.ACTIVITY_SERVICE;
import static com.pax.paylauncher.utils.DeviceUtils.enableKeys;

/**
 * @author ligq
 * @date 2018/8/8
 */

public class ActivityUtils {
    private ActivityUtils() {
        throw new IllegalStateException();
    }

    public static void launchApp(String packageName, View view) {
        launchApp(App.getApp(), packageName, view);
        enableKeys(true);
    }

    public static void launchApp(Context context, String packageName, View view) {
        ActivityOptionsCompat compat = ActivityOptionsCompat.makeScaleUpAnimation(view
                , view.getWidth() / 2, view.getHeight() / 2, 0, 0);
        Intent launchAppIntent = getLaunchAppIntent(context, packageName, true);
        if (launchAppIntent != null) {
            ActivityCompat.startActivity(context, launchAppIntent
                    , compat.toBundle());
        }
    }

    public static void launchDesktop() {
        App.getApp().startActivity(getLaunchAppIntent(App.getApp(), App.getApp().getPackageName()
                , true));
    }

    public static Intent getLaunchAppIntent(Context context, String packageName, boolean isNewTask) {
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(packageName);
        if (intent == null) {
            return null;
        }
        return isNewTask ? intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) : intent;
    }

    public static void clickHome() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addCategory(Intent.CATEGORY_HOME);
        App.getApp().startActivity(intent);
    }

    public static boolean isRestartServiceRunning(Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if ("com.pax.paylauncher.ui.activity.RestartService".equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

}
