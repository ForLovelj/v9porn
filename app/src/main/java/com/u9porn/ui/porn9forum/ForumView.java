package com.u9porn.ui.porn9forum;

import com.u9porn.data.model.F9PronItem;
import com.u9porn.data.model.PinnedHeaderEntity;
import com.u9porn.ui.BaseView;

import java.util.List;

/**
 * @author flymegoc
 * @date 2018/1/23
 */

public interface ForumView extends BaseView {
    void setForumListData(List<F9PronItem> f9PronItemList);

    void setForumIndexListData(List<PinnedHeaderEntity<F9PronItem>> pinnedHeaderEntityList);

    void loadMoreFailed();

    void noMoreData();

    void setMoreData(List<F9PronItem> f9PronItemList);

    void loadData(boolean pullToRefresh);
}
