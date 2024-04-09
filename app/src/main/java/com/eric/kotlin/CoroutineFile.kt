package com.eric.kotlin

import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/**

 * @Author: chen

 * @datetime: 2024/4/9

 * @desc:

 */
//fun main() = runBlocking {
//    launch {
//        delay(1000)
//        println("this is ${Thread.currentThread().name}")
//    }
//    println("this is main")
//}

//fun main() = runBlocking {
//    repeat(100000) {
//        launch {
//            println("index:${it},hello world!!!")
//        }
//    }
//}

//fun main() = runBlocking {
//    repeat(100000) {
//        Thread(Runnable {
//            println("index:${it},hello world!!!")
//        }).start()
//    }
//}

fun main() = runBlocking {
//    repeat(1000) {
//        val job = launch {
//            println("this is in $it")
//            delay(500)
//        }
//    }
    val job = launch {
        repeat(1000) {
            println("this is in $it")
            delay(500)
        }
    }
    delay(1800)
    //取消协程
    job.cancelAndJoin()
    println("main end")
}