package com.eric.audio

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.widget.Toast
import androidx.core.app.ActivityCompat
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class AudioRecorder {
    private var audioRecord: AudioRecord? = null
    private val bufferSize = AudioRecord.getMinBufferSize(
        44100,  // 采样率
        AudioFormat.CHANNEL_IN_MONO,  // 单声道输入
        AudioFormat.ENCODING_PCM_16BIT // 16位PCM格式
    )

    // Flow用于实时发射音量数据
    fun startRecording(context: Context): Flow<Float> = flow {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.RECORD_AUDIO
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            audioRecord = AudioRecord(
                MediaRecorder.AudioSource.MIC,
                44100, // 采样率
                AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT,
                bufferSize
            )

            val buffer = ByteArray(bufferSize)
            audioRecord?.startRecording()

            while (audioRecord?.recordingState == AudioRecord.RECORDSTATE_RECORDING) {
                val read = audioRecord?.read(buffer, 0, buffer.size) ?: 0

                if (read > 0) {
                    // 计算当前音量值
                    val volume = calculateVolume(buffer, read)
                    // 发射音量数据
                    emit(volume)
                }
            }
        } else {
            Toast.makeText(context, "没有音视频相关权限",Toast.LENGTH_SHORT).show()
        }
    }

    fun stopRecording() {
        audioRecord?.stop()
        audioRecord?.release()
        audioRecord = null
    }

    private fun calculateVolume(buffer: ByteArray, readSize: Int): Float {
        var sum: Long = 0
        for (i in 0 until readSize step 2) {
            val amplitude = (buffer[i].toInt() and 0xFF) or (buffer[i + 1].toInt() shl 8)
            sum += Math.abs(amplitude.toLong())
        }
        return sum.toFloat() / (readSize / 2)
    }
}
