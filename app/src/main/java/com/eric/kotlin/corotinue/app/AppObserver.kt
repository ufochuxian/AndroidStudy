package com.eric.kotlin.corotinue.app

import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent

/**

 * @Author: chen

 * @datetime: 2024/7/9

 * @desc:

 */
class AppObserver : LifecycleObserver {

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun moveToForeGround() {
        // 应用程序回到前台
        Log.d("AppObserver", "Returning to foreground...")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun moveToBackGround() {
        // 应用程序移至后台
        Log.d("AppObserver", "Moving to background...")
    }

}