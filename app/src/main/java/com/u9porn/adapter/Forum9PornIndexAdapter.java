package com.u9porn.adapter;

import com.chad.library.adapter.base.BaseViewHolder;
import com.u9porn.R;
import com.u9porn.data.model.F9PronItem;
import com.u9porn.data.model.PinnedHeaderEntity;

import java.util.List;

/**
 * @author flymegoc
 * @date 2018/1/24
 */

public class Forum9PornIndexAdapter extends BaseHeaderAdapter<PinnedHeaderEntity<F9PronItem>> {


    public Forum9PornIndexAdapter(List<PinnedHeaderEntity<F9PronItem>> data) {
        super(data);
    }

    @Override
    protected void addItemTypes() {
        addItemType(BaseHeaderAdapter.TYPE_HEADER, R.layout.item_forum_9_porn_section_head);
        addItemType(BaseHeaderAdapter.TYPE_DATA, R.layout.item_forum_9_porn);
    }

    @Override
    protected void convert(BaseViewHolder helper, PinnedHeaderEntity<F9PronItem> item) {
        switch (helper.getItemViewType()) {
            case TYPE_HEADER:
                helper.setText(R.id.tv_item_forum_91_porn_section_header_title, item.getPinnedHeaderName());
                break;
            case TYPE_DATA:
                helper.setText(R.id.tv_item_forum_91_porn_title, item.getData().getTitle());
                helper.setText(R.id.tv_item_forum_91_porn_author_publish_time, item.getData().getAuthor() + "\n" + item.getData().getAuthorPublishTime());
                helper.setText(R.id.tv_item_forum_91_porn_reply_view, item.getData().getReplyCount() + "/" + item.getData().getViewCount());
                helper.setText(R.id.tv_item_forum_91_porn_last_post_author_time, item.getData().getLastPostAuthor() + "\n" + item.getData().getLastPostTime());
                break;
            default:
        }
    }
}
