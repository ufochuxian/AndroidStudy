package com.eric.kotlin

/**

 * @Author: chen

 * @datetime: 2024/4/7

 * @desc:

 */

//密封类是一种ADT(Algebraic Data Types（代数数据类型)的表现形式，是一种编程语言中，描述数据类型的一种方式
sealed class LicenseStatus() {
    //这里因为QUALIFIED继承至LicenseStatus，是一个类，所以他可以有独有的状态属性
    class QUALIFIED(val cardNO:String) : LicenseStatus()
   //这两种状态下，因为还没有驾照，所以没有cardNO,无状态（也就是类没有属性），所以可以直接使用object对象
    object STUDYING : LicenseStatus()
    //这两种状态下，因为还没有驾照，所以没有cardNO,无状态（也就是类没有属性），所以可以直接使用object对象
    object EXPIRED : LicenseStatus()
}

class Driver() {
    fun checkStatus(status : LicenseStatus) :String {
        return when(status) {
            is LicenseStatus.QUALIFIED -> "当前驾照状态是合格的，驾照的编码是:${status.cardNO}"
            is LicenseStatus.STUDYING -> "当前正在学习驾照中"
            is LicenseStatus.EXPIRED -> "当前驾照已经过期啦"
        }
    }
}

fun main() {
    println(Driver().checkStatus(LicenseStatus.QUALIFIED("NO.123")))
    println(Driver().checkStatus(LicenseStatus.STUDYING))

}