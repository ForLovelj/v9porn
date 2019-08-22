package com.u9porn.ui.download;

import com.u9porn.data.db.entity.V9PornItem;
import com.u9porn.ui.porn9video.favorite.IBaseFavorite;

/**
 * @author flymegoc
 * @date 2017/11/26
 * @describe
 */

public interface IBaseDownload extends IBaseFavorite{
    void downloadVideo(V9PornItem v9PornItem, boolean isForceReDownload);
}
