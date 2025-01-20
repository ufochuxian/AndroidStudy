package com.eric.kotlin.lock

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.eric.base.ext.ERIC_TAG

// LockLifecycleObserver.kt
class LockLifecycleObserver(
    private val context: Context,
    private val conditionProvider: () -> Boolean
) : LifecycleObserver {

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        if (isIgnorePage()) return

        val lockInterceptor = LockInterceptor(conditionProvider)
        val shouldIntercept = lockInterceptor.shouldIntercept()
        Log.d(ERIC_TAG,"是否满足上锁的条件,shouldIntercept:${shouldIntercept}")
        if (shouldIntercept) {
            NavigationManager.showLockActivity(context)
        }
    }

    private fun isIgnorePage(): Boolean {
        if (context is Activity) {
            val activity = context as Activity
            val currentActivityName = activity.javaClass.name
            val isIgnorePage = LockIgnorePagesConfigs.isIgnorePage(currentActivityName)
            Log.d(ERIC_TAG, "isIgnorePage:${isIgnorePage},currentActivityName:${currentActivityName}")
            if (isIgnorePage) return true
        }
        return false
    }
}