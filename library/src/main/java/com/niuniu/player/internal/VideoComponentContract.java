package com.niuniu.player.internal;

public interface VideoComponentContract {

    interface View {

        void attach(Presenter presenter);

        void detach();

        void handlePlayEvent(int event, Object data);
    }

    interface Presenter {

    }
}
