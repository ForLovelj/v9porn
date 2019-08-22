package com.u9porn.ui.main;

import android.text.TextUtils;

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;
import com.u9porn.data.DataManager;
import com.u9porn.ui.notice.NoticePresenter;
import com.u9porn.ui.update.UpdatePresenter;

import javax.inject.Inject;

/**
 * @author flymegoc
 * @date 2017/12/23
 */
public class MainPresenter extends MvpBasePresenter<MainView> implements IMain {

    private UpdatePresenter updatePresenter;
    private NoticePresenter noticePresenter;
    private DataManager dataManager;

    @Inject
    public MainPresenter(DataManager dataManager, UpdatePresenter updatePresenter, NoticePresenter noticePresenter) {
        this.dataManager = dataManager;
        this.updatePresenter = updatePresenter;
        this.noticePresenter = noticePresenter;
    }


    @Override
    public void setIgnoreUpdateVersionCode(int versionCode) {
        dataManager.setIgnoreUpdateVersionCode(versionCode);
    }

    @Override
    public void saveNoticeVersionCode(int versionCode) {
        dataManager.setNoticeVersionCode(versionCode);
    }

    @Override
    public void setMainSecondTabShow(String tabId) {
        dataManager.setMainSecondTabShow(tabId);
    }

    @Override
    public String getMainSecondTabShow() {
        return dataManager.getMainSecondTabShow();
    }

    @Override
    public void setMainFirstTabShow(String tabId) {
        dataManager.setMainFirstTabShow(tabId);
    }

    @Override
    public String getMainFirstTabShow() {
        return dataManager.getMainFirstTabShow();
    }

    @Override
    public boolean haveNotSetF9pornAddress() {
        return TextUtils.isEmpty(dataManager.getPorn9ForumAddress());
    }

    @Override
    public boolean haveNotSetV9pronAddress() {
        return TextUtils.isEmpty(dataManager.getPorn9VideoAddress());
    }

    @Override
    public boolean haveNotSetPavAddress() {
        return TextUtils.isEmpty(dataManager.getPavAddress());
    }

    @Override
    public boolean haveNotSetAxgleAddress() {
        return false;
    }

    @Override
    public boolean isUserLogin() {
        return dataManager.isUserLogin();
    }

    @Override
    public void setPorn9VideoAddress(String porn9VideoAddress) {
        dataManager.setPorn9VideoAddress(porn9VideoAddress);
    }

    @Override
    public void setPorn9ForumAddress(String porn9ForumAddress) {
        dataManager.setPorn9ForumAddress(porn9ForumAddress);
    }

    @Override
    public boolean isFixMainNavigation() {
        return dataManager.isFixMainNavigation();
    }
}
