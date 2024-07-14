package com.eric.ui

import com.eric.network.ApiService
import com.eric.network.BaseResponse
import com.eric.network.UserData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class UserRepository(private val apiService: ApiService) {
    fun login(userName: String, password: String): Flow<BaseResponse<UserData>> {
        return flow {
            val baseResponse = apiService.login(mapOf("username" to userName, "password" to password))
            emit(baseResponse)
        }.catch {
            emit(BaseResponse(null,-1,"请求出错了:${it.cause}"))
        }.flowOn(Dispatchers.IO)
    }
}