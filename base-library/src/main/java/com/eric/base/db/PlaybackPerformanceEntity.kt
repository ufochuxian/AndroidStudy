package com.eric.base.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.json.JSONObject

@Entity(tableName = "playback_performance")
data class PlaybackPerformanceEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val url: String,
    val dnsTime: Long,
    val connectTime: Long,
    val tlsTime: Long,
    val requestTime: Long,
    val responseTime: Long,
    val totalTime: Long,
    val startTime: Long,
    val endTime: Long,
    val sniffTime: Long,
    val demuxTime: Long,
    val audioDecodeTime: Long,
    val videoDecodeTime: Long,
    val avSyncTime: Long,
    val renderTime: Long
) {
    fun toJson(): JSONObject {
        return JSONObject().apply {
            put("url", url)
            put("network_load_time", JSONObject().apply {
                put("dns_time", dnsTime)
                put("connect_time", connectTime)
                put("tls_time", tlsTime)
                put("request_time", requestTime)
                put("response_time", responseTime)
                put("total_time", totalTime)
                put("start_time", startTime)
                put("end_time", endTime)
            })
            put("sniff_time", sniffTime)
            put("demux_time", demuxTime)
            put("audio_decode_time", audioDecodeTime)
            put("video_decode_time", videoDecodeTime)
            put("av_sync_time", avSyncTime)
            put("render_time", renderTime)
        }
    }
}
