package com.transsion.architecturemodule.base.ktx

import android.content.Context
import android.widget.Toast
import android.widget.Toast.makeText

fun Toast.showShort(ctx : Context,string : String) {
    makeText(ctx,string,Toast.LENGTH_SHORT)
}

fun Toast.showLong(ctx : Context,string : String) {
    makeText(ctx,string,Toast.LENGTH_LONG)
}