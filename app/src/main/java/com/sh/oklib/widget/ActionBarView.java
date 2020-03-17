package com.sh.oklib.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sh.oklib.R;

public class ActionBarView extends RelativeLayout {

    private View left, center, right;
    private OnActionBarViewClickListener mOnActionBarViewClickListener;

    public ActionBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        left = findViewById(R.id.left);
        center = findViewById(R.id.center);
        right = findViewById(R.id.right);
        left.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnActionBarViewClickListener != null) {
                    mOnActionBarViewClickListener.onLeftClick(v);
                }
            }
        });
        center.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnActionBarViewClickListener != null) {
                    mOnActionBarViewClickListener.onCenterClick(v);
                }
            }
        });
        right.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnActionBarViewClickListener != null) {
                    mOnActionBarViewClickListener.onRightClick(v);
                }
            }
        });
    }

    public void setBtnVisibility(int position, int visibility) {
        switch (position) {
            case 0:
                left.setVisibility(visibility);
                break;
            case 1:
                center.setVisibility(visibility);
                break;
            case 2:
                right.setVisibility(visibility);
                break;
        }
    }

    public void setView(Object arg0, Object arg1, Object arg2) {
        if (arg0 == null) {

        } else if (arg0 instanceof String) {
            TextView left_tv = (TextView) findViewById(R.id.left_tv);
            left_tv.setText((String) arg0);
        } else if (arg0 instanceof Integer) {
            ImageView left_iv = (ImageView) findViewById(R.id.left_iv);
            left_iv.setImageResource((int) arg0);
        }
        if (arg1 == null) {

        } else if (arg1 instanceof String) {
            TextView center_tv = (TextView) findViewById(R.id.center_tv);
            center_tv.setText((String) arg1);
        } else if (arg0 instanceof Integer) {
            ImageView center_iv = (ImageView) findViewById(R.id.center_iv);
            center_iv.setImageResource((int) arg1);
        }
        if (arg2 == null) {

        } else if (arg2 instanceof String) {
            TextView right_tv = (TextView) findViewById(R.id.right_tv);
            right_tv.setText((String) arg2);
        } else if (arg0 instanceof Integer) {
            ImageView right_iv = (ImageView) findViewById(R.id.right_iv);
            right_iv.setImageResource((int) arg2);
        }
    }

    public void setSplitBackgroundColor(int color) {
        View actionBarSplit = findViewById(R.id.action_bar_split);
        actionBarSplit.setBackgroundColor(color);
    }

    public void setOnActionBarViewClickListener(OnActionBarViewClickListener listener) {
        mOnActionBarViewClickListener = listener;
    }

    public interface OnActionBarViewClickListener {
        void onLeftClick(View view);

        void onCenterClick(View view);

        void onRightClick(View view);
    }
}
