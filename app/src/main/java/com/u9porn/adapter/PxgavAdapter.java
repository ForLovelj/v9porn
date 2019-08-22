package com.u9porn.adapter;

import android.support.v7.widget.AppCompatImageView;

import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.u9porn.R;
import com.u9porn.data.model.pxgav.PxgavModel;
import com.u9porn.utils.GlideApp;

/**
 * @author flymegoc
 * @date 2018/1/30
 */

public class PxgavAdapter extends BaseQuickAdapter<PxgavModel, BaseViewHolder> {

    public PxgavAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, PxgavModel item) {
        helper.setText(R.id.tv_item_pig_av_title, item.getTitle());
        AppCompatImageView imageView = helper.getView(R.id.iv_item_pig_av_img);
        GlideApp.with(helper.itemView).load(item.getImgUrl()).placeholder(R.drawable.placeholder).transition(new DrawableTransitionOptions().crossFade(300)).into(imageView);
    }
}
