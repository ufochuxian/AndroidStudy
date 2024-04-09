package com.eric.kotlin

import java.io.IOException

/**

 * @Author: chen

 * @datetime: 2024/4/8

 * @desc:

 */
class Teacher {

    @JvmField
    var name : String = ""

    var age : Int = 0

    fun sayHello(param1 : String = "param1",param2 : String = "param2") {
        "param1:$param1,param2::$param2".run(::println)
    }

    @JvmOverloads
    fun multyParamsMethod(param1 : String = "param1",param2 : String = "param2") {
        "param1:$param1,param2::$param2".run(::println)
    }

    //这里必须使用 @Throws(IOException::class)，注解进行修饰，那么在java中调用的话，才能正确捕获到
    @Throws(IOException::class)
    fun showException() {
        throw IOException("这是一个演示抛出IoException的例子")
    }

}