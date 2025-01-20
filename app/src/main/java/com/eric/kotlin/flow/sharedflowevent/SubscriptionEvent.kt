package com.eric.kotlin.flow.sharedflowevent

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

sealed class SubscriptionEvent {
    object SubscriptionSuccess : SubscriptionEvent()
}

object EventBus {
    private val _subscriptionEvents = MutableSharedFlow<SubscriptionEvent>(
        replay = 0, // 不保留历史事件
        extraBufferCapacity = 1 // 避免事件丢失
    )
    val subscriptionEvents = _subscriptionEvents.asSharedFlow()

    // 发送事件
    suspend fun sendSubscriptionSuccessEvent() {
        _subscriptionEvents.emit(SubscriptionEvent.SubscriptionSuccess)
    }
}
