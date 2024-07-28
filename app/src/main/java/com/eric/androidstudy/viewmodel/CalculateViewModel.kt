package com.eric.androidstudy.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**

 * @Author: chen

 * @datetime: 2024/7/28

 * @desc:

 */
class CalculateViewModel : ViewModel() {

    private val _state = MutableStateFlow(CoroutineUIState())

    //对外暴露的state，使用不可修改状态的state。这样符合软件开发的原则。对外支持扩展，对修改关闭
    var state : StateFlow<CoroutineUIState> = _state

    fun increase() {
        _state.value = _state.value.copy(count = _state.value.count + 1)
    }

    fun decrease() {
        _state.value = _state.value.copy(count = _state.value.count- 1)
    }
}

enum class Action {
    INCREASE,DECREASE
}

data class CoroutineUIState(var count: Int = 0)