package com.pax.paylauncher.ui.activity;

import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import com.pax.paylauncher.R;
import com.pax.paylauncher.adapter.DesktopAdapter;
import com.pax.paylauncher.adapter.common.RecycleItemTouchHelper;
import com.pax.paylauncher.utils.ActivityUtils;
import com.pax.paylauncher.utils.SpUtils;

import java.util.ArrayList;
import java.util.List;

import static com.pax.paylauncher.utils.ActivityUtils.clickHome;
import static com.pax.paylauncher.utils.DeviceUtils.enableKeys;

/**
 * @author ligq
 */
public class MainActivity extends AppCompatActivity {
    public static final String TAG = "test";
    private List<ResolveInfo> apps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (ActivityUtils.isRestartServiceRunning(this)) {
            Log.i(TAG, "Restart Service Running:" + true);
        } else {
//            startService(new Intent(this, RestartService.class));
            Log.i(TAG, "Restart Service Running:" + false);
        }
        loadApps();
        Log.i(TAG, "onCreate:apps size = " + apps.size());
        initView();
    }

    private void initView() {
        RecyclerView rvDesktop = findViewById(R.id.rv_desktop);
        GridLayoutManager manager = new GridLayoutManager(this, 3);
        rvDesktop.setLayoutManager(manager);
        DesktopAdapter adapter = new DesktopAdapter(this, R.layout.item_apps, apps);
        rvDesktop.setAdapter(adapter);

        ItemTouchHelper.Callback callback = new RecycleItemTouchHelper(adapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(rvDesktop);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.i(TAG, "onKeyDown: KeyCode=" + keyCode + "\n" +
                "keyEvent:" + event.getAction());
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        //do nothing
    }

    private void loadApps() {
        apps = new ArrayList<>();
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> resolveInfos = getPackageManager().queryIntentActivities(mainIntent, 0);
        mainIntent.addCategory(Intent.CATEGORY_HOME);
        List<ResolveInfo> launchers = getPackageManager().queryIntentActivities(mainIntent, 0);
        List<String> launcherPkg = new ArrayList<>();
        if (!launchers.isEmpty()) {
            for (ResolveInfo launcher : launchers) {
                Log.i(TAG, "loadApps: launcher package:" + launcher.activityInfo.packageName + "\n" +
                        "contains:" + resolveInfos.contains(launcher));
                launcherPkg.add(launcher.activityInfo.packageName);
            }
        }
        for (ResolveInfo resolveInfo : resolveInfos) {
            String packageName = resolveInfo.activityInfo.packageName;
            if (/*packageName.contains("com.pax") && */!launcherPkg.contains(packageName)) {
                apps.add(resolveInfo);
            }
        }
    }

    @Override
    protected void onResume() {
        clickHome();
        enableKeys(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
        super.onResume();
    }
}
