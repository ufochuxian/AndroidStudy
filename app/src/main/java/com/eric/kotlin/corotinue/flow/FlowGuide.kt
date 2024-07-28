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
