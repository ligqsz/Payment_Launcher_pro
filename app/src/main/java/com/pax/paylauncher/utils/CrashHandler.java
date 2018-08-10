package com.pax.paylauncher.utils;

import android.content.Context;
import android.util.Log;

import static com.pax.paylauncher.ui.activity.MainActivity.TAG;

/**
 * @author ligq
 * @date 2018/8/10
 */

public class CrashHandler implements Thread.UncaughtExceptionHandler {
    private static CrashHandler crashHandler = new CrashHandler();

    //使用饿汉单例模式
    public static CrashHandler getInstance() {
        return crashHandler;
    }

    public void init(Context context) {
        //把当前的crash捕获器设置成默认的crash捕获器
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        Log.e(TAG, "uncaughtException: ", ex);
        ActivityUtils.launchDesktop();
        android.os.Process.killProcess(android.os.Process.myPid());
    }
}
