package com.u9porn.ui.kedouwo.play;

import android.arch.lifecycle.Lifecycle;
import android.support.v7.app.AppCompatActivity;

import com.trello.lifecycle2.android.lifecycle.AndroidLifecycle;
import com.trello.rxlifecycle2.LifecycleProvider;

import dagger.Module;
import dagger.Provides;

/**
 * Created by alex
 * Des:
 * Date: 2019/8/28.
 */
@Module
public class KeDouPlayActivityModule {

    @Provides
    AppCompatActivity provideAppCompatActivity(KeDouPlayActivity keDouPlayActivity){
        return keDouPlayActivity;
    }

    @Provides
    static LifecycleProvider<Lifecycle.Event> providerLifecycleProvider(AppCompatActivity mAppCompatActivity) {
        return AndroidLifecycle.createLifecycleProvider(mAppCompatActivity);
    }
}
