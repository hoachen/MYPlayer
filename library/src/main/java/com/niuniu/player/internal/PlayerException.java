package com.niuniu.player.internal;

import android.text.TextUtils;

import com.niuniu.player.utils.Logger;

import androidx.annotation.NonNull;


public class PlayerException extends Exception {
    private static final String TAG = "PlayerException";
    public static final String REASON_SOURCE_INVALID = "Response code: 410";
    public static final String REASON_SOURCE_HTTP_CODE = "Response code";

    /**
     * for SIPlayer
     */
    public static final int TYPE_SIPLAYER_DEFAULT = 10;  // siplayer unknown error
    public static final int TYPE_SIPLAYER_CREATE_ERROR = 20; // siplayer create is null, maybe can retry once
    public static final int TYPE_SIPLAYER_SOURCE_NULL = 30;  // siplayer get source is null, don't retry
    public static final int TYPE_SIPLAYER_SOURCE_DIRECT_RETRY = 40; //siplayer get direct exception, auto retry,not show error
    public static final int TYPE_SIPLAYER_NO_NETWORK = 50; // no network

    /**
     * for ExoPlayer
     */
    public static final int TYPE_EXO_DRM_INIT_ERROR = 110; // drm 初始化失败
    public static final int TYPE_EXO_DRM_ERROR = 120; // drm请求失败或解码失败, don't retry, 显示版权原因
    public static final int TYPE_EXO_SOURCE_ERROR = 130; // exoplayer请求源失败，don't retry，显示资源原因？，也可能是链接失效等等
    public static final int TYPE_EXO_RENDERER_ERROR = 140; // exoplayer 请求渲染失败，can retry，可能是解码器初始化失败
    public static final int TYPE_EXO_UNEXPECTED_ERROR = 150; //exoplayer 播放中的意外失败，can retry，可能是网络错误，也可能是链接失效等等,其他的默认错误

    /**
     * for ProtoPlayer (MediaPlayer)
     */
    public static final int TYPE_PROTO_UNKNOWN_ERROR = 210; //proto player Unspecified player error. can retry
    public static final int TYPE_PROTO_SOURCE_IO = 220; //proto player File or network related operation errors. can retry
    public static final int TYPE_PROTO_SOURCE_UNSUPPORT = 230; // proto player Bitstream or framework cannot support. don't retry
    public static final int TYPE_PROTO_SERVER_DIED = 240; // proto player must release and new one. can retry
    public static final int TYPE_PROTO_NOT_VALID_PLAYBACK = 250; // proto player cannot do seek. handel disable seek
    public static final int TYPE_PROTO_TIME_OUT = 260; // Some operation takes too long to complete, usually more than 3-5 seconds. can retry

    /**
     * for Youtube WebPlayer and SDKPlayer
     */
    public static final int TYPE_YTB_UNKNOWN = 310;  //ytb player error, can retry
    public static final int TYPE_YTB_H5_PLAYER = 320; // ytb web player load or cue error, can retry
    public static final int TYPE_YTB_NOT_PLAYABLE_101 = 330; // ytb web player cannot play, because video don't allow play in web. don't retry
    public static final int TYPE_YTB_NOT_PLAYABLE_150 = 340; // ytb web player cannot play, because video don't allow play by uploader . don't retry
    public static final int TYPE_YTB_NOT_FOUND = 350;        //ytb web player cannot play, because video has deleted . don't retry
    public static final int TYPE_YTB_NETWORK_TIMEOUT = 360; // ytb network time out, can retry
    public static final int TYPE_YTB_INVALID_PARAMENTER = 370;  // ytb web player request video by invaild params. don't retry
    public static final int TYPE_YTB_SOURCE_NULL = 380; //ytb player had source is null, don't retry
    public static final int TYPE_YTB_SDK_INIT_FAIL = 390;// ytb sdk init fail ,can retry;
    public static final int TYPE_YTB_SDK_UNAUTHORIZED_OVERLAY = 400;

    /**
     * for VlcPlayer
     */
    public static final int TYPE_VLCPLAYER_ERROR = 410; // siplayer create is null, maybe can retry once

    /**
     * for IJKPlayer
     */
    public static final int TYPE_IJKPLAYER_ERROR = 610; //

    public static final int TYPE_IJKPLAYER_OPEN_401 = 611; //

    public static final int TYPE_IJKPLAYER_OPEN_403 = 612; //

    public static final int TYPE_IJKPLAYER_OPEN_TIMEOUT = 613; //

    public static final int TYPE_IJKPLAYER_OPEN_INVALID = 614; //

    public static final int TYPE_IJKPLAYER_READ_401 = 615; //

    public static final int TYPE_IJKPLAYER_READ_403 = 616; //

    public static final int TYPE_IJKPLAYER_READ_EIO = 617; //

    public static final int TYPE_IJKPLAYER_READ_TIMEOUT = 618; //

    public static final int TYPE_IJKPLAYER_READ_UNKNOWN = 619; //

    public static final int TYPE_IJKPLAYER_OPEN_UNKNOWN = 620; //

    public static final int TYPE_IJKPLAYER_FFMPEG_OPTIONS = 621;

    public static final int TYPE_IJKPLAYER_FFMPEG_ENOMEM = 622;

    public static final int TYPE_IJKPLAYER_FFMPEG_NO_STREAMS = 623;

    public static final int TYPE_IJKPLAYER_FFMPEG_NO_CODECS = 624;

    /**
     * ------------------------------->for external exception player<---------------------------------
     */
    public static final int TYPE_EXTERNAL_DEFAULT = 510;  //默认错误
    public static final int TYPE_NO_SOURCE = 520;  //没有资源 no retry
    public static final int TYPE_NO_URL = 530;  //没有播放链接 no retry
    public static final int TYPE_DELETED = 540; // 下架 no retry
    public static final int TYPE_REQUEST_SOURCE_ERROR = 550; // 请求item失败, 如播放剧集
    public static final int TYPE_UNAVAILABLE = 560; //失效链接 Unavailable

    private int type;
    private String errorMessage;
    private boolean isUseProxy;
    private int urlTransErr;
    private int proxyPort;

    private PlayerException(int type, Exception exception) {
        super(exception.getMessage());
        setType(type);
        initCause(exception);
        this.errorMessage = TextUtils.isEmpty(exception.getMessage()) ? parseErrorMsg(type) : exception.getMessage();
    }

    private PlayerException(int type, String message) {
        super(message);
        setType(type);
        this.errorMessage = message;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isUseProxy() {
        return isUseProxy;
    }

    public PlayerException setUrlTransErr(int urlTransErr) {
        this.urlTransErr = urlTransErr;
        return this;
    }

    public PlayerException setProxyPort(int proxyPort) {
        this.proxyPort = proxyPort;
        return this;
    }

    public PlayerException setUseProxy(boolean useProxy) {
        isUseProxy = useProxy;
        return this;
    }

    public int getUrlTransErr() {
        return urlTransErr;
    }

    public int getProxyPort() {
        return proxyPort;
    }

    @Override
    public String getMessage() {
        return this.errorMessage;
    }

    public static PlayerException createException(int type) {
        return new PlayerException(type, parseErrorMsg(type));
    }

    public static PlayerException createException(int type, @NonNull Exception exception) {
        return new PlayerException(type, exception);
    }

    public static PlayerException createException(int type, @NonNull String message) {
        return new PlayerException(type, message);
    }

    public static PlayerException createException(int type, int error) {
        return new PlayerException(type, parseErrorMsg(type, error));
    }

    private static String parseErrorMsg(int type, int error) {

        return parseErrorMsg(type) + ": " + error;
    }

    private static String parseErrorMsg(int type) {
        String errMsg;
        switch (type) {
            case TYPE_NO_SOURCE:
                errMsg = "Video view source is null";
                break;
            case TYPE_NO_URL:
                errMsg = "Video view source url is null ";
                break;
            case TYPE_PROTO_SOURCE_IO:
                errMsg = "proto player source io error";
                break;
            case TYPE_UNAVAILABLE:
                errMsg = "this video is unavailable";
                break;
            case TYPE_YTB_NETWORK_TIMEOUT:
                errMsg = "youtube web network timeout";
                break;
            case TYPE_EXO_DRM_INIT_ERROR:
                errMsg = "Device does not support DRM initialization failure for altiblaji";
                break;
            case TYPE_SIPLAYER_SOURCE_NULL:
                errMsg = "SIPlayer no source or url";
                break;
            case TYPE_SIPLAYER_CREATE_ERROR:
                errMsg = "SIPlayer create error";
                break;
            case TYPE_DELETED:
                errMsg = "Video has been deleted";
                break;
            case TYPE_EXO_UNEXPECTED_ERROR:
                errMsg = "SIPlayer unknown error occurred";
                break;
            case TYPE_YTB_H5_PLAYER:
                errMsg = "Youtube web player create failed";
                break;
            case TYPE_SIPLAYER_NO_NETWORK:
                errMsg = "SIPlayer no network";
                break;
            case TYPE_SIPLAYER_DEFAULT:
                errMsg = "SIPlayer unknown error";
                break;
            case TYPE_YTB_SDK_INIT_FAIL:
                errMsg = "Ytbsdk player init fail";
                break;
            case PlayerException.TYPE_IJKPLAYER_FFMPEG_ENOMEM:
                errMsg = "few memory to use";
                break;
            case PlayerException.TYPE_IJKPLAYER_FFMPEG_NO_CODECS:
                errMsg = "codec not supported";
                break;
            case PlayerException.TYPE_IJKPLAYER_FFMPEG_NO_STREAMS:
                errMsg = "file has no AV streams";
                break;
            case PlayerException.TYPE_IJKPLAYER_FFMPEG_OPTIONS:
                errMsg = "options are illegal";
                break;
            case PlayerException.TYPE_IJKPLAYER_OPEN_401:
                errMsg = "open_input occurs 401";
                break;
            case PlayerException.TYPE_IJKPLAYER_OPEN_403:
                errMsg = "open_input occurs 403";
                break;
            case PlayerException.TYPE_IJKPLAYER_OPEN_INVALID:
                errMsg = "open_input occurs invalid data";
                break;
            case PlayerException.TYPE_IJKPLAYER_OPEN_TIMEOUT:
                errMsg = "open_input occurs timeout";
                break;
            case PlayerException.TYPE_IJKPLAYER_OPEN_UNKNOWN:
                errMsg = "open_input occurs unknown error";
                break;
            case PlayerException.TYPE_IJKPLAYER_READ_401:
                errMsg = "read_packet occurs 401";
                break;
            case PlayerException.TYPE_IJKPLAYER_READ_403:
                errMsg = "read_packet occurs 403";
                break;
            case PlayerException.TYPE_IJKPLAYER_READ_EIO:
                errMsg = "read_packet occur IO error(pre-send EOF)";
                break;
            case PlayerException.TYPE_IJKPLAYER_READ_TIMEOUT:
                errMsg = "read_packet occurs timeout";
                break;
            case PlayerException.TYPE_IJKPLAYER_READ_UNKNOWN:
                errMsg = "read_packet occur unknown error";
                break;
            default:
                errMsg = "UNKONWN";
                break;
        }
        Logger.e(TAG, "error message: " + errMsg);
        return errMsg;
    }

    public String getStackTraceMessage() {
        StringBuilder sb = new StringBuilder();
        Throwable cause = getCause();
        if (cause == null) {
            return "";
        }
        sb.append(cause).append("\n");
        Throwable cause2 = cause;
        if (cause.getCause() != null && (cause2 = cause) != cause.getCause()) {
            cause2 = cause.getCause();
            sb.append(cause2).append("\n");
        }
        StackTraceElement[] stackTrace = cause2.getStackTrace();
        if (stackTrace != null) {
            for (int length = stackTrace.length, i = 0; i < length; ++i) {
                final StackTraceElement stackTraceElement = stackTrace[i];
                if (i > 5) {
                    break;
                }
                sb.append(stackTraceElement).append("\n");
            }
        }
        return sb.toString();
    }
}
