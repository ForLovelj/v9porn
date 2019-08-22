package com.u9porn.ui.pxgav.playpxgav;

import com.u9porn.data.model.pxgav.PxgavModel;
import com.u9porn.data.model.pxgav.PxgavVideoParserJsonResult;
import com.u9porn.ui.BaseView;

import java.util.List;

/**
 * @author flymegoc
 * @date 2018/1/30
 */

public interface PlayPxgavView extends BaseView {
    void playVideo(PxgavVideoParserJsonResult pxgavVideoParserJsonResult);

    void listVideo(List<PxgavModel> pxgavModelList);
}
