package com.u9porn.data.model;

import java.util.List;

/**论坛帖子
 * @author flymegoc
 * @date 2018/1/24
 */

public class F9PornContent {
    /**
     * 帖子内容
     */
    private String content;
    /**
     * 帖子中所有图片链接
     */
    private List<String> imageList;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<String> getImageList() {
        return imageList;
    }

    public void setImageList(List<String> imageList) {
        this.imageList = imageList;
    }
}
