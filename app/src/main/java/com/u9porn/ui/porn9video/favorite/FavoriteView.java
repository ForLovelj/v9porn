package com.u9porn.ui.porn9video.favorite;

import com.u9porn.data.db.entity.V9PornItem;
import com.u9porn.ui.BaseView;

import java.util.List;

/**
 * @author flymegoc
 * @date 2017/11/25
 * @describe
 */

public interface FavoriteView extends BaseView {
    void setFavoriteData(List<V9PornItem> v9PornItemList);

    void loadMoreDataComplete();

    void loadMoreFailed();

    void noMoreData();

    void setMoreData(List<V9PornItem> v9PornItemList);

    void deleteFavoriteSucc(String message);
    void deleteFavoriteError(String message);
    void showDeleteDialog();
}
