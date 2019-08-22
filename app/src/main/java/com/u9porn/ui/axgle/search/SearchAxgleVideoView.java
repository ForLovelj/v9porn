package com.u9porn.ui.axgle.search;

import com.u9porn.data.model.axgle.AxgleVideo;
import com.u9porn.ui.BaseView;

import java.util.List;

public interface SearchAxgleVideoView extends BaseView {

    void setData(List<AxgleVideo> axgleVideoList);

    void loadMoreFailed();

    void noMoreData();

    void setMoreData(List<AxgleVideo> axgleVideoList);
}
