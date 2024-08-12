package com.eric.kotlin.corotinue.flow

import android.app.Activity
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.coroutines.CoroutineContext


private suspend fun Activity.flow() {
    flow {
        for (i in 1..30) {
            emit(i)
        }
    }.map { it ->
        println(it * 2)
    }.collect()
}

fun main() {
    runBlocking {
        val messageModelFlow = flow {
            arrayOf(
                MessageModel(ModelType.ASR, "asr"), MessageModel(ModelType.TTL, "ttl"),
                MessageModel(ModelType.JLGL, "JLGL")
            ).forEach {
                emit(it)
            }
        }

        delay(3000)

        //flow是一个冷流，有观察者，才会触发生产发射，并且数据对于 每个观察者，都是独立的一份，不共享，并且没有生命周期的概念，发射完成就结束了.并且数据不会重置。并且没有缓存数据的概念
        //这些都是区别于LiveData
        messageModelFlow.collect {
            println(it.content)
        }

        messageModelFlow.map {
            it.content.plus("_super")
        }.collect {
            println(it)
        }
    }
}

data class MessageModel(val type: ModelType,val content : String)

enum class ModelType {
    ASR,TTL,JLGL
}
