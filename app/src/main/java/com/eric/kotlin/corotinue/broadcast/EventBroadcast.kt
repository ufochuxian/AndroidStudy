package com.eric.kotlin.corotinue.broadcast

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

/**

 * @Author: chen

 * @datetime: 2024/7/7

 * @desc:

 */
object EventBroadcast {
    private val mEvents = MutableSharedFlow<Message>()
    val events = mEvents.asSharedFlow()

    suspend fun sendEvent(msg : Message) {
        mEvents.emit(msg)
    }
}

data class Message(val msgType: String, val content: String)


fun main() {
    GlobalScope.launch {
        EventBroadcast.sendEvent(Message("msg","利用SharedFlow实现的全局广播机制"))
    }
}