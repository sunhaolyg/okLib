package com.sh.oklib.util;

import android.content.Context;
import android.widget.Toast;

import com.sh.oklib.base.OkLibApp;

public class ToastUtils {

    public static void toast(Context context, String content) {
        Toast.makeText(context, content, Toast.LENGTH_SHORT).show();
    }

    public static void toast(String content) {
        Toast.makeText(OkLibApp.getContext(), content, Toast.LENGTH_SHORT).show();
    }


}
