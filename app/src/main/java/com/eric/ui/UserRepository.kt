package com.eric.ui

import com.eric.network.ApiService
import com.eric.network.BaseResponse
import com.eric.network.UserData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart

class UserRepository(private val apiService: ApiService) {
    suspend fun login(userName: String, password: String): Flow<BaseResponse<UserData>> = flow {
        apiService.login(mapOf("userName" to userName, "password" to password)).onStart {
            println("onLoading.........")
        }.catch {
            println("登陆发生错误:${it}")
        }
    }
}