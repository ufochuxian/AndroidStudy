package com.eric.base.mgr

import android.content.Context

interface SpecialPermissionPollingHandler {
    fun onTick(context: Context)
    fun onTimeout(context: Context)
}
