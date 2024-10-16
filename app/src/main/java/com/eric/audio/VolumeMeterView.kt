package com.eric.audio

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class VolumeMeterView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var volume: Float = 0f
    private val paint = Paint().apply {
        color = Color.BLUE
        style = Paint.Style.FILL
    }

    fun updateVolume(newVolume: Float) {
        volume = newVolume
        invalidate()  // 触发重绘
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // 基于音量绘制音量条
        val barHeight = height.toFloat()
        val barWidth = (width * (volume / 32767f)).coerceAtMost(width.toFloat())

        canvas.drawRect(0f, 0f, barWidth, barHeight, paint)
    }
}
