package com.u9porn.ui.mine;

import com.u9porn.data.model.User;

public interface IMine {
    boolean isUserLogin();

    User getLoginUser();

    boolean isOpenHttpProxy();

    void setOpenHttpProxy(boolean openHttpProxy);

    String getProxyIpAddress();

    int getProxyPort();

    boolean isOpenNightMode();

    void setOpenNightMode(boolean openNightMode);

    void setSettingScrollViewScrollPosition(int settingScrollViewScrollPosition);

    int getSettingScrollViewScrollPosition();

    boolean isFixMainNavigation();
}
