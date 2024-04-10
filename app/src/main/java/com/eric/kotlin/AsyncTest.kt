package com.eric.kotlin

import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

/**

 * @Author: chen

 * @datetime: 2024/4/10

 * @desc:

 */

fun main() = runBlocking {
    //执行异步任务
    val deferred1 = async {
        println("准备执行网络请求一")
        delay(1000)
        println("网络请求一")
    }
    val deferred2 = async {
        println("准备执行网络请求二")
        delay(900)
        println("网络请求二")
    }
    deferred1.await()
    deferred2.await()
    println("执行异步任务结束")

    val lazyDeferred1 = async (start =  CoroutineStart.LAZY){
        println("这是懒加载的协程，只有调用对应的deferred的start()方法或者await方法才会真正执行")
    }

    val lazyDeferred2 = async (start =  CoroutineStart.LAZY){
        println("这是懒加载的协程，只有调用对应的deferred的start()方法或者await方法才会真正执行")
    }

    //这里如果调用的是start方法，那么下面一句print的打印就会先执行啦，表明不是阻塞的
    lazyDeferred1.await()
    lazyDeferred2.await()
    println("执行懒加载协程结束")
}