package com.pax.paylauncher.adapter.common;

import android.content.Context;
import android.view.LayoutInflater;

import com.pax.paylauncher.adapter.common.base.ItemViewDelegate;
import com.pax.paylauncher.adapter.common.base.ViewHolder;

import java.util.Collections;
import java.util.List;

/**
 * @author ligq
 * @date 2018/8/3
 */

@SuppressWarnings({"WeakerAccess", "unused"})
public abstract class CommonAdapter<T> extends MultiItemTypeAdapter<T> implements RecycleItemTouchHelper.ItemTouchHelperCallback {
    protected int mLayoutId;
    protected LayoutInflater mInflater;

    public CommonAdapter(final Context context, final int layoutId, List<T> datas) {
        super(context, datas);
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mLayoutId = layoutId;
        mDatas = datas;

        addItemViewDelegate(new ItemViewDelegate<T>() {
            @Override
            public int getItemViewLayoutId() {
                return layoutId;
            }

            @Override
            public boolean isForViewType(T item, int position) {
                return true;
            }

            @Override
            public void convert(ViewHolder holder, T t, int position) {
                CommonAdapter.this.convert(holder, t, position);
            }
        });
    }

    protected abstract void convert(ViewHolder holder, T t, int position);

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
