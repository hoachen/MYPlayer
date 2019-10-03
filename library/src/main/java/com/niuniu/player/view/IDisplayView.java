package com.niuniu.player.view;

import android.view.Surface;
import android.view.SurfaceHolder;

import com.niuniu.player.internal.VideoComponentContract;

public interface IDisplayView extends VideoComponentContract.View {

    //use TextureView for display
    int RENDER_TYPE_TEXTURE_VIEW = 0;

    //use SurfaceView for display
    int RENDER_TYPE_SURFACE_VIEW = 1;

    void setDisplay(int type);

    void reset();

    boolean canZoom();

    void setScale(float zoom);

    void setAspectRatio(float widthHeightRatio);

    interface Listener {

        void onSurfaceUpdated(Surface surface, boolean ownSurface);

        void onSurfaceHolderUpdated(SurfaceHolder surface, boolean ownSurface);

        void updateSurfaceSizeChanged(int width, int height);
    }
}
