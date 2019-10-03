package com.niuniu.player;


import com.niuniu.player.internal.Source;

import androidx.annotation.WorkerThread;

public class VideoSource implements Source {

    private String source;

    public VideoSource(String source) {
        this.source = source;
    }

    @Override
    public String value() {
        return this.source;
    }

    @Override
    public <T> boolean is(Class<T> clazz) {
        return clazz.isInstance(this);
    }

    @Override
    public <T> T as(Class<T> clazz) {
        if (is(clazz)) {
            return (T) this;
        }
        return null;
    }

    @Override
    public String toString() {
        return "source: " + this.source;
    }

    public void update(String source) {
        this.source = source;
    }

    @WorkerThread
    public void selfAdapt() {
    }
}
