package com.eric.task

import androidx.lifecycle.MutableLiveData

class BroadCastViewModel : BaseViewModel() {
    var resultData = MutableLiveData<BroadCastResult?>()
}

data class BroadCastResult(val code: Int, val msg: String)