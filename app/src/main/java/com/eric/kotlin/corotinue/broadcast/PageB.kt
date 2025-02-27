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
class PageB {

     fun registerBroadcastByFlow() {
        CoroutineScope(Dispatchers.Main).launch {
            GlobalEvent.events.collect { msg ->

                handleEvent(msg)
            }
        }
    }

    private fun handleEvent(msg: Message) {
        Log.i("PageB","PageB received msg:${msg.content}")
    }
}