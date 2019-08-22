package com.u9porn.ui.download;

import com.u9porn.data.db.entity.V9PornItem;
import com.u9porn.ui.BaseView;

import java.util.List;

/**
 * @author flymegoc
 * @date 2017/11/27
 * @describe
 */

public interface DownloadView extends BaseView {
    void setDownloadingData(List<V9PornItem> v9PornItems);

    void setFinishedData(List<V9PornItem> v9PornItems);
}
