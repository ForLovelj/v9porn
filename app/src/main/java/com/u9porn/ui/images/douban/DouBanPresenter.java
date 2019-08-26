package com.u9porn.ui.images.douban;

import android.arch.lifecycle.Lifecycle;
import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;
import com.trello.rxlifecycle2.LifecycleProvider;
import com.u9porn.data.DataManager;
import com.u9porn.data.model.DouBanMeizi;
import com.u9porn.rxjava.CallBackWrapper;
import com.u9porn.rxjava.RxSchedulersHelper;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;

/**
 * @author clow
 */

public class DouBanPresenter extends MvpBasePresenter<DouBanView> implements IDouBan {

    private LifecycleProvider<Lifecycle.Event> provider;
    private DataManager                        dataManager;
    private int                                page = 1;
    private int                                pageSize = 20;

    @Inject
    public DouBanPresenter(LifecycleProvider<Lifecycle.Event> provider, DataManager dataManager) {
        this.provider = provider;
        this.dataManager = dataManager;
    }
    @Override
    public void listDouBanMeiZhi(int categoryId, final boolean pullToRefresh) {
        if (pullToRefresh) {
            page = 1;
        }
        dataManager.listDouBanMeiZhi(categoryId,page,pullToRefresh)
                .compose(RxSchedulersHelper.<List<DouBanMeizi>>ioMainThread())
                .compose(provider.<List<DouBanMeizi>>bindUntilEvent(Lifecycle.Event.ON_DESTROY))
                .subscribe(new CallBackWrapper<List<DouBanMeizi>>() {
                    @Override
                    public void onBegin(Disposable d) {
                        ifViewAttached(new ViewAction<DouBanView>() {
                            @Override
                            public void run(@NonNull DouBanView view) {
                                view.showLoading(pullToRefresh);
                            }
                        });
                    }

                    @Override
                    public void onSuccess(final List<DouBanMeizi> douBanMeizis) {
                        ifViewAttached(new ViewAction<DouBanView>() {
                            @Override
                            public void run(@NonNull DouBanView view) {
                                if (page == 1) {
                                    view.setData(douBanMeizis);
                                    view.showContent();
                                } else {
                                    view.setMoreData(douBanMeizis);
                                }

                                if (douBanMeizis != null && douBanMeizis.size() < pageSize) {
                                    view.noMoreData();
                                    return;
                                }
                                page++;
                            }
                        });
                    }

                    @Override
                    public void onError(final String msg, int code) {
                        ifViewAttached(new ViewAction<DouBanView>() {
                            @Override
                            public void run(@NonNull DouBanView view) {
                                view.showError(msg);
                            }
                        });
                    }
                });
    }
}
