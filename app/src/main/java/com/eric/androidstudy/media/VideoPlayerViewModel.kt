package com.eric.androidstudy.media

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer

class VideoPlayerViewModel(application: Application) : AndroidViewModel(application) {

    private val _player = MutableLiveData<ExoPlayer?>()
    val player: LiveData<ExoPlayer?> get() = _player

    private val repository = VideoRepository() // 可拓展的数据仓库

    init {
        _player.value = createPlayer()
    }

    // 初始化 ExoPlayer
    private fun createPlayer(): ExoPlayer {
        return ExoPlayer.Builder(getApplication()).build().apply {
            playWhenReady = true
            repeatMode = Player.REPEAT_MODE_OFF
        }
    }

    // 播放视频
    fun playVideo(url: String) {
        val mediaItem = MediaItem.fromUri(repository.getVideoUrl(url))
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
