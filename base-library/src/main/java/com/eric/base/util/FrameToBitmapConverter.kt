package com.eric.base.util

import android.graphics.Bitmap
import android.graphics.Bitmap.Config
import org.bytedeco.javacv.Frame
import org.bytedeco.javacv.AndroidFrameConverter

object FrameToBitmapConverter {
    private val converter = AndroidFrameConverter()

    fun convert(frame: Frame?): Bitmap? {
        return frame?.let { converter.convert(it) }
    }
}
