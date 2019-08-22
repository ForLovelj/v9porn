package com.u9porn.ui.about;

import com.u9porn.ui.update.UpdateView;

/**
 * @author flymegoc
 * @date 2017/12/23
 */

public interface AboutView extends UpdateView {
    void showCleanDialog(String message);

    void cleanCacheSuccess(String message);

    void cleanCacheFailure(String message);

    void finishCountCacheFileSize(String message);

    void countCacheFileSizeError(String message);

    void loadCommonQuestionsSuccess(String mdString);

    void loadCommonQuestionsFailure(String errorMessage, int code);
}
