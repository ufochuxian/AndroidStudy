package com.eric.kotlin.corotinue.flow

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectIndexed
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/**

 * @Author: chen

 * @datetime: 2024/7/29

 * @desc:

 */


class MutableSharedViewModel : ViewModel() {
    val _state = MutableSharedFlow<String>()

    var state: SharedFlow<String> = _state


    suspend fun sendEvent(event: String) {
        _state.emit(event)
    }
}

/**
 *
 * Collector 1:count:2,name:MutableStateFlow
 * Collector 2:count:2,name:MutableStateFlow
 *
 *
 */

fun main() {
    runBlocking {
        val mutableStateViewModel = MutableSharedViewModel()
        mutableStateViewModel.sendEvent("event1")

        //订阅之前的状态，不会被收到，也就是使用MutableSharedFlow的话，那么观察者只会收到从观察之后，发射过来额值，这样就避免了“粘性”消息，
        // 这里需要结合具体的使用场景，来进行选择
        launch {
            mutableStateViewModel._state.collectIndexed { index, value ->
                println("index:${index},value:${value}")
            }
        }
        mutableStateViewModel.sendEvent("event2")
//        launch {
//            mutableStateViewModel.state.collect(mutableStateViewModel._state)
//        }
    }
}