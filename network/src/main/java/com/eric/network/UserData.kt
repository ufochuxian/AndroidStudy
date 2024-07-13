package com.eric.network

import androidx.annotation.Keep

@Keep
class BaseResponse<T> {
    var data: T? = null
    var errorCode: Int = 0
    var errorMsg: String? = null
}

@Keep
class UserData {
    private var admin = false
    private var chapterTops: List<String>? = null
    private var coinCount = 0
    private var collectIds: List<String>? = null
    private var email: String? = null
    private var icon: String? = null
    private var id: Long = 0
    private var nickname: String? = null
    private var password: String? = null
    private var publicName: String? = null
    private var token: String? = null
    private var type = 0
    private var username: String? = null
    fun setAdmin(admin: Boolean) {
        this.admin = admin
    }

    fun getAdmin(): Boolean {
        return admin
    }

    fun setChapterTops(chapterTops: List<String>?) {
        this.chapterTops = chapterTops
    }

    fun getChapterTops(): List<String>? {
        return chapterTops
    }

    fun setCoinCount(coinCount: Int) {
        this.coinCount = coinCount
    }

    fun getCoinCount(): Int {
        return coinCount
    }

    fun setCollectIds(collectIds: List<String>?) {
        this.collectIds = collectIds
    }

    fun getCollectIds(): List<String>? {
        return collectIds
    }

    fun setEmail(email: String?) {
        this.email = email
    }

    fun getEmail(): String? {
        return email
    }

    fun setIcon(icon: String?) {
        this.icon = icon
    }

    fun getIcon(): String? {
        return icon
    }

    fun setId(id: Long) {
        this.id = id
    }

    fun getId(): Long {
        return id
    }

    fun setNickname(nickname: String?) {
        this.nickname = nickname
    }

    fun getNickname(): String? {
        return nickname
    }

    fun setPassword(password: String?) {
        this.password = password
    }

    fun getPassword(): String? {
        return password
    }

    fun setPublicName(publicName: String?) {
        this.publicName = publicName
    }

    fun getPublicName(): String? {
        return publicName
    }

    fun setToken(token: String?) {
        this.token = token
    }

    fun getToken(): String? {
        return token
    }

    fun setType(type: Int) {
        this.type = type
    }

    fun getType(): Int {
        return type
    }

    fun setUsername(username: String?) {
        this.username = username
    }

    fun getUsername(): String? {
        return username
    }
}