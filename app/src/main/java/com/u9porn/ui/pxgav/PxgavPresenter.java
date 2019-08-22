package com.u9porn.ui.pxgav;

import android.arch.lifecycle.Lifecycle;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.orhanobut.logger.Logger;
import com.trello.rxlifecycle2.LifecycleProvider;
import com.u9porn.data.DataManager;
import com.u9porn.data.model.pxgav.PxgavResultWithBlockId;
import com.u9porn.rxjava.CallBackWrapper;
import com.u9porn.rxjava.RxSchedulersHelper;
import com.u9porn.ui.MvpBasePresenter;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;

/**
 * @author flymegoc
 * @date 2018/1/30
 */

public class PxgavPresenter extends MvpBasePresenter<PxgavView> implements IPxgav {
    private static final String TAG = PxgavPresenter.class.getSimpleName();

    private int page = 2;
    private DataManager dataManager;
    private String lastBlockId;

    @Inject
    public PxgavPresenter(LifecycleProvider<Lifecycle.Event> provider, DataManager dataManager) {
        super(provider);
        this.dataManager = dataManager;
    }

    @Override
    public void videoList(String category, boolean pullToRefresh) {
        if (pullToRefresh) {
            page = 2;
        }
        dataManager.loadPxgavListByCategory(category, pullToRefresh)
                .compose(RxSchedulersHelper.<PxgavResultWithBlockId>ioMainThread())
                .compose(provider.<PxgavResultWithBlockId>bindUntilEvent(Lifecycle.Event.ON_DESTROY))
                .subscribe(new CallBackWrapper<PxgavResultWithBlockId>() {

                    @Override
                    public void onBegin(Disposable d) {
                        ifViewAttached(new ViewAction<PxgavView>() {
                            @Override
                            public void run(@NonNull PxgavView view) {
                                view.showLoading(true);
                            }
                        });
                    }

                    @Override
                    public void onSuccess(final PxgavResultWithBlockId pxgavResultWithBlockId) {
                        ifViewAttached(new ViewAction<PxgavView>() {
                            @Override
                            public void run(@NonNull PxgavView view) {
                                lastBlockId = pxgavResultWithBlockId.getBlockId();
                                view.setData(pxgavResultWithBlockId.getPxgavModelList());
                                view.showContent();
                            }
                        });
                    }

                    @Override
                    public void onError(final String msg, int code) {
                        ifViewAttached(new ViewAction<PxgavView>() {
                            @Override
                            public void run(@NonNull PxgavView view) {
                                view.showError(msg);
                            }
                        });
                    }
                });

    }

    @Override
    public void moreVideoList(String category, boolean pullToRefresh) {
        if (TextUtils.isEmpty(lastBlockId)) {
            lastBlockId = "td_uid_11_5c30cc4f62e14";
        }
        dataManager.loadMorePxgavListByCategory(category, page, lastBlockId, pullToRefresh)
                .compose(RxSchedulersHelper.ioMainThread())
                .compose(provider.bindUntilEvent(Lifecycle.Event.ON_DESTROY))
                .subscribe(new CallBackWrapper<PxgavResultWithBlockId>() {
                    @Override
                    public void onSuccess(final PxgavResultWithBlockId pxgavResultWithBlockId) {
                        ifViewAttached(view -> {
                            if (pxgavResultWithBlockId.getPxgavModelList().size() == 0) {
                                Logger.t(TAG).d("没有数据哦");
                            } else {
                                lastBlockId = pxgavResultWithBlockId.getBlockId();
                                view.setMoreData(pxgavResultWithBlockId.getPxgavModelList());
                                page++;
                            }
                        });
                    }

                    @Override
                    public void onError(String msg, int code) {
                        ifViewAttached(PxgavView::loadMoreFailed);
                    }
                });

    }
}
