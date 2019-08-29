package com.u9porn.adapter;

import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.u9porn.R;
import com.u9porn.data.model.kedouwo.KeDouModel;
import com.u9porn.utils.GlideApp;

public class KeDouAdapter extends BaseQuickAdapter<KeDouModel, BaseViewHolder> {

    public KeDouAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, KeDouModel item) {
        helper.setText(R.id.tv_kedou_title, getTitleWithDuration(item));
        helper.setText(R.id.tv_kedou_info, item.getInfo());
        ImageView simpleDraweeView = helper.getView(R.id.iv_kedou_img);
        Uri uri = Uri.parse(item.getImgUrl());
        GlideApp.with(helper.itemView).load(uri).placeholder(R.drawable.placeholder).transition(new DrawableTransitionOptions().crossFade(300)).into(simpleDraweeView);
    }

    private String getTitleWithDuration(KeDouModel keDouModel) {
        return keDouModel.getTitle() + "  (" + keDouModel.getDuration() + ")";
    }

}
