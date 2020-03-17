package com.sh.oklib.util;

import android.util.Log;

public class LogUtils {

    private static final String TAG = "LogUtils";
    private static final boolean DEBUG = true;

    public static void d(String TAG, String content) {
        if (DEBUG) {
            Log.d(TAG, content);
        }
    }

    public static void d(String content) {
        d(TAG, content);
    }

    public static void d(String TAG, String content, Throwable tr) {
        if (DEBUG) {
            Log.d(TAG, content, tr);
        }
    }

}
