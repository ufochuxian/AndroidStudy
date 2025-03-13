import android.content.Context
import android.graphics.Bitmap
import com.bumptech.glide.Glide
import com.bumptech.glide.load.Options
import com.bumptech.glide.load.ResourceDecoder
import com.bumptech.glide.load.engine.Resource
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapResource
import com.bumptech.glide.load.resource.bitmap.DownsampleStrategy
import com.bumptech.glide.load.resource.bitmap.TransformationUtils
import com.bumptech.glide.load.resource.bitmap.VideoDecoder
import com.bumptech.glide.util.Preconditions
import com.eric.base.util.FrameToBitmapConverter
import org.bytedeco.javacv.FFmpegFrameGrabber
import java.io.ByteArrayInputStream
import java.io.InputStream

class FFmpegAvifDecoder(private val context: Context) : ResourceDecoder<InputStream, Bitmap> {

    override fun handles(source: InputStream, options: Options): Boolean = true

    override fun decode(
        source: InputStream, width: Int, height: Int, options: Options
    ): Resource<Bitmap>? {
        val avifData = source.readBytes()
        val frame = FFmpegFrameGrabber(ByteArrayInputStream(avifData)).use { grabber ->
            grabber.start()
            grabber.grabImage() // 获取第一帧
        }

        val bitmap = FrameToBitmapConverter.convert(frame) ?: return null
        val bitmapPool: BitmapPool = Glide.get(context).bitmapPool

        return BitmapResource.obtain(bitmap, bitmapPool)
    }
}
