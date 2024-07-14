package com.eric.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.observeOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class WanAndroidActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val wanAndroidViewModel = ViewModelProvider(this)[WanAndroidViewModel::class.java]


//        val liveData = wanAndroidViewModel.login("ufochuxian@", "wanandroidTest")
//        liveData.observe(this) {
//            Log.i("WanAndroidActivity","登陆成功:${it.data?.username}")
//        }

//        wanAndroidViewModel.register("ufochuxian123##","123456","123456").observe(this) {
//            if(it.errorCode == 0) {
//                println("注册成功:${it.data?.username}")
//            } else {
//                println("注册失败:${it.errorMsg}")
//            }
//        }

        lifecycleScope.launch {
            wanAndroidViewModel.loginOutFlow.collect { it ->
                if(it.errorCode == 0) {
                    println("登出成功:${it.errorCode}")
                }
            }
        }

        wanAndroidViewModel.loginOut()
    }
}