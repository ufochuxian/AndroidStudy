package com.eric.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.flowWithLifecycle
import com.eric.kotlin.corotinue.User
import com.eric.network.ApiClient
import com.eric.network.BaseResponse
import com.eric.network.UserData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class WanAndroidViewModel : ViewModel() {

    fun login(userName: String, password: String): LiveData<BaseResponse<UserData>> =
        UserRepository(ApiClient.apiService)
            .login(userName, password).asLiveData()

    fun register(
        userName: String,
        password: String,
        repassWord: String
    ): LiveData<BaseResponse<UserData>> = UserRepository(ApiClient.apiService).register(
        userName,
        password,
        repassWord
    ).asLiveData()
}