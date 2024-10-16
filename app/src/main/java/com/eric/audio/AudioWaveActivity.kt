package com.eric.audio

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.eric.androidstudy.R
import kotlinx.coroutines.launch

class AudioWaveActivity : AppCompatActivity() {
    private lateinit var volumeMeterView: VolumeMeterView
    private lateinit var audioRecorder: AudioRecorder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_wave)

        volumeMeterView = findViewById(R.id.volumeMeterView)
        audioRecorder = AudioRecorder()

        // 开始录音并使用协程收集音量数据
        lifecycleScope.launch {
            audioRecorder.startRecording(this@AudioWaveActivity).collect { volume ->
                // 更新 View 的音量
                volumeMeterView.updateVolume(volume)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // 停止录音
        audioRecorder.stopRecording()
    }
}
