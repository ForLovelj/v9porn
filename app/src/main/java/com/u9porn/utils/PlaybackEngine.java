package com.u9porn.utils;

import android.content.Context;
import android.content.Intent;

import com.u9porn.ui.porn9video.play.ExoMediaPlayerActivity;
import com.u9porn.ui.porn9video.play.JiaoZiVideoPlayerActivity;

/**
 * 播放引擎切换
 *
 * @author flymegoc
 * @date 2018/1/2
 */

public class PlaybackEngine {
    public static final String[] PLAY_ENGINE_ITEMS = new String[]{"Google Exoplayer Engine", "JiaoZiPlayer Engine",};
    private static final int EXOMEDIAPLAYER_ENGINE = 0;
    private static final int JIAOZIVIDEOPLAYER_ENGINE = 1;
    public static final int DEFAULT_PLAYER_ENGINE = EXOMEDIAPLAYER_ENGINE;

    /**
     * 获取播放引擎
     *
     * @param context 上下文
     * @return intent
     */
    public static Intent getPlaybackEngineIntent(Context context, int engine) {

        Intent intent = new Intent();
        switch (engine) {
            case EXOMEDIAPLAYER_ENGINE:
                intent.setClass(context, ExoMediaPlayerActivity.class);
                break;
            case JIAOZIVIDEOPLAYER_ENGINE:
                intent.setClass(context, JiaoZiVideoPlayerActivity.class);
                break;
            default:
        }
        return intent;
    }
}
