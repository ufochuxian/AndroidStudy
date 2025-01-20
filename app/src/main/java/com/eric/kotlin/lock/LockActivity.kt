package com.eric.kotlin.lock

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.eric.androidstudy.R

class LockActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_applock)
    }

    fun onUnlockSuccess() {
        // 解锁成功后，更新状态
        LockStateManager.lockStatus = LockStatus.UNLOCK_SUCCESS
    }

    override fun onBackPressed() {
        super.onBackPressed()
        onUnlockSuccess()
    }
}