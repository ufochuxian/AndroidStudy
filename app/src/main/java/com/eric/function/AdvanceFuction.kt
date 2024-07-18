package com.eric.function

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.single

/**

 * @Author: chen

 * @datetime: 2024/7/17

 * @desc:

 */

/**
 *
 * 执行结果
 *
 * 2024-07-17 00:35:48.172  5224-5267  costTime                com.eric.androidstudy                I  start delay hello:YaoMing
 * 2024-07-17 00:35:48.172  5224-5224  costTime                com.eric.androidstudy                I  Function0<java.lang.Object>,costTime:2
 * 2024-07-17 00:35:50.173  5224-5267  costTime                com.eric.androidstudy                I  after delay:YaoMing
 */

/**
 * 高阶函数： 使用函数作为“参数”或者“返回值”的“函数”
 *
 * 函数作为参数，使用() -> 返回值，作为“函数”类型（也叫lambada）
 * 这里不管函数的参数是什么，可以不用写入参数
 *
 */
inline fun costTime(noinline block: () -> Any) {
    val start = System.currentTimeMillis()
    block()
    val end = System.currentTimeMillis()
    //这句会继续执行，协程内部的delay只会在协程内部执行
    Log.i("costTime", "$block,costTime:${end - start}")

    //添加一些用于git测试的代码
    //多添加一些用于测试
}

suspend fun sayHello(name: String) {
    println(flow<String> {
        Log.i("costTime","start delay hello:${name}")
        delay(2000)
        Log.i("costTime","after delay:${name}")
        emit("haha:${name}")
    }.flowOn(Dispatchers.Default).single())
}

fun main() {
//    costTime { sayHello("YaoMing") }
}