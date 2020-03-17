package com.sh.oklib.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.sh.oklib.R;
import com.sh.oklib.util.KeyboardUtils;
import com.sh.oklib.util.LogUtils;
import com.sh.oklib.widget.ActionBarView;


public class BaseActivity extends AppCompatActivity implements ActionBarView.OnActionBarViewClickListener {

    private static final String TAG = "BaseActivity";
    protected Context mContext;
    protected ActionBarView mActionBarView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setStatusBarColor();
        super.onCreate(savedInstanceState);
        mContext = this;
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        int color = getActionBarbg();
        if (showActionBar()) {
            mActionBarView = findViewById(R.id.action_bar);
            if (color != 0) {
                mActionBarView.setBackgroundColor(color);
            }
        }
        initView();
        init();
    }


    protected boolean showActionBar() {
        return true;
    }

    protected void setActionView(Object arg0, Object arg1, Object arg2) {
        if (mActionBarView != null) {
            if (arg0 == null) {
                arg0 = R.mipmap.back;
            }
            mActionBarView.setView(arg0, arg1, arg2);
            mActionBarView.setOnActionBarViewClickListener(this);
        }
    }

    public void setActionViewSplitBg(int color){
        if (mActionBarView != null) {
            mActionBarView.setSplitBackgroundColor(getResources().getColor(color));
        }
    }

    protected void setFullScreen(boolean fullScreen) {
        if (fullScreen) {
            setActionbarVisibility(View.GONE);
            int uiFlags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_FULLSCREEN;
            uiFlags |= 0x00001000;
            getWindow().getDecorView().setSystemUiVisibility(uiFlags);
        } else {
            setActionbarVisibility(View.VISIBLE);
            int uiFlags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            uiFlags |= 0x00001000;
            getWindow().getDecorView().setSystemUiVisibility(uiFlags);
        }
    }

    protected void setBtnVisibility(int position, int visibility) {
        if (mActionBarView != null) {
            mActionBarView.setBtnVisibility(position, visibility);
        }
    }

    protected void setActionbarVisibility(int visibility) {
        if (mActionBarView != null) {
            mActionBarView.setVisibility(visibility);
        }
    }

    protected int getActionbarVisibility() {
        if (mActionBarView != null) {
            return mActionBarView.getVisibility();
        }
        return View.VISIBLE;
    }

    protected void initView() {
    }

    protected void init() {
    }

    public int getActionBarbg() {
        return 0;
    }

    protected void setStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            //取消设置Window半透明的Flag
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //添加Flag把状态栏设为可绘制模式
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            //设置状态栏为透明
            window.setStatusBarColor(getStatusBarColor());
        }
    }

    protected int getStatusBarColor() {
        return getResources().getColor(R.color.color_action_bg);
    }

    protected void log(String TAG, String content, Throwable tr) {
        LogUtils.d(TAG, content, tr);
    }

    protected void log(String TAG, String content) {
        LogUtils.d(TAG, content);
    }

    protected void log(String content) {
        log(TAG, content);
    }

    protected void toast(String content) {
        Toast.makeText(mContext, content, Toast.LENGTH_SHORT).show();
    }

    protected boolean checkpermission(String permission) {
        //检查是否已经给了权限
        int checkpermission = ContextCompat.checkSelfPermission(mContext,
                permission);
        if (checkpermission != PackageManager.PERMISSION_GRANTED) {//没有给权限
            //参数分别是当前活动，权限字符串数组，requestcode
            ActivityCompat.requestPermissions((Activity) mContext,
                    new String[]{/*Manifest.permission.INTERNET*/permission}, 1);
        } else {
            return true;
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //grantResults数组与权限字符串数组对应，里面存放权限申请结果
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            permissionsResult(true);
        } else {
            permissionsResult(false);
        }
    }

    protected void permissionsResult(boolean result) {
    }

    protected void startActivity(Class z) {
        startActivity(new Intent(mContext, z));
    }

    protected void startActivityAndFinish(Class z) {
        startActivity(z);
        finish();
    }

    protected void startActivityForResult(Class z, int requestCode) {
        startActivityForResult(new Intent(mContext, z), requestCode);
    }

    @Override
    public void onLeftClick(View view) {
        finish();
    }

    @Override
    public void onCenterClick(View view) {
    }

    @Override
    public void onRightClick(View view) {
    }

    //此方法只是关闭软键盘 可以在finish之前调用一下
    protected void hideSoft() {
        KeyboardUtils.hideSoft(this);
    }

}
