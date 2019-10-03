package com.niuniu.player;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.widget.FrameLayout;

import com.niuniu.player.internal.BasePlayer;
import com.niuniu.player.internal.IVideoView;
import com.niuniu.player.internal.Source;


public class MYVideoView extends FrameLayout implements IVideoView {

    private BasePlayer mPlayer;

    public MYVideoView(Context context) {
        this(context, null);
    }

    public MYVideoView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MYVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    @Override
    public void source(Source source) {
    }

    @Override
    public void prepare() {
    }

    @Override
    public String getPlayerType() {
        return mPlayer.getPlayerType();
    }

    @Override
    public void start(long msc) {
        mPlayer.start(msc);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {

    }

    @Override
    public void seekTo(long msc) {

    }

    @Override
    public void stop() {

    }

    @Override
    public void reset() {

    }

    @Override
    public void restart() {

    }

    @Override
    public void release() {

    }

    @Override
    public void surface(Surface surface) {

    }

    @Override
    public void surfaceHolder(SurfaceHolder surfaceHolder) {

    }

    @Override
    public void surfaceSizeChanged(int width, int height) {

    }

    @Override
    public void volume(float volume) {

    }

    @Override
    public void mute(boolean mute) {

    }

    @Override
    public void selectTrack(String trackName) {

    }
}
