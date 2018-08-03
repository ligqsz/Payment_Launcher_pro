package com.pax.paylauncher.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.pax.paylauncher.R;
import com.pax.paylauncher.adapter.common.CommonAdapter;
import com.pax.paylauncher.adapter.common.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ligq
 */
public class MainActivity extends AppCompatActivity {
    private static final String TAG = "test";
    private List<ResolveInfo> apps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadApps();
        Log.i(TAG, "onCreate:apps size = " + apps.size());
        initView();

    }

    private void initView() {
        RecyclerView rvDesktop = findViewById(R.id.rv_desktop);
        GridLayoutManager manager = new GridLayoutManager(this, 3);
        rvDesktop.setLayoutManager(manager);
        rvDesktop.setAdapter(new CommonAdapter<ResolveInfo>(this, R.layout.item_apps, apps) {
            @Override
            protected void convert(ViewHolder holder, final ResolveInfo info, int position) {
                Log.d(TAG, "packageName:" + info.activityInfo.packageName);
                FrameLayout item = holder.getView(R.id.item_app);
                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        getScreenWidth() / 3);
                item.setLayoutParams(params);
                holder.setText(R.id.app_name, info.loadLabel(getPackageManager()).toString());
                holder.setImageDrawable(R.id.app_icon, info.loadIcon(getPackageManager()));
                item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        launchApp(info.activityInfo.packageName);
                    }
                });
            }
        });
    }

    @Override
    public void onBackPressed() {
        //do nothing
    }

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

    public void launchApp(final String packageName) {
        startActivity(getLaunchAppIntent(packageName, true));
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
        for (ResolveInfo resolveInfo : resolveInfos) {
            String packageName = resolveInfo.activityInfo.packageName;
            if (packageName.contains("com.pax")
                    && !packageName.equals(getPackageName())) {
                apps.add(resolveInfo);
            }
        }
    }
}
