package com.eric.base.view

import android.content.Context
import android.util.AttributeSet
import android.view.SurfaceView

class AspectRatiSurfaceView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : SurfaceView(context, attrs, defStyleAttr) {

    private var videoWidth = 0
    private var videoHeight = 0

    // 调整宽高比
    fun adjustAspectRatio(videoWidth: Int, videoHeight: Int) {
        this.videoWidth = videoWidth
        this.videoHeight = videoHeight
        requestLayout()  // 触发布局调整
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        if (videoWidth == 0 || videoHeight == 0) {
            setMeasuredDimension(widthMeasureSpec, heightMeasureSpec)
            return
        }

        val viewWidth = MeasureSpec.getSize(widthMeasureSpec)
        val viewHeight = MeasureSpec.getSize(heightMeasureSpec)
        val aspectRatio = videoWidth.toFloat() / videoHeight

        var newWidth = viewWidth
        var newHeight = (viewWidth / aspectRatio).toInt()

        if (newHeight > viewHeight) {
            // 如果高度超过了视图的高度，则适配高度
            newHeight = viewHeight
            newWidth = (newHeight * aspectRatio).toInt()
        }

        setMeasuredDimension(newWidth, newHeight)
    }
}
