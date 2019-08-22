package com.u9porn.ui.porn9video.history;

import com.u9porn.data.db.entity.V9PornItem;
import com.u9porn.ui.BaseView;

import java.util.List;

/**
 * @author flymegoc
 * @date 2017/12/22
 */

public interface HistoryView extends BaseView {
    void loadMoreDataComplete();

    void loadMoreFailed();

    void noMoreData();

    void setData(List<V9PornItem> v9PornItemList);

    void setMoreData(List<V9PornItem> v9PornItemList);
}
