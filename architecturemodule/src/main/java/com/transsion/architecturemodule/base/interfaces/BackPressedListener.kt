package com.transsion.architecturemodule.base.interfaces

interface BackPressedListener {
    fun handleBackPressed(): Boolean

    fun getBackFromPage() : String? {
        return null
    }

    fun onBackPressClick(param : String?) {

    }
}