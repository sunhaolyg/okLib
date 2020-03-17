package com.sh.oklib.base;

import android.app.Application;
import android.content.Context;



public class OkLibApp extends Application {

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }

    public static Context getContext(){
        return mContext;
    }
}
