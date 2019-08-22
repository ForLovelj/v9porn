package com.u9porn.ui.splash;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.Button;

import com.orhanobut.logger.Logger;
import com.u9porn.R;
import com.u9porn.constants.Keys;
import com.u9porn.data.model.Notice;
import com.u9porn.data.model.UpdateVersion;
import com.u9porn.ui.MvpActivity;
import com.u9porn.ui.main.MainActivity;
import com.u9porn.utils.ApkVersionUtils;

import java.io.Serializable;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author flymegoc
 */
public class SplashActivity extends MvpActivity<SplashView, SplashPresenter> implements SplashView {

    private static final String TAG = SplashActivity.class.getSimpleName();

    @Inject
    protected SplashPresenter splashPresenter;
    @BindView(R.id.bt_skip_splash)
    Button btSkipSplash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        findViewById(android.R.id.content).setPadding(0, 0, 0, 0);
        checkUpdate();

    }

    private void startMain(String key, Serializable serializable) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(key, serializable);
        startActivity(intent);
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @NonNull
    @Override
    public SplashPresenter createPresenter() {
        return splashPresenter;
    }


    private void checkUpdate() {
        int versionCode = ApkVersionUtils.getVersionCode(this);
        if (versionCode == 0) {
            Logger.t(TAG).d("获取应用本版失败");
            return;
        }
        presenter.checkUpdate(versionCode);
    }

    private void checkNewNotice() {
        presenter.checkNewNotice();
    }


    @Override
    public void needUpdate(UpdateVersion updateVersion) {
        int versionCode = presenter.getIgnoreUpdateVersionCode();
        //如果保存的版本号等于当前要升级的版本号，表示用户已经选择不在提示，不显示提示对话框了
        if (versionCode == updateVersion.getVersionCode()) {
            return;
        }
        //有更新直接跳转主界面并提示更新，不检查公告
        startMain(Keys.KEY_INTENT_UPDATE, updateVersion);
    }


    @Override
    public void haveNewNotice(Notice notice) {
        startMain(Keys.KEY_INTENT_NOTICE, notice);
    }

    @Override
    public void noNewNotice() {
        Logger.t(TAG).d("没有新公告");
        startMain(null, null);
    }

    @Override
    public void checkNewNoticeError(String message) {
        Logger.t(TAG).d("检查新公告：" + message);
        startMain(null, null);
    }


    @Override
    public void noNeedUpdate() {
        Logger.t(TAG).d("当前已是最新版本");
        //没有更新在检查公告
        checkNewNotice();
    }

    @Override
    public void checkUpdateError(String message) {
        Logger.t(TAG).d("检查更新错误：" + message);
        checkNewNotice();
    }

    @Override
    public void showError(String message) {

    }

    @Override
    public void showLoading(boolean pullToRefresh) {

    }

    @Override
    public void showContent() {

    }

    @Override
    public void showMessage(String msg, int type) {
        super.showMessage(msg, type);
    }

    @OnClick(R.id.bt_skip_splash)
    public void onViewClicked() {
        Intent intent = new Intent(this, MainActivity.class);
        //intent.putExtra(key, serializable);
        startActivity(intent);
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}
