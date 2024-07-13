package com.eric.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.launch

class WanAndroidActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val wanAndroidViewModel = ViewModelProvider(this)[WanAndroidViewModel::class.java]


        val liveData = wanAndroidViewModel.login("ufochuxian@", "wanandroidTest")
        liveData.observe(this) {
            Log.i("WanAndroidActivity","登陆成功:${it.data?.getUsername()}")
        }
}
}