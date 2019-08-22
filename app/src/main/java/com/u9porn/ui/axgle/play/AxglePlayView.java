package com.u9porn.ui.axgle.play;

import com.u9porn.data.model.axgle.AxgleVideo;
import com.u9porn.ui.BaseView;

import java.util.List;

public interface AxglePlayView extends BaseView {
    void showLoading();

    void getVideoUrlSuccess(String videoUrl);

    void getVideoUrlError();

    void setData(List<AxgleVideo> axgleVideoList);

    void loadMoreFailed();

    void noMoreData();

    void setMoreData(List<AxgleVideo> axgleVideoList);
}
