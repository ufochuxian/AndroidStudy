package com.eric.kotlin

/**

 * @Author: chen

 * @datetime: 2024/7/23

 * @desc:

 */


/**
 * fun <T, R> List<T>.foldRight(initial: R, operation: (T, acc: R) -> R): R {
 *
 * 这里的List<T>.这个“字面函数接受者”，使用List中的T这个泛型参数，那么就指明了这里的T是List中的“单个元素”的类型
 *
 * R点击进入函数，就会发现是返回值类型
 *
 */

//foldRight用于从右到左，将数组中的数值进行"操作",并且赋值给acc
fun foldRight() {
    val resultString = arrayListOf("world", "hello", "haha").foldRight("initial") { element, acc ->
        acc.plus(" ").plus(element)
    }
    println("resultString:${resultString}")
}

fun main() {

    foldRight()

}