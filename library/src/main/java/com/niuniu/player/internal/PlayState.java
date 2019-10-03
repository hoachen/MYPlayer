package com.niuniu.player.internal;

public enum PlayState {

    RELEASE(-20, "release"),
    ERROR(-10, "error"),
    IDLE(0, "idle"),
    INITED(1, "init"),
    BUFFERING(2, "buffering"),
    PREPARE(3, "prepare"),
    PREPARED(4, "prepared"),
    PLAYING(5, "playing"),
    PAUSED(6, "paused"),
    STOPPED(7, "stopped"),
    COMPLETE(7, "complete");

    private int state;
    private String name;

    PlayState(int state, String name) {
        this.state = state;
        this.name = name;
    }

    public int getValue() {
        return state;
    }

    public String getName() {
        return this.name;
    }
}
