package com.eric.kotlin

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ImageSpan
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import com.eric.androidstudy.R

class ExpandableTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {

    companion object {
        private const val MAX_COLLAPSED_LINES = 3
        private const val DEFAULT_ANIM_DURATION = 300
    }

    private var maxCollapsedLines: Int
    private var animationDuration: Int
    private var isExpanded = false
    private var isAnimating = false

    private var expandDrawable: Drawable?
    private var collapseDrawable: Drawable?

    private var spannableString: SpannableStringBuilder? = null
    private var originalText: String? = null
    private lateinit var expandImageSpan: ImageSpan
    private lateinit var collapseImageSpan: ImageSpan

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ExpandableTextView)
        maxCollapsedLines = typedArray.getInt(R.styleable.ExpandableTextView_maxCollapsedLines, MAX_COLLAPSED_LINES)
        animationDuration = typedArray.getInt(R.styleable.ExpandableTextView_animDuration, DEFAULT_ANIM_DURATION)
        expandDrawable = typedArray.getDrawable(R.styleable.ExpandableTextView_expandDrawable)
        collapseDrawable = typedArray.getDrawable(R.styleable.ExpandableTextView_collapseDrawable)
        typedArray.recycle()

        // 设置默认图标
        if (expandDrawable == null) {
            expandDrawable = ContextCompat.getDrawable(context, R.drawable.ic_arrow_up)
        }
        if (collapseDrawable == null) {
            collapseDrawable = ContextCompat.getDrawable(context, R.drawable.ic_arrow_down)
        }

        // 设置图标大小
        val iconSize = (textSize * 1.2f).toInt() // 稍大于文字
        expandDrawable?.setBounds(0, 0, iconSize, iconSize)
        collapseDrawable?.setBounds(0, 0, iconSize, iconSize)

        // 创建ImageSpan
        expandDrawable?.let {
            expandImageSpan = ImageSpan(it, ImageSpan.ALIGN_CENTER)

        }
        collapseDrawable?.let {
            collapseImageSpan = ImageSpan(it, ImageSpan.ALIGN_CENTER)
        }

        // 设置点击监听
        setOnClickListener {
            toggle()
        }
    }

    override fun setText(text: CharSequence?, type: BufferType?) {
        originalText = text?.toString()
        super.setText(text, type)

        // 文本设置后，检查是否需要处理省略和展开
        post {
            layout?.let {
                if (it.lineCount > maxCollapsedLines) {
                    setEllipsizedText()
                }
            }
        }
    }

    private fun toggle() {
        if (isAnimating) return

        isExpanded = !isExpanded
        setEllipsizedText()

        // 动画效果
        isAnimating = true
        val animator = ValueAnimator.ofFloat(0f, 1f)
        animator.duration = animationDuration.toLong()
        animator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                isAnimating = false
            }
        })
        animator.start()
    }

    private fun setEllipsizedText() {
        val text = originalText ?: return

        if (isExpanded) {
            // 展开状态 - 显示全部文本，后跟收起图标
            spannableString = SpannableStringBuilder(text).apply {
                append(" ")
                setSpan(collapseImageSpan, length - 1, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
            super.setText(spannableString, BufferType.SPANNABLE)
            super.setMaxLines(Integer.MAX_VALUE)
        } else {
            // 收起状态 - 截取部分文本，后跟省略号和展开图标
            layout?.let { layout ->
                val endIndex = layout.getLineEnd(maxCollapsedLines - 1)
                // 确保有足够空间放置省略号和图标，防止文本过短导致问题
                val safeEndIndex = (endIndex - 10).coerceAtLeast(0)
                val truncatedText = text.substring(0, safeEndIndex) + "... "

                spannableString = SpannableStringBuilder(truncatedText).apply {
                    setSpan(expandImageSpan, length - 1, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                }

                super.setText(spannableString, BufferType.SPANNABLE)
                super.setMaxLines(maxCollapsedLines)
            }
        }
    }

    // 提供一个方法检查文本是否需要展开/收起功能
    fun hasOverflowText(): Boolean {
        return layout?.let { it.lineCount > maxCollapsedLines } ?: false
    }

    // 手动设置展开状态
    fun setExpanded(expanded: Boolean) {
        if (isExpanded != expanded) {
            isExpanded = expanded
            setEllipsizedText()
        }
    }

    // 辅助函数转换dp
    private fun dp(value: Int): Int {
        return (value * resources.displayMetrics.density).toInt()
    }
}