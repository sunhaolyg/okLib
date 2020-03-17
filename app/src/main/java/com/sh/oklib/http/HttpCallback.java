package com.sh.oklib.http;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.alibaba.fastjson.JSON;
import com.sh.oklib.R;
import com.sh.oklib.util.LogUtils;
import com.sh.oklib.util.ToastUtils;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class HttpCallback<Receive> implements Callback {

    private static final String TAG = "HttpCallback";
    private static final int SUCCESS = 1;
    private static final int FAILURE = 0;
    private static final int STRING_FAILURE = 2;
    private static final int STRING_SUCCESS = 3;
    private Class<Receive> receive;
    private String mFailureMsg;
    protected ProgressDialog progressDialog;

    public HttpCallback() {

    }

    public HttpCallback(Class<Receive> t) {
        this.receive = t;
    }

    public void setProgressDialog(ProgressDialog dialog) {
        progressDialog = dialog;
    }

    protected Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SUCCESS:
                    callback(true, (Receive) msg.obj);
                    break;
                case FAILURE:
                    LogUtils.d(TAG, "FAILURE = " + msg.obj);
                    callback(false, null);
                    break;
                case STRING_SUCCESS:
                    parseResult(true, (String) msg.obj);
                    break;
                case STRING_FAILURE:
                    parseResult(false, null);
                    break;
                default:
                    break;
            }
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
        }
    };

    @Override
    public void onFailure(Call call, IOException e) {
        mFailureMsg = e.getMessage();
        if (handResult()) {
            mHandler.obtainMessage(STRING_FAILURE, mFailureMsg).sendToTarget();
            return;
        }
        mHandler.obtainMessage(FAILURE, mFailureMsg).sendToTarget();
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        if (response.code() != 200) {
            setFailureMsg(response.code() + "");
            mHandler.obtainMessage(FAILURE, mFailureMsg).sendToTarget();
            return;
        }
        if (response.body() == null) {

            return;
        }
        String responseString = response.body().string();
        LogUtils.d(TAG, "responseString = " + responseString);
        if (handResult()) {
            mHandler.obtainMessage(STRING_SUCCESS, responseString).sendToTarget();
            return;
        }
        Receive r = null;
        try {
            r = JSON.parseObject(responseString, this.receive);
        } catch (Exception e) {
            mFailureMsg = e.getMessage();
            mHandler.obtainMessage(FAILURE, mFailureMsg).sendToTarget();
            return;
        }
        mHandler.obtainMessage(SUCCESS, r).sendToTarget();
    }

    public static <T> T parseObject(String text, Class<T> clazz) {
        try {
            return JSON.parseObject(text, clazz);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    protected boolean handResult() {
        return false;
    }

    /**
     * 不需要json解析
     *
     * @param success
     * @param result
     */
    protected void parseResult(boolean success, String result) {
    }

    protected <Receive extends BaseResponse> boolean isErrorData(Context context, boolean success, Receive r) {
        if (!success) {
            ToastUtils.toast(context, getFailureMsg());
            return true;
        }
        if (r == null || r.getList() == null || r.getList().size() == 0) {
            ToastUtils.toast(context, "无数据");
            return true;
        }
        return false;
    }

    public void callback(boolean success, Receive t) {
    }

    public String getFailureMsg() {
        return mFailureMsg;
    }

    public void setFailureMsg(String failureMsg) {
        this.mFailureMsg = failureMsg;
    }

    public void onNetworkDisconnected(Context context) {
        setFailureMsg(context.getString(R.string.network_disconnect));
        callback(false, null);
    }
}