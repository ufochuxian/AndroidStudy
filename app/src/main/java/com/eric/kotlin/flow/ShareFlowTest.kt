package com.eric.kotlin.flow

import android.util.Log
import com.eric.base.ext.ERIC_TAG
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

sealed class UIEvent {
    object ClickEvent : UIEvent()
    data class PurchaseSuccessEvent(val amount: Int) : UIEvent()
}

object ShareFlowTest {

    fun testShareFlow() {
        var sharedFlow = MutableSharedFlow<String>(replay = 2)


        GlobalScope.launch {
            sharedFlow.emit("1")
            sharedFlow.emit("2")
            sharedFlow.emit("3")
        }

        //这里是先发射，后订阅的情况

        //这种collect收到历史事件，可以支持配置显示的“历史事件”的数量
        //这里会打印出2，3两个数值，两个历史数值
        GlobalScope.launch {
            sharedFlow.collect {
                Log.i(ERIC_TAG,"testShareFlow sharedflow collect:${it}")
            }
        }

    }

    fun testSharedFlow2() {
        val sharedFlow = MutableSharedFlow<String>()
        GlobalScope.launch {
            sharedFlow.collect {
                Log.i(ERIC_TAG,"testSharedFlow2 sharedflow collect:${it}")
            }
        }

        GlobalScope.launch {
            sharedFlow.emit("1")
            sharedFlow.emit("2")
            sharedFlow.emit("3")
        }
    }

    // 创建一个 MutableSharedFlow
    val eventFlow = MutableSharedFlow<UIEvent>(
        replay = 1, // 不保留历史事件
        extraBufferCapacity = 5 // 缓冲区大小
    )

    // 观察事件流 (适用于Activity或ViewModel)
    fun observeEvents() = CoroutineScope(Dispatchers.Main).launch {
        eventFlow.collect { event ->
            when (event) {
                is UIEvent.ClickEvent -> Log.d(ERIC_TAG,"🔥 Button Clicked!")
                is UIEvent.PurchaseSuccessEvent -> Log.d(ERIC_TAG,"💰 Purchase successful: ${event.amount}")
            }
        }
    }

        fun testSharedFlow3() {
            runBlocking {
                eventFlow.emit(UIEvent.ClickEvent)

                observeEvents()

//                eventFlow.emit(UIEvent.PurchaseSuccessEvent(2))
            }
    }
}