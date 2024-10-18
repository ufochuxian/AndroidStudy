package com.eric.kotlin.corotinue.broadcast

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**

 * @Author: chen

 * @datetime: 2024/7/8

 * @desc:

 */
class PageA {


    fun registerBroadcastByFlow() {
        CoroutineScope(Dispatchers.Main).launch {
            GlobalEvent.events.collect { event ->
                handleEvent(event)
            }
        }
    }

    private fun handleEvent(msg: Message) {
        Log.i("PageA","PageA received msg:${msg.content}")
    }
}