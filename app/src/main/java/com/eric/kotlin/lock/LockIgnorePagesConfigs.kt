package com.eric.kotlin.lock

object LockIgnorePagesConfigs {

    val lockIgnorePages = arrayListOf<String>()

    init {
        lockIgnorePages.add("com.eric.kotlin.lock.PageAActivity")
    }

    fun isIgnorePage(pageName: String) : Boolean {
        return lockIgnorePages.contains(pageName)
    }
}