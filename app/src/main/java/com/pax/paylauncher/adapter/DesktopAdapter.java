package com.pax.paylauncher.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ResolveInfo;
import android.graphics.Point;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.pax.paylauncher.R;
import com.pax.paylauncher.adapter.common.CommonAdapter;
import com.pax.paylauncher.adapter.common.RecycleItemTouchHelper;
import com.pax.paylauncher.adapter.common.base.ViewHolder;
import com.pax.paylauncher.view.RedPointImageView;

import java.util.Collections;
import java.util.List;

import static com.pax.paylauncher.ui.activity.MainActivity.TAG;
import static com.pax.paylauncher.utils.ActivityUtils.launchApp;

/**
 * @author ligq
 * @date 2018/8/8
 */

@SuppressWarnings("WeakerAccess")
public class DesktopAdapter extends CommonAdapter<ResolveInfo> implements RecycleItemTouchHelper.ItemTouchHelperCallback {
    private Context context;

    public DesktopAdapter(Context context, int layoutId, List<ResolveInfo> datas) {
        super(context, layoutId, datas);
        this.context = context;
    }

    @Override
    protected void convert(ViewHolder holder, final ResolveInfo info, final int position) {
        Log.d(TAG, "packageName:" + info.activityInfo.packageName);
        final FrameLayout item = holder.getView(R.id.item_app);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                getScreenWidth() / 3);
        item.setLayoutParams(params);
        holder.setText(R.id.app_name, info.loadLabel(context.getPackageManager()).toString());
        holder.setImageDrawable(R.id.app_icon, info.loadIcon(context.getPackageManager()));
        RedPointImageView clickImgView = holder.getView(R.id.app_icon);
        if (position % 2 == 0) {
            clickImgView.setPointText(null);
        } else {
            clickImgView.setPointText(position + "");
        }
        item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (position % 2 != 0) {
                    int i = 1 / 0;
                }
                launchApp(info.activityInfo.packageName, view);
            }
        });
    }

    @SuppressLint("ObsoleteSdkInt")
    public int getScreenWidth() {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (wm == null) {
            return context.getResources().getDisplayMetrics().widthPixels;
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
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (wm == null) {
            return context.getResources().getDisplayMetrics().heightPixels;
        }
        Point point = new Point();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            wm.getDefaultDisplay().getRealSize(point);
        } else {
            wm.getDefaultDisplay().getSize(point);
        }
        return point.y;
    }

    @Override
    public void onItemDelete(int position) {
        mDatas.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public void onMove(int fromPosition, int toPosition) {
        //交换数据
        Collections.swap(mDatas, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
    }
}
