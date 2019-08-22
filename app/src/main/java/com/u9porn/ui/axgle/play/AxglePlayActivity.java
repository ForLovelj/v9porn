package com.u9porn.ui.axgle.play;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.devbrackets.android.exomedia.listener.OnPreparedListener;
import com.flymegoc.exolibrary.widget.ExoVideoControlsMobile;
import com.flymegoc.exolibrary.widget.ExoVideoView;
import com.jaeger.library.StatusBarUtil;
import com.orhanobut.logger.Logger;
import com.sdsmdg.tastytoast.TastyToast;
import com.u9porn.R;
import com.u9porn.adapter.AxgleAdapter;
import com.u9porn.constants.Keys;
import com.u9porn.data.model.axgle.AxgleVideo;
import com.u9porn.ui.MvpActivity;
import com.u9porn.ui.axgle.search.SearchAxgleVideoActivity;
import com.u9porn.utils.DialogUtils;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author megoc
 */
public class AxglePlayActivity extends MvpActivity<AxglePlayView, AxglePlayPresenter> implements AxglePlayView, OnPreparedListener, SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = AxglePlayActivity.class.getSimpleName();
    @Inject
    protected AxglePlayPresenter axglePlayPresenter;
    @BindView(R.id.video_view)
    ExoVideoView videoView;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.swipe_layout)
    SwipeRefreshLayout swipeLayout;
    @BindView(R.id.fr_container)
    FrameLayout frContainer;
    @BindView(R.id.fac_search)
    FloatingActionButton facSearch;

    private AlertDialog alertDialog;
    private boolean isPauseByActivityEvent;
    private ExoVideoControlsMobile videoControlsMobile;
    private AxgleVideo axgleVideo;

    private AxgleAdapter axgleAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_axgle_play);
        ButterKnife.bind(this);
        axgleAdapter = new AxgleAdapter(R.layout.item_axgle);
        axgleAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                AxgleVideo axgleVideo = (AxgleVideo) adapter.getItem(position);
                if (axgleVideo == null) {
                    return;
                }
                AxglePlayActivity.this.axgleVideo = axgleVideo;
                presenter.getPlayVideoUrl(axgleVideo.getVid());
            }
        });
        axgleAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                presenter.loadSimilarVideo(axgleVideo.getKeyword(), false);
            }
        }, recyclerView);
        swipeLayout.setOnRefreshListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(axgleAdapter);
        axgleVideo = (AxgleVideo) getIntent().getSerializableExtra(Keys.KEY_INTENT_AXGLE_VIDEO_ITEM);
        if (axgleVideo == null) {
            return;
        }
        alertDialog = DialogUtils.initLoadingDialog(this, "获取视频地址中，请稍候...");
        videoView.setOnPreparedListener(this);
        videoControlsMobile = (ExoVideoControlsMobile) videoView.getVideoControls();
        videoControlsMobile.setOnBackButtonClickListener(new ExoVideoControlsMobile.OnBackButtonClickListener() {
            @Override
            public void onBackClick(View view) {
                onBackPressed();
            }
        });
        presenter.getPlayVideoUrl(axgleVideo.getVid());
    }

    @NonNull
    @Override
    public AxglePlayPresenter createPresenter() {
        return axglePlayPresenter;
    }

    @Override
    public void showLoading() {
        if (alertDialog != null && !alertDialog.isShowing()) {
            alertDialog.show();
        }
    }

    @Override
    public void getVideoUrlSuccess(String videoUrl) {
        Logger.t(TAG).d("视频播放地址：" + videoUrl);
        videoView.setVideoURI(Uri.parse(videoUrl));
        videoControlsMobile.setTitle(axgleVideo.getTitle());
        dismissDialog();
        recyclerView.scrollToPosition(0);
        presenter.loadSimilarVideo(axgleVideo.getKeyword(), true);
    }

    @Override
    public void getVideoUrlError() {
        showMessage("获取视频地址失败", TastyToast.ERROR);
        dismissDialog();
    }

    @Override
    public void setData(List<AxgleVideo> axgleVideoList) {
        if (axgleVideoList.size() != 0) {
            axgleAdapter.setNewData(axgleVideoList);
        } else {
            showMessage("没找到相似的视频", TastyToast.INFO);
        }
    }

    @Override
    public void loadMoreFailed() {
        axgleAdapter.loadMoreFail();
    }

    @Override
    public void noMoreData() {
        axgleAdapter.loadMoreEnd();
    }

    @Override
    public void setMoreData(List<AxgleVideo> axgleVideoList) {
        axgleAdapter.loadMoreComplete();
        axgleAdapter.addData(axgleVideoList);
    }

    private void dismissDialog() {
        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.dismiss();
        }
    }

    @Override
    public void onPrepared() {
        videoView.start();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE || newConfig.orientation == ActivityInfo.SCREEN_ORIENTATION_USER) {
            //这里没必要，因为我们使用的是setColorForSwipeBack，并不会有这个虚拟的view，而是设置的padding
            StatusBarUtil.hideFakeStatusBarView(this);
        } else if (newConfig.orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!videoView.isPlaying() && isPauseByActivityEvent) {
            isPauseByActivityEvent = false;
            videoView.start();
        }
    }

    @Override
    protected void onPause() {
        videoView.pause();
        isPauseByActivityEvent = true;
        super.onPause();

    }

    @Override
    public void onBackPressed() {
        if (videoControlsMobile.onBackPressed()) {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        videoView.release();
        super.onDestroy();
    }

    @Override
    public void showLoading(boolean pullToRefresh) {
        swipeLayout.setRefreshing(true);
    }

    @Override
    public void showContent() {
        swipeLayout.setRefreshing(false);
    }

    @Override
    public void showMessage(String msg, int type) {
        super.showMessage(msg, type);
    }

    @Override
    public void showError(String message) {
        showMessage("无法加载更多相似视频", TastyToast.ERROR);
    }

    @Override
    public void onRefresh() {
        presenter.loadSimilarVideo(axgleVideo.getKeyword(), true);
    }

    @OnClick(R.id.fac_search)
    public void onViewClicked() {
        Intent axgleIntent = new Intent(this, SearchAxgleVideoActivity.class);
        startActivityWithAnimation(axgleIntent);
    }
}
