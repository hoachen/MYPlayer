package com.niuniu.player.internal;

import android.view.Surface;
import android.view.SurfaceHolder;


public interface IVideoView {

    // player must be setting before prepare, source include some player init params
    void source(final Source source);

    // player must call after source
    void prepare();

    // player get play type
    String getPlayerType();

    // bsplayer can be handler player lifecycle
    void start(long msc);

    void pause();

    void resume();

    void seekTo(long msc);

    void stop();

    void reset();

    void restart(); // must after the completed

    void release();

    // player can be settings value or attribute
    void surface(Surface surface);

    void surfaceHolder(SurfaceHolder surfaceHolder);

    void surfaceSizeChanged(int width, int height);

    void volume(float volume);

    void mute(boolean mute);

    void selectTrack(String trackName);
}
