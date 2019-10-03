package com.niuniu.player.internal;

import android.view.Surface;
import android.view.SurfaceHolder;

import java.util.Map;

import androidx.annotation.NonNull;

public interface IPlayer {

    // player must be setting before prepare, source include some player init params
    IPlayer source(final Source source);

    // player must call after source
    IPlayer prepare();

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

    interface EventListener {

        void onStateChanged(@NonNull PlayState state);

        void onBufferingStart();

        void onBufferingEnd();

        void onSeekTo(long currentMs, long newPositionMs);

        void onSeekCompleted(long currentMs);

        void onVideoRenderStart();

        void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio);

        void onTrackInfoChanged(Map<String, Object> trackInfo);

        void onProgressUpdate(long position);

        void onError(Exception exception);

        void onOrientationChanged(boolean landscape);

        void onQualityChanged(String qualityName, int bitrate, boolean isAuto);

    }
}
