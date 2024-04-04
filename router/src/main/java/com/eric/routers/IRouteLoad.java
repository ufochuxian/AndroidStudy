package com.eric.routers;

import android.app.Activity;

import java.util.Map;

/**
 * @Author: chen
 * @datetime: 2021/6/30
 * @desc:
 */
public interface IRouteLoad {

    void onLoad(Map<String, Class<? extends Activity>> routes);
}
