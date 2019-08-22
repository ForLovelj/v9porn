package com.u9porn.data.db;

import com.u9porn.data.db.entity.Category;
import com.u9porn.data.db.entity.V9PornItem;
import com.u9porn.data.db.entity.VideoResult;

import java.util.List;

/**
 * @author flymegoc
 * @date 2018/3/4
 */

public interface DbHelper {

    void initCategory(int type, String[] value, String[] name);

    void updateV9PornItem(V9PornItem v9PornItem);

    List<V9PornItem> loadDownloadingData();

    List<V9PornItem> loadFinishedData();

    List<V9PornItem> loadHistoryData(int page, int pageSize);

    long saveV9PornItem(V9PornItem v9PornItem);

    long saveVideoResult(VideoResult videoResult);

    V9PornItem findV9PornItemByViewKey(String viewKey);

    V9PornItem findV9PornItemByDownloadId(int downloadId);

    List<V9PornItem> loadV9PornItems();

    List<V9PornItem> findV9PornItemByDownloadStatus(int status);

    List<Category> loadAllCategoryDataByType(int type);

    List<Category> loadCategoryDataByType(int type);

    void updateCategoryData(List<Category> categoryList);

    Category findCategoryById(Long id);
}
