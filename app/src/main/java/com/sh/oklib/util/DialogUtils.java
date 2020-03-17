package com.sh.oklib.util;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.EditText;

public class DialogUtils {

    public static Dialog showEditDialog(final Context context, String title, String content,
                                        String hint, String btn, final OnEditListener listener) {
        final EditText editText = new EditText(context);
        editText.setText(content);
        editText.setHint(hint);
        editText.setSelection(content.length());
        AlertDialog dialog = new AlertDialog
                .Builder(context)
                .setCancelable(false)
                .setTitle(title)
                .setView(editText)
                .setPositiveButton(btn, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (listener != null) {
                            listener.onEditCompleted(editText);
                        }
                        KeyboardUtils.hideSoft(editText);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        KeyboardUtils.hideSoft(editText);
                    }
                }).create();
        dialog.show();
        return dialog;
    }

    public static ProgressDialog showProgressDialog(Context context) {
        return ProgressDialog.show(context, "加载中", "加载中,请稍后..."
                , false, true);
    }

    public static AlertDialog showEnsureDialog
            (Context context, String title, String positive,
             DialogInterface.OnClickListener positiveListener,
             String negative, DialogInterface.OnClickListener negativeListener) {
        AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle(title)
                .setPositiveButton(positive, positiveListener)
                .setNegativeButton(negative, negativeListener).create();
        dialog.show();
        return dialog;
    }


    public static ProgressDialog showLoadProgress(Context context) {
        ProgressDialog dialog = new ProgressDialog(context);
        dialog.setTitle("正在加载数据......");
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setMax(100);
        dialog.show();
        return dialog;
    }

    public interface OnEditListener {
        void onEditCompleted(EditText editText);
    }

}
