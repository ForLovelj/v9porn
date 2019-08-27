package com.u9porn.ui.kedouwo;

import android.arch.lifecycle.Lifecycle;

import com.trello.rxlifecycle2.LifecycleProvider;
import com.u9porn.data.AppDataManager;
import com.u9porn.ui.MvpBasePresenter;

import javax.inject.Inject;

/**
 * Created by alex
 * Des:
 * Date: 2019/8/27.
 */
public class KeDouPresenter extends MvpBasePresenter<KeDouView> implements IKeDou {

    @Inject
    public KeDouPresenter(LifecycleProvider<Lifecycle.Event> provider, AppDataManager appDataManager) {
        super(provider, appDataManager);
    }
}
