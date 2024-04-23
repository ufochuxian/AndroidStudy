package com.eric.kotlin

import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking

fun log(msg: String) = println("thread name:${Thread.currentThread().name},msg:${msg}")

/**
 * 如何添加协程调试参数的信息？
 *
 * 1. edit configurations
 *
 * 2.vm options选项添加:-Dkotlinx.coroutines.debug
 *
 *
 * 这样在编译输出的时候，就会带出打印协程相关的信息
 *
 *
 */
fun main() = runBlocking {

    val job1 = async {
        log("async1")
        2
    }

    val job2 = async {
        log("async2")
        4
    }
    log("${job1.await() * job2.await()}")
}