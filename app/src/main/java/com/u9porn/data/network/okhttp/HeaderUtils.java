package com.u9porn.data.network.okhttp;

import com.u9porn.utils.AddressHelper;

/**
 * 给每个请求添加对应的referer header，一定程度上能改善超时现象
 *
 * @author flymegoc
 * @date 2018/1/2
 */

public class HeaderUtils {
    /**
     * 来自播放列表的header
     *
     * @param viewKey 视频key
     * @return header
     */
    public static String getPlayVideoReferer(String viewKey, AddressHelper addressHelper) {
        return addressHelper.getVideo9PornAddress() + "view_video.php?viewkey=" + viewKey;
    }

    /**
     * 来自主页的header
     *
     * @return header
     */
    public static String getIndexHeader(AddressHelper addressHelper) {
        return addressHelper.getVideo9PornAddress() + "index.php";
    }

    /**
     * 收藏
     *
     * @return header
     */
    public static String getFavHeader(AddressHelper addressHelper) {
        return addressHelper.getVideo9PornAddress() + "my_favour.php";
    }

    /**
     * 获取用户header
     *
     * @param action login or register
     * @return header
     */
    public static String getUserHeader(AddressHelper addressHelper, String action) {
        return addressHelper.getVideo9PornAddress() + action + ".php";
    }
}
