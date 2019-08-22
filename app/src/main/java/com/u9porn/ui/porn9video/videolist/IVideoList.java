package com.u9porn.ui.porn9video.videolist;

/**
 * @author flymegoc
 * @date 2017/11/27
 * @describe
 */

public interface IVideoList {
    /**
     * 加载数据
     *
     * @param pullToRefresh 刷新
     * @param cleanCache    清除缓存
     * @param category      类别
     * @param skipPage      跳页 0为正常
     */
    void loadVideoListData(boolean pullToRefresh, boolean cleanCache, String category, int skipPage);

    int getPlayBackEngine();

    boolean isOpenSkipPage();
}
