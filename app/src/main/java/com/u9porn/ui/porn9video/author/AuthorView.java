package com.u9porn.ui.porn9video.author;

import com.u9porn.data.db.entity.V9PornItem;
import com.u9porn.ui.BaseView;

import java.util.List;

/**
 *
 * @author flymegoc
 * @date 2018/1/8
 */

public interface AuthorView extends BaseView{
    void loadMoreDataComplete();

    void loadMoreFailed();

    void noMoreData();

    void setMoreData(List<V9PornItem> v9PornItemList);

    void setData(List<V9PornItem> data);
}
