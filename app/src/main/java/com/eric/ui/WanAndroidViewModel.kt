package com.eric.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.eric.network.ApiClient
import com.eric.network.BaseResponse
import com.eric.network.UserData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class WanAndroidViewModel : ViewModel() {

    val loginOutFlow = MutableStateFlow<BaseResponse<Unit>>(BaseResponse())
    val loginOutStateFlow : StateFlow<BaseResponse<Unit>> get() = loginOutFlow

    fun login(userName: String, password: String): LiveData<BaseResponse<UserData>> =
        UserRepository(ApiClient.apiService)
            .login(userName, password).asLiveData()

    fun loginOut() {
        viewModelScope.launch {
            flow<BaseResponse<Unit>> {
                val response = UserRepository(ApiClient.apiService).loginOut()
                loginOutFlow.value = response
            }.flowOn(Dispatchers.IO).collect {

            }
        }
    }

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