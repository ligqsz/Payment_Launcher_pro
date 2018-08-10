package com.pax.paylauncher;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import com.pax.paylauncher.utils.CrashHandler;
import com.pax.paylauncher.utils.SpUtils;

import java.util.ArrayList;
import java.util.List;

import static com.pax.paylauncher.ui.activity.MainActivity.TAG;

/**
 * @author ligq
 * @date 2018/8/8
 */

public class App extends Application {
    private static App app;
    private List<Activity> activities;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate: -------app start");
        app = this;
        CrashHandler.getInstance().init(this);
        SpUtils.init(this);
        activities = new ArrayList<>();
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                activities.add(activity);
            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                activities.remove(activity);
            }
        });
    }

    public static App getApp() {
        return app;
    }

    public boolean isRunning() {
        for (Activity activity : activities) {
            Log.i(TAG, "isRunning: activitys---" + activity.getClass().getSimpleName());
        }
        return !activities.isEmpty();
    }

    public void clearActivity() {
        activities.clear();
    }
}
