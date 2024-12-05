package com.eric.base.mgr

import android.os.Handler
import android.os.Looper
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit

data class PollingConfig(
    val interval: Long = 500, // 轮询间隔，单位：毫秒
    val timeout: Long = 1000 * 60,// 超时时间，单位：毫秒
    val pollingCallBack: PollingCallBack? = null
)

interface PollingCallBack {
    fun onTick()
    fun onTimeout()
}

class PollingManager(private val config: PollingConfig) {

    private val scheduler = Executors.newScheduledThreadPool(1)
    private var scheduledFuture: ScheduledFuture<*>? = null
    private val handler = Handler(Looper.getMainLooper())

    fun start() {
        // 启动定时任务，每次执行 onTick
        scheduledFuture = scheduler.scheduleWithFixedDelay({
            handler.post { config.pollingCallBack?.onTick() }
        }, 0, config.interval, TimeUnit.MILLISECONDS)

        // 设置超时，超时后停止轮询并执行 onTimeout
        handler.postDelayed({
            stop()
            handler.post { config.pollingCallBack?.onTimeout() }
        }, config.timeout)
    }

    fun stop() {
        scheduledFuture?.cancel(true)
        scheduledFuture = null
        handler.removeCallbacksAndMessages(null)
    }
}
