package com.u9porn.ui.pxgav;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sdsmdg.tastytoast.TastyToast;
import com.u9porn.R;
import com.u9porn.adapter.PxgavAdapter;
import com.u9porn.constants.Keys;
import com.u9porn.data.model.pxgav.PxgavModel;
import com.u9porn.ui.MvpFragment;
import com.u9porn.ui.pxgav.playpxgav.PlayPxgavActivity;
import com.u9porn.utils.AppUtils;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 *
 * @author flymegoc
 */
public class PxgavFragment extends MvpFragment<PxgavView, PxgavPresenter> implements PxgavView, SwipeRefreshLayout.OnRefreshListener {


    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.swipe_layout)
    SwipeRefreshLayout swipeLayout;
    Unbinder unbinder;
    private PxgavAdapter pxgavAdapter;

    @Inject
    protected PxgavPresenter pigAvPresenter;

    public PxgavFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pxgavAdapter = new PxgavAdapter(R.layout.item_pxgav);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_pav, container, false);
    }

    @NonNull
    @Override
    public PxgavPresenter createPresenter() {

        return pigAvPresenter;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
        swipeLayout.setOnRefreshListener(this);
        AppUtils.setColorSchemeColors(context, swipeLayout);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(pxgavAdapter);
        pxgavAdapter.setOnItemClickListener((adapter, view1, position) -> {
            PxgavModel pxgavModel = (PxgavModel) adapter.getItem(position);
            if (pxgavModel == null) {
                return;
            }
            Intent intent = new Intent(context, PlayPxgavActivity.class);
            intent.putExtra(Keys.KEY_INTENT_PAV_ITEM, pxgavModel);
            startActivityWithAnimation(intent);
        });
        //主页没有更多
        if (!"index".equalsIgnoreCase(category.getCategoryValue())) {
            pxgavAdapter.setOnLoadMoreListener(() -> presenter.moreVideoList(category.getCategoryValue(), false));
        }
    }

    @Override
    protected void onLazyLoadOnce() {
        super.onLazyLoadOnce();
        presenter.videoList(category.getCategoryValue(), false);
    }

    public static PxgavFragment getInstance() {
        return new PxgavFragment();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void setData(List<PxgavModel> pxgavModelList) {
        pxgavAdapter.setNewData(pxgavModelList);
    }

    @Override
    public void loadMoreFailed() {
        pxgavAdapter.loadMoreFail();
    }

    @Override
    public void noMoreData() {
        pxgavAdapter.loadMoreEnd(true);
    }

    @Override
    public void setMoreData(List<PxgavModel> pxgavModelList) {
        pxgavAdapter.loadMoreComplete();
        pxgavAdapter.addData(pxgavModelList);
    }

    @Override
    public void showLoading(boolean pullToRefresh) {
        swipeLayout.setRefreshing(pullToRefresh);
    }

    @Override
    public void showContent() {
        swipeLayout.setRefreshing(false);
    }

    @Override
    public void showMessage(String msg, int type) {
        super.showMessage(msg, type);
    }

    @Override
    public void showError(String message) {
        swipeLayout.setRefreshing(false);
        showMessage(message, TastyToast.ERROR);
    }

    @Override
    public void onRefresh() {
        presenter.videoList(category.getCategoryValue(), true);
    }
}
