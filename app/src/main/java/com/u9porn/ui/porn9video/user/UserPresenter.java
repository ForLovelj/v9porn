package com.u9porn.ui.porn9video.user;

import android.arch.lifecycle.Lifecycle;
import android.graphics.Bitmap;

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;
import com.trello.rxlifecycle2.LifecycleProvider;
import com.u9porn.data.DataManager;
import com.u9porn.data.model.User;
import com.u9porn.rxjava.CallBackWrapper;
import com.u9porn.rxjava.RxSchedulersHelper;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;

/**
 * 用户登录
 *
 * @author flymegoc
 * @date 2017/12/10
 */

public class UserPresenter extends MvpBasePresenter<UserView> implements IUser {

    private LifecycleProvider<Lifecycle.Event> provider;
    private DataManager dataManager;

    @Inject
    public UserPresenter(LifecycleProvider<Lifecycle.Event> provider, DataManager dataManager) {
        this.provider = provider;
        this.dataManager = dataManager;
    }

    @Override
    public void login(String username, String password, String captcha) {
        login(username, password, captcha, null);
    }

    public void login(String username, String password, String captcha, final LoginListener loginListener) {
        dataManager.userLoginPorn9Video(username, password, captcha)
                .compose(RxSchedulersHelper.ioMainThread())
                .compose(provider.bindUntilEvent(Lifecycle.Event.ON_DESTROY))
                .subscribe(new CallBackWrapper<User>() {
                    @Override
                    public void onBegin(Disposable d) {
                        ifViewAttached(view -> {
                            if (loginListener == null) {
                                view.showLoading(true);
                            }
                        });
                    }

                    @Override
                    public void onSuccess(final User user) {
                        user.copyProperties(dataManager.getUser());
                        if (loginListener != null) {
                            loginListener.loginSuccess(user);
                        } else {
                            ifViewAttached(view -> {
                                view.showContent();
                                view.loginSuccess(user);
                            });
                        }

                    }

                    @Override
                    public void onError(final String msg, int code) {
                        if (loginListener != null) {
                            loginListener.loginFailure(msg);
                        } else {
                            ifViewAttached(view -> {
                                view.showContent();
                                view.loginError(msg);
                            });
                        }
                    }
                });
    }

    @Override
    public void register(String username, String password1, String password2, String email, String captchaInput) {
        dataManager.userRegisterPorn9Video(username, password1, password2, email, captchaInput)
                .compose(RxSchedulersHelper.ioMainThread())
                .compose(provider.bindUntilEvent(Lifecycle.Event.ON_DESTROY))
                .subscribe(new CallBackWrapper<User>() {
                    @Override
                    public void onBegin(Disposable d) {
                        ifViewAttached(view -> view.showLoading(true));
                    }

                    @Override
                    public void onSuccess(final User user) {
                        ifViewAttached(view -> {
                            user.copyProperties(dataManager.getUser());
                            view.showContent();
                            view.registerSuccess(user);
                        });
                    }

                    @Override
                    public void onError(final String msg, int code) {
                        ifViewAttached(view -> {
                            view.showContent();
                            view.registerFailure(msg);
                        });
                    }
                });
    }

    /**
     * 注册成功，默认保存用户名和密码
     */
    @Override
    public void saveUserInfoPrf(String username, String password) {
        dataManager.setPorn9VideoLoginUserName(username);
        //记住密码
        dataManager.setPorn9VideoLoginUserPassWord(password);
    }

    @Override
    public void saveUserInfoPrf(String username, String password, boolean isRememberPassword, boolean isAutoLogin) {
        dataManager.setPorn9VideoLoginUserName(username);
        //记住密码
        if (isRememberPassword) {
            dataManager.setPorn9VideoLoginUserPassWord(password);
        } else {
            dataManager.setPorn9VideoLoginUserPassWord("");
        }
        //自动登录
        if (isAutoLogin) {
            dataManager.setPorn9VideoUserAutoLogin(true);
        } else {
            dataManager.setPorn9VideoUserAutoLogin(false);
        }
    }

    @Override
    public void loadCaptcha() {
        dataManager.porn9VideoLoginCaptcha()
                .compose(RxSchedulersHelper.ioMainThread())
                .compose(provider.bindUntilEvent(Lifecycle.Event.ON_STOP))
                .subscribe(new CallBackWrapper<Bitmap>() {
                    @Override
                    public void onSuccess(final Bitmap bitmap) {
                        ifViewAttached(view -> view.loadCaptchaSuccess(bitmap));
                    }

                    @Override
                    public void onError(final String msg, final int code) {
                        ifViewAttached(view -> view.loadCaptchaFailure(msg, code));
                    }
                });
    }

    @Override
    public String getUserName() {
        return dataManager.getPorn9VideoLoginUserName();
    }

    @Override
    public String getPassword() {
        return dataManager.getPorn9VideoLoginUserPassword();
    }

    @Override
    public boolean isAutoLogin() {
        return dataManager.isPorn9VideoUserAutoLogin();
    }

    @Override
    public String getVideo9PornAddress() {
        return dataManager.getPorn9VideoAddress();
    }

    @Override
    public void existLogin() {
        dataManager.existLogin();
    }

    public interface LoginListener {
        void loginSuccess(User user);

        void loginFailure(String message);
    }
}
