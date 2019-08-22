package com.u9porn.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.u9porn.R;

/**
 * @author flymegoc
 * @date 2018/3/8
 */

public class SkipPageAdapter extends BaseQuickAdapter<Integer, BaseViewHolder> {

    private int currentPage = 1;

    public SkipPageAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, Integer item) {
        helper.setText(R.id.tv_skip_page, String.valueOf(item));
        Context context = helper.itemView.getContext();
        if (item == currentPage) {
            helper.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary));
        } else {
            helper.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.common_background));
        }
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
        notifyDataSetChanged();
    }
}
