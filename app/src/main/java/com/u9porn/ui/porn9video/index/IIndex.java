package com.u9porn.ui.porn9video.index;

/**
 * @author flymegoc
 * @date 2017/11/27
 * @describe
 */

public interface IIndex {
    void loadIndexData(final boolean pullToRefresh, boolean cleanCache);

    int getPlayBackEngine();
}
