package com.sh.oklib.http;

import java.io.File;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class HttpThread {

    public static final MediaType BODY_JSON = MediaType.parse("application/json; charset=utf-8");
    public static final MediaType BODY_TXT = MediaType.parse("txt");

    public void post(final String url, final String json, final HttpCallback callback) {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();
//        OkHttpClient client = new OkHttpClient();
        RequestBody body = FormBody.create(BODY_JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Call call = client.newCall(request);
        call.enqueue(callback);
    }

    public void get(String url, HttpCallback callback) {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();
        Request request = new Request.Builder()
                .get()
                .url(url)
                .build();
        Call call = client.newCall(request);
        call.enqueue(callback);
    }

    public void upload(String url, File[] files, HttpCallback callback) {
        OkHttpClient client = new OkHttpClient();
        MultipartBody.Builder requestBody = new MultipartBody.Builder();
        requestBody.setType(MultipartBody.FORM);
        for (File file : files) {
            if (file.exists()) {
                requestBody.addFormDataPart(UUID.randomUUID() + "", file.getName(),
                        RequestBody.create(MediaType.parse("multipart/form-data"), file));
            }
        }

        Request request = new Request.Builder()
                .header("Authorization", "Client-ID " + UUID.randomUUID())
                .url(url)
                .post(requestBody.build())
                .build();

        client.newCall(request).enqueue(callback);
    }

    public void downloadFile(final String url, HttpCallback callback) {
        Request request = new Request.Builder()
                .url(url)
                .build();

        //异步请求
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.newCall(request).enqueue(callback);
    }

}
