package com.pax.paylauncher.ui.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.pax.dal.ISys;
import com.pax.dal.entity.ENavigationKey;
import com.pax.neptunelite.api.NeptuneLiteUser;
import com.pax.paylauncher.R;
import com.pax.paylauncher.adapter.common.CommonAdapter;
import com.pax.paylauncher.adapter.common.RecycleItemTouchHelper;
import com.pax.paylauncher.adapter.common.base.ViewHolder;
import com.pax.paylauncher.view.RedPointImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ligq
 */
public class MainActivity extends AppCompatActivity {
    public static final String TAG = "test";
    private List<ResolveInfo> apps;
    private RedPointImageView clickImgView;
    private Vibrator vibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadApps();
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        Log.i(TAG, "onCreate:apps size = " + apps.size());
        initView();
    }

    private void initView() {
        RecyclerView rvDesktop = findViewById(R.id.rv_desktop);
        GridLayoutManager manager = new GridLayoutManager(this, 3);
        rvDesktop.setLayoutManager(manager);
        final CommonAdapter<ResolveInfo> adapter = new CommonAdapter<ResolveInfo>(this, R.layout.item_apps, apps) {
            @Override
            protected void convert(ViewHolder holder, final ResolveInfo info, final int position) {
                Log.d(TAG, "packageName:" + info.activityInfo.packageName);
                final FrameLayout item = holder.getView(R.id.item_app);
                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        getScreenWidth() / 3);
                item.setLayoutParams(params);
                clickImgView = holder.getView(R.id.app_icon);
                holder.setText(R.id.app_name, info.loadLabel(getPackageManager()).toString());
                holder.setImageDrawable(R.id.app_icon, info.loadIcon(getPackageManager()));
                if (position % 2 == 0) {
                    clickImgView.setPointText(null);
                } else {
                    clickImgView.setPointText(position + "");
                }
                item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        launchApp(info.activityInfo.packageName, view);
                    }
                });
            }
        };
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

    @SuppressLint("ObsoleteSdkInt")
    public int getScreenWidth() {
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        if (wm == null) {
            return getResources().getDisplayMetrics().widthPixels;
        }
        Point point = new Point();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            wm.getDefaultDisplay().getRealSize(point);
        } else {
            wm.getDefaultDisplay().getSize(point);
        }
        return point.x;
    }

    @SuppressLint("ObsoleteSdkInt")
    public int getScreenHeight() {
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        if (wm == null) {
            return getResources().getDisplayMetrics().heightPixels;
        }
        Point point = new Point();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            wm.getDefaultDisplay().getRealSize(point);
        } else {
            wm.getDefaultDisplay().getSize(point);
        }
        return point.y;
    }

    public void launchApp(final String packageName, View view) {
        ActivityOptionsCompat compat = ActivityOptionsCompat.makeScaleUpAnimation(view
                , view.getWidth() / 2, view.getHeight() / 2, 0, 0);
        Intent launchAppIntent = getLaunchAppIntent(packageName, true);
        if (launchAppIntent != null) {
            enableKeys(true);
            ActivityCompat.startActivity(this, launchAppIntent
                    , compat.toBundle());
        }
//        <p>startActivity(getLaunchAppIntent(packageName, true));</p>
    }

    private Intent getLaunchAppIntent(final String packageName, final boolean isNewTask) {
        Intent intent = getPackageManager().getLaunchIntentForPackage(packageName);
        if (intent == null) {
            return null;
        }
        return isNewTask ? intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) : intent;
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
//        View view = getCurrentFocus();
//        if (view != null) {
//            ActivityOptionsCompat compat = ActivityOptionsCompat.makeBasic();
//            Intent launchAppIntent = getLaunchAppIntent(getPackageName(), true);
//            if (launchAppIntent != null) {
//                ActivityCompat.startActivity(this, launchAppIntent
//                        , compat.toBundle());
//            }
//        }
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

    /**
     * enable/disable status bar
     *
     * @param enable true/false
     */
    public void enableStatusBar(boolean enable) {
        getSys().enableStatusBar(enable);
    }

    /**
     * enable/disable home key
     *
     * @param enable true/false
     */
    public void enableHomeKey(boolean enable) {
        getSys().enableNavigationKey(ENavigationKey.HOME, enable);
    }

    /**
     * enable/disable recent key
     *
     * @param enable true/false
     */
    public void enableRecentKey(boolean enable) {
        getSys().enableNavigationKey(ENavigationKey.RECENT, enable);
    }

    public ISys getSys() {
        try {
            return NeptuneLiteUser.getInstance().getDal(getApplicationContext()).getSys();
        } catch (Exception e) {
            return null;
        }
    }

    public void enableKeys(boolean enable) {
//        enableHomeKey(enable);
//        enableRecentKey(enable);
//        enableStatusBar(enable);
    }

    public void clickHome() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
    }
}
