package com.u9porn.ui.kedouwo.play;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;

import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.devbrackets.android.exomedia.listener.OnPreparedListener;
import com.flymegoc.exolibrary.widget.ExoVideoControlsMobile;
import com.flymegoc.exolibrary.widget.ExoVideoView;
import com.jaeger.library.StatusBarUtil;
import com.sdsmdg.tastytoast.TastyToast;
import com.u9porn.R;
import com.u9porn.adapter.KeDouAdapter;
import com.u9porn.constants.Keys;
import com.u9porn.data.model.kedouwo.KeDouModel;
import com.u9porn.data.model.kedouwo.KeDouRelated;
import com.u9porn.ui.MvpActivity;
import com.u9porn.utils.DialogUtils;
import com.u9porn.utils.GlideApp;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by alex
 * Des:
 * Date: 2019/8/28.
 */
public class KeDouPlayActivity extends MvpActivity<KeDouPlayView, KeDouPlayPresenter> implements KeDouPlayView, SwipeRefreshLayout.OnRefreshListener, OnPreparedListener {

    @BindView(R.id.video_view)
    ExoVideoView         mVideoView;
    @BindView(R.id.fr_container)
    FrameLayout          mFrContainer;
    @BindView(R.id.recyclerView)
    RecyclerView         mRecyclerView;
//    @BindView(R.id.swipe_layout)
//    SwipeRefreshLayout   mSwipeLayout;
    @Inject
    protected KeDouPlayPresenter     mKeDouPlayPresenter;
    private KeDouAdapter           mKeDouAdapter;
    private KeDouModel             mKeDouModel;
    private AlertDialog            mAlertDialog;
    private ExoVideoControlsMobile mExoVideoControlsMobile;
    private Unbinder mUnbinder;
    private boolean isPauseByActivityEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ke_dou_wo);
        mUnbinder = ButterKnife.bind(this);
        mKeDouAdapter = new KeDouAdapter(R.layout.item_kedouwo);
        mKeDouAdapter.setOnItemClickListener((adapter, view, position) -> {
            KeDouModel keDouModel = (KeDouModel) adapter.getItem(position);
            if (keDouModel == null) {
                return;
            }
            mKeDouModel = keDouModel;
            playVideo();
        });
//        mSwipeLayout.setOnRefreshListener(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mKeDouAdapter);
        mKeDouModel = (KeDouModel) getIntent().getSerializableExtra(Keys.KEY_INTENT_KEDOUWO_ITEM);
        if (mKeDouModel == null) {
            return;
        }
        mExoVideoControlsMobile = (ExoVideoControlsMobile) mVideoView.getVideoControls();

        mExoVideoControlsMobile.setTitle(mKeDouModel.getTitle());
        mAlertDialog = DialogUtils.initLoadingDialog(this, "获取视频地址中，请稍候...");
        mVideoView.setOnPreparedListener(this);
        mExoVideoControlsMobile.setOnBackButtonClickListener(new ExoVideoControlsMobile.OnBackButtonClickListener() {
            @Override
            public void onBackClick(View view) {
                onBackPressed();
            }
        });
        playVideo();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!mVideoView.isPlaying() && isPauseByActivityEvent) {
            isPauseByActivityEvent = false;
            mVideoView.start();
        }
    }

    @Override
    protected void onPause() {
        mVideoView.pause();
        isPauseByActivityEvent = true;
        super.onPause();

    }

    @Override
    public void onBackPressed() {
        if (mExoVideoControlsMobile.onBackPressed()) {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        mVideoView.release();
        mUnbinder.unbind();
        super.onDestroy();
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

    @NonNull
    @Override
    public KeDouPlayPresenter createPresenter() {
        return mKeDouPlayPresenter;
    }

    @Override
    public void showLoading(boolean pullToRefresh) {
        if (mAlertDialog != null && !mAlertDialog.isShowing()) {
            mAlertDialog.show();
        }
//        mSwipeLayout.setRefreshing(true);
    }

    @Override
    public void showContent() {
        dismissDialog();
//        mSwipeLayout.setRefreshing(false);
    }

    @Override
    public void showMessage(String msg, int type) {
        super.showMessage(msg,type);
    }

    @Override
    public void showError(String message) {
        showMessage("加载失败", TastyToast.ERROR);
    }

    @Override
    public void onRefresh() {
//        mSwipeLayout.setRefreshing(false);
    }

    @Override
    public void onPrepared() {
        mVideoView.start();
    }

    @Override
    public void onVideoDetail(KeDouRelated keDouRelated) {
        if(keDouRelated == null)return;
        String videoUrl = keDouRelated.getVideoUrl();
        presenter.getRealVideoUrl(videoUrl);

        List<KeDouModel> relatedList = keDouRelated.getRelatedList();
        mKeDouAdapter.setNewData(relatedList);
    }

    @Override
    public void onVideoUrl(String url) {
        mKeDouModel.setVideoUrl(url);
        mVideoView.setVideoPath(url);
    }

    private void dismissDialog() {
        if (mAlertDialog != null && mAlertDialog.isShowing()) {
            mAlertDialog.dismiss();
        }
    }

    private void playVideo() {
        if (!TextUtils.isEmpty(mKeDouModel.getImgUrl())) {
            GlideApp.with(this).load(Uri.parse(mKeDouModel.getImgUrl())).transition(new DrawableTransitionOptions().crossFade(300)).into(mVideoView.getPreviewImageView());
        }
        presenter.videoDetail(mKeDouModel.getContentUrl());
        mExoVideoControlsMobile.setTitle(mKeDouModel.getTitle());
    }
}
