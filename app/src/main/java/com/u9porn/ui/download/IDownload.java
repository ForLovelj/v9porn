package com.u9porn.ui.download;

import com.u9porn.data.db.entity.V9PornItem;

import java.util.List;

/**
 * @author flymegoc
 * @date 2017/11/27
 * @describe
 */

public interface IDownload extends IBaseDownload {
    void downloadVideo(V9PornItem v9PornItem, boolean isForceReDownload, DownloadPresenter.DownloadListener downloadListener);

    void loadDownloadingData();

    void loadFinishedData();

    void deleteDownloadingTask(V9PornItem v9PornItem);

    void deleteDownloadedTask(V9PornItem v9PornItem, boolean isDeleteFile);

    V9PornItem findUnLimit91PornItemByDownloadId(int downloadId);

    List<V9PornItem> loadDownloadingDatas();

    void updateV9PornItem(V9PornItem v9PornItem);

    String getCustomDownloadVideoDirPath();
}
