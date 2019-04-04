package com.ryan.router.sample.utils;

import android.content.Context;
import android.widget.Toast;

public class ToastUtils {

    private static Toast mToast = null;

    public static void showToastShort(Context context, String msg) {
        showToast(context, msg, Toast.LENGTH_SHORT);
    }

    public static void showToastLong(Context context, String msg) {
        showToast(context, msg, Toast.LENGTH_LONG);
    }

    public static void showToastShort(Context context, int resId) {
        showToast(context, context.getResources().getString(resId), Toast.LENGTH_SHORT);
    }

    public static void showToastLong(Context context, int resId) {
        showToast(context, context.getResources().getString(resId), Toast.LENGTH_LONG);
    }

    private static void showToast(Context context, String msg, int duration) {
        if (mToast == null) {
            mToast = Toast.makeText(context, msg, duration);
        } else {
            mToast.setText(msg);
            mToast.setDuration(duration);
        }
        mToast.show();
    }

    public static void cancel() {
        if (mToast != null) {
            mToast.cancel();
        }
    }
}

