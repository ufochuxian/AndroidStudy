package com.eric.base.media

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.eric.baselibrary.R

class VideoPlayerActivity : AppCompatActivity() {

    private lateinit var playerView: PlayerView
    private lateinit var player: ExoPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        playerView = findViewById(R.id.player_view)

        // 初始化 ExoPlayer
        player = ExoPlayer.Builder(this).build()

        // 将 Player 关联到 PlayerView
        playerView.player = player

        // 准备要播放的 MediaItem（本地或网络视频）
        val mediaItem = MediaItem.fromUri("https://www.w3schools.com/html/movie.mp4")

        // 将媒体项设置给播放器
        player.setMediaItem(mediaItem)

        // 准备并播放
        player.prepare()
        player.play()
    }

    override fun onStop() {
        super.onStop()
        // 释放播放器
        player.release()
    }
}
