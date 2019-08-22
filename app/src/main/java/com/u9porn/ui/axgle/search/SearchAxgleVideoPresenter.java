package com.u9porn.ui.axgle.search;

import android.arch.lifecycle.Lifecycle;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;
import com.trello.rxlifecycle2.LifecycleProvider;
import com.u9porn.data.DataManager;
import com.u9porn.data.model.axgle.AxgleResponse;
import com.u9porn.data.model.axgle.AxgleVideo;
import com.u9porn.rxjava.CallBackWrapper;
import com.u9porn.rxjava.RxSchedulersHelper;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;

public class SearchAxgleVideoPresenter extends MvpBasePresenter<SearchAxgleVideoView> implements ISearchAxgleVideo {

    private DataManager dataManager;
    private LifecycleProvider<Lifecycle.Event> provider;

    private String keyWord;
    private int page;
    private boolean isJavSearch;
    private boolean isHaveMore;

    @Inject
    public SearchAxgleVideoPresenter(DataManager dataManager, LifecycleProvider<Lifecycle.Event> provider) {
        this.dataManager = dataManager;
        this.provider = provider;
    }

    @Override
    public void searchAxgleVideo(String keyWord, boolean isJavSearch, final boolean pullToRefresh) {
        if (pullToRefresh || (!TextUtils.isEmpty(this.keyWord) && !this.keyWord.equals(keyWord)) || this.isJavSearch != isJavSearch) {
            page = 1;
        }
        this.keyWord = keyWord;
        this.isJavSearch = isJavSearch;
        dataManager.searchAxgleVideo(keyWord, page)
                .map(new Function<AxgleResponse, List<AxgleVideo>>() {
                    @Override
                    public List<AxgleVideo> apply(AxgleResponse axgleResponse) throws Exception {
                        isHaveMore = axgleResponse.isHas_more();
                        return axgleResponse.getVideos();
                    }
                })
                .compose(RxSchedulersHelper.<List<AxgleVideo>>ioMainThread())
                .compose(provider.<List<AxgleVideo>>bindUntilEvent(Lifecycle.Event.ON_STOP))
                .subscribe(new CallBackWrapper<List<AxgleVideo>>() {
                    @Override
                    public void onBegin(Disposable d) {
                        ifViewAttached(new ViewAction<SearchAxgleVideoView>() {
                            @Override
                            public void run(@NonNull SearchAxgleVideoView view) {
                                if (page == 1) {
                                    view.showLoading(pullToRefresh);
                                }
                            }
                        });
                    }

                    @Override
                    public void onSuccess(final List<AxgleVideo> axgleVideos) {
                        ifViewAttached(new ViewAction<SearchAxgleVideoView>() {
                            @Override
                            public void run(@NonNull SearchAxgleVideoView view) {
                                if (page == 1) {
                                    view.setData(axgleVideos);
                                } else {
                                    view.setMoreData(axgleVideos);
                                }
                                if (isHaveMore) {
                                    page++;
                                } else {
                                    view.noMoreData();
                                }
                                view.showContent();
                            }
                        });
                    }

                    @Override
                    public void onError(final String msg, final int code) {
                        ifViewAttached(new ViewAction<SearchAxgleVideoView>() {
                            @Override
                            public void run(@NonNull SearchAxgleVideoView view) {
                                if (page == 1) {
                                    view.showError(msg);
                                } else {
                                    view.loadMoreFailed();
                                }
                            }
                        });
                    }
                });
    }
}
