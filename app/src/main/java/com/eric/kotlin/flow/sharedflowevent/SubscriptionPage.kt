package com.eric.kotlin.flow.sharedflowevent

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.blankj.utilcode.util.LogUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SubscriptionPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val button = Button(this).apply {
            text = "Subscribe Now"
            setOnClickListener {
                CoroutineScope(Dispatchers.Main).launch {
                    LogUtils.d("发送购买成功订阅消息")
                    EventBus.sendSubscriptionSuccessEvent()
                }
            }
        }
        setContentView(button)
    }
}
