package com.u9porn.ui.kedouwo.play;

import com.u9porn.data.model.kedouwo.KeDouRelated;
import com.u9porn.ui.BaseView;

/**
 * Created by alex
 * Des:
 * Date: 2019/8/28.
 */
public interface KeDouPlayView extends BaseView {

    void onVideoRelated(KeDouRelated keDouRelated);

    void onVideoRelatedError(String msg);

    void onVideoUrl(String url);
}
