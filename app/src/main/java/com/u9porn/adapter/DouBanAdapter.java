package com.u9porn.adapter;

import android.widget.ImageView;

import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.u9porn.R;
import com.u9porn.data.model.DouBanMeizi;
import com.u9porn.utils.GlideApp;


public class DouBanAdapter extends BaseQuickAdapter<DouBanMeizi, BaseViewHolder> {

    public DouBanAdapter(int layoutResId) {
        super(layoutResId);
    }


    @Override
    protected void convert(BaseViewHolder helper, DouBanMeizi item) {
        ImageView imageView = helper.getView(R.id.iv_meizhi);
        GlideApp.with(helper.itemView.getContext()).load(item.getUrl()).transition(new DrawableTransitionOptions().crossFade(300)).into(imageView);
        helper.setText(R.id.tv_title, item.getTitle());

        imageView.setTag(R.string.app_name, item.getUrl());
    }
}
