package com.eric.kotlin

import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {

    async {
        //在协程作用域内部，通过coroutineContext[Job]可以获取到当前协程的job对象
        println("$coroutineContext[Job],isActive:${coroutineContext[Job]?.isActive}")

    }
    println("")
}