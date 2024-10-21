package com.eric.base.media

import android.os.Bundle
import android.view.SurfaceHolder
import android.view.SurfaceView
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.VideoSize
import androidx.media3.exoplayer.ExoPlayer
import com.eric.baselibrary.R


class VideoPlayerSurfaceActivity : AppCompatActivity(), SurfaceHolder.Callback {

    private lateinit var surfaceView: SurfaceView
    private lateinit var player: ExoPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_surface)

        surfaceView = findViewById(R.id.surface_view)

        // 设置 SurfaceHolder 的回调监听
        surfaceView.holder.addCallback(this)

        // 初始化 ExoPlayer
        player = ExoPlayer.Builder(this).build()

        // 播放视频
        val mediaItem = MediaItem.fromUri("https://media.w3.org/2010/05/sintel/trailer.mp4")
        player.setMediaItem(mediaItem)

        // 监听视频的宽高变化
        player.addListener(object : Player.Listener {
            override fun onVideoSizeChanged(videoSize: VideoSize) {
                // 根据视频的实际宽高比调整 SurfaceView 的尺寸
                adjustSurfaceViewSize(videoSize.width, videoSize.height)
            }
        })
        player.prepare()
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        // 获取 Surface 并与 ExoPlayer 绑定
        val surface = holder.surface
        player.setVideoSurface(surface)

        // 开始播放
        player.play()
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        // Surface 大小或格式发生变化时处理
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        // Surface 销毁时停止播放
        player.setVideoSurface(null)
    }

    // 调整 SurfaceView 尺寸的方法
    private fun adjustSurfaceViewSize(videoWidth: Int, videoHeight: Int) {
        val viewWidth = surfaceView.width
        val viewHeight = surfaceView.height

        val aspectRatio = videoWidth.toFloat() / videoHeight.toFloat()

        // 计算新的宽高，保持宽高比
        var newWidth = viewWidth
        var newHeight = (viewWidth / aspectRatio).toInt()

        if (newHeight > viewHeight) {
            // 如果新的高度超过了视图的高度，则以视图高度为基准调整宽度
            newHeight = viewHeight
            newWidth = (viewHeight * aspectRatio).toInt()
        }

        // 更新 SurfaceView 的布局参数
        val layoutParams = surfaceView.layoutParams
        layoutParams.width = newWidth
        layoutParams.height = newHeight
        surfaceView.layoutParams = layoutParams
    }

    override fun onDestroy() {
        super.onDestroy()
        // 释放播放器资源
        player.release()
    }
}
