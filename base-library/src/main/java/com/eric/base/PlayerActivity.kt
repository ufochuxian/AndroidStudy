package com.eric.base

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.eric.base.media.MediaPlayerManager
import com.eric.base.media.PlayerControlView
import com.eric.baselibrary.databinding.ActivityPlayerBinding
import kotlinx.coroutines.launch

private const val TAG = "PlayerActivity"
class PlayerActivity : AppCompatActivity(), PlayerControlView.ControlListener {

    private lateinit var binding: ActivityPlayerBinding
    private lateinit var mediaPlayerManager: MediaPlayerManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mediaPlayerManager = MediaPlayerManager(this)

        binding.playerControlView.setControlListener(this)

        // 设置视频源 (这里用一个示例 URL)
        mediaPlayerManager.setMediaItem("https://media.w3.org/2010/05/sintel/trailer.mp4")

        lifecycleScope.launch {
            mediaPlayerManager.playbackState.collect { state ->
                when (state) {
                    is MediaPlayerManager.PlaybackState.Playing -> binding.playerControlView.updatePlayPauseButton(true)
                    is MediaPlayerManager.PlaybackState.Paused -> binding.playerControlView.updatePlayPauseButton(false)
                    // 处理其他状态...
                    MediaPlayerManager.PlaybackState.Buffering -> {
                        Log.d(TAG,"Buffering")
                    }
                    MediaPlayerManager.PlaybackState.Ended -> {
                        Log.d(TAG,"Ended")

                    }
                    MediaPlayerManager.PlaybackState.Idle -> {
                        Log.d(TAG,"Idle")

                    }
                }
            }
        }
    }

    override fun onPlayPauseClicked() {
        if (mediaPlayerManager.playbackState.value is MediaPlayerManager.PlaybackState.Playing) {
            mediaPlayerManager.pause()
        } else {
            mediaPlayerManager.play()
        }
    }

    override fun onForwardClicked() {
        mediaPlayerManager.seekTo(mediaPlayerManager.player.currentPosition + 10000) // 前进10秒
    }

    override fun onRewindClicked() {
        mediaPlayerManager.seekTo(mediaPlayerManager.player.currentPosition - 10000) // 后退10秒
    }

    override fun onSpeedClicked() {
        // 实现倍速播放逻辑，例如循环切换不同的播放速度
        val speeds = listOf(0.5f, 1.0f, 1.5f, 2.0f)
        val currentSpeedIndex = speeds.indexOf(mediaPlayerManager.player.playbackParameters.speed)
        val nextSpeedIndex = (currentSpeedIndex + 1) % speeds.size
        mediaPlayerManager.setPlaybackSpeed(speeds[nextSpeedIndex])
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayerManager.release()
    }
}