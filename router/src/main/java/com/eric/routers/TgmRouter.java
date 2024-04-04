package com.eric.routers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @Author: chen
 * @datetime: 2021/6/29
 * @desc:
 */
public class TgmRouter {

    private static final String TAG = "TgmRouter";

    private Context mCtx;

    public void init(Context context) {
        this.mCtx = context;
        try {
            Set<String> classNames = ClassUtils.getFileNameByPackageName(context, "com.eric.routers.apt");

            for (String className : classNames) {
                Class<?> clazz = Class.forName(className);
                if (IRouteLoad.class.isAssignableFrom(clazz)) {
                    //当前clazz文件是IRouteLoad的实现类
                    Log.v(TAG, "[init] classNames size:" + classNames.size());
                    IRouteLoad isRoutePath = (IRouteLoad) clazz.getConstructor().newInstance();
                    isRoutePath.onLoad(routes);
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "初始化失败");
            e.printStackTrace();
        }
    }

    //path 集合
    public Map<String, Class<? extends Activity>> routes = new HashMap<>();

    private static TgmRouter mInstance = null;

    private TgmRouter() {

    }

    public synchronized static TgmRouter getInstance() {
        if (mInstance == null) {
            synchronized (TgmRouter.class) {
                if (mInstance == null) {
                    mInstance = new TgmRouter();
                }
            }
        }
        return mInstance;
    }

    public void register(String path, Class clazz) {
        routes.put(path, clazz);
    }


    public void startActivity(String path) {
        Class aClass = routes.get(path);

        if (aClass == null) {
            Log.e(TAG, "路由跳转的地址不存在");
            return;
        }

        Intent intent = new Intent(mCtx, aClass);
        this.mCtx.startActivity(intent);
    }

}
