package com.eric.audio

import android.Manifest
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.eric.androidstudy.R
import com.eric.base.PermissionManager
import kotlinx.coroutines.launch

class AudioWaveActivity : AppCompatActivity() {
    private lateinit var volumeMeterView: VolumeMeterView
    private lateinit var audioRecorder: AudioRecorder

    private val permissionManager by lazy {
        PermissionManager(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_wave)

        volumeMeterView = findViewById(R.id.volumeMeterView)
        audioRecorder = AudioRecorder()

        // 开始录音并使用协程收集音量数据
        val permissions = arrayOf(
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.CAMERA
        )

        permissionManager.requestPermissions(
            permissions,
            object : PermissionManager.PermissionCallback {
                override fun onPermissionGranted() {
                    // 所有权限都已被授予
                    Toast.makeText(this@AudioWaveActivity, "权限已授予", Toast.LENGTH_SHORT).show()
                    // 开始处理音视频相关的逻辑
                    lifecycleScope.launch {
                        audioRecorder.startRecording(this@AudioWaveActivity).collect { volume ->
                            // 更新 View 的音量
                            volumeMeterView.updateVolume(volume)
                        }
                    }
                }

                override fun onPermissionDenied(deniedPermissions: List<String>) {
                    // 有权限被拒绝
                    Toast.makeText(
                        this@AudioWaveActivity,
                        "权限被拒绝: $deniedPermissions",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }

    override fun onDestroy() {
        super.onDestroy()
        // 停止录音
        audioRecorder.stopRecording()
    }
}
