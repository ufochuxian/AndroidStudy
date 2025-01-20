package com.eric.kotlin.lock

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.eric.androidstudy.R
import com.eric.base.ext.ERIC_TAG

// PageAActivity.kt
class PageAActivity : AppCompatActivity() {
    private var isLockRequired: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 注册 LifecycleObserver
        lifecycle.addObserver(LockLifecycleObserver(this) {
            val isNeedLock = isLockRequired && !LockStateManager.isUnlockSuccess()
            Log.d(ERIC_TAG,"isNeedLock:${isNeedLock},isLockRequired:${isLockRequired},status:${LockStateManager.lockStatus}")
            isNeedLock
        })

        setContentView(R.layout.activity_a)

        findViewById<TextView>(R.id.goToB).setOnClickListener {
            navigateToPageB()
        }
    }

    fun navigateToPageB() {
        val intent = Intent(this, PageBActivity::class.java)
        NavigationManager.navigate(this, intent)
    }

    fun navigateToThirdPartyPage() {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.example.com"))
        NavigationManager.navigate(this, intent)
    }
}

// PageBActivity.kt
class PageBActivity : AppCompatActivity() {
    private var isLockRequired: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 注册 LifecycleObserver
        lifecycle.addObserver(LockLifecycleObserver(this) { isLockRequired })

        setContentView(R.layout.activity_b)

        findViewById<TextView>(R.id.goToA).setOnClickListener {
            nativigateToPageA()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    private fun nativigateToPageA() {
        val intent = Intent(this, PageAActivity::class.java)
        NavigationManager.navigate(this, intent)
    }
}