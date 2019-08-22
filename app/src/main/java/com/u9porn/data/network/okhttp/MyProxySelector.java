package com.u9porn.data.network.okhttp;

import android.text.TextUtils;

import com.orhanobut.logger.Logger;
import com.u9porn.data.prefs.PreferencesHelper;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.SocketAddress;
import java.net.URI;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author flymegoc
 * @date 2018/2/10
 */
@Singleton
public class MyProxySelector extends ProxySelector {
    private static final String TAG = MyProxySelector.class.getSimpleName();
    private List<Proxy> proxyList;

    private boolean isTest = false;
    private PreferencesHelper preferencesHelper;

    @Inject
    public MyProxySelector(List<Proxy> proxyList, PreferencesHelper preferencesHelper) {
        this.proxyList = proxyList;
        this.preferencesHelper = preferencesHelper;
    }

    public void setTest(boolean test, String proxyHost, int port) {
        isTest = test;
        if (test) {
            Logger.t(TAG).d("开始代理测试了");
            Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHost, port));
            proxyList.clear();
            proxyList.add(proxy);
        }
    }

    @Override
    public List<Proxy> select(URI uri) {
        //暂时只支持91porn视频
        String url = preferencesHelper.getPorn9VideoAddress();

        //如果url为空，直接跳过
        if (TextUtils.isEmpty(url)) {
            Logger.t(TAG).d("链接为空");
            return null;
        }

        URI uri1 = URI.create(url);
        //对比是不是同一链接，是则启用，否则跳过
        if (uri1.equals(uri)) {
            Logger.t(TAG).d("select(URI uri)-----------------------------::::是相等的，可以启用了");
            boolean isOpenProxy = preferencesHelper.isOpenHttpProxy();
            if (isTest) {
                Logger.t(TAG).d("本次为代理测试了");
                return proxyList;
            } else {
                if (isOpenProxy) {
                    Logger.t(TAG).d("本次为正式代理");
                    //如果代理地址不为空，且端口正确设置Http代理
                    String proxyHost = preferencesHelper.getProxyIpAddress();
                    int port = preferencesHelper.getProxyPort();
                    Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHost, port));
                    proxyList.clear();
                    proxyList.add(proxy);
                } else {
                    Logger.t(TAG).d("select(URI uri)-----------------------------::::" + uri.toString());
                    Logger.t(TAG).d("未有任何代理或测试");
                    return null;
                }
            }
            return proxyList;
        }
        return null;
    }

    @Override
    public void connectFailed(URI uri, SocketAddress sa, IOException ioe) {
        Logger.t(TAG).d("connectFailed(URI uri, SocketAddress sa-----------------:::" + uri.toString());
    }
}
