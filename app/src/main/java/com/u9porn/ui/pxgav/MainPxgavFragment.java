package com.u9porn.ui.pxgav;

import com.u9porn.data.db.entity.Category;
import com.u9porn.ui.basemain.BaseMainFragment;

/**
 * @author flymegoc
 * @date 2018/1/29
 */

public class MainPxgavFragment extends BaseMainFragment {

    public static MainPxgavFragment getInstance() {
        return new MainPxgavFragment();
    }

    @Override
    public int getCategoryType() {
        return Category.TYPE_PXG_AV;
    }

    @Override
    public boolean isNeedDestroy() {
        return true;
    }
}
