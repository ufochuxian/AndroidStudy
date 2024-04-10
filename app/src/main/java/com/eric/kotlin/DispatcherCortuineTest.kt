package com.eric.kotlin

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/**

 * @Author: chen

 * @datetime: 2024/4/11

 * @desc:

 */


//fun main() = runBlocking {
//    launch {
//        println("world,threadName:${Thread.currentThread().name}")
//    }
//    println("hello,threadName:${Thread.currentThread().name}")
//}

fun main() = runBlocking {
    //非受限调度器。线程运行的线程是不受控制的。不定会运行在主线程，还是子线程。所以通常不建议用来执行特定UI更新任务，以及cpu密集型消耗时间的异步task
    launch(context = Dispatchers.Unconfined) {
        println("Dispatch CoroutineStart.UNDISPATCHED 执行任务,thread name:${Thread.currentThread().name}")
        delay(1000)
        println("Dispatch CoroutineStart.UNDISPATCHED 执行挂起函数，延迟之后执行任务,thread name:${Thread.currentThread().name}")
    }
    println("")
}