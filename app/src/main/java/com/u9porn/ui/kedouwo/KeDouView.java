package com.u9porn.ui.kedouwo;

import com.u9porn.data.model.kedouwo.KeDouModel;
import com.u9porn.ui.BaseView;

import java.util.List;

/**
 * Created by alex
 * Des:
 * Date: 2019/8/27.
 */
public interface KeDouView extends BaseView {

    void setData(List<KeDouModel> keDouModelList);

    void loadMoreFailed();

    void noMoreData();

    void setMoreData(List<KeDouModel> keDouModelList);
}
