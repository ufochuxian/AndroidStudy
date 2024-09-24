package com.eric.media

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

data class ImageCompressionConfig(
    val compressionRatio: Float = 1.0f, // 压缩比例，默认1.0表示不压缩
    val quality: Int = 75 // 图像质量，默认80
)

class ImageCompressor(private val context: Context) {

    suspend fun compressImage(
        inputFile: File, 
        outputFile: File, 
        config: ImageCompressionConfig = ImageCompressionConfig()
    ): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                // 计算压缩后的尺寸
                val options = BitmapFactory.Options().apply {
                    inJustDecodeBounds = true // 仅获取图片尺寸，不加载图片内容
                }
                BitmapFactory.decodeFile(inputFile.absolutePath, options)
                val originalWidth = options.outWidth
                val originalHeight = options.outHeight

                val newWidth = (originalWidth * config.compressionRatio).toInt()
                val newHeight = (originalHeight * config.compressionRatio).toInt()

                // 使用Glide压缩图像
                Glide.with(context)
                    .asBitmap()
                    .load(inputFile)
                    .override(newWidth, newHeight) // 根据比例设置新的尺寸
                    .encodeQuality(config.quality) // 根据配置设置质量
                    .into(object : CustomTarget<Bitmap>() {
                        override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                            FileOutputStream(outputFile).use { outputStream ->
                                resource.compress(Bitmap.CompressFormat.JPEG, config.quality, outputStream)
                            }
                        }

                        override fun onLoadCleared(placeholder: Drawable?) {}
                    })

                true // 压缩成功
            } catch (e: Exception) {
                e.printStackTrace()
                false // 压缩失败
            }
        }
    }
}
