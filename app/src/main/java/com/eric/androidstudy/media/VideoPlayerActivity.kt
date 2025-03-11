package com.eric.androidstudy.media

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.eric.androidstudy.R

class VideoPlayerActivity : AppCompatActivity() {

    private lateinit var playerView: PlayerView
    private val viewModel: VideoPlayerViewModel by viewModels() // 使用 ViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_player)

        playerView = findViewById(R.id.playerView)

        // 监听播放器实例变化
        viewModel.player.observe(this, Observer { player ->
            playerView.player = player
        })

        // 播放指定视频
        val videoUrl = intent.getStringExtra("VIDEO_URL") ?: "https://media.w3.org/2010/05/sintel/trailer.mp4"
        viewModel.playVideo(videoUrl)
    }

    override fun onPause() {
        super.onPause()
        viewModel.pauseVideo()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.releasePlayer()
    }
}
