package com.eric.base.util

import android.content.Context
import android.graphics.Bitmap
import com.bumptech.glide.Glide
import com.bumptech.glide.load.Options
import com.bumptech.glide.load.ResourceDecoder
import com.bumptech.glide.load.engine.Resource
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapResource
import org.aomedia.avif.android.AvifDecoder
import java.io.InputStream
import java.nio.ByteBuffer

class AvifGlideDecoder(private val context: Context) : ResourceDecoder<InputStream, Bitmap> {

    override fun handles(source: InputStream, options: Options): Boolean {
        return true // 让 Glide 识别 AVIF 格式
    }

    override fun decode(source: InputStream, width: Int, height: Int, options: Options): Resource<Bitmap>? {
        return try {
            val byteArray = source.readBytes()
            val byteBuffer = ByteBuffer.wrap(byteArray) // 转换为 ByteBuffer

            if (!AvifDecoder.isAvifImage(byteBuffer)) {
                return null // 不是 AVIF 图片
            }

            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            val success = AvifDecoder.decode(byteBuffer, byteBuffer.remaining(), bitmap)

            if (!success) return null

            BitmapResource.obtain(bitmap, Glide.get(context).bitmapPool)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}