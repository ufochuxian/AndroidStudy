package com.transsion.architecturemodule.base.ktx

import com.google.gson.Gson

fun String.isNumeric(): Boolean {
    return this.isNotEmpty() && this.all { it in '0'..'9' }
}

fun String.isOperatorOrDigit(): Boolean {
    val operators = arrayListOf("+","-","×","÷",".")
    return this.isNotEmpty() && operators.indexOf(this) != -1
}

fun String.isNumericWithDigit(): Boolean {
    // 正则表达式匹配正数、负数和小数
    val numericPattern = Regex("^-?\\d+(\\.\\d+)?$")
    return this.matches(numericPattern)
}

fun String.toNegatedNumber(): String? {
    return if (this.isNumericWithDigit()) {
        val number = this.toDouble() * -1
        number.toString()
    } else {
        null
    }
}

// 解析 JSON 字符串为数据类
inline fun <reified T>String.parseJson(): T? {
    return try {
        val gson = Gson()
        gson.fromJson(this, T::class.java)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}