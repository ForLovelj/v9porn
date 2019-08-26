package com.u9porn.ui.images.douban;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.sdsmdg.tastytoast.TastyToast;
import com.u9porn.R;
import com.u9porn.adapter.DouBanAdapter;
import com.u9porn.data.model.DouBanMeizi;
import com.u9porn.ui.MvpFragment;
import com.u9porn.ui.images.viewimage.PhotoImageActivity;
import com.u9porn.utils.AppUtils;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * A simple {@link Fragment} subclass.
 *
 * @author clow
 */
public class DouBanFragment extends MvpFragment<DouBanView, DouBanPresenter> implements DouBanView, SwipeRefreshLayout.OnRefreshListener{

    @BindView(R.id.recyclerView)
    RecyclerView       recyclerView;
    @BindView(R.id.swipe_layout)
    SwipeRefreshLayout swipeLayout;
    Unbinder unbinder;
    private   DouBanAdapter   mDouBanAdapter;
    @Inject
    protected DouBanPresenter douBanPresenter;

    public DouBanFragment() {
        // Required empty public constructor
    }

    public static DouBanFragment getInstance() {
        return new DouBanFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dou_ban, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
        swipeLayout.setOnRefreshListener(this);
        AppUtils.setColorSchemeColors(context, swipeLayout);
        mDouBanAdapter = new DouBanAdapter(R.layout.item_dou_ban_mei_zi);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setAdapter(mDouBanAdapter);
        mDouBanAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                presenter.listDouBanMeiZhi(Integer.parseInt(category.getCategoryValue()), false);
            }
        });
        mDouBanAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                DouBanMeizi douBanMeizi = mDouBanAdapter.getData().get(position);
                if (douBanMeizi != null) {
                    startOptionsActivity(getActivity(), view, position,douBanMeizi.getUrl());
                }
            }
        });
    }

    @NonNull
    @Override
    public DouBanPresenter createPresenter() {
        return douBanPresenter;
    }
    private static final String OPTION_IMAGE = "img";

    public void startOptionsActivity(Activity activity, View transitionView, int position, String url) {
        Intent intent = new Intent(activity, PhotoImageActivity.class);
        intent.putExtra("imgurl", url);
        // 这里指定了共享的视图元素
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, transitionView, OPTION_IMAGE);
        ActivityCompat.startActivity(activity, intent, options.toBundle());
    }

    @Override
    protected void onLazyLoadOnce() {
        super.onLazyLoadOnce();
        presenter.listDouBanMeiZhi(Integer.parseInt(category.getCategoryValue()), false);
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
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onRefresh() {
        presenter.listDouBanMeiZhi(Integer.parseInt(category.getCategoryValue()), true);
    }

    @Override
    public void loadMoreFailed() {
        mDouBanAdapter.loadMoreFail();
    }

    @Override
    public void noMoreData() {
        mDouBanAdapter.loadMoreEnd(true);
    }

    @Override
    public void setMoreData(List<DouBanMeizi> douBanMeiziList) {
        mDouBanAdapter.loadMoreComplete();
        mDouBanAdapter.addData(douBanMeiziList);
    }

    @Override
    public void setData(List<DouBanMeizi> data) {
        mDouBanAdapter.setNewData(data);
    }
}
