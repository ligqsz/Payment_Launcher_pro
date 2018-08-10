package com.pax.paylauncher.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import static com.pax.paylauncher.ui.activity.MainActivity.TAG;

/**
 * @author ligq
 * @date 2018/8/10
 */

public class SpUtils {
    private static SharedPreferences sp;

    public static SharedPreferences init(Context context) {
        synchronized (SpUtils.class) {
            if (sp == null) {
                sp = context.getSharedPreferences("launcher_running", Context.MODE_PRIVATE);
            }
            return sp;
        }
    }

    public static void saveRunning(boolean isRunning) {
        Log.i(TAG, "saveRunning: " + isRunning);
        sp.edit().putBoolean("launcher_running", isRunning).apply();
    }

    public static boolean isRunning() {
        return sp.getBoolean("launcher_running", false);
    }
}
