package com.u9porn.ui.pxgav.playpxgav;

import android.arch.lifecycle.Lifecycle;
import android.support.annotation.NonNull;

import com.trello.rxlifecycle2.LifecycleProvider;
import com.u9porn.data.DataManager;
import com.u9porn.data.model.pxgav.PxgavVideoParserJsonResult;
import com.u9porn.rxjava.CallBackWrapper;
import com.u9porn.rxjava.RxSchedulersHelper;
import com.u9porn.ui.MvpBasePresenter;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;

/**
 * @author flymegoc
 * @date 2018/1/30
 */

public class PlayPxgavPresenter extends MvpBasePresenter<PlayPxgavView> implements IPlayPxgav {
    private static final String TAG = PlayPxgavPresenter.class.getSimpleName();

    private DataManager dataManager;

    @Inject
    public PlayPxgavPresenter(LifecycleProvider<Lifecycle.Event> provider, DataManager dataManager) {
        super(provider);
        this.dataManager = dataManager;
    }

    @Override
    public void parseVideoUrl(String url, String pId, boolean pullToRefresh) {

        dataManager.loadPxgavVideoUrl(url, pId, pullToRefresh)
                .compose(RxSchedulersHelper.<PxgavVideoParserJsonResult>ioMainThread())
                .compose(provider.<PxgavVideoParserJsonResult>bindUntilEvent(Lifecycle.Event.ON_DESTROY))
                .subscribe(new CallBackWrapper<PxgavVideoParserJsonResult>() {

                    @Override
                    public void onBegin(Disposable d) {
                        ifViewAttached(new ViewAction<PlayPxgavView>() {
                            @Override
                            public void run(@NonNull PlayPxgavView view) {
                                view.showLoading(true);
                            }
                        });
                    }

                    @Override
                    public void onSuccess(final PxgavVideoParserJsonResult pxgavVideoParserJsonResult) {
                        ifViewAttached(new ViewAction<PlayPxgavView>() {
                            @Override
                            public void run(@NonNull PlayPxgavView view) {
                                view.showContent();
                                view.playVideo(pxgavVideoParserJsonResult);
                                view.listVideo(pxgavVideoParserJsonResult.getPxgavModelList());
                            }
                        });
                    }

                    @Override
                    public void onError(final String msg, int code) {
                        ifViewAttached(new ViewAction<PlayPxgavView>() {
                            @Override
                            public void run(@NonNull PlayPxgavView view) {
                                view.showError(msg);
                            }
                        });
                    }
                });
    }

    @Override
    public String getVideoCacheProxyUrl(final String originalVideoUrl) {
        return dataManager.getVideoCacheProxyUrl(originalVideoUrl);
    }
}
