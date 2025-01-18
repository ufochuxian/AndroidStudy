package com.eric

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import androidx.lifecycle.ProcessLifecycleOwner
import com.eric.androidstudy.BuildConfig
import com.eric.base.log.LogbackConfigurator
import com.eric.base.log.LogbackTree
import com.eric.kotlin.corotinue.app.AppObserver
import timber.log.Timber


/**

 * @Author: chen

 * @datetime: 2024/7/9

 * @desc:

 */
class AndroidStudyApplication : Application() {

    companion object {
        @SuppressLint("StaticFieldLeak")
        private var instance: Context? = null

        val context: Context
            get() = instance!!.applicationContext
    }


    override fun onCreate() {
        super.onCreate()
        instance = this
        //ProcessLifecycleOwner 提供的 Lifecycle 跟踪整个应用程序的生命周期，而不是单个活动或碎片的生命周期。
        ProcessLifecycleOwner.get().lifecycle.addObserver(AppObserver())

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        } else {
            // 初始化 Logback 配置（如果有单独配置的方法）
            LogbackConfigurator.configure(applicationContext)
            Timber.plant(LogbackTree(AndroidStudyApplication::class.java))
        }
    }

}