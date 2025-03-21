package com.eric.material

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ImageSpan
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import com.eric.androidstudy.R
import com.eric.androidstudy.databinding.ViewExpandableTextBinding

class ExpandableTextView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private var isExpanded = false
    private val collapsedMaxLines = 2
    private var fullText: String = ""

    private val binding: ViewExpandableTextBinding =
        ViewExpandableTextBinding.inflate(LayoutInflater.from(context), this)

    private val animationDuration = 300L  // 动画持续时间

    init {
        setupView()
    }

    private fun setupView() {
        binding.expandableTextView.setOnClickListener { toggle() }
    }

    fun setText(text: String) {
        fullText = text
        binding.expandableTextView.text = text

        // 初始化为收起状态
        binding.expandableTextView.maxLines = collapsedMaxLines
        isExpanded = false

        // 延迟检查文本是否超过两行
        binding.expandableTextView.post { checkIfTextExceedsMaxLines() }
    }

    @SuppressLint("LogNotTimber")
    private fun checkIfTextExceedsMaxLines() {
        binding.expandableTextView.maxLines = Integer.MAX_VALUE  // 显示完整文本以进行检测
        val layout = binding.expandableTextView.layout

        if (layout == null) {
            Log.w("ExpandableTextView", "Layout is null. Trying again after post.")
            binding.expandableTextView.post { checkIfTextExceedsMaxLines() }
            return
        }

        val lineCount = layout.lineCount
        Log.d("ExpandableTextView", "TextView Line Count: $lineCount, Collapsed Max Lines: $collapsedMaxLines")

        val isLastLineTruncated = if (lineCount >= collapsedMaxLines) {
            val lastLineIndex = collapsedMaxLines - 1
            val lastLineEnd = layout.getLineEnd(lastLineIndex)
            val lastLineWidth = layout.getLineWidth(lastLineIndex)
            val textViewWidth = binding.expandableTextView.width

            Log.d("ExpandableTextView", "Last Line End Index: $lastLineEnd, TextView Width: $textViewWidth, Last Line Width: $lastLineWidth")

            lastLineWidth >= textViewWidth || lineCount > collapsedMaxLines
        } else false

        if (lineCount > collapsedMaxLines || isLastLineTruncated) {
            Log.d("ExpandableTextView", "Text exceeds $collapsedMaxLines lines or is truncated, displaying arrow.")
            binding.expandableTextView.maxLines = collapsedMaxLines
            binding.expandableTextView.text = createCollapsedText()
        } else {
            Log.d("ExpandableTextView", "Text does NOT exceed $collapsedMaxLines lines, hiding arrow.")
        }

        updateArrowIcon()
    }

    private fun createCollapsedText(): SpannableString {
        val layout = binding.expandableTextView.layout ?: return SpannableString(fullText)
        val lastLineIndex = collapsedMaxLines - 1
        val endIndex = layout.getLineEnd(lastLineIndex)
        val displayText = fullText.substring(0, endIndex).trim() + "..."

        val spannableString = SpannableString("$displayText ")
        val arrowDrawable = ContextCompat.getDrawable(context, R.drawable.ic_arrow_down)
        arrowDrawable?.setBounds(0, 0, arrowDrawable.intrinsicWidth, arrowDrawable.intrinsicHeight)
        val imageSpan = ImageSpan(arrowDrawable!!, ImageSpan.ALIGN_BASELINE)

        spannableString.setSpan(
            imageSpan,
            spannableString.length - 1,
            spannableString.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        return spannableString
    }

    private fun toggle() {
        if (isExpanded) collapse() else expand()
    }

    private fun expand() {
        isExpanded = true
        binding.expandableTextView.maxLines = Integer.MAX_VALUE

        val startHeight = binding.expandableTextView.height
        measure(
            MeasureSpec.makeMeasureSpec(binding.expandableTextView.width, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
        )
        val endHeight = binding.expandableTextView.measuredHeight

        animateHeight(startHeight, endHeight)
        binding.expandableTextView.text = fullText
        updateArrowIcon()
    }

    private fun collapse() {
        isExpanded = false
        binding.expandableTextView.maxLines = collapsedMaxLines

        val startHeight = binding.expandableTextView.height
        measure(
            MeasureSpec.makeMeasureSpec(binding.expandableTextView.width, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
        )
        val endHeight = binding.expandableTextView.measuredHeight

        animateHeight(startHeight, endHeight)
        binding.expandableTextView.text = createCollapsedText()
        updateArrowIcon()
    }

    private fun animateHeight(startHeight: Int, endHeight: Int) {
        val animator = ValueAnimator.ofInt(startHeight, endHeight)
        animator.addUpdateListener { animation ->
            val value = animation.animatedValue as Int
            binding.expandableTextView.layoutParams.height = value
            binding.expandableTextView.requestLayout()
        }
        animator.interpolator = AccelerateDecelerateInterpolator()
        animator.duration = animationDuration
        animator.start()
    }

    private fun updateArrowIcon() {
        if (isExpanded) {
            binding.expandableTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_up, 0)
        }
    }
}
