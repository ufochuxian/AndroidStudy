package com.eric.kotlin.flow

import android.util.Log
import com.eric.base.ext.ERIC_TAG
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


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
}