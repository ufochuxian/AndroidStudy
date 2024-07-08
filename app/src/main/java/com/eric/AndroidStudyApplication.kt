package com.eric

import android.app.Application
import androidx.lifecycle.ProcessLifecycleOwner
import com.eric.kotlin.corotinue.app.AppObserver

/**

 * @Author: chen

 * @datetime: 2024/7/9

 * @desc:

 */
class AndroidStudyApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        //ProcessLifecycleOwner 提供的 Lifecycle 跟踪整个应用程序的生命周期，而不是单个活动或碎片的生命周期。
        ProcessLifecycleOwner.get().lifecycle.addObserver(AppObserver())
    }
}