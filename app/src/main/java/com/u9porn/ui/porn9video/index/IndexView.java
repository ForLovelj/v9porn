package com.u9porn.ui.porn9video.index;

import com.u9porn.data.db.entity.V9PornItem;
import com.u9porn.ui.BaseView;

import java.util.List;

/**
 * @author flymegoc
 * @date 2017/11/15
 * @describe
 */

public interface IndexView extends BaseView {

    void loadData(boolean pullToRefresh,boolean cleanCache);

    void setData(List<V9PornItem> data);
}
