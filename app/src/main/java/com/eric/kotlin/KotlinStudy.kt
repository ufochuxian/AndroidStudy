package com.eric.kotlin

/**

 * @Author: chen

 * @datetime: 2024/4/4

 * @desc:

 */
fun sayHello(name : String) :String {
    val message = "hello $name"
    println(message)
    return message
}

fun detailConnectFunction(vararg parms : String) :String {
    var result = ""
    parms.forEach {
        result = result.plus(it)
    }
    return result
}
