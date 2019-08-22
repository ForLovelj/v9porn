package com.u9porn.ui.basemain;

import com.u9porn.data.db.entity.Category;
import com.u9porn.ui.BaseView;

import java.util.List;

/**
 *
 * @author flymegoc
 * @date 2018/1/25
 */

public interface BaseMainView extends BaseView{
    void onLoadCategoryData(List<Category> categoryList);
    void onLoadAllCategoryData(List<Category> categoryList);
}
