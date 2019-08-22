package com.u9porn.ui.notice;

import com.u9porn.data.model.Notice;
import com.u9porn.ui.update.UpdateView;

/**
 * @author flymegoc
 * @date 2018/1/26
 */

public interface NoticeView extends UpdateView {
    void haveNewNotice(Notice notice);

    void noNewNotice();

    void checkNewNoticeError(String message);
}
