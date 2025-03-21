package com.eric.material

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class CoroutineScopeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    suspend fun fetchData() {
        coroutineScope {
            launch {
                // 启动第一个协程
            }
            launch {
                // 启动第二个协程
            }
        }
    }

    suspend fun fetchUserData(): String {
        return coroutineScope {
            delay(500)  // 模拟网络请求延迟
            val userInfo = "用户信息：Name=John, Age=28"
            println(userInfo)
            userInfo  // 返回用户信息字符串
        }
    }


    suspend fun fetchProductData(): String {
        return coroutineScope {
            delay(1000)
            // 模拟从网络获取商品数据的延迟
            println("获取商品信息")
            "获取商品信息"
        }
    }
}