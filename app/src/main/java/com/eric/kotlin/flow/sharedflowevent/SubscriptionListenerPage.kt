package com.eric.kotlin.flow.sharedflowevent

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.blankj.utilcode.util.LogUtils
import kotlinx.coroutines.*

class SubscriptionListenerPage : AppCompatActivity() {
    private val scope = CoroutineScope(Dispatchers.Main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        scope.launch {
            LogUtils.d("SubscriptionListenerPage 订阅购买成功事件")
            EventBus.subscriptionEvents.collect { event ->
                when (event) {
                    is SubscriptionEvent.SubscriptionSuccess -> {
                        LogUtils.d("SubscriptionListenerPage 收到购买成功事件")
                        Toast.makeText(this@SubscriptionListenerPage, "🎉 Subscription Successful!", Toast.LENGTH_SHORT).show()
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
