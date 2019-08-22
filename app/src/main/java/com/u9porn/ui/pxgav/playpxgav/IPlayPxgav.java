package com.u9porn.ui.pxgav.playpxgav;

/**
 * @author flymegoc
 * @date 2018/1/30
 */

public interface IPlayPxgav {
    void parseVideoUrl(String url, String pId, boolean pullToRefresh);

    String getVideoCacheProxyUrl(String originalVideoUrl);
}
