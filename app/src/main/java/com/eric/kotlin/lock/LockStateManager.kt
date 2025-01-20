package com.eric.kotlin.lock

import android.util.Log
import com.eric.base.ext.ERIC_TAG

// LockStateManager.kt
object LockStateManager {
    var lockStatus: LockStatus = LockStatus.DEFAULT // 默认状态为锁定

    fun isUnlockSuccess() : Boolean {
        return lockStatus == LockStatus.UNLOCK_SUCCESS
    }

    fun setDefault() {
        Log.d(ERIC_TAG,"setDefault before,lockStatus:${lockStatus}")
        lockStatus = LockStatus.DEFAULT
        Log.d(ERIC_TAG,"setDefault afater,lockStatus:${lockStatus}")
    }
}

enum class LockStatus {
    DEFAULT,LOCKED,UNLOCKED,UNLOCK_SUCCESS
}