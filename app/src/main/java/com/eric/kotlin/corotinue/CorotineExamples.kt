package com.eric.kotlin.corotinue

import kotlin.coroutines.suspendCoroutine

/**

 * @Author: chen

 * @datetime: 2024/6/20

 * @desc:

 */

class User(val name: String) {

}

// 类似于Promise，kotlin中定义一个使用协程的挂起函数，必须使用suspend关键字，编译器，会默认把我们在方法，传入一个参数Continuation

//类似于js中的Promise
suspend fun getUserSuspend(name : String) = suspendCoroutine<User> {
    if(name.isNotEmpty()) {
        it.resumeWith(Result.success(User("haha")))
    } else {
        it.resumeWith(Result.failure(Throwable("请求发生了错误")))
    }
}
