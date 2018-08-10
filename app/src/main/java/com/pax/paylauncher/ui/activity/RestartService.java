package com.pax.paylauncher.ui.activity;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.util.Log;

import com.pax.paylauncher.utils.SpUtils;

import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import static com.pax.paylauncher.ui.activity.MainActivity.TAG;

/**
 * @author ligq
 */
public class RestartService extends Service {

    private ScheduledThreadPoolExecutor task;

    public RestartService() {
        //do nothing
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "onStartCommand: ----------");
        startTask(2);
        return super.onStartCommand(intent, flags, startId);
    }


    private void startTask(int time) {
        Runnable command = new Runnable() {
            @Override
            public void run() {
                boolean appRunning = SpUtils.isRunning();
                Log.i(TAG, "onStartCommand: App is running:" + appRunning);
                if (!appRunning) {
                    startActivity(getLaunchAppIntent(getPackageName(), true));
                }
            }
        };
        task = new ScheduledThreadPoolExecutor(2, new ThreadFactory() {
            @Override
            public Thread newThread(@NonNull Runnable r) {
                Thread thread = new Thread(r, "ScheduledThreadPoolExecutor Task");
                thread.setPriority(Thread.MIN_PRIORITY);
                thread.setDaemon(true);
                return thread;
            }
        });
        task.scheduleAtFixedRate(command, time, time, TimeUnit.SECONDS);

    }

    /**
     * 方法描述：判断某一应用是否正在运行
     * Created by cafeting on 2017/2/4.
     *
     * @param context     上下文
     * @param packageName 应用的包名
     * @return true 表示正在运行，false 表示没有运行
     */
    public boolean isAppRunning(Context context, String packageName) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(100);
        if (list.isEmpty()) {
            return false;
        }
        for (ActivityManager.RunningTaskInfo info : list) {
            if (info.baseActivity.getPackageName().equals(packageName)) {
                return true;
            }
        }
        return false;
    }

    private Intent getLaunchAppIntent(final String packageName, final boolean isNewTask) {
        Intent intent = getPackageManager().getLaunchIntentForPackage(packageName);
        if (intent == null) {
            return null;
        }
        return isNewTask ? intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) : intent;
    }
}
