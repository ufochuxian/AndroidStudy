package com.eric.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class WanAndroidActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val wanAndroidViewModel = ViewModelProvider(this)[WanAndroidViewModel::class.java]


        wanAndroidViewModel.userDataLiveData.observe(this) {
            println("hello ${it.getUsername()},登陆成功！ ")
        }

        lifecycleScope.launch {
            wanAndroidViewModel.login("ufochuxian@","wanandroidTest")
        }
}
}