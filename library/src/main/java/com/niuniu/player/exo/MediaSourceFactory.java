package com.niuniu.player.exo;

import android.content.Context;
import android.net.Uri;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.source.dash.DashMediaSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.source.smoothstreaming.SsMediaSource;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.util.Util;


import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import androidx.annotation.Nullable;
import okhttp3.JavaNetCookieJar;
import okhttp3.OkHttpClient;

public class MediaSourceFactory {

    private Context mContext;

    private OkHttpClient mOkHttpClient;

    public MediaSourceFactory(Context context) {
        mContext = context;
    }

    public MediaSource buildMediaSource(Uri uri) {
        return buildMediaSource(uri, null);
    }

    public MediaSource buildMediaSource(Uri uri, @Nullable String overrideExtension) {
        @C.ContentType int type = Util.inferContentType(uri, overrideExtension);

        DataSource.Factory dataSourceFactory = buildDataSourceFactory();
        switch (type) {
            case C.TYPE_DASH:
                return new DashMediaSource.Factory(dataSourceFactory)
                        .createMediaSource(uri);
            case C.TYPE_SS:
                return new SsMediaSource.Factory(dataSourceFactory)
                        .createMediaSource(uri);
            case C.TYPE_HLS:
                return new HlsMediaSource.Factory(dataSourceFactory)
                        .createMediaSource(uri);
            case C.TYPE_OTHER:
                return new ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(uri);
            default: {
                throw new IllegalStateException("Unsupported type: " + type);
            }
        }
    }

    /**
     * Returns a {@link DataSource.Factory}.
     */
    public DataSource.Factory buildDataSourceFactory() {
        OkHttpDataSourceFactory upstreamFactory = new OkHttpDataSourceFactory(getOkHttpClient(), Util.getUserAgent(mContext, "MYPlayer"));
        return upstreamFactory;
    }


    private OkHttpClient getOkHttpClient() {
        if (mOkHttpClient == null) {
            CookieManager cookieManager = new CookieManager();
            cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);

            mOkHttpClient = new OkHttpClient.Builder().hostnameVerifier(new HostnameVerifier() {

                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            }).connectTimeout(15, TimeUnit.SECONDS)
                    .readTimeout(15, TimeUnit.SECONDS)
                    .writeTimeout(15, TimeUnit.SECONDS)
                    .cookieJar(new JavaNetCookieJar(cookieManager)).build();

        }
        return mOkHttpClient;
    }

}
