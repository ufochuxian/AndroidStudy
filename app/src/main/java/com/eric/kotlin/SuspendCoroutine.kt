package com.eric.kotlin

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.suspendCancellableCoroutine

import kotlinx.coroutines.*

import kotlinx.coroutines.*

fun doSomeAsync() {
    // 模拟一些异步操作
    println("Doing some async work...").printWithDate { }
}

@OptIn(DelicateCoroutinesApi::class)
fun main() {
    runBlocking {
        suspendCancellableCoroutine { continuation ->
            // 在一个新协程中使用 delay
            GlobalScope.launch {
                doSomeAsync()
                delay(3000)
                continuation.resumeWith(Result.success(true))
            }
        }.let { result ->
            if (result) {
                println("执行task的结果:${result}").printWithDate { }
            }
        }

    }
}
