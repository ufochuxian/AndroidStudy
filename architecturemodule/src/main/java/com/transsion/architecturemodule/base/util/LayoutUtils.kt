package com.transsion.architecturemodule.base.util

import android.text.TextUtils
import android.view.View
import java.util.Locale


object LayoutUtils {
    fun isRtl(): Boolean {
        return TextUtils.getLayoutDirectionFromLocale(Locale.getDefault()) == View.LAYOUT_DIRECTION_RTL
    }
}