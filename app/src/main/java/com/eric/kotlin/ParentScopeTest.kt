package com.eric.kotlin

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.coroutines.CoroutineContext

/**
 *
 *
 * 这个例子的结果表明：
 *
 * 1.GlobalScope的作用域比较长，通常等同于app生命周期
 *
 * 2. 父协程的取消，会导致“子协程”，也被取消
 */
fun main() = runBlocking {
    val parentJob = launch {


        GlobalScope.launch {
            println("Global Scope launch")
            delay(1000)
            println("Global Scope launch doSomething end")
        }

        try {
            //这里是自定义协程的名字
            launch(CoroutineName("launch1")) {
                println("name:${this.coroutineContext[CoroutineName.Key]?.name}")
                delay(500)
                println("launch1 end")
            }
        } catch (e: Exception) {
            println(e)
        }

    }

    delay(300)
    parentJob.cancel()
    delay(200)

    println("main end")


}