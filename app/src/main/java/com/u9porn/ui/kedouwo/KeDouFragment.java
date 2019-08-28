package com.u9porn.ui.kedouwo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.helper.loadviewhelper.load.LoadViewHelper;
import com.sdsmdg.tastytoast.TastyToast;
import com.u9porn.R;
import com.u9porn.adapter.KeDouAdapter;
import com.u9porn.constants.Keys;
import com.u9porn.data.model.kedouwo.KeDouModel;
import com.u9porn.ui.MvpFragment;
import com.u9porn.ui.kedouwo.play.KeDouPlayActivity;
import com.u9porn.utils.AppUtils;

import java.util.List;

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

    public static KeDouFragment getInstance() {
        return new KeDouFragment();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        //因为用的FragmentPagerAdapter不会缓存状态，fragment切换回onCreateView->onDestroyView 此处初始化adapter防止adapter被重复创建
        keDouAdapter = new KeDouAdapter(R.layout.item_kedouwo);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_kedou, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
        contentView.setOnRefreshListener(this);
        recyclerViewKeDou.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewKeDou.setAdapter(keDouAdapter);

        helper = new LoadViewHelper(recyclerViewKeDou);
        helper.setListener(() -> loadData(false));

        keDouAdapter.setOnItemClickListener((adapter, view1, position) -> {
            KeDouModel keDouModel = (KeDouModel) adapter.getItem(position);
            if (keDouModel == null) {
                return;
            }
            Intent intent = new Intent(context, KeDouPlayActivity.class);
            intent.putExtra(Keys.KEY_INTENT_KEDOUWO_ITEM, keDouModel);
            startActivityWithAnimation(intent);
        });
        keDouAdapter.setOnLoadMoreListener(() -> loadData(false), recyclerViewKeDou);
        AppUtils.setColorSchemeColors(context, contentView);
    }

    @Override
    protected void onLazyLoadOnce() {
        loadData(false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void showLoading(boolean pullToRefresh) {
        contentView.setRefreshing(false);
        helper.showLoading();
    }

    @Override
    public void showContent() {
        contentView.setRefreshing(false);
        helper.showContent();
    }

    @Override
    public void showMessage(String msg, int type) {
        super.showMessage(msg,type);
    }

    @Override
    public void showError(String message) {
        contentView.setRefreshing(false);
        showMessage(message, TastyToast.ERROR);
        helper.showError();
    }

    @NonNull
    @Override
    public KeDouPresenter createPresenter() {
        return mKeDouPresenter;
    }

    @Override
    public void onRefresh() {
        loadData(true);
    }

    private void loadData(boolean pullToRefresh) {
        mKeDouPresenter.videoList(category.getCategoryValue(), pullToRefresh);
    }

    @Override
    public void setData(List<KeDouModel> keDouModelList) {
        keDouAdapter.setNewData(keDouModelList);
    }

    @Override
    public void loadMoreFailed() {
        keDouAdapter.loadMoreFail();
    }

    @Override
    public void noMoreData() {
        keDouAdapter.loadMoreEnd();
    }

    @Override
    public void setMoreData(List<KeDouModel> keDouModelList) {
        keDouAdapter.addData(keDouModelList);
        keDouAdapter.loadMoreComplete();
    }
}
