package com.u9porn.ui.proxy;

import android.arch.lifecycle.Lifecycle;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;
import com.trello.rxlifecycle2.LifecycleProvider;
import com.u9porn.data.DataManager;
import com.u9porn.data.model.BaseResult;
import com.u9porn.data.model.ProxyModel;
import com.u9porn.rxjava.CallBackWrapper;
import com.u9porn.rxjava.RxSchedulersHelper;
import com.u9porn.utils.AddressHelper;
import com.u9porn.utils.RegexUtils;
import com.u9porn.constants.Constants;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;

/**
 * @author flymegoc
 * @date 2018/1/20
 */

public class ProxyPresenter extends MvpBasePresenter<ProxyView> implements IProxy {

    private static final String TAG = ProxyPresenter.class.getSimpleName();
    private long successTime = 0;
    private LifecycleProvider<Lifecycle.Event> provider;
    private int totalPage = 1;
    private int page = 1;
    private DataManager dataManager;

    @Inject
    public ProxyPresenter(LifecycleProvider<Lifecycle.Event> provider, DataManager dataManager) {
        this.provider = provider;
        this.dataManager = dataManager;
    }

    @Override
    public void testProxy(String proxyIpAddress, int proxyPort) {
        if (RegexUtils.isIP(proxyIpAddress) && proxyPort < Constants.PROXY_MAX_PORT && proxyPort > 0) {
            dataManager.testProxy(proxyIpAddress, proxyPort)
                    .compose(RxSchedulersHelper.<Boolean>ioMainThread())
                    .subscribe(new CallBackWrapper<Boolean>() {
                        @Override
                        public void onBegin(Disposable d) {
                            successTime = System.currentTimeMillis();
                            ifViewAttached(new ViewAction<ProxyView>() {
                                @Override
                                public void run(@NonNull ProxyView view) {
                                    view.showLoading(false);
                                }
                            });
                        }

                        @Override
                        public void onSuccess(final Boolean isSuccess) {
                            successTime = System.currentTimeMillis() - successTime;
                            ifViewAttached(new ViewAction<ProxyView>() {
                                @Override
                                public void run(@NonNull ProxyView view) {
                                    if (isSuccess) {
                                        view.showContent();
                                        view.testProxySuccess("测试成功，用时：" + successTime + " ms");
                                    } else {
                                        view.testProxyError("测试失败：可以访问，但无法获取对应数据");
                                    }
                                }
                            });
                        }

                        @Override
                        public void onError(final String msg, int code) {
                            ifViewAttached(new ViewAction<ProxyView>() {
                                @Override
                                public void run(@NonNull ProxyView view) {
                                    view.testProxyError("测试失败：" + msg);
                                }
                            });
                        }
                    });
        } else {
            ifViewAttached(new ViewAction<ProxyView>() {
                @Override
                public void run(@NonNull ProxyView view) {
                    view.testProxyError("代理IP或端口不正确");
                }
            });
        }

    }

    @Override
    public void parseXiCiDaiLi(final boolean pullToRefresh) {
        if (pullToRefresh) {
            page = 1;
        }
        dataManager.loadXiCiDaiLiProxyData(page)
                .map(new Function<BaseResult<List<ProxyModel>>, List<ProxyModel>>() {
                    @Override
                    public List<ProxyModel> apply(BaseResult<List<ProxyModel>> baseResult) throws Exception {
                        if (page == 1) {
                            totalPage = baseResult.getTotalPage();
                        }
                        return baseResult.getData();
                    }
                })
                .compose(RxSchedulersHelper.<List<ProxyModel>>ioMainThread())
                .compose(provider.<List<ProxyModel>>bindUntilEvent(Lifecycle.Event.ON_STOP))
                .subscribe(new CallBackWrapper<List<ProxyModel>>() {
                    @Override
                    public void onBegin(Disposable d) {
                        ifViewAttached(new ViewAction<ProxyView>() {
                            @Override
                            public void run(@NonNull ProxyView view) {
                                if (!pullToRefresh && page == 1) {
                                    view.beginParseProxy();
                                }
                            }
                        });
                    }

                    @Override
                    public void onSuccess(final List<ProxyModel> proxyModels) {
                        ifViewAttached(new ViewAction<ProxyView>() {
                            @Override
                            public void run(@NonNull ProxyView view) {
                                if (page == 1) {
                                    view.parseXiCiDaiLiSuccess(proxyModels);
                                    view.showContent();
                                } else {
                                    view.setMoreData(proxyModels);
                                    view.loadMoreDataComplete();
                                }
                                if (page >= totalPage) {
                                    view.noMoreData();
                                } else {
                                    page++;
                                }
                            }
                        });
                    }

                    @Override
                    public void onError(String msg, int code) {
                        ifViewAttached(new ViewAction<ProxyView>() {
                            @Override
                            public void run(@NonNull ProxyView view) {
                                if (page == 1) {
                                    view.showError("解析失败了");
                                } else {
                                    view.loadMoreFailed();
                                }
                            }
                        });
                    }
                });

    }

    @Override
    public boolean isSetPorn91VideoAddress() {
        return TextUtils.isEmpty(dataManager.getPorn9VideoAddress());
    }

    @Override
    public void exitTest() {
        dataManager.existProxyTest();
    }

    @Override
    public String getProxyIpAddress() {
        return dataManager.getProxyIpAddress();
    }

    @Override
    public int getProxyPort() {
        return dataManager.getProxyPort();
    }

    @Override
    public void setProxyIpAddress(String proxyIpAddress) {
        dataManager.setProxyIpAddress(proxyIpAddress);
    }

    @Override
    public void setProxyPort(int proxyPort) {
        dataManager.setProxyPort(proxyPort);
    }

    @Override
    public void setOpenHttpProxy(boolean openHttpProxy) {
        dataManager.setOpenHttpProxy(openHttpProxy);
    }
}
