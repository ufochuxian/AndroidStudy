package com.eric.base.media

import android.graphics.SurfaceTexture
import android.net.Uri
import android.os.Bundle
import android.view.Surface
import android.view.TextureView
import android.view.TextureView.SurfaceTextureListener
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.VideoSize
import androidx.media3.exoplayer.ExoPlayer
import jp.co.cyberagent.android.gpuimage.GPUImage
import jp.co.cyberagent.android.gpuimage.filter.GPUImageKuwaharaFilter
import jp.co.cyberagent.android.gpuimage.GPUImageView
import com.eric.baselibrary.R

class VideoPlayerWithFilterActivity : AppCompatActivity() {

    private lateinit var gpuImageView: GPUImageView
    private lateinit var player: ExoPlayer
    private lateinit var gpuImage: GPUImage
    private lateinit var textureView: TextureView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_with_filter)

        gpuImageView = findViewById(R.id.gpu_image_view)
        textureView = TextureView(this)

        // 初始化 GPUImage 并设置 KuwaharaFilter
        gpuImage = GPUImage(this)
        gpuImage.setFilter(GPUImageKuwaharaFilter()) // 设置滤镜

        // 初始化 ExoPlayer
        player = ExoPlayer.Builder(this).build()

        // 设置视频文件
        val mediaItem = MediaItem.fromUri(Uri.parse("https://media.w3.org/2010/05/sintel/trailer.mp4"))
        player.setMediaItem(mediaItem)


        // 设置视频宽高比监听
        player.addListener(object : Player.Listener {
            override fun onVideoSizeChanged(videoSize: VideoSize) {
                // 获取视频的宽高比
                val videoWidth = videoSize.width
                val videoHeight = videoSize.height

                // 调整 TextureView 和 GPUImageView 的宽高比
                adjustAspectRatio(videoWidth, videoHeight)
            }
        })

        // 设置 TextureView 用于渲染视频帧
        textureView.surfaceTextureListener = object : SurfaceTextureListener {
            override fun onSurfaceTextureAvailable(surfaceTexture: SurfaceTexture, width: Int, height: Int) {
                // 使用 Surface 绑定视频输出
                val surface = Surface(surfaceTexture)
                player.setVideoSurface(surface)

                // 开始播放
                player.prepare()
                player.play()
            }

            override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture, width: Int, height: Int) {
                // 处理 Surface 尺寸变化
            }

            override fun onSurfaceTextureDestroyed(surface: SurfaceTexture): Boolean {
                return true
            }

            override fun onSurfaceTextureUpdated(surface: SurfaceTexture) {
                // 每次 TextureView 更新时，获取当前帧的 Bitmap 并传递给 GPUImage 进行滤镜处理
                val currentFrameBitmap = textureView.bitmap
                if (currentFrameBitmap != null) {
                    gpuImage.setImage(currentFrameBitmap)  // 设置当前帧
                    gpuImageView.setImage(gpuImage.bitmapWithFilterApplied)  // 显示滤镜处理后的帧
                }
            }
        }

        // 将 TextureView 动态添加到 GPUImageView 上方用于视频渲染
        gpuImageView.addView(textureView)
    }

    // 调整 TextureView 和 GPUImageView 的尺寸以适应视频的宽高比
    private fun adjustAspectRatio(videoWidth: Int, videoHeight: Int) {
        val viewWidth = gpuImageView.width
        val viewHeight = gpuImageView.height

        val videoAspectRatio = videoWidth.toFloat() / videoHeight
        val viewAspectRatio = viewWidth.toFloat() / viewHeight

        var newWidth = viewWidth
        var newHeight = viewHeight

        if (videoAspectRatio > viewAspectRatio) {
            // 视频的宽高比大于视图的宽高比，以宽度为基准调整高度
            newHeight = (viewWidth / videoAspectRatio).toInt()
        } else {
            // 视频的宽高比小于视图的宽高比，以高度为基准调整宽度
            newWidth = (viewHeight * videoAspectRatio).toInt()
        }

        // 更新 TextureView 和 GPUImageView 的布局参数
        val layoutParams = FrameLayout.LayoutParams(newWidth, newHeight)
        textureView.layoutParams = layoutParams
        gpuImageView.layoutParams = layoutParams
    }

    override fun onDestroy() {
        super.onDestroy()
        player.release()  // 释放播放器资源
    }
}
