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
            val baseResponse =
                apiService.login(mapOf("username" to userName, "password" to password))
            emit(baseResponse)
        }.catch {
            emit(BaseResponse(null, -1, "请求出错了:${it.cause}"))
        }.flowOn(Dispatchers.IO)

    }

    fun register(
        userName: String,
        password: String,
        rePassword: String
    ): Flow<BaseResponse<UserData>> {
        return flow<BaseResponse<UserData>> {

            //因为flow内部实现是“冷流”，这里在请求完成后，一定要通过emit方法发送出去，外部才能收到。（调用collect方法，也是一个道理，内部也是emit方法发送的结果）
            emit(apiService.register(
                mapOf(
                    "username" to userName,
                    "password" to password,
                    "repassword" to rePassword
                )
            ))
        }.catch {
            emit(BaseResponse(null, -1, "注册出错了:${it.cause}"))
        }
            .flowOn(Dispatchers.IO)
    }
}