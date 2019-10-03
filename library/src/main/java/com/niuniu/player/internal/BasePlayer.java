package com.niuniu.player.internal;

import com.niuniu.player.utils.Logger;

import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;

public abstract class BasePlayer implements IPlayer {

    protected volatile PlayState mState = PlayState.IDLE;

    protected CopyOnWriteArraySet<EventListener> mEventListeners = new CopyOnWriteArraySet<>();


    public void notifyStatusChanged(PlayState state) {
        this.mState = state;
        Logger.d("IPlayer", "notifyStateChanged:" + state.getName());
        for (EventListener listener : this.mEventListeners) {
            listener.onStateChanged(state);
        }
    }

    public void notifyBufferingStart() {
        for (EventListener listener : this.mEventListeners) {
            listener.onBufferingStart();
        }
    }

    public void notifyBufferingEnd() {
        for (EventListener listener : this.mEventListeners) {
            listener.onBufferingEnd();
        }
    }

    public void notifySeekTo(long currentMs, long newPositionMs) {
        for (EventListener listener : this.mEventListeners) {
            listener.onSeekTo(currentMs, newPositionMs);
        }
    }


    public void notifySeekCompleted(long currentMs) {
        for (EventListener listener : this.mEventListeners) {
            listener.onSeekCompleted(currentMs);
        }
    }

    public final void notifyVideoRenderStart() {
        for (EventListener listener : this.mEventListeners) {
            listener.onVideoRenderStart();
        }
    }

    public final void notifyVideoSizeChanged(int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio) {
        for (EventListener listener : this.mEventListeners) {
            listener.onVideoSizeChanged(width, height, unappliedRotationDegrees, pixelWidthHeightRatio);
        }
    }

    public final void notifyTrackInfoChanged(Map<String, Object> trackInfo) {
        for (EventListener listener : this.mEventListeners) {
            listener.onTrackInfoChanged(trackInfo);
        }
    }

    public void notifyProgressUpdate(long position) {
        for (EventListener listener : this.mEventListeners) {
            listener.onProgressUpdate(position);
        }
    }

    public void notifyError(Exception exception) {
        if (exception == null)
            return;
        for (EventListener listener : this.mEventListeners) {
            listener.onError(exception);
        }
    }

    public final void notifyOrientationChange(boolean landscape) {
        for (EventListener listener : this.mEventListeners) {
            listener.onOrientationChanged(landscape);
        }
    }

    public final void notifyQualityChanged(String qualityName, int bitrate, boolean isAuto) {
        for (EventListener listener : this.mEventListeners) {
            listener.onQualityChanged(qualityName, bitrate, isAuto);
        }
    }

}
