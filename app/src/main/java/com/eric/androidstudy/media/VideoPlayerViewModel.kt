package com.eric.androidstudy.media

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.VideoSize
import androidx.media3.exoplayer.ExoPlayer

class VideoPlayerViewModel(application: Application) : AndroidViewModel(application) {

    private val _player = MutableLiveData<ExoPlayer?>()
    val player: LiveData<ExoPlayer?> get() = _player

    private val _videoAspectRatio = MutableLiveData<Float>()
    val videoAspectRatio: LiveData<Float> get() = _videoAspectRatio

    init {
        val player = ExoPlayer.Builder(application).build()
        player.addListener(object : Player.Listener {
            override fun onVideoSizeChanged(videoSize: VideoSize) {
                super.onVideoSizeChanged(videoSize)
                if (videoSize.height > 0) {
                    val aspectRatio = videoSize.width.toFloat() / videoSize.height.toFloat()
                    _videoAspectRatio.postValue(aspectRatio)
                    Log.d("VideoAspectRatio", "宽高比：$aspectRatio")
                }
            }
        })
        _player.value = player
    }

    private val repository = VideoRepository() // 可拓展的数据仓库

    // 播放视频
    fun playVideo(url: String) {
        val proxyUrl = "http://127.0.0.1:8080/$url"
        val mediaItem = MediaItem.fromUri(proxyUrl)
        Log.d("VideoPlayerViewModel", "🎬 播放器加载 URL: $proxyUrl")  // 检查是否走代理
        _player.value?.setMediaItem(mediaItem)
        _player.value?.prepare()
    }

    // 暂停视频
    fun pauseVideo() {
        _player.value?.pause()
    }

    // 释放播放器
    fun releasePlayer() {
        _player.value?.release()
        _player.value = null
    }
}
