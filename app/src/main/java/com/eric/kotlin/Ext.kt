package com.eric.kotlin

import android.view.View
import java.util.Date
import kotlin.properties.Delegates
import kotlin.random.Random

/**

 * @Author: chen

 * @datetime: 2024/4/7

 * @desc:

 */

//扩展函数
 fun Person.superHello() {
     println("hell super $name,index:${index}")
 }

//增加一个打印所有函数执行时间的扩展
fun Any.printWithDate(lambada : ()->Unit) {
    println("start:${Date().toLocaleString()}")
    lambada.invoke()
    println("end:${Date().toLocaleString()}")
}

public fun <T> View.findById(id : Int) : T {
    return this.findViewById(id)
}
//扩展属性
var Person.index: Int
    get() {
        return Random.nextInt(0,10)
    }
    set(value) {
        this.index - 1
    }


