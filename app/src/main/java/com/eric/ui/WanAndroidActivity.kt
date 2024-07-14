package com.eric.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.work.Operation.State.SUCCESS
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.launch

class WanAndroidActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val wanAndroidViewModel = ViewModelProvider(this)[WanAndroidViewModel::class.java]


//        val liveData = wanAndroidViewModel.login("ufochuxian@", "wanandroidTest")
//        liveData.observe(this) {
//            Log.i("WanAndroidActivity","登陆成功:${it.data?.username}")
//        }

        wanAndroidViewModel.register("ufochuxian123##","123456","123456").observe(this) {
            if(it.errorCode == 0) {
                println("注册成功:${it.data?.username}")
            } else {
                println("注册失败:${it.errorMsg}")
            }
        }
}
}