package com.eric.base

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.RippleDrawable
import android.util.TypedValue
import android.view.View
import androidx.core.content.ContextCompat
import com.eric.baselibrary.R
import timber.log.Timber

fun View.setRippleBackground(
    context: Context,
    bgColor: Int,
    rippleColor: Int,
    cornerRadius: Float
) {
    this.isClickable = true
    this.isFocusable = true
    // 创建背景 drawable
    val cornerRadiusInPx = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP, cornerRadius, context.resources.displayMetrics
    )
    val backgroundDrawable = GradientDrawable().apply {
        shape = GradientDrawable.RECTANGLE
        setColor(ContextCompat.getColor(context, bgColor))

        cornerRadii = floatArrayOf(
            cornerRadiusInPx, cornerRadiusInPx, // Top left
            cornerRadiusInPx, cornerRadiusInPx, // Top right
            cornerRadiusInPx, cornerRadiusInPx, // Bottom right
            cornerRadiusInPx, cornerRadiusInPx  // Bottom left
        )
    }

    // 创建 RippleDrawable
    val maskDrawable = GradientDrawable().apply {
        shape = GradientDrawable.RECTANGLE
        setColor(R.color.black.toInt()) // 黑色遮罩
        cornerRadii = floatArrayOf(
            cornerRadiusInPx, cornerRadiusInPx,
            cornerRadiusInPx, cornerRadiusInPx,
            cornerRadiusInPx, cornerRadiusInPx,
            cornerRadiusInPx, cornerRadiusInPx
        )
    }

    val rippleDrawable = ContextCompat.getColorStateList(context, rippleColor)?.let {
        RippleDrawable(
            it,
            backgroundDrawable,
            maskDrawable
        )
    }

    // 设置背景
    this.background = rippleDrawable
}

fun View.setRippleForeground(
    context: Context,
    rippleColor: Int,
    cornerRadius: Float
) {
    this.isClickable = true
    this.isFocusable = true

    val cornerRadiusInPx = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP, cornerRadius, context.resources.displayMetrics
    )

    val maskDrawable = GradientDrawable().apply {
        shape = GradientDrawable.RECTANGLE
        setColor(Color.BLACK)
        cornerRadii = floatArrayOf(
            cornerRadiusInPx, cornerRadiusInPx,
            cornerRadiusInPx, cornerRadiusInPx,
            cornerRadiusInPx, cornerRadiusInPx,
            cornerRadiusInPx, cornerRadiusInPx
        )
    }
    val rippleDrawable = ContextCompat.getColorStateList(context, rippleColor)?.let {
        RippleDrawable(
            it,
            null,
            maskDrawable
        )
    }
    this.foreground = rippleDrawable
}

fun logTd(tag: String, msg: String) {
    Timber.tag(tag).d(msg)
}

fun logTi(tag: String, msg: String) {
    Timber.tag(tag).i(msg)
}

fun logTw(tag: String, msg: String) {
    Timber.tag(tag).w(msg)
}

fun logTe(tag: String, msg: String) {
    Timber.tag(tag).e(msg)
}

fun logTwtf(tag: String, msg: String) {
    Timber.tag(tag).wtf(msg)
}