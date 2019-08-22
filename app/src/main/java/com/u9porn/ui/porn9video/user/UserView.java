package com.u9porn.ui.porn9video.user;

import android.graphics.Bitmap;

import com.u9porn.data.model.User;
import com.u9porn.ui.BaseView;

/**
 * @author flymegoc
 * @date 2017/12/10
 */

public interface UserView extends BaseView {

    void loginSuccess(User user);

    void loginError(String message);

    void registerSuccess(User user);

    void registerFailure(String message);

    void loadCaptchaSuccess(Bitmap bitmap);

    void loadCaptchaFailure(String errorMessage, int code);
}
