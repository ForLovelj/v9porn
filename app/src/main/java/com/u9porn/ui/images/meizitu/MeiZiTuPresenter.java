package com.u9porn.ui.images.meizitu;

import android.arch.lifecycle.Lifecycle;
import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;
import com.trello.rxlifecycle2.LifecycleProvider;
import com.u9porn.data.DataManager;
import com.u9porn.data.model.BaseResult;
import com.u9porn.data.model.MeiZiTu;
import com.u9porn.rxjava.CallBackWrapper;
import com.u9porn.rxjava.RxSchedulersHelper;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;

/**
 * @author flymegoc
 * @date 2018/1/25
 */

public class MeiZiTuPresenter extends MvpBasePresenter<MeiZiTuView> implements IMeiZiTu {

    protected LifecycleProvider<Lifecycle.Event> provider;
    private int page = 1;
    private int totalPage = 1;
    private DataManager dataManager;

    @Inject
    public MeiZiTuPresenter(LifecycleProvider<Lifecycle.Event> provider, DataManager dataManager) {
        this.provider = provider;
        this.dataManager = dataManager;
    }

    @Override
    public void listMeiZi(String tag, final boolean pullToRefresh) {
        if (pullToRefresh) {
            page = 1;
        }
        dataManager.listMeiZiTu(tag, page, pullToRefresh)
                .map(new Function<BaseResult<List<MeiZiTu>>, List<MeiZiTu>>() {
                    @Override
                    public List<MeiZiTu> apply(BaseResult<List<MeiZiTu>> baseResult) throws Exception {
                        if (page == 1) {
                            totalPage = baseResult.getTotalPage();
                        }
                        return baseResult.getData();
                    }
                })
                .compose(RxSchedulersHelper.<List<MeiZiTu>>ioMainThread())
                .compose(provider.<List<MeiZiTu>>bindUntilEvent(Lifecycle.Event.ON_DESTROY))
                .subscribe(new CallBackWrapper<List<MeiZiTu>>() {

                    @Override
                    public void onBegin(Disposable d) {
                        ifViewAttached(new ViewAction<MeiZiTuView>() {
                            @Override
                            public void run(@NonNull MeiZiTuView view) {
                                view.showLoading(pullToRefresh);
                            }
                        });
                    }

                    @Override
                    public void onSuccess(final List<MeiZiTu> meiZiTus) {
                        ifViewAttached(new ViewAction<MeiZiTuView>() {
                            @Override
                            public void run(@NonNull MeiZiTuView view) {
                                if (page == 1) {
                                    view.setData(meiZiTus);
                                    view.showContent();
                                } else {
                                    view.setMoreData(meiZiTus);
                                }
                                //已经最后一页了
                                if (page >= totalPage) {
                                    view.noMoreData();
                                } else {
                                    page++;
                                }
                            }
                        });
                    }

                    @Override
                    public void onError(final String msg, int code) {
                        ifViewAttached(new ViewAction<MeiZiTuView>() {
                            @Override
                            public void run(@NonNull MeiZiTuView view) {
                                view.showError(msg);
                            }
                        });
                    }
                });

    }
}
