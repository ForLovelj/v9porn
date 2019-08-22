package com.u9porn.ui.pxgav.playpxgav;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.devbrackets.android.exomedia.listener.OnPreparedListener;
import com.flymegoc.exolibrary.widget.ExoVideoControlsMobile;
import com.flymegoc.exolibrary.widget.ExoVideoView;
import com.jaeger.library.StatusBarUtil;
import com.orhanobut.logger.Logger;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.sdsmdg.tastytoast.TastyToast;
import com.u9porn.R;
import com.u9porn.adapter.PxgavAdapter;
import com.u9porn.constants.Keys;
import com.u9porn.data.model.pxgav.PxgavModel;
import com.u9porn.data.model.pxgav.PxgavVideoParserJsonResult;
import com.u9porn.ui.MvpActivity;
import com.u9porn.utils.DialogUtils;
import com.u9porn.utils.GlideApp;
import com.u9porn.utils.MyHeaderInjector;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author flymegoc
 */
public class PlayPxgavActivity extends MvpActivity<PlayPxgavView, PlayPxgavPresenter> implements PlayPxgavView, OnPreparedListener {

    private static final String TAG = PlayPxgavActivity.class.getSimpleName();
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.video_view)
    ExoVideoView videoPlayer;
    @BindView(R.id.play_container)
    FrameLayout playContainer;
    private ExoVideoControlsMobile videoControlsMobile;
    private boolean isPauseByActivityEvent = false;

    private AlertDialog alertDialog;

    @Inject
    protected PlayPxgavPresenter playPigAvPresenter;

    @Inject
    protected MyHeaderInjector myHeaderInjector;

    private PxgavModel pxgavModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_pav);
        ButterKnife.bind(this);
        setVideoViewHeight(playContainer);
        initDialog();
        videoControlsMobile = (ExoVideoControlsMobile) videoPlayer.getVideoControls();
        videoPlayer.setOnPreparedListener(this);
        videoControlsMobile.setOnBackButtonClickListener(new ExoVideoControlsMobile.OnBackButtonClickListener() {
            @Override
            public void onBackClick(View view) {
                onBackPressed();
            }
        });
        pxgavModel = (PxgavModel) getIntent().getSerializableExtra(Keys.KEY_INTENT_PAV_ITEM);
        if (pxgavModel != null) {
            parseVideoUrl(pxgavModel);
        } else {
            showMessage("参数错误，无法播放", TastyToast.WARNING);
        }
    }

    private void parseVideoUrl(PxgavModel pxgavModel) {
        videoControlsMobile.setTitle(pxgavModel.getTitle());
        presenter.parseVideoUrl(pxgavModel.getContentUrl(), pxgavModel.getpId(), false);
    }

    private void initDialog() {
        alertDialog = DialogUtils.initLoadingDialog(this, "解析视频地址中，请稍后...");
    }

    /**
     * 根据屏幕宽度信息重设videoview宽高为16：9比例
     */
    protected void setVideoViewHeight(View playerView) {
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) playerView.getLayoutParams();
        layoutParams.height = QMUIDisplayHelper.getScreenWidth(this) * 9 / 16;
        playerView.setLayoutParams(layoutParams);
    }

    @Override
    public void onPrepared() {
        videoPlayer.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!videoPlayer.isPlaying() && isPauseByActivityEvent) {
            isPauseByActivityEvent = false;
            videoPlayer.start();
        }
    }

    @NonNull
    @Override
    public PlayPxgavPresenter createPresenter() {
        return playPigAvPresenter;
    }

    @Override
    protected void onPause() {
        videoPlayer.pause();
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
        videoPlayer.release();
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

    @Override
    public void playVideo(PxgavVideoParserJsonResult pxgavVideoParserJsonResult) {
        String url = pxgavVideoParserJsonResult.getFile();
        GlideApp.with(context).load(pxgavVideoParserJsonResult.getImage()).into(videoPlayer.getPreviewImageView());
        if (TextUtils.isEmpty(url)) {
            showMessage("播放地址无效", TastyToast.ERROR);
            return;
        }
        myHeaderInjector.getHashMap().put("Referer", pxgavModel.getContentUrl());
        myHeaderInjector.addHeaders(url);
        //String proxyUrl = presenter.getVideoCacheProxyUrl(url);
        //视频地址已经变了，所有的视频都改成m3u8的格式了，不能缓存了，只能直接播放了
        Logger.t(TAG).d("视频地址："+url);
        videoPlayer.setVideoURI(Uri.parse(url));
    }

    @Override
    public void listVideo(List<PxgavModel> pxgavModelList) {
        PxgavAdapter pxgavAdapter = new PxgavAdapter(R.layout.item_pxgav);
        pxgavAdapter.setNewData(pxgavModelList);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(pxgavAdapter);
        pxgavAdapter.setOnItemClickListener((adapter, view, position) -> {
            PxgavModel pxgavModel = (PxgavModel) adapter.getItem(position);
            if (pxgavModel == null) {
                return;
            }
            videoPlayer.pause();
            videoPlayer.reset();
            parseVideoUrl(pxgavModel);
        });
    }

    @Override
    public void showLoading(boolean pullToRefresh) {
        alertDialog.show();
    }

    @Override
    public void showContent() {
        dismissDialog();
    }

    @Override
    public void showMessage(String msg, int type) {
        super.showMessage(msg, type);
    }

    @Override
    public void showError(String message) {
        showMessage(message, TastyToast.ERROR);
        dismissDialog();
    }

    private void dismissDialog() {
        if (alertDialog != null && alertDialog.isShowing() && !isFinishing()) {
            alertDialog.dismiss();
        }
    }
}
