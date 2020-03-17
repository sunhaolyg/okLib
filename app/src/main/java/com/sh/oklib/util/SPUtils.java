package com.sh.oklib.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.sh.oklib.R;
import com.sh.oklib.base.OkLibApp;


public class SPUtils {

    public static void put(String key, String value) {
        SharedPreferences.Editor editor = getSP().edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static void put(String key, int value) {
        SharedPreferences.Editor editor = getSP().edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public static void put(String key, boolean value) {
        SharedPreferences.Editor editor = getSP().edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public static String getString(String key) {
        return getSP().getString(key, "");
    }

    public static boolean getBoolean(String key) {
        return getSP().getBoolean(key, false);
    }

    public static int getInt(String key) {
        return getSP().getInt(key, 0);
    }

    private static SharedPreferences getSP() {
        Context context = OkLibApp.getContext();
        String name = context.getResources().getString(R.string.sp_name);
        return context.getSharedPreferences(name, Context.MODE_PRIVATE);
    }

}
