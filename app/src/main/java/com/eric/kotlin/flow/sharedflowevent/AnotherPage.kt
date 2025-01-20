package com.eric.kotlin.flow.sharedflowevent

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.blankj.utilcode.util.LogUtils
import kotlinx.coroutines.*

class AnotherPage : AppCompatActivity() {
    private val scope = CoroutineScope(Dispatchers.Main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        scope.launch {
            LogUtils.d("AnotherPage 订阅购买成功事件")
            EventBus.subscriptionEvents.collect { event ->
                when (event) {
                    is SubscriptionEvent.SubscriptionSuccess -> {
                        LogUtils.d("AnotherPage 收到购买成功事件")
                        Toast.makeText(this@AnotherPage, "✅ Another Page: Subscription Success!", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()  // 防止内存泄漏
    }
}
