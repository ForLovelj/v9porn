package com.u9porn.ui.porn9forum;

/**
 * @author flymegoc
 * @date 2018/1/23
 */

public interface IForum {
    void loadForumIndexListData(boolean pullToRefresh);

    void loadForumListData(boolean pullToRefresh,String fid);

    String getForum9PornAddress();
}
