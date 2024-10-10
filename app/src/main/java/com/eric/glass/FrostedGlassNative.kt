package com.eric.glass

import android.graphics.Bitmap

class FrostedGlassNative {
    companion object {
        init {
            System.loadLibrary("frosted-glass")
        }
    }

    external fun applyFrostedGlassEffect(bitmap: Bitmap, radius: Int)
}