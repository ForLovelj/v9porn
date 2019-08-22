package com.u9porn.ui.images;

import com.u9porn.data.db.entity.Category;
import com.u9porn.ui.basemain.BaseMainFragment;

/**
 * @author flymegoc
 * @date 2018/2/1
 */

public class Main99MmFragment extends BaseMainFragment {


    public static Main99MmFragment getInstance() {
        return new Main99MmFragment();
    }

    @Override
    public int getCategoryType() {
        return Category.TYPE_99_MM;
    }

    @Override
    public boolean isNeedDestroy() {
        return true;
    }
}
