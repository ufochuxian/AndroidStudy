package com.transsion.architecturemodule.base.util

import java.util.Timer
import java.util.TimerTask

class TimerMgr {

    private val timer = Timer()
    fun startAutoScroll(lambada: () -> Unit) {
        timer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                lambada()
            }
        }, 3000, 3000)
    }

    fun stopTimer() {
        timer.cancel()
    }
}