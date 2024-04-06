package com.eric.kotlin

import android.provider.Telephony.Mms.Addr
import android.util.Log
import java.util.Locale

/**

 * @Author: chen

 * @datetime: 2024/4/4

 * @desc:

 */

//这是一个匿名函数
val connectFun :(s1:String,s2:String) -> String = { s1, s2 ->
    s1.plus(s2)
}


fun main() {
    val value = 5
    println(value)

    /* in关键字 */
    val age = 19
    if(age in 0..18) {
        println("未成年")
    } else {
        println("成年人")
    }

    //when关键字
    val shoesLogo = "nike"

    when(shoesLogo) {
        "nike" -> println("这是耐克鞋子")
        "lining"-> println("这是李宁鞋子")
        else -> println("这我还不认识的品牌")
    }

    // for关键字

    for(x in 1..10) {
        if(x == 9) {
            continue
        } else {
            println(x)
        }
    }


    //`` 用来处理一些关键字的命名，不好调用的问题
    Test.`is`()

    sayHello("yaoming")

    //匿名函数
    println(connectFun("hello "," world"))

    //具名函数
    println(detailConnectFunction("a","b","c"))

    //使用lambada表达式的地方，也可以使用函数引用
    println(compare(2,2, ::connect))

    //自带的一些lambada
    val list = listOf("a","b","c","d")

    val jointedString = list.fold("initial") { acc: String, s: String ->
        //acc = acc + plus //注意这里不能写成这样，这样会认为返回值为unit,而这里的Lambada需要的返回值是String
        acc.plus(s)
    }
    println(jointedString)

    //省略括号的lambada表示法
    run { println("这是只有一个参数，并且参数还是lambada的形式的时候，可以完全省略圆括号") }

    //如果不省略的话
    run ({ println("这是只有一个参数，并且参数还是lambada的形式的时候，可以完全省略圆括号") })

    //高阶函数，使用的一些例子
    val names = listOf("yaoming","ataisite","damahou","li","haha","a")
    val newNames = names.filterNot { it.length > 4 }.sortedBy { it }.map { it.toUpperCase() }
    println(names)
    println(newNames)

    //空合并操作符

    var isNullString = null
    var x = isNullString?:"butterflay"
    println(isNullString)

    //字符串相关操作

    val strs = "'jack' is playing basketball"

    val name = strs.substring(strs.indexOf("\'") + 1 until strs.lastIndexOf("\'") )
    println(name)

    //解构操作 这里的使用圆括号
    val domainString = "prefix,domain,name"
    val (prfix,domain,n) = domainString.split(",")
    println("$prfix,$domain,$n")

    //字符串,正则表达式，匹配，并可以跟一个lambda表达式，进行相应的替换
    val tips = "this is a top car"
    val newTips = tips.replace(Regex("[top]")) {
        when(it.value) {
            "t" -> "l"
            "o" -> "o"
            "p" -> "w"
            else -> it.value
        }
    }
    println("newTips:$newTips")


    //比较两个对象
    val name1:String = "jason"
    val name2:String = "jason"

    //因为在java中，字符串存储于字符串常量池，如果两个字符串内容一样的话，那么就意味着他们指向的是常量池的同一个对象，所以相等
    println(name1 === name2)

    val name3 = "JASON"
    val name4 = name2.capitalize(Locale.ROOT)

    //这里尽管name4转换之后,也变成了"JASON",但是由于在java中字符串内容是不可变的，这里如果调用capitalize方法之后
    //实际的操作是在常量池，重新开辟了空间，生成了一个新的包含内容"JASON"的对象，所以和name3是不同的对象
    println(name3 === name4)

    println(sayWelcome(null))

    println(sayWelcome("yaoming"))

    //标准库中函数的区别
    /**
     * apply函数，会有一个字面接收者，然后在lambada内部，可以直接使用这个接收者的函数，返回的也是这个“字面接收者”
     *
     * let函数，和apply的区别是返回类型不一样，默认把函数内部的最后一行进行返回
     *
     * also函数作用和let函数类似，都有一个“字面接收者”，区别是let返回lambada计算结果，而also返回的是原始“字面接收者”，所以特别适合对原始对象的链式调用
     *
     * run函数，和apply的区别是，默认返回lambada的运算结果，不返回“字面接收者”，并且run函数，可以执行“函数引用”
     *
     * with函数和run函数类似，区别是with函数，需要把“字面接收者”，作为第一个参数，传入
     *
     * takeif函数，这个函数参数会接受一个lambada表达式，如果这个lambada表达式的计算结果true，那么就会返回"字面接收者"，如果为false，那么就返回null，结合对于函数返回结果的判空操作
     * 那么就可以做很多事情啦
     *
     */

    //run函数，可以使用函数引用，作为参数
    (if("abcdefghigklmna".run (::nameTooLoginNotice)) "you name is too long" else "you name is ok").run { logi("nameCheck",this) }
    //这个例子中，第二个使用了run函数，所以接着的also就可以打印出lmbada的计算结果啦
    "abcdefghigklmna".also(::println).run (::nameTooLoginNotice).also(::println)

    //with函数和run函数类似，区别是with函数，需要把“字面接收者”，作为第一个参数，传入
    with("abcdefghigklmna",::println)

    val dnsNames = listOf("a","b","c","d","e","f")
    //这里如果传入的字符是包含其中的，那么就可以打印出来list，如果不包含，那么就打印不出来啦
    dnsNames.takeIf { it.contains("g")}?.run (::println)


    /**
     * 集合相关
     * list set map array
     *
     */

    //只读list
    val onlyReadList = listOf("1","2","3")

    //可变（内容可变）的list
    val mutableList = mutableListOf("yao","li","fang","fang")

//    //运算符重载，等同于 add remove方法
//    mutableList += "zheng"
//    mutableList -= "yao"
//    println(mutableList)
//
//    //提供了快捷函数，可以去除重复元素
//    mutableList.distinct().run(::println)

    //提供了快捷函数，list和set可以互相转换
    val toMutableList = mutableList.toSet().toMutableList()
    val result = toMutableList.run { add("haha") }
    //这里的toMutableList之后，会生成一个新的list,所以紧接着的add方法，添加到的是这个新的集合，这个需要注意下
    println(toMutableList)

    toMutableList.getOrNull(8).run(::println)
    toMutableList.getOrElse(10) { "defaultValue"
    }.run (::println)

    //    val {"one","two","three","eight"} = toMutableList

    //这里注意结构操作的写法，不用花括号，圆括号，并且起的参数，不需要双引号进行括号起啦
    val (one,two,three,eight) = toMutableList



    //map
    val map = mapOf("one" to "1","Two" to "2","Three" to "3")

    val mutableMap = mutableMapOf("one" to "1","Two" to "2","Three" to "3")

    mutableMap += "eight" to "8"

    println(mutableMap)






}

fun logi(tag: String,message:String) {
    println("$tag,$message")
}

fun nameTooLoginNotice(name:String):Boolean{
    return name.length > 10
}



fun connect(a:Any ,b: Any) :Boolean {
    return  a == b
}


//自定义一个比较任意两个元素的，高阶函数（函数式变成）
//这里是具名函数，所以一定要使用return显示返回，如果是匿名函数，可以不用
fun compare(x1:Any,x2:Any,connect:(a:Any,b:Any)->Boolean):Boolean {
    return connect(x1,x2)
}

fun currentNetWorkTools(type:String) {
    val toolsTemp :(type : String) ->String = {
        "当前正在使用的是${type}的框架"
    }
    toolsTemp(type)
}

fun sayWelcome(name:String?) :String {
    //函数类型可空，如果需要使用这种表达式的话，这里有个小细节，需要注意下就是?:,这两个符号要靠在一起，否则会有编译报错
    return name?.let { "Welcome $it" } ?: "Please type you name first"
}
