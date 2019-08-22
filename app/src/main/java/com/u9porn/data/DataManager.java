package com.u9porn.data;

import com.u9porn.data.db.DbHelper;
import com.u9porn.data.model.User;
import com.u9porn.data.network.ApiHelper;
import com.u9porn.data.prefs.PreferencesHelper;

/**
 * @author flymegoc
 * @date 2018/3/4
 */

public interface DataManager extends DbHelper, ApiHelper, PreferencesHelper {
    String getVideoCacheProxyUrl(String originalVideoUrl);

    boolean isVideoCacheByProxy(String originalVideoUrl);

    void existLogin();

    void resetPorn91VideoWatchTime(boolean reset);

    User getUser();

    boolean isUserLogin();
}
