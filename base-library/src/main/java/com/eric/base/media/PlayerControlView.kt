package com.eric.base.media

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.eric.baselibrary.R
import com.eric.baselibrary.databinding.LayoutMediaControlPannelBinding

class PlayerControlView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val binding: LayoutMediaControlPannelBinding =
        LayoutMediaControlPannelBinding.inflate(LayoutInflater.from(context), this, true)

    fun setControlListener(listener: ControlListener) {
        with(binding) {
            btnPlay.setOnClickListener { listener.onPlayPauseClicked() }
            btnForward.setOnClickListener { listener.onForwardClicked() }
            btnRewind.setOnClickListener { listener.onRewindClicked() }
            btnSpeed.setOnClickListener { listener.onSpeedClicked() }
        }
    }

    fun updatePlayPauseButton(isPlaying: Boolean) {
        binding.btnPlay.setImageResource(
            if (isPlaying) R.drawable.ic_pause else R.drawable.ic_play
        )
    }

    interface ControlListener {
        fun onPlayPauseClicked()
        fun onForwardClicked()
        fun onRewindClicked()
        fun onSpeedClicked()
    }
}