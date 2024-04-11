package com.eric.kotlin

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

//这种写法比较符合kotlin的协程的风格
//把主线程包装成了一个协程的作用域中
//fun main() = runBlocking {
//    val globalCoroutine = GlobalScope.launch {
//        delay(1000)
//        println(Thread.currentThread().name)
//    }
//    println("${Thread.currentThread().name},hello")
//    //使用join函数，会将当前协程，绑定到调用的协程，当前协程执行完毕后，
//    globalCoroutine.join()
//}

fun main() = runBlocking {

    //这里的xie
    launch {
        delay(1000)
        "launch 协程运行完毕".apply(::println)
    }


    //此处已经进入了自定义的“协程作用域”，这个作用域会让当前协程挂起(所以后面的一句打印就不会执行啦)，要等上面的协程都执行完毕，才会继续向下执行
//    coroutineScope {
//        launch {
//            delay(2000)
//            "coroutineScope launch 协程运行完毕".apply(::println)
//        }
//    }

    println("在CoroutineScope中，所有协程全部执行完毕，才会结束")
}