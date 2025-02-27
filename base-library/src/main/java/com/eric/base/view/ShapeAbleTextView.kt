package com.eric.base.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.eric.base.setRippleForeground
import com.eric.baselibrary.R
import timber.log.Timber

/**
 * @Author: jianxiang.chen
 * @Date 2021/08/21
 * 自定义Shape View
 *
 */

class ShapeAbleTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {

    private var strokeColor: Int = Color.TRANSPARENT
    private var rippleColor: Int = Color.TRANSPARENT
    private var strokeWidth: Float = 0f
    private var cornerRadius: Float = 0f
    private var solidColor: Int = Color.TRANSPARENT

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL_AND_STROKE
    }
    private val rectF = RectF()
    private val adjustedRectF = RectF()

    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.ShapeAbleTextView,
            0, 0
        ).apply {
            try {
                strokeColor = getColor(R.styleable.ShapeAbleTextView_strokeColor, Color.TRANSPARENT)
                rippleColor =  getColor(R.styleable.ShapeAbleTextView_rippleColor, Color.TRANSPARENT)
                strokeWidth = getDimension(R.styleable.ShapeAbleTextView_strokeWidth, 0f)
                cornerRadius = getDimension(R.styleable.ShapeAbleTextView_cornerRadius, 0f)
                solidColor = getColor(R.styleable.ShapeAbleTextView_solidColor, Color.TRANSPARENT)
            } finally {
                recycle()
            }
        }
        updateAdjustedRectF()
        paint.strokeWidth = strokeWidth

        if(rippleColor != Color.TRANSPARENT) {
            setRippleForeground(getContext(),R.color.colorTextHint,cornerRadius)
        }

    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        rectF.set(0f, 0f, w.toFloat(), h.toFloat())
        updateAdjustedRectF()
    }

    private fun updateAdjustedRectF() {
        val halfStrokeWidth = strokeWidth / 2
        adjustedRectF.set(
            rectF.left + halfStrokeWidth,
            rectF.top + halfStrokeWidth,
            rectF.right - halfStrokeWidth,
            rectF.bottom - halfStrokeWidth
        )
    }

    override fun onDraw(canvas: Canvas) {
        // 绘制填充背景
        paint.color = solidColor
        paint.style = Paint.Style.FILL
        canvas.drawRoundRect(adjustedRectF, cornerRadius, cornerRadius, paint)

        // 绘制边框
        paint.color = strokeColor
        paint.style = Paint.Style.STROKE
        canvas.drawRoundRect(adjustedRectF, cornerRadius, cornerRadius, paint)

        super.onDraw(canvas)
    }
}


