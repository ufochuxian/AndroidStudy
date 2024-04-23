package com.eric.kotlin

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.coroutines.CoroutineContext


//suspend fun someCoroutines() : Job {
//    return runBlocking {
//        GlobalScope.launch {
//            println("这是一个GlobalScope的协程")
//            delay(1000)
//            println("GlobalScope的协程计算结束")
//        }
//
//        async {
//            println("这是子协程1")
//            delay(500)
//            println("这是子协程1，如果它的父作用域的协程取消了的话，那么就会被取消")
//        }
//
//        async {
//            println("这是子协程2")
//            delay(800)
//            println("这是子协程2，如果它的父作用域的协程取消了的话，那么就会被取消")
//        }
//    }
////}
//
//fun main() = runBlocking {
//    val job = someCoroutines()
//    println("开始准备结束suspend方法")
//    job.cancel()
//    println("main")
//}

//如果其中⼀个⼦协程（即 two ）失败，第⼀个 async 以及等待中的⽗协程都会被取消
fun main() = runBlocking<Unit>{
    try {
        failedConcurrentSum()
    }catch (e:ArithmeticException){
        println("Computation failed with ArithmeticException")
    }
}

/**
 * 这种用一个suspend方法，并发执行多个协程的方式，在实际的开发中，并不推荐
 * 因为这里如果其中一个协程异常了，那么其他的协程，也会跟着被取消任务
 *从而有可能导致业务逻辑的不正确
 *
 */
suspend fun failedConcurrentSum(): Int = coroutineScope {
    val one = async<Int> {
        try{
            delay(Long.MAX_VALUE)
            42
        }finally {
            println("First child was cancelled")
        }
    }
    val two = async<Int> {
        println("Second child throw an Exception.")
        throw ArithmeticException()
    }
    one.await() + two.await()
}
