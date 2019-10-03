package com.niuniu.player.utils;


import android.util.Log;

import com.niuniu.player.BuildConfig;

public final class Logger {

    private static boolean sPrintLog = BuildConfig.DEBUG;

    public static void i(String tag, String message) {
        i(tag, message, null);
    }

    public static void i(String tag, String message, Throwable t) {
        if (sPrintLog) {
            Log.i(tag, message, t);
        }
    }

    public static void v(String tag, String message) {
        v(tag, message, null);
    }

    public static void v(String tag, String message, Throwable t) {
        if (sPrintLog) {
            Log.v(tag, message, t);
        }
    }


    public static void d(String tag, String message) {
        v(tag, message, null);
    }

    public static void d(String tag, String message, Throwable t) {
        if (sPrintLog) {
            Log.d(tag, message, t);
        }
    }


    public static void e(String tag, String message) {
        e(tag, message, null);
    }

    public static void e(String tag, String message, Throwable t) {
        if (sPrintLog) {
            Log.e(tag, message, t);
        }
    }


    public static void w(String tag, String message) {
        w(tag, message, null);
    }

    public static void w(String tag, String message, Throwable t) {
        if (sPrintLog) {
            Log.w(tag, message, t);
        }
    }


}
