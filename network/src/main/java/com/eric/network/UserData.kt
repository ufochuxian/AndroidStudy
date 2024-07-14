package com.eric.network

import androidx.annotation.Keep

@Keep
data class BaseResponse<T>(
    val data: T? = null,
    val errorCode: Int? = -1,
    val errorMsg: String? = ""
)

@Keep
data class UserData(
    val admin: Boolean?,
    val chapterTops: List<Any>,
    val coinCount: Int,
    val collectIds: List<Any>,
    val email: String,
    val icon: String,
    val id: Int,
    val nickname: String,
    val password: String,
    val publicName: String,
    val token: String,
    val type: Int,
    val username: String?
)

enum class Result(val code:Int) {
    SUCCESS(0),
    FAIL(-1)
}