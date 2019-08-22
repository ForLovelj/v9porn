package com.u9porn.ui.porn9video.user;

/**
 * @author flymegoc
 * @date 2017/12/10
 */

public interface IUser extends IBaseUser {
    void register(String username, String password1, String password2, String email, String captchaInput);
    void existLogin();
    void saveUserInfoPrf(String username, String password);
    void saveUserInfoPrf(String username, String password,boolean isRememberPassword,boolean isAutoLogin);

    void loadCaptcha();

    String getUserName();
    String getPassword();
    boolean isAutoLogin();

    String getVideo9PornAddress();
}
