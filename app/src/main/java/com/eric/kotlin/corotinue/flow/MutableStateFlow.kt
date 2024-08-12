package com.eric.kotlin.corotinue.flow

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/**

 * @Author: chen

 * @datetime: 2024/7/29

 * @desc:

 */

data class UIState(var count: Int, var name: String)


class MutableStateViewModel : ViewModel() {
    private val _state = MutableStateFlow(UIState(0, "MutableStateFlow"))

    var state: StateFlow<UIState> = _state

    fun increase(count: Int) {
        _state.value.count += count
    }

    fun decrease(count: Int) {
        _state.value.count -= count

    }
}

/**
 *
 * Collector 1:count:2,name:MutableStateFlow
 * Collector 2:count:2,name:MutableStateFlow
 *
 *
 * 总结：StateFlow等同于一个特殊的“SharedFlow”,只有一个应答者，指定只缓存一个值。并且采用CompareGetAndSet，对新旧发射的值进行对比，如果新旧发射的值一样，就不会触发消费方进行消费啦
 * 适用于UI状态的更新，永远需要保持ui最新状态的一些场景，因为没有生命周期感应能力，需要结合lifecycle使用。避免不必要的资源消耗。另外就是不能处理生产和消费速度不匹配的问题。
 *
 * SharedFlow可以看作一个加强版的生产，消费处理模型，可以设置多个应答者，可以设置缓存大小，处理生产大于消费的问题。当然也没有生命周期感应能力，需要结合Lifecycle的使用。
 *
 */

fun main() {
    runBlocking {
        val mutableStateViewModel = MutableStateViewModel()
        mutableStateViewModel.increase(1)

        //订阅之前的状态，也会被收到，并且收到的是最新的“状态值”，这点从下面，订阅之后，又发射了新的值，可以看出来，
        //Collector 1，这个接收器，收到的值count也是2
        launch {
            mutableStateViewModel.state.collect { uiState ->
                println("Collector 1:count:${uiState.count},name:${uiState.name}")
            }
        }

        mutableStateViewModel.increase(1)

        launch {
            mutableStateViewModel.state.collect {
                println("Collector 2:count:${it.count},name:${it.name}")
            }
        }
    }
}