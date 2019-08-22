package com.u9porn.ui.axgle.search;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.u9porn.R;
import com.u9porn.adapter.AxgleAdapter;
import com.u9porn.constants.Keys;
import com.u9porn.data.model.axgle.AxgleVideo;
import com.u9porn.ui.MvpActivity;
import com.u9porn.ui.axgle.play.AxglePlayActivity;
import com.u9porn.utils.DialogUtils;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchAxgleVideoActivity extends MvpActivity<SearchAxgleVideoView, SearchAxgleVideoPresenter> implements SearchAxgleVideoView {

    @Inject
    protected SearchAxgleVideoPresenter searchAxgleVideoPresenter;
    @BindView(R.id.search_view)
    SearchView searchView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recycler_view_search_axgle_video)
    RecyclerView recyclerViewSearchAxgleVideo;
    @BindView(R.id.checkbox_search_jav_video)
    AppCompatCheckBox checkboxSearchJavVideo;

    private String searchId;

    private AxgleAdapter axgleAdapter;

    private AlertDialog searchAlertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_axgle_video);
        ButterKnife.bind(this);
        init();
        setListener();
    }

    private void init() {
        searchAlertDialog = DialogUtils.initLoadingDialog(this, "搜索中，请稍后...");
        initToolBar(toolbar);
        searchView.setQueryHint("输入关键词或者番号...");
        searchView.onActionViewExpanded();

        axgleAdapter = new AxgleAdapter(R.layout.item_axgle);
        axgleAdapter.openLoadAnimation();
        recyclerViewSearchAxgleVideo.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewSearchAxgleVideo.setAdapter(axgleAdapter);

        axgleAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(SearchAxgleVideoActivity.this, AxglePlayActivity.class);
                AxgleVideo axgleVideo = (AxgleVideo) adapter.getItem(position);
                if (axgleVideo == null) {
                    return;
                }
                intent.putExtra(Keys.KEY_INTENT_AXGLE_VIDEO_ITEM, axgleVideo);
                startActivityWithAnimation(intent);
            }
        });
        axgleAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                presenter.searchAxgleVideo(searchId, checkboxSearchJavVideo.isChecked(), false);
            }
        }, recyclerViewSearchAxgleVideo);
    }

    private void setListener() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                searchId = query;
                presenter.searchAxgleVideo(searchId, checkboxSearchJavVideo.isChecked(), true);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                return true;
            }
        });
    }

    @NonNull
    @Override
    public SearchAxgleVideoPresenter createPresenter() {
        return searchAxgleVideoPresenter;
    }

    @Override
    public void showLoading(boolean pullToRefresh) {
        if (searchAlertDialog != null && !searchAlertDialog.isShowing()) {
            searchAlertDialog.show();
        }
    }

    @Override
    public void showContent() {
        dismissDialog();
    }

    @Override
    public void showMessage(String msg, int type) {
        super.showMessage(msg, type);
    }

    @Override
    public void showError(String message) {
        dismissDialog();
    }

    @Override
    public void setData(List<AxgleVideo> axgleVideoList) {
        axgleAdapter.setNewData(axgleVideoList);
    }

    @Override
    public void loadMoreFailed() {
        axgleAdapter.loadMoreFail();
    }

    @Override
    public void noMoreData() {
        axgleAdapter.loadMoreEnd(true);
    }

    @Override
    public void setMoreData(List<AxgleVideo> axgleVideoList) {
        axgleAdapter.addData(axgleVideoList);
        axgleAdapter.loadMoreComplete();
    }

    private void dismissDialog(){
        if (searchAlertDialog != null && searchAlertDialog.isShowing()) {
            searchAlertDialog.dismiss();
        }
    }
}
