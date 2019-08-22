package com.u9porn.ui.images.mm99;

import android.arch.lifecycle.Lifecycle;
import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;
import com.trello.rxlifecycle2.LifecycleProvider;
import com.u9porn.data.DataManager;
import com.u9porn.data.model.BaseResult;
import com.u9porn.data.model.Mm99;
import com.u9porn.rxjava.CallBackWrapper;
import com.u9porn.rxjava.RxSchedulersHelper;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;

/**
 * @author flymegoc
 * @date 2018/2/1
 */

public class Mm99Presenter extends MvpBasePresenter<Mm99View> implements IMm99 {

    private int page = 1;
    private int totalPage = 1;
    private LifecycleProvider<Lifecycle.Event> provider;
    private DataManager dataManager;

    @Inject
    public Mm99Presenter(LifecycleProvider<Lifecycle.Event> provider, DataManager dataManager) {
        this.provider = provider;
        this.dataManager = dataManager;
    }

    @Override
    public void loadData(String category, final boolean pullToRefresh, boolean cleanCache) {
        if (pullToRefresh) {
            page = 1;
            totalPage = 1;
        }
        dataManager.list99Mm(category, page, cleanCache)
                .map(new Function<BaseResult<List<Mm99>>, List<Mm99>>() {
                    @Override
                    public List<Mm99> apply(BaseResult<List<Mm99>> baseResult) throws Exception {
                        if (page == 1) {
                            totalPage = baseResult.getTotalPage();
                        }
                        return baseResult.getData();
                    }
                })
                .compose(RxSchedulersHelper.<List<Mm99>>ioMainThread())
                .compose(provider.<List<Mm99>>bindUntilEvent(Lifecycle.Event.ON_DESTROY))
                .subscribe(new CallBackWrapper<List<Mm99>>() {
                    @Override
                    public void onBegin(Disposable d) {
                        ifViewAttached(new ViewAction<Mm99View>() {
                            @Override
                            public void run(@NonNull Mm99View view) {
                                view.showLoading(pullToRefresh);
                            }
                        });
                    }

                    @Override
                    public void onSuccess(final List<Mm99> mm99s) {
                        ifViewAttached(new ViewAction<Mm99View>() {
                            @Override
                            public void run(@NonNull Mm99View view) {
                                if (page == 1) {
                                    view.setData(mm99s);
                                    view.showContent();
                                } else {
                                    view.setMoreData(mm99s);
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
                        ifViewAttached(new ViewAction<Mm99View>() {
                            @Override
                            public void run(@NonNull Mm99View view) {
                                view.showError(msg);
                            }
                        });
                    }
                });
    }
}
