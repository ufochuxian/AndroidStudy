package com.eric.base.media

import android.content.Context
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.trackselection.DefaultTrackSelector
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@UnstableApi
class MediaPlayerManager(context: Context) {

    private val trackSelector = DefaultTrackSelector(context).apply {
        setParameters(buildUponParameters().setMaxVideoSizeSd())
    }

    val player: ExoPlayer = ExoPlayer.Builder(context)
        .setTrackSelector(trackSelector)
        .build()


    private val _playbackState = MutableStateFlow<PlaybackState>(PlaybackState.Idle)
    val playbackState: StateFlow<PlaybackState> = _playbackState

    init {
        player.videoScalingMode = C.VIDEO_SCALING_MODE_SCALE_TO_FIT
        player.addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(state: Int) {
                updatePlaybackState()
            }

            override fun onIsPlayingChanged(isPlaying: Boolean) {
                updatePlaybackState()
            }
        })
    }

    fun setMediaItem(uri: String) {
        player.setMediaItem(MediaItem.fromUri(uri))
        player.prepare()
    }

    fun play() {
        player.play()
    }

    fun pause() {
        player.pause()
    }

    fun seekTo(position: Long) {
        player.seekTo(position)
    }

    fun setPlaybackSpeed(speed: Float) {
        player.setPlaybackSpeed(speed)
    }

    fun release() {
        player.release()
    }

    private fun updatePlaybackState() {
        _playbackState.value = when {
            player.isPlaying -> PlaybackState.Playing
            player.playbackState == Player.STATE_ENDED -> PlaybackState.Ended
            player.playbackState == Player.STATE_BUFFERING -> PlaybackState.Buffering
            else -> PlaybackState.Paused
        }
    }

    sealed class PlaybackState {
        object Idle : PlaybackState()
        object Buffering : PlaybackState()
        object Playing : PlaybackState()
        object Paused : PlaybackState()
        object Ended : PlaybackState()
    }
}