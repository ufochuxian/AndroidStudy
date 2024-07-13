package com.eric.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eric.network.ApiClient
import com.eric.network.UserData
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class WanAndroidViewModel : ViewModel() {

    val userDataLiveData: MutableLiveData<UserData> = MutableLiveData()

    suspend fun login(userName: String, password: String) {
        viewModelScope.launch {
            val userRepository = UserRepository(ApiClient.apiService)
            val responseFlow = userRepository
                .login(userName, password)
            responseFlow.catch {
                println("请求发生错误了")
            }
                .collect {
                    it.data?.let { data ->
                        userDataLiveData.value = data
                    }
                }
        }
    }

}