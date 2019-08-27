package com.u9porn.ui.kedouwo;

import com.u9porn.data.db.entity.Category;
import com.u9porn.ui.basemain.BaseMainFragment;

/**
 * Created by alex
 * Des:
 * Date: 2019/8/27.
 */
public class MainKeDouFragment extends BaseMainFragment {
    @Override
    public int getCategoryType() {
        return Category.TYPE_KE_DOU_WO;
    }

    public static MainKeDouFragment getInstance() {
        return new MainKeDouFragment();
    }
}
