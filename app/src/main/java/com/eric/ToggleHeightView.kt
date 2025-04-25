package com.eric

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.view.updateLayoutParams

class ToggleHeightView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {

    private val shortHeightDp = 100
    private val tallHeightDp = 300
    private var isExpanded = false

    private val label = TextView(context).apply {
        text = "点击我切换高度"
        textSize = 16f
        gravity = TEXT_ALIGNMENT_CENTER
        setPadding(40, 40, 40, 40)
        setOnClickListener {
            toggleHeight()
        }
    }

    init {
        addView(label)
        post {
            // 设置初始高度
            updateLayoutParams {
                height = dpToPx(shortHeightDp)
            }
        }
    }

    fun toggleHeight() {
        isExpanded = !isExpanded
        val newHeight = if (isExpanded) tallHeightDp else shortHeightDp
        label.text = if (isExpanded) "当前为高状态，点击缩小" else "当前为低状态，点击放大"
        updateLayoutParams {
            height = dpToPx(newHeight)
        }
    }

    private fun dpToPx(dp: Int): Int {
        return (dp * context.resources.displayMetrics.density).toInt()
    }
}
