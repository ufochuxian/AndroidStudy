package com.eric;

import android.content.Context;

import androidx.annotation.DimenRes;

/**
 * @Author: chen
 * @datetime: 2024/6/11
 * @desc:
 */
public class ScreenSizeUtil {

    /**
     * 计算当前的SP的值
     *
     * @param context
     * @param spSize  :R.dimen.sp_16
     * @return
     */
    public static int getSP(Context context, @DimenRes int spSize) {

        float pxValue = context.getResources().getDimension(spSize);//获取对应资源文件下的sp值
        //将px值转换成sp值
        return px2sp(context, pxValue);
    }

    /**
     * 计算当前的DP的值
     */
    public static int getDP(Context context, @DimenRes int dpSize) {
        float pxValue = context.getResources().getDimension(dpSize);//获取对应资源文件下的dp值
        //将px值转换成dp值
        return px2dip(context, pxValue);
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        if (context == null) {
            return (int) dpValue;
        }
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        if (context == null) {
            return (int) pxValue;
        }
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * px转换为sp
     */
    public static int px2sp(Context context, float pxValue) {
        if (context == null) {
            return (int) pxValue;
        }
        final float scale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / scale + 0.5f);
    }
}