package com.u9porn.ui.porn9video;


import android.support.v4.app.Fragment;

import com.u9porn.data.db.entity.Category;
import com.u9porn.ui.basemain.BaseMainFragment;

/**
 * A simple {@link Fragment} subclass.
 *
 * @author flymegoc
 */
public class Main9PronVideoFragment extends BaseMainFragment {

    @Override
    public int getCategoryType() {
        return Category.TYPE_91PORN;
    }

    public static Main9PronVideoFragment getInstance() {
        return new Main9PronVideoFragment();
    }
}
