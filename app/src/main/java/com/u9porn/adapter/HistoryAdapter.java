package com.u9porn.adapter;

import android.net.Uri;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.u9porn.R;
import com.u9porn.data.db.entity.V9PornItem;
import com.u9porn.utils.GlideApp;

import java.util.List;

/**
 * @author flymegoc
 * @date 2017/11/23
 * @describe
 */

public class HistoryAdapter extends BaseQuickAdapter<V9PornItem,BaseViewHolder>{

    public HistoryAdapter(int layoutResId, @Nullable List<V9PornItem> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, V9PornItem item) {
        helper.setText(R.id.tv_91porn_item_title, item.getTitle() + "  (" + item.getDuration() + ")");
        helper.setText(R.id.tv_91porn_item_info, item.getInfo());
        ImageView simpleDraweeView = helper.getView(R.id.iv_91porn_item_img);
        Uri uri = Uri.parse(item.getImgUrl());
        GlideApp.with(helper.itemView).load(uri).placeholder(R.drawable.placeholder).transition(new DrawableTransitionOptions().crossFade(300)).into(simpleDraweeView);

        helper.addOnClickListener(R.id.right_menu_delete);
    }
}
