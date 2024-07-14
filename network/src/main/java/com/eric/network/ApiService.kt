package com.eric.network

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiService {

    @FormUrlEncoded
    @POST("/user/register")
    suspend fun register(@FieldMap params: Map<String, String>): BaseResponse<UserData>

    @FormUrlEncoded
    @POST("/user/login")
    suspend fun login(@FieldMap params: Map<String, String>): BaseResponse<UserData>

}