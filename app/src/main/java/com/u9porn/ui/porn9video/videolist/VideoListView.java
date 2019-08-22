package com.u9porn.ui.porn9video.videolist;

import com.u9porn.data.db.entity.V9PornItem;
import com.u9porn.ui.BaseView;

import java.util.List;

/**
 * @author flymegoc
 * @date 2017/11/16
 * @describe
 */

public interface VideoListView extends BaseView {
    void loadMoreDataComplete();

    void loadMoreFailed();

    void noMoreData();

    void setMoreData(List<V9PornItem> v9PornItemList);

    void loadData(boolean pullToRefresh, boolean cleanCache, int skipPage);

    void setData(List<V9PornItem> data);

    void setPageData(List<Integer> pageData);

    void updateCurrentPage(int currentPage);

    void showSkipPageLoading();

    void hideSkipPageLoading();
}
