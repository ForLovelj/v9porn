package com.u9porn.ui.kedouwo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.u9porn.R;
import com.u9porn.ui.MvpFragment;

import javax.inject.Inject;

/**
 * Created by alex
 * Des:
 * Date: 2019/8/27.
 */
public class KeDouFragment extends MvpFragment<KeDouView,KeDouPresenter> implements KeDouView {

    @Inject
    protected KeDouPresenter mKeDouPresenter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_kedou, container, false);
    }

    @Override
    public void showLoading(boolean pullToRefresh) {

    }

    @Override
    public void showContent() {

    }

    @Override
    public void showMessage(String msg, int type) {

    }

    @Override
    public void showError(String message) {

    }

    @NonNull
    @Override
    public KeDouPresenter createPresenter() {
        return mKeDouPresenter;
    }
}
