package com.u9porn.ui.pxgav;

import com.u9porn.data.model.pxgav.PxgavModel;
import com.u9porn.ui.BaseView;

import java.util.List;

/**
 * @author flymegoc
 * @date 2018/1/30
 */

public interface PxgavView extends BaseView {
    void setData(List<PxgavModel> pxgavModelList);

    void loadMoreFailed();

    void noMoreData();

    void setMoreData(List<PxgavModel> pxgavModelList);
}
