package com.eric.kotlin

import kotlinx.coroutines.Job
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

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

//fun main() = runBlocking {
////    repeat(1000) {
////        val job = launch {
////            println("this is in $it")
////            delay(500)
////        }
////    }
//    val job = launch {
//        repeat(1000) {
//            println("this is in $it")
//            delay(500)
//        }
//    }
//    delay(1800)
//    //取消协程
//    job.cancelAndJoin()
//    println("main end")
//}

fun main() = runBlocking {
    val job = launch {
        try {
            repeat(1000) {
                println("打印一下数字:$it")
                delay(500)
                //这里是可能发生异常信息的（如果调用了job.cancel的方法的话，因为协程在计算状态的时候，不允许取消）
                //那么就意味着如果后续有一些release资源的操作的话，需要放置到finally方法中，这样确保在发生异常的时候
                //也能正确的释放资源，比如流文件的读取句柄等等资源
            }
        } catch (e: Exception) {
            println(e.message)
        } finally {
//            try {
//                //挂起函数（协程取消的时候，如果有挂起函数，那么需要捕获下异常，因为是有可能报异常的，会阻止下面的逻辑的运行）
//                //如何在finally中调用挂起函数的行为，都有可能引起异常。所以通常如何需要在finally里面调用一些挂起函数
//                //那么可以使用withContext(noCancelable)来实现
//                delay(1000)
//            }catch (e:Exception) {
//                println(e)
//            }
//            println("finally 中的实现逻辑")



            //如果需要在finally中使用挂起函数，那么可以使用这个不允许取消的作用域
            withContext(NonCancellable) {
                delay(1000)
                println("finally 中的实现逻辑")
            }
        }

    }
    delay(2200)
    job.cancelAndJoin()
    println("main end")
}