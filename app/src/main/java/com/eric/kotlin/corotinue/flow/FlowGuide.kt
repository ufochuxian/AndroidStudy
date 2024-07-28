package com.eric.kotlin.corotinue.flow

import android.app.Activity
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


private suspend fun Activity.flow() {
    flow {
        for (i in 1..30) {
            emit(i)
        }
    }.map { it ->
        println(it * 2)
    }.collect()
}

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
