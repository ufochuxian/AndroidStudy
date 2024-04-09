package com.eric.kotlin

/**

 * @Author: chen

 * @datetime: 2024/4/8

 * @desc:

 */
fun main() {

    //kt调用java的方法
    JavaToKt().sayWelcome().run(::println)
}

val lambada:(String) -> Unit = { inputString : String ->
    println(inputString.toLowerCase().capitalize())
}