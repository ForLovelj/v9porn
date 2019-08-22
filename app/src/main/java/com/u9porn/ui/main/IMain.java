package com.u9porn.ui.main;

/**
 * @author flymegoc
 * @date 2017/12/23
 */

public interface IMain {

    void saveNoticeVersionCode(int versionCode);

    void setIgnoreUpdateVersionCode(int versionCode);

    void setMainSecondTabShow(String tabId);

    String getMainSecondTabShow();

    void setMainFirstTabShow(String tabId);

    String getMainFirstTabShow();

    boolean haveNotSetF9pornAddress();

    boolean haveNotSetV9pronAddress();

    boolean haveNotSetPavAddress();

    boolean haveNotSetAxgleAddress();

    boolean isUserLogin();

    void setPorn9VideoAddress(String porn9VideoAddress);

    void setPorn9ForumAddress(String porn9ForumAddress);

    boolean isFixMainNavigation();
}
