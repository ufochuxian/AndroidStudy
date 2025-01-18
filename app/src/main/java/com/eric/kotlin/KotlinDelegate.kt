package com.eric.kotlin

import kotlin.properties.Delegates

class Order {
    var quality: Int  by Delegates.observable(20) { property, oldValue, newValue ->
        println("property name:${property.name},oldValue:$oldValue,newValue:$newValue")
    }


}

fun main() {
    val order = Order()

    order.quality = 50
}