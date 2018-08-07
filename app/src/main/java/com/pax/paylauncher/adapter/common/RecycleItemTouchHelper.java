package com.pax.paylauncher.adapter.common;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;

/**
 * @author ligq
 * @date 2018/8/7
 */

public class RecycleItemTouchHelper extends ItemTouchHelper.Callback {
    private static final String TAG = "RecycleItemTouchHelper";
    private final ItemTouchHelperCallback helperCallback;

    public RecycleItemTouchHelper(ItemTouchHelperCallback helperCallback) {
        this.helperCallback = helperCallback;
    }

    /**
     * 设置滑动类型标记
     *
     * @param recyclerView
     * @param viewHolder
     * @return 返回一个整数类型的标识，用于判断Item那种移动行为是允许的
     */
    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        Log.e(TAG, "getMovementFlags: ");
        //START  右向左 END左向右 LEFT  向左 RIGHT向右  UP向上
        //如果某个值传0，表示不触发该操作，次数设置支持上下拖拽，支持向右滑动
        return makeMovementFlags(ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.START | ItemTouchHelper.END
                , 0);
    }

    /**
     * Item是否支持长按拖动
     *
     * @return true  支持长按操作
     * false 不支持长按操作
     */
    @Override
    public boolean isLongPressDragEnabled() {
        return super.isLongPressDragEnabled();
    }

    /**
     * Item是否支持滑动
     *
     * @return true  支持滑动操作
     * false 不支持滑动操作
     */
    @Override
    public boolean isItemViewSwipeEnabled() {
        return super.isItemViewSwipeEnabled();
    }

    /**
     * 拖拽切换Item的回调
     *
     * @param recyclerView
     * @param viewHolder
     * @param target
     * @return 如果Item切换了位置，返回true；反之，返回false
     */
    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        Log.e(TAG, "onMove: ");
        helperCallback.onMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }

    /**
     * 滑动Item
     *
     * @param viewHolder
     * @param direction  Item滑动的方向
     */
    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        Log.e(TAG, "onSwiped: ");
        helperCallback.onItemDelete(viewHolder.getAdapterPosition());
    }

    /**
     * Item被选中时候回调
     *
     * @param viewHolder
     * @param actionState 当前Item的状态
     *                    ItemTouchHelper.ACTION_STATE_IDLE   闲置状态
     *                    ItemTouchHelper.ACTION_STATE_SWIPE  滑动中状态
     *                    ItemTouchHelper#ACTION_STATE_DRAG   拖拽中状态
     */
    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        super.onSelectedChanged(viewHolder, actionState);
    }

    public interface ItemTouchHelperCallback {
        void onItemDelete(int positon);

        void onMove(int fromPosition, int toPosition);
    }
}
