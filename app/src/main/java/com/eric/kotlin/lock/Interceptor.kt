package com.eric.kotlin.lock

import android.content.Context
import android.content.Intent
import java.util.concurrent.locks.Lock

// Interceptor.kt
interface Interceptor {
    fun shouldIntercept(): Boolean
}

// LockInterceptor.kt
class LockInterceptor(private val conditionProvider: () -> Boolean) : Interceptor {
    override fun shouldIntercept(): Boolean {
        return conditionProvider()
    }
}

// NavigationManager.kt
object NavigationManager {
    fun navigate(context: Context, intent: Intent) {
        LockStateManager.setDefault()
        context.startActivity(intent)
    }

    fun showLockActivity(context: Context) {
        val lockIntent = Intent(context, LockActivity::class.java)
        context.startActivity(lockIntent)
    }
}