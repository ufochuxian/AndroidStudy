package com.eric.kotlin.corotinue

import kotlin.coroutines.Continuation
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.createCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.startCoroutine
import kotlin.coroutines.suspendCoroutine

/**

 * @Author: chen

 * @datetime: 2024/6/20

 * @desc:

 */

class User(val name: String) {

}

/**
 * kotlin的coroutine，总结来说就是干两件事，挂起和恢复
 *
 * 主要目标：简化代码的异步调用，在语言层面来说，简化异步同样逻辑的理解成本，避免回调嵌套，更符合人的直观思维。
 *
 * 在语言运行层面，得益于Dispatcher，帮我们实现了线程调度。在做异步任务的时候，不一定发生了线程切换，可以在同一个线程，有多个异步任务
 *
 * 并且可以使用job对象来控制这些生命周期，比线程直接实现异步，更加的轻量。从资源占用的角度来说，资源占用更轻。因为线程的开辟都是需要占用一定栈的内存空间的。
 * 在很多系统是1MB左右。这也是为什么在官方文档有句说法是，“协程是轻量级的线程”。
 *
 *
 */

// 类似于Promise，kotlin中定义一个使用协程的挂起函数，必须使用suspend关键字，编译器，会默认把我们在方法，传入一个参数Continuation

//类似于js中的Promise

//默认传递的这个Continuation对象，可以帮助我们实现“挂起”。不过如果我们在下面的例子中，没有使用Dispatcher切换线程，那么也不是真正的"挂起"。
suspend fun getUserSuspend(name : String) = suspendCoroutine<User> {
    if(name.isNotEmpty()) {
        it.resumeWith(Result.success(User("haha")))
    } else {
        it.resumeWith(Result.failure(Throwable("请求发生了错误")))
    }
}

fun main() {

    //1. 最原始的创建一个Coroutine的方式，传入一个suspend{}函数，
    // 然后完成后，返回的是一个包装好了结果的Continuation
    suspend {  }.createCoroutine(object :Continuation<Unit> {
        override val context: CoroutineContext
            get() = TODO("Not yet implemented")

        override fun resumeWith(result: Result<Unit>) {
            TODO("Not yet implemented")
        }
    })
        //这个resume方法用于启动协程
        .resume(Unit)

    //2. 简化创建协程，并且启动的方式,startCoroutine，等同于上面create之后，再resume
    suspend {  }.startCoroutine(object :Continuation<Unit> {
        override val context: CoroutineContext
            get() = TODO("Not yet implemented")

        override fun resumeWith(result: Result<Unit>) {
            TODO("Not yet implemented")
        }

    })
}