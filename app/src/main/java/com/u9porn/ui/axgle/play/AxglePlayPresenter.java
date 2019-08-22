package com.u9porn.ui.axgle.play;

import android.arch.lifecycle.Lifecycle;
import android.content.Context;
import android.support.annotation.NonNull;

import com.awesapp.isafe.svs.parsers.PSVS21;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;
import com.trello.rxlifecycle2.LifecycleProvider;
import com.u9porn.data.DataManager;
import com.u9porn.data.model.axgle.AxgleResponse;
import com.u9porn.data.model.axgle.AxgleVideo;
import com.u9porn.di.ApplicationContext;
import com.u9porn.rxjava.CallBackWrapper;
import com.u9porn.rxjava.RxSchedulersHelper;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author megoc
 */
public class AxglePlayPresenter extends MvpBasePresenter<AxglePlayView> implements IAxglePlay {
    private DataManager dataManager;
    private LifecycleProvider<Lifecycle.Event> provider;
    private Context context;
    private int page = 1;
    private boolean isHaveMore;

    @Inject
    public AxglePlayPresenter(DataManager dataManager, LifecycleProvider<Lifecycle.Event> provider, @ApplicationContext Context context) {
        this.dataManager = dataManager;
        this.provider = provider;
        this.context = context;
    }

    @Override
    public void getPlayVideoUrl(String vid) {
        String ts = String.valueOf(System.currentTimeMillis() / 1000);
        String axgleAddress = dataManager.getAxgleAddress();
        //如果用户忘记输入最后一个“/”，我们手动加上去不就行了，或者设置的时候直接加上就ok了
        if (!axgleAddress.endsWith("/")) {
            axgleAddress += "/";
        }
        String url = String.format(
                axgleAddress.replace("api.", "") + "mp4.php?vid=%s&ts=%s&hash=%s&m3u8"
                , vid
                , ts
                , PSVS21.computeHash(new PSVS21.StubContext(context), vid, ts));
        ifViewAttached(new ViewAction<AxglePlayView>() {
            @Override
            public void run(@NonNull AxglePlayView view) {
                view.showLoading();
            }
        });
        dataManager.getPlayVideoUrl(url).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                final String url = response.raw().request().url().toString();
                ifViewAttached(new ViewAction<AxglePlayView>() {
                    @Override
                    public void run(@NonNull AxglePlayView view) {
                        view.getVideoUrlSuccess(url);
                    }
                });
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                ifViewAttached(new ViewAction<AxglePlayView>() {
                    @Override
                    public void run(@NonNull AxglePlayView view) {
                        view.getVideoUrlError();
                    }
                });
            }
        });
    }

    @Override
    public void loadSimilarVideo(String keyWord, final boolean pullToRefresh) {
        if (pullToRefresh) {
            page = 1;
        }
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
                        ifViewAttached(new ViewAction<AxglePlayView>() {
                            @Override
                            public void run(@NonNull AxglePlayView view) {
                                if (page == 1) {
                                    view.showLoading(pullToRefresh);
                                }
                            }
                        });
                    }

                    @Override
                    public void onSuccess(final List<AxgleVideo> axgleVideos) {
                        ifViewAttached(new ViewAction<AxglePlayView>() {
                            @Override
                            public void run(@NonNull AxglePlayView view) {
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
                        ifViewAttached(new ViewAction<AxglePlayView>() {
                            @Override
                            public void run(@NonNull AxglePlayView view) {
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
