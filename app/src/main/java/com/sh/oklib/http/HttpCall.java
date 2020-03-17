package com.sh.oklib.http;


import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.sh.oklib.base.OkLibApp;
import com.sh.oklib.util.DialogUtils;
import com.sh.oklib.util.LogUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.Response;


public class HttpCall {

    public static final MediaType BODY_JSON
            = MediaType.parse("application/json; charset=utf-8");
    private static ProgressDialog dialog;

    public static void upload(Context context, String url, File[] files, HttpCallback callback) {
        if (!isNetworkConnected()) {
            callback.onNetworkDisconnected(OkLibApp.getContext());
            return;
        }
        if (context != null) {
            dialog = DialogUtils.showProgressDialog(context);
            callback.setProgressDialog(dialog);
        }
        new HttpThread().upload(url, files, callback);
    }

    public static void uploadPlatform(String url,File[] files, HttpCallback callback) {
        if (!isNetworkConnected()) {
            callback.onNetworkDisconnected(OkLibApp.getContext());
            return;
        }
        new HttpThread().upload(url, files, callback);
    }

    public static void post(Context context, String url, final String json, final HttpCallback callback) {
        if (!isNetworkConnected()) {
            callback.onNetworkDisconnected(OkLibApp.getContext());
            return;
        }
        if (context != null) {
            dialog = DialogUtils.showProgressDialog(context);
            callback.setProgressDialog(dialog);
        }
        new HttpThread().post(url, json, callback);
    }

    public static void get(Context context, String url, final HttpCallback callback) {
        if (!isNetworkConnected()) {
            callback.onNetworkDisconnected(OkLibApp.getContext());
            return;
        }
        if (context != null) {
            dialog = DialogUtils.showProgressDialog(context);
            callback.setProgressDialog(dialog);
        }
        new HttpThread().get(url, callback);
    }

    public static void onDestroy() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    public static void downloadFile(Context context, String url, final String destFileDir,
                                    final String destFileName, final OnDownloadListener listener) {
        dialog = DialogUtils.showProgressDialog(context);

        new HttpThread().downloadFile(url, new HttpCallback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // 下载失败监听回调
                listener.onDownloadFailed(e);
                if (dialog != null) {
                    dialog.dismiss();
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream is = null;
                byte[] buf = new byte[2048];
                int len = 0;
                FileOutputStream fos = null;

                //储存下载文件的目录
                File dir = new File(destFileDir);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                File file = new File(dir, destFileName);
                try {
                    String contentDisposition = response.header("Content-Disposition");
                    /*String tag = "filename=";
                    int index = contentDisposition.indexOf(tag);
                    String fileName = contentDisposition.substring(index + tag.length());
                    if (!TextUtils.isEmpty(fileName)) {
                        file = new File(dir, fileName);
                    }*/
                    is = response.body().byteStream();
                    long total = response.body().contentLength();
                    LogUtils.d("total = " + total);
                    fos = new FileOutputStream(file);
                    long sum = 0;
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                        sum += len;
                        int progress = (int) (sum * 1.0f / total * 100);
                        //下载中更新进度条
                        listener.onDownloading(progress);
                    }
                    fos.flush();
                    //下载完成
                    listener.onDownloadSuccess(file);
                } catch (Exception e) {
                    listener.onDownloadFailed(e);
                } finally {
                    try {
                        if (is != null) {
                            is.close();
                        }
                        if (fos != null) {
                            fos.close();
                        }
                    } catch (IOException e) {

                    }
                    if (dialog != null) {
                        dialog.dismiss();
                    }
                }
            }
        });
    }

    public static boolean isNetworkConnected() {
        ConnectivityManager mConnectivityManager = (ConnectivityManager) OkLibApp.getContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
        if (mNetworkInfo != null) {
            return mNetworkInfo.isAvailable();
        }
        return false;
    }


    public static boolean hasWifiConnection(Context context) {
        final ConnectivityManager connectivityManager = (ConnectivityManager) context.
                getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo networkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        return (networkInfo != null && networkInfo.isConnectedOrConnecting());
    }

    protected String getURL(Context context, String url) {
        return url;
    }

}
