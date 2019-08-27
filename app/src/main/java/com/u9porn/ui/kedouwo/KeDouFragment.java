package com.u9porn.ui.kedouwo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.helper.loadviewhelper.help.OnLoadViewListener;
import com.helper.loadviewhelper.load.LoadViewHelper;
import com.u9porn.R;
import com.u9porn.adapter.KeDouAdapter;
import com.u9porn.ui.MvpFragment;
import com.u9porn.utils.AppUtils;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by alex
 * Des:
 * Date: 2019/8/27.
 */
public class KeDouFragment extends MvpFragment<KeDouView,KeDouPresenter> implements KeDouView, SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.recyclerView_kedou)
    RecyclerView recyclerViewKeDou;
    @BindView(R.id.contentView)
    SwipeRefreshLayout contentView;
    Unbinder unbinder;
    private LoadViewHelper helper;

    @Inject
    protected KeDouPresenter mKeDouPresenter;
    private KeDouAdapter keDouAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_kedou, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
        keDouAdapter = new KeDouAdapter(R.layout.item_kedouwo);
        contentView.setOnRefreshListener(this);
        recyclerViewKeDou.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewKeDou.setAdapter(keDouAdapter);

        helper = new LoadViewHelper(recyclerViewKeDou);
        helper.setListener(new OnLoadViewListener() {
            @Override
            public void onRetryClick() {
//                loadData(false);
            }
        });
        AppUtils.setColorSchemeColors(context, contentView);
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

    @Override
    public void onRefresh() {

    }
}
