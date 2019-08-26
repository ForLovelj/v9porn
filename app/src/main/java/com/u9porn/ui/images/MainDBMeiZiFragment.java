package com.u9porn.ui.images;
import com.u9porn.data.db.entity.Category;
import com.u9porn.ui.basemain.BaseMainFragment;


public class MainDBMeiZiFragment extends BaseMainFragment {

    @Override
    public int getCategoryType() {
        return Category.TYPE_DOU_BAN;
    }

    public static MainDBMeiZiFragment getInstance() {
        return new MainDBMeiZiFragment();
    }

    @Override
    public boolean isNeedDestroy() {
        return true;
    }
}
