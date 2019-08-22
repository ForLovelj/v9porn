package com.u9porn.ui.proxy;

/**
 * @author flymegoc
 * @date 2018/1/20
 */

public interface IProxy {
    void testProxy(String proxyIpAddress, int proxyPort);

    void parseXiCiDaiLi(boolean pullToRefresh);

    boolean isSetPorn91VideoAddress();

    void exitTest();

    String getProxyIpAddress();

    int getProxyPort();

    void setProxyIpAddress(String proxyIpAddress);

    void setProxyPort(int proxyPort);

    void setOpenHttpProxy(boolean openHttpProxy);
}
