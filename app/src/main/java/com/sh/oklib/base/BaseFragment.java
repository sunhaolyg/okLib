package com.sh.oklib.base;


import android.app.Fragment;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.sh.oklib.util.LogUtils;


public class BaseFragment extends Fragment {

    private static final String TAG = "BaseFragment";
    protected View mView;
    protected Context mContext;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (getLayoutId() != 0) {
            mView = inflater.inflate(getLayoutId(), container,false);
        } else {
            mView = generateView(inflater);
        }
        if (mView != null) {
            initView();
            init();
            return mView;
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    protected void init() {
    }

    protected View generateView(LayoutInflater inflater) {
        return null;
    }

    protected int getLayoutId() {
        return 0;
    }

    protected void initView() {
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    protected void toast(String content) {
        Toast.makeText(mContext, content, Toast.LENGTH_SHORT).show();
    }

    protected void log(String content) {
        LogUtils.d(TAG, content);
    }

}
