package com.u9porn.ui.images.douban;

import com.u9porn.data.model.DouBanMeizi;
import com.u9porn.ui.BaseView;

import java.util.List;


public interface DouBanView extends BaseView {

    void loadMoreFailed();

    void noMoreData();

    void setMoreData(List<DouBanMeizi> douBanMeiziList);

    void setData(List<DouBanMeizi> data);
}
