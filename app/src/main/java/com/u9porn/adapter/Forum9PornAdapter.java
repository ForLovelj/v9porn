package com.u9porn.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.View;

import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qmuiteam.qmui.span.QMUIAlignMiddleImageSpan;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.u9porn.R;
import com.u9porn.data.model.F9PronItem;
import com.u9porn.utils.GlideApp;

import java.util.List;

/**
 * @author flymegoc
 * @date 2018/1/23
 */

public class Forum9PornAdapter extends BaseQuickAdapter<F9PronItem, Forum9PornAdapter.ViewHolder> {
    private Context context;
    private String forum9PornAddress;

    public Forum9PornAdapter(Context context, String forum9PornAddress , int layoutResId, @Nullable List<F9PronItem> data) {
        super(layoutResId, data);
        this.context = context;
        this.forum9PornAddress=forum9PornAddress;
    }

    @Override
    protected void convert(final ViewHolder helper, final F9PronItem item) {
        final String title = "  " + item.getTitle() + "      " + (TextUtils.isEmpty(item.getAgreeCount()) ? " " : item.getAgreeCount());
        helper.spannableString = SpannableString.valueOf(title);
        GlideApp.with(context).asDrawable().load(Uri.parse(forum9PornAddress + item.getFolder())).into(new SimpleTarget<Drawable>() {
            @Override
            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                resource.setBounds(0, 0, QMUIDisplayHelper.px2dp(context, 150), QMUIDisplayHelper.px2dp(context, 150));
                QMUIAlignMiddleImageSpan qmuiAlignMiddleImageSpan = new QMUIAlignMiddleImageSpan(resource, QMUIAlignMiddleImageSpan.ALIGN_MIDDLE);
                helper.spannableString.setSpan(qmuiAlignMiddleImageSpan, 0, 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                helper.setText(R.id.tv_item_forum_91_porn_title, helper.spannableString);
            }
        });
        if (!TextUtils.isEmpty(item.getIcon())) {
            GlideApp.with(context).asDrawable().load(Uri.parse(forum9PornAddress + item.getIcon())).into(new SimpleTarget<Drawable>() {
                @Override
                public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                    resource.setBounds(0, 0, QMUIDisplayHelper.px2dp(context, 150), QMUIDisplayHelper.px2dp(context, 150));
                    QMUIAlignMiddleImageSpan qmuiAlignMiddleImageSpan = new QMUIAlignMiddleImageSpan(resource, QMUIAlignMiddleImageSpan.ALIGN_MIDDLE);
                    helper.spannableString.setSpan(qmuiAlignMiddleImageSpan, 1, 2, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                    helper.setText(R.id.tv_item_forum_91_porn_title, helper.spannableString);
                }
            });
        }
        if (item.getImageList() != null) {
            for (int i = 0; i < item.getImageList().size(); i++) {
                final int j = i;
                String url = item.getImageList().get(i);
                GlideApp.with(context).asDrawable().load(Uri.parse(forum9PornAddress + url)).into(new SimpleTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        resource.setBounds(0, 0, QMUIDisplayHelper.px2dp(context, 150), QMUIDisplayHelper.px2dp(context, 150));
                        QMUIAlignMiddleImageSpan qmuiAlignMiddleImageSpan = new QMUIAlignMiddleImageSpan(resource, QMUIAlignMiddleImageSpan.ALIGN_MIDDLE);
                        final int startIndex = item.getTitle().length() + j + 2;
                        final int endIndex = item.getTitle().length() + j + 3;
                        if (startIndex < endIndex && endIndex < helper.spannableString.length()) {
                            //很低的概率会发生越界setSpan (41 ... 42) ends beyond length 32 还超那么多
                            helper.spannableString.setSpan(qmuiAlignMiddleImageSpan, startIndex, endIndex, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                            helper.setText(R.id.tv_item_forum_91_porn_title, helper.spannableString);
                        }
                    }
                });
            }

            String essenceTag = "images/default/digest_1.gif";
            if (item.getImageList().contains(essenceTag)) {
                helper.setTextColor(R.id.tv_item_forum_91_porn_title, ContextCompat.getColor(context, R.color.forum_9_porn_essence));
            } else {
                helper.setTextColor(R.id.tv_item_forum_91_porn_title, ContextCompat.getColor(context, R.color.item_v9pron_title_text_color));
            }
        }
        helper.setText(R.id.tv_item_forum_91_porn_author_publish_time, item.getAuthor() + "\n" + item.getAuthorPublishTime());
        helper.setText(R.id.tv_item_forum_91_porn_reply_view, item.getReplyCount() + "/" + item.getViewCount());
        helper.setText(R.id.tv_item_forum_91_porn_last_post_author_time, item.getLastPostAuthor() + "\n" + item.getLastPostTime());
    }

    class ViewHolder extends BaseViewHolder {
        SpannableString spannableString;

        public ViewHolder(View view) {
            super(view);
        }
    }
}
