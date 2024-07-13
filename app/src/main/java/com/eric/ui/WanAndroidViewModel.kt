package com.eric.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.eric.network.ApiClient
import com.eric.network.BaseResponse
import com.eric.network.UserData

class WanAndroidViewModel : ViewModel() {

    fun login(userName: String, password: String): LiveData<BaseResponse<UserData>> =
        UserRepository(ApiClient.apiService)
            .login(userName, password).asLiveData()
}