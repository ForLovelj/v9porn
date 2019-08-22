package com.u9porn.ui;

import android.arch.lifecycle.Lifecycle;

import com.trello.rxlifecycle2.LifecycleProvider;
import com.u9porn.data.cache.CacheProviders;
import com.u9porn.data.AppDataManager;

/**
 * @author flymegoc
 * @date 2018/2/4
 */

public class BasePresenter {
    protected CacheProviders cacheProviders;
    protected LifecycleProvider<Lifecycle.Event> provider;
    protected AppDataManager appDataManager;

    public BasePresenter(AppDataManager appDataManager) {
        this.appDataManager = appDataManager;
    }

    public BasePresenter(CacheProviders cacheProviders) {
        this.cacheProviders = cacheProviders;
    }

    public BasePresenter(LifecycleProvider<Lifecycle.Event> provider) {
        this.provider = provider;
    }


    public BasePresenter(CacheProviders cacheProviders, LifecycleProvider<Lifecycle.Event> provider) {
        this.cacheProviders = cacheProviders;
        this.provider = provider;
    }

    public BasePresenter(LifecycleProvider<Lifecycle.Event> provider, AppDataManager appDataManager) {
        this.provider = provider;
        this.appDataManager = appDataManager;
    }

    public BasePresenter(CacheProviders cacheProviders, LifecycleProvider<Lifecycle.Event> provider, AppDataManager appDataManager) {
        this.cacheProviders = cacheProviders;
        this.provider = provider;
        this.appDataManager = appDataManager;
    }
}
