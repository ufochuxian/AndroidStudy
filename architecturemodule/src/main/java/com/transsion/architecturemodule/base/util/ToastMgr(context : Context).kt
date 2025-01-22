package com.transsion.architecturemodule.base.util

import android.content.Context
import android.widget.Toast

class ToastMgr(val context : Context) {
    private val mShortToast = Toast.makeText(context, "", Toast.LENGTH_SHORT)


    fun showShort(msg: String) {
        context.let {
            mShortToast.duration = Toast.LENGTH_SHORT
            mShortToast.setText(msg)
            mShortToast.show()
        }
    }

    fun showShort(msgID: Int) {
        context.let {
            mShortToast.duration = Toast.LENGTH_SHORT
            mShortToast.setText(msgID)
            mShortToast.show()
        }
    }
}