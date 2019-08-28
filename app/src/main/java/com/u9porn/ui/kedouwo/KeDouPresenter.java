package com.u9porn.ui.kedouwo;

import android.arch.lifecycle.Lifecycle;

import com.trello.rxlifecycle2.LifecycleProvider;
import com.u9porn.data.AppDataManager;
import com.u9porn.data.model.kedouwo.KeDouModel;
import com.u9porn.rxjava.CallBackWrapper;
import com.u9porn.rxjava.RxSchedulersHelper;
import com.u9porn.ui.MvpBasePresenter;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;

/**
 * Created by alex
 * Des:
 * Date: 2019/8/27.
 */
public class KeDouPresenter extends MvpBasePresenter<KeDouView> implements IKeDou {

    private int page = 1;
    private int pageNum = 10;

    @Inject
    public KeDouPresenter(LifecycleProvider<Lifecycle.Event> provider, AppDataManager appDataManager) {
        super(provider, appDataManager);
    }

    @Override
    public void videoList(String type, boolean pullToRefresh) {
//        if (TextUtils.equals(type, "1")) {
//            videoListLatest(pullToRefresh);
//        }else if(TextUtils.equals(type,"2")){
//            videoListTop(pullToRefresh);
//        } else if (TextUtils.equals(type, "3")) {
//            videoListPopular(pullToRefresh);
//        }
        if (pullToRefresh) {
            page = 1;
        }
        appDataManager.videoList(type,page,pullToRefresh)
                .compose(RxSchedulersHelper.ioMainThread())
                .compose(provider.bindUntilEvent(Lifecycle.Event.ON_DESTROY))
                .subscribe(new CallBackWrapper<List<KeDouModel>>() {
                    @Override
                    public void onBegin(Disposable d) {
                        ifViewAttached(view -> {
                            if (page == 1) {
                                view.showLoading(pullToRefresh);
                            }
                        });
                    }

                    @Override
                    public void onSuccess(List<KeDouModel> keDouModels) {
                        ifViewAttached(view -> {
                            int size = keDouModels.size();
                            if (page == 1) {
                                pageNum = size;
                                view.setData(keDouModels);
                            } else {
                                view.setMoreData(keDouModels);
                            }
                            if (size < pageNum) {
                                view.noMoreData();
                            } else {
                                page++;
                            }
                            view.showContent();
                        });
                    }

                    @Override
                    public void onError(String msg, int code) {
                        ifViewAttached(view -> {
                            if (page == 1) {
                                view.showError(msg);
                            } else {
                                view.loadMoreFailed();
                            }
                        });
                    }
                });
    }

    private void videoListLatest(boolean pullToRefresh) {
        appDataManager.videoListLatest(page)
                .compose(RxSchedulersHelper.ioMainThread())
                .compose(provider.bindUntilEvent(Lifecycle.Event.ON_DESTROY))
                .subscribe(new CallBackWrapper<List<KeDouModel>>() {
                    @Override
                    public void onBegin(Disposable d) {
                        ifViewAttached(view -> {
                            if (page == 1) {
                                view.showLoading(pullToRefresh);
                            }
                        });
                    }

                    @Override
                    public void onSuccess(List<KeDouModel> keDouModels) {
                        ifViewAttached(view -> {
                            int size = keDouModels.size();
                            if (page == 1) {
                                pageNum = size;
                                view.setData(keDouModels);
                            } else {
                                view.setMoreData(keDouModels);
                            }
                            if (size < pageNum) {
                                view.noMoreData();
                            } else {
                                page++;
                            }
                            view.showContent();
                        });
                    }

                    @Override
                    public void onError(String msg, int code) {
                        ifViewAttached(view -> {
                            if (page == 1) {
                                view.showError(msg);
                            } else {
                                view.loadMoreFailed();
                            }
                        });
                    }
                });

    }

    private void videoListPopular(boolean pullToRefresh) {

    }
    private void videoListTop(boolean pullToRefresh) {

    }
}
