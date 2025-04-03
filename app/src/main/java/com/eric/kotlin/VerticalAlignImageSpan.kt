package com.eric.kotlin

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.text.style.DynamicDrawableSpan

class VerticalAlignImageSpan(
    private val drawable: Drawable,
    private val verticalOffset: Int = 0 // 可以添加微调偏移量
) : DynamicDrawableSpan() {

    override fun getDrawable(): Drawable {
        return drawable
    }

    override fun getSize(
        paint: Paint, text: CharSequence?, start: Int, end: Int, fm: Paint.FontMetricsInt?
    ): Int {
        val rect = drawable.bounds
        if (fm != null) {
            val fontHeight = fm.descent - fm.ascent
            val drHeight = rect.bottom - rect.top
            
            // 计算使drawable垂直居中所需的ascent和descent
            val centerY = fontHeight / 2 - drHeight / 2
            
            fm.ascent = -drHeight - centerY + verticalOffset
            fm.descent = centerY + verticalOffset
            
            fm.top = fm.ascent
            fm.bottom = fm.descent
        }
        return rect.right
    }

    override fun draw(
        canvas: Canvas, text: CharSequence, start: Int, end: Int,
        x: Float, top: Int, y: Int, bottom: Int, paint: Paint
    ) {
        val fm = paint.fontMetricsInt
        // 计算图片垂直居中的Y坐标
        val fontHeight = fm.descent - fm.ascent
        val drHeight = drawable.bounds.height()
        val centerY = y + (fontHeight - drHeight) / 2 + verticalOffset
        
        canvas.save()
        canvas.translate(x, centerY.toFloat())
        drawable.draw(canvas)
        canvas.restore()
    }
}