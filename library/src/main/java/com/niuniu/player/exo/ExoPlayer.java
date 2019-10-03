package com.niuniu.player.exo;

import android.content.Context;
import android.net.Uri;
import android.view.Surface;
import android.view.SurfaceHolder;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.video.VideoListener;
import com.niuniu.player.internal.BasePlayer;
import com.niuniu.player.internal.IPlayer;
import com.niuniu.player.internal.PlayState;
import com.niuniu.player.internal.PlayerException;
import com.niuniu.player.internal.Source;


public class ExoPlayer extends BasePlayer {

    private SimpleExoPlayer mPlayer;

    private ExoPlayerEventListener mPlayerListener;

    private Source mSource;

    private Context mContext;

    private long mStartPos;

    private boolean isPendingSeek;
    private boolean isBuffering;
    private boolean isPreparing;

    public ExoPlayer(Context context) {
        mContext = context.getApplicationContext();
        mPlayer = ExoPlayerFactory.newSimpleInstance(mContext);
        mPlayerListener = new ExoPlayerEventListener();
        mPlayer.addListener(mPlayerListener);
        mPlayer.addVideoListener(mPlayerListener);
        notifyStatusChanged(PlayState.INITED);
    }

    @Override
    public IPlayer source(Source source) {
        mSource = source;
        return this;
    }

    @Override
    public IPlayer prepare() {
        isPreparing = true;
        MediaSourceFactory factory = new MediaSourceFactory(mContext.getApplicationContext());
        MediaSource mediaSource = factory.buildMediaSource(Uri.parse(mSource.value()));
        mPlayer.prepare(mediaSource);
        mPlayer.setPlayWhenReady(false);
        notifyStatusChanged(PlayState.PREPARE);
        return this;
    }

    @Override
    public String getPlayerType() {
        return "exo";
    }

    @Override
    public void start(long msc) {
        mStartPos = msc;
        mPlayer.setPlayWhenReady(true);
    }

    @Override
    public void pause() {
        if (PlayState.PLAYING == mState) {
            mPlayer.setPlayWhenReady(false);
        }
    }

    @Override
    public void resume() {
        if (PlayState.PAUSED == mState) {
            mPlayer.setPlayWhenReady(true);
        }
    }

    @Override
    public void seekTo(long msc) {
        isPendingSeek = true;
        notifySeekTo(mPlayer.getCurrentPosition(), msc);
        mPlayer.seekTo(msc);
    }

    @Override
    public void stop() {
        if (mPlayer != null) {
            mPlayer.stop();
        }
        notifyStatusChanged(PlayState.STOPPED);
    }

    @Override
    public void reset() {
        if (mPlayer != null) {
            mPlayer.stop();
        }
        isPendingSeek = false;
        isPreparing = false;
    }

    @Override
    public void restart() {
        if (PlayState.COMPLETE == mState) {
            mPlayer.seekTo(0);
        }
    }

    @Override
    public void release() {
        if (mPlayer != null) {
            mPlayer.release();
            mPlayer.removeListener(mPlayerListener);
            mPlayer.removeVideoListener(mPlayerListener);
            mPlayer = null;
            notifyStatusChanged(PlayState.RELEASE);
        }
        resetParam();
    }


    private void resetParam() {
        this.isPreparing = false;
        this.isPendingSeek = false;
        this.isBuffering = false;
        this.mStartPos = 0;
    }

    @Override
    public void surface(Surface surface) {
        mPlayer.setVideoSurface(surface);
    }

    @Override
    public void surfaceHolder(SurfaceHolder surfaceHolder) {
        mPlayer.setVideoSurfaceHolder(surfaceHolder);
    }

    @Override
    public void surfaceSizeChanged(int width, int height) {

    }

    @Override
    public void volume(float volume) {
        if (mPlayer != null) {
            mPlayer.setVolume(volume);
        }
    }

    @Override
    public void mute(boolean mute) {
        if (mPlayer != null) {
            mPlayer.setVolume(mute ? 0 : 1);
        }
    }

    @Override
    public void selectTrack(String trackName) {
    }


    class ExoPlayerEventListener implements Player.EventListener, VideoListener {

        @Override
        public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
            if (!isPreparing && playbackState != Player.STATE_IDLE && !isPendingSeek) {
                if (playWhenReady) {
                    notifyStatusChanged(PlayState.PLAYING);
                } else {
                    notifyStatusChanged(PlayState.PAUSED);
                    return;
                }
            }

            switch (playbackState) {
                case Player.STATE_READY:
                    if (isPreparing) {
                        isPreparing = false;
                        notifyStatusChanged(PlayState.PREPARED);
                        if (mStartPos > 0) {
                            mPlayer.seekTo(mStartPos);
                            mStartPos = -1;
                        }
                    }
                    if (isBuffering) {
                        isBuffering = false;
                        notifyBufferingEnd();
                        notifyStatusChanged(PlayState.PLAYING);
                    }
                    if (isPendingSeek) {
                        isPendingSeek = false;
                        notifySeekCompleted(mPlayer.getCurrentPosition());
                        if (mPlayer != null)
                            mPlayer.setPlayWhenReady(true);
                    }
                    break;
                case Player.STATE_BUFFERING:
                    if (!isPreparing) {
                        isBuffering = true;
                        notifyStatusChanged(PlayState.BUFFERING);
                        notifyBufferingStart();
                    }
                    break;
                case Player.STATE_ENDED:
                    if (isBuffering) {
                        isBuffering = false;
                        notifyBufferingEnd();
                        notifyStatusChanged(PlayState.PLAYING);
                    }
                    if (!isPreparing) {
                        notifyStatusChanged(PlayState.COMPLETE);
                    }
                    break;
            }
        }

        @Override
        public void onPlayerError(ExoPlaybackException error) {
            ExoPlayer.this.isPreparing = false;
            if (error == null) {
                notifyError(PlayerException.createException(PlayerException.TYPE_EXO_UNEXPECTED_ERROR));
                return;
            }
            int type = error.type;
            switch (type) {
                case ExoPlaybackException.TYPE_SOURCE:
                    notifyError(PlayerException.createException(PlayerException.TYPE_EXO_SOURCE_ERROR, error.getSourceException()));
                    break;
                case ExoPlaybackException.TYPE_RENDERER:
                    notifyError(PlayerException.createException(PlayerException.TYPE_EXO_RENDERER_ERROR, error.getRendererException()));
                    break;
                case ExoPlaybackException.TYPE_UNEXPECTED:
                    notifyError(PlayerException.createException(PlayerException.TYPE_EXO_UNEXPECTED_ERROR, error.getUnexpectedException()));
                    break;
            }
        }

        @Override
        public void onVideoSizeChanged(int width, int height,
                                       int unappliedRotationDegrees, float pixelWidthHeightRatio) {
            notifyVideoSizeChanged(width, height, unappliedRotationDegrees, pixelWidthHeightRatio);
        }
    }

}
