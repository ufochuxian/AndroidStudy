package com.eric.ui

import com.eric.network.ApiService
import com.eric.network.BaseResponse
import com.eric.network.UserData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart

class UserRepository(private val apiService: ApiService) {
    fun login(userName: String, password: String): Flow<BaseResponse<UserData>> {
        return flow<BaseResponse<UserData>> {
            val response = apiService.login(mapOf("username" to userName, "password" to password))
            println("${response.data?.getUsername()}")
        }.onStart {
            println("onLoading.........")
        }.catch {
            println("请求登陆发生错误:${it.cause}")
        }
    }
}