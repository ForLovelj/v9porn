package com.u9porn.ui.kedouwo.play;

import android.arch.lifecycle.Lifecycle;
import android.text.TextUtils;

import com.trello.rxlifecycle2.LifecycleProvider;
import com.u9porn.data.AppDataManager;
import com.u9porn.data.model.kedouwo.KeDouRelated;
import com.u9porn.rxjava.CallBackWrapper;
import com.u9porn.rxjava.RetryWhenProcess;
import com.u9porn.rxjava.RxSchedulersHelper;
import com.u9porn.ui.MvpBasePresenter;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;

/**
 * Created by alex
 * Des:
 * Date: 2019/8/28.
 */
public class KeDouPlayPresenter extends MvpBasePresenter<KeDouPlayView> implements IKeDouPlay{

    @Inject
    public KeDouPlayPresenter(LifecycleProvider<Lifecycle.Event> provider, AppDataManager appDataManager) {
        super(provider, appDataManager);
    }

    @Override
    public void videoRelated(String url) {
        appDataManager.videoRelated(url)
                .map(keDouRelated -> {
                    if (TextUtils.isEmpty(keDouRelated.getVideoUrl())) {
                        if (keDouRelated.isOutOfWatch()) {
                            //尝试强行重置
                            appDataManager.resetKeDouWoVideoWatchTime();
                        }
                    }
                    return keDouRelated;
                })
                .retryWhen(new RetryWhenProcess(RetryWhenProcess.PROCESS_TIME))
                .compose(RxSchedulersHelper.ioMainThread())
                .compose(provider.bindUntilEvent(Lifecycle.Event.ON_DESTROY))
                .subscribe(new CallBackWrapper<KeDouRelated>() {

                    @Override
                    public void onBegin(Disposable d) {
                        ifViewAttached(view -> view.showLoading(true));
                    }

                    @Override
                    public void onSuccess(KeDouRelated keDouRelated) {
                        ifViewAttached(view -> {
                            view.onVideoRelated(keDouRelated);
                        });
                    }

                    @Override
                    public void onError(String msg, int code) {
                        ifViewAttached(view -> {
                            view.onVideoRelatedError(msg);
                            view.showContent();
                        });
                    }
                });
    }

    @Override
    public void getRealVideoUrl(String url) {
       appDataManager.getRealVideoUrl(url)
                        .compose(RxSchedulersHelper.ioMainThread())
                        .compose(provider.bindUntilEvent(Lifecycle.Event.ON_DESTROY))
                        .subscribe(new CallBackWrapper<String>() {

                           @Override
                           public void onSuccess(String s) {
                               ifViewAttached(view -> {
                                   view.onVideoUrl(s);
                                   view.showContent();
                               });
                           }

                           @Override
                           public void onError(String msg, int code) {
                               ifViewAttached(view -> {
                                   view.showError(msg);
                                   view.showContent();
                               });
                           }
                       });


    }

}
