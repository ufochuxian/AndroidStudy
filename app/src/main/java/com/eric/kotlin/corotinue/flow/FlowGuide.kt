package com.eric.kotlin.corotinue.flow

import android.app.Activity
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map


private suspend fun Activity.flow() {
    flow {
        for (i in 1..30) {
            emit(i)
        }
    }.map { it->
        println(it * 2)
    }.collect()
}
