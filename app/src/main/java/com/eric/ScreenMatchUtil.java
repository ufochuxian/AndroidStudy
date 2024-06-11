package com.eric;

/**
 * @Author: chen
 * @datetime: 2024/6/11
 * @desc:
 */

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import com.eric.rxjava.R;


public class ScreenMatchUtil {

    /**
     *
     * 适配方案（https://blog.csdn.net/weixin_39583013/article/details/117617567）
     * @param context
     */

    public static void log(Activity context) {
        //  px的全称是（ pixel ）, 即像素的意思；
        //我们常数的 480*800 、720*1280、1080*1920指的就是像素值宽高的意思；
        //指屏幕是由多少个象数点所组成；
        //在实际编程中，我们通常不使用该单位；比如，我们指定一张图片的大小是240*400px,它在480*800的手机屏幕上占的大小是一半，而在1080*1920分辨率的手机屏幕上，这张图片占有的空间连1/4都不到，这样就造成兼容性事故；
        //但是在某些特定情况下，使用PX会具有优势，比如设置一道分割线，原因将在下一节解释完dp后给出；
        //使用方法如下(这里分别是px和dp，解释在下一节)：
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        int API_LEVEL = android.os.Build.VERSION.SDK_INT;
        // 方法1
        DisplayMetrics displayMetrics = new DisplayMetrics();
        if (API_LEVEL >= 17) {
            display.getRealMetrics(displayMetrics);
        } else {
            display.getMetrics(displayMetrics);
        }

        // 关于参数的一些讲解：https://blog.csdn.net/u013597998/article/details/51093728s

        // 首先 dp=dip，老版本的Android系统中，常用dip，现在都用dp；
        //dp也是一种像素，全称“Device-Independent Pixel”，叫“设备独立像素”,这个值和“像素密度”有关；
        //先来说说这个“像素密度”，简称是dpi，全称是（ Dots pre Inch），即每一个英寸屏幕包含的像素点数，这个值和手机的物理尺寸相关联；
        //px像素只说明了一个屏幕包含的点数有多少，但是点的大小不是确定的，同样是480*800，可能是手掌那么大，也可能是电影院屏幕那么大；
        //现在引入像素密度，用每英寸包含的像素数目，来衡量屏幕的质量水准；
        //一部横向宽度2英寸、480像素的手机，它的横向像数密度就是 480/2 = 240dpi ；
        //可以看出，“设备独立像素”是一个常量；
        //获取像素密度的方法是：
        int densityDpi = displayMetrics.densityDpi;

        // SP的全称是“ScaledPixels”,即放大像素；
        //Android的系统，允许字体自由缩放，在整个屏幕大小不变、像素不变的情况下，字体可以进行缩放；这样，字体的大小就和之前的英寸，像素，设备独立像素，密度都没有关系了；这个 “放大像素”和另一个参数“缩放密度（scaledDensity）”有关；
        //在实际编程中，我们通常将字体使用sp单位，以适应不同屏幕分辨率；
        float scaledDensity = displayMetrics.scaledDensity;

        String param = "px=" + displayMetrics.widthPixels + "*" + displayMetrics.heightPixels + " dp(dip)=" + densityDpi
                + " ，smallDp=" + displayMetrics.widthPixels * 160 / densityDpi + "*" + displayMetrics.heightPixels * 160 / densityDpi
                + "\n sp=" + scaledDensity + " density=" + displayMetrics.density + " scaledDensity=" + displayMetrics.scaledDensity + " xdpi=" + displayMetrics.xdpi + " ydpi=" + displayMetrics.ydpi
                + "\n current dp1=" + context.getResources().getDimension(R.dimen.dp1) + ",match dp=" + String.valueOf(context.getResources().getDimension(R.dimen.dp1) / displayMetrics.density);
        Log.e("screen_param", param);
    }
}