package com.eric.kotlin

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

//扩展属性
var Person.index: Int
    get() {
        return Random.nextInt(0,10)
    }
    set(value) {
        this.index - 1
    }


