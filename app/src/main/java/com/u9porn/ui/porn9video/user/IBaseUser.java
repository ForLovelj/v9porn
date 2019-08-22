package com.u9porn.ui.porn9video.user;

/**
 * @author flymegoc
 * @date 2017/12/10
 */

public interface IBaseUser {
    /**
     * @param username 用户名
     * @param password 密码
     * @param captcha  验证码
     */

    void login(String username, String password, String captcha);

}
