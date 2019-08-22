package com.u9porn.ui.porn9forum;

import android.arch.lifecycle.Lifecycle;
import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;
import com.trello.rxlifecycle2.LifecycleProvider;
import com.u9porn.data.DataManager;
import com.u9porn.data.model.BaseResult;
import com.u9porn.data.model.F9PronItem;
import com.u9porn.data.model.PinnedHeaderEntity;
import com.u9porn.rxjava.CallBackWrapper;
import com.u9porn.rxjava.RxSchedulersHelper;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;

/**
 * @author flymegoc
 * @date 2018/1/23
 */

public class ForumPresenter extends MvpBasePresenter<ForumView> implements IForum {

    protected LifecycleProvider<Lifecycle.Event> provider;
    private int page = 1;
    private int totalPage = 1;
    private DataManager dataManager;

    @Inject
    public ForumPresenter(LifecycleProvider<Lifecycle.Event> provider, DataManager dataManager) {
        this.provider = provider;
        this.dataManager = dataManager;
    }

    @Override
    public void loadForumIndexListData(final boolean pullToRefresh) {
        dataManager.loadPorn9ForumIndex()
                .compose(RxSchedulersHelper.<List<PinnedHeaderEntity<F9PronItem>>>ioMainThread())
                .compose(provider.<List<PinnedHeaderEntity<F9PronItem>>>bindUntilEvent(Lifecycle.Event.ON_DESTROY))
                .subscribe(new CallBackWrapper<List<PinnedHeaderEntity<F9PronItem>>>() {

                    @Override
                    public void onBegin(Disposable d) {
                        ifViewAttached(new ViewAction<ForumView>() {
                            @Override
                            public void run(@NonNull ForumView view) {
                                if (pullToRefresh) {
                                    view.showLoading(pullToRefresh);
                                }
                            }
                        });
                    }

                    @Override
                    public void onSuccess(final List<PinnedHeaderEntity<F9PronItem>> pinnedHeaderEntityList) {
                        ifViewAttached(new ViewAction<ForumView>() {
                            @Override
                            public void run(@NonNull ForumView view) {
                                view.setForumIndexListData(pinnedHeaderEntityList);
                                view.showContent();
                            }
                        });
                    }

                    @Override
                    public void onError(final String msg, int code) {
                        ifViewAttached(new ViewAction<ForumView>() {
                            @Override
                            public void run(@NonNull ForumView view) {
                                view.showError(msg);
                            }
                        });
                    }
                });
    }

    @Override
    public void loadForumListData(final boolean pullToRefresh, String fid) {
        if (pullToRefresh) {
            page = 1;
        }
        dataManager.loadPorn9ForumListData(fid, page)
                .map(new Function<BaseResult<List<F9PronItem>>, List<F9PronItem>>() {
                    @Override
                    public List<F9PronItem> apply(BaseResult<List<F9PronItem>> baseResult) throws Exception {
                        if (page == 1) {
                            totalPage = baseResult.getTotalPage();
                        }
                        return baseResult.getData();
                    }
                })
                .compose(RxSchedulersHelper.<List<F9PronItem>>ioMainThread())
                .compose(provider.<List<F9PronItem>>bindUntilEvent(Lifecycle.Event.ON_DESTROY))
                .subscribe(new CallBackWrapper<List<F9PronItem>>() {

                    @Override
                    public void onBegin(Disposable d) {
                        ifViewAttached(new ViewAction<ForumView>() {
                            @Override
                            public void run(@NonNull ForumView view) {
                                if (pullToRefresh) {
                                    view.showLoading(pullToRefresh);
                                }
                            }
                        });
                    }

                    @Override
                    public void onSuccess(final List<F9PronItem> f9PronItems) {
                        ifViewAttached(new ViewAction<ForumView>() {
                            @Override
                            public void run(@NonNull ForumView view) {
                                if (page == 1) {
                                    view.setForumListData(f9PronItems);
                                    view.showContent();
                                } else {
                                    view.setMoreData(f9PronItems);
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
                        ifViewAttached(new ViewAction<ForumView>() {
                            @Override
                            public void run(@NonNull ForumView view) {
                                view.showError(msg);
                            }
                        });
                    }
                });
    }

    @Override
    public String getForum9PornAddress() {
        return dataManager.getPorn9ForumAddress();
    }
}
