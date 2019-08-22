package com.u9porn.ui.porn9forum.browse9forum;

import com.u9porn.ui.BaseView;

import java.util.List;

/**
 *
 * @author flymegoc
 * @date 2018/1/24
 */

public interface Browse9View extends BaseView{
    void loadContentSuccess(String contentHtml, List<String> contentImageList,boolean isNightModel);
}
