package com.u9porn.ui.update;

import com.u9porn.data.model.UpdateVersion;
import com.u9porn.ui.BaseView;

/**
 * @author flymegoc
 * @date 2017/12/22
 */

public interface UpdateView extends BaseView {
    void needUpdate(UpdateVersion updateVersion);

    void noNeedUpdate();

    void checkUpdateError(String message);
}
