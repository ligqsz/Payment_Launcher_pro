package com.pax.paylauncher.adapter.common.base;

/**
 * @author ligq
 * @date 2018/8/3
 */

public interface ItemViewDelegate<T> {

    int getItemViewLayoutId();

    boolean isForViewType(T item, int position);

    void convert(ViewHolder holder, T t, int position);
}
