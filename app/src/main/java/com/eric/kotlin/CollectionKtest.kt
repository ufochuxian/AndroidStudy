package com.eric.kotlin


fun main() {


    val buffer = arrayOf("1","2","3","4","5")

    //使用loop@可以标志跳转的地方
    buffer.forEachIndexed loop@{ index, s ->

        if(s == "3") return@loop
        println(s)

    }
}