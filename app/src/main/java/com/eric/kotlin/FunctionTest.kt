package com.eric.kotlin

/**

 * @Author: chen

 * @datetime: 2024/4/8

 * @desc:

 */

//如果一个数字，只能被1和他自己整除，那么就是素数
fun Int.isPrime(): Boolean {
    return (2 until this).filter { num ->
        this % num == 0
    }.isEmpty()
}

fun main() {
    (1..5000).toList().filter(Int::isPrime).take(10).run(::println)

    //当这样一种场景，我们需要根据某个条件产生一些列结果的时候，并且这个结果的个数是事先不知道需要多大的最大容量的时候
    //可以使用“惰性序列”
    generateSequence(2) { value ->
        value + 1
    }.filter(Int::isPrime).take(100).toList().run(::println)

}