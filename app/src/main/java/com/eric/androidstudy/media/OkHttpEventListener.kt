package com.eric.androidstudy.media

import android.util.Log
import com.eric.base.db.PlaybackPerformanceEntity
import com.eric.base.db.PlaybackPerformanceDao
import okhttp3.Call
import okhttp3.EventListener
import okhttp3.Handshake
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import java.net.InetAddress
import java.net.InetSocketAddress
import java.net.Proxy

class OkHttpEventListener(
    private val url: String,
    private val dao: PlaybackPerformanceDao // Room 数据库 DAO
) : EventListener() {

    private var startTime: Long = 0
    private var dnsStartTime: Long = 0
    private var connectStartTime: Long = 0
    private var secureStartTime: Long = 0
    private var requestStartTime: Long = 0
    private var responseStartTime: Long = 0

    private var dnsTime: Long = 0
    private var connectTime: Long = 0
    private var tlsTime: Long = 0
    private var requestTime: Long = 0
    private var responseTime: Long = 0
    private var totalTime: Long = 0

    override fun callStart(call: Call) {
        startTime = System.currentTimeMillis()
    }

    override fun dnsStart(call: Call, domainName: String) {
        dnsStartTime = System.currentTimeMillis()
    }

    override fun dnsEnd(call: Call, domainName: String, inetAddressList: List<InetAddress>) {
        dnsTime = System.currentTimeMillis() - dnsStartTime
    }

    override fun connectStart(call: Call, inetSocketAddress: java.net.InetSocketAddress, proxy: Proxy) {
        connectStartTime = System.currentTimeMillis()
    }

    override fun connectEnd(call: Call, inetSocketAddress: InetSocketAddress, proxy: Proxy, protocol: Protocol?) {
        connectTime = System.currentTimeMillis() - connectStartTime
        super.connectEnd(call, inetSocketAddress, proxy, protocol)
    }

    override fun secureConnectStart(call: Call) {
        secureStartTime = System.currentTimeMillis()
    }

    override fun secureConnectEnd(call: Call, handshake: Handshake?) {
        tlsTime = System.currentTimeMillis() - secureStartTime
        super.secureConnectEnd(call, handshake)
    }

    override fun requestHeadersStart(call: Call) {
        requestStartTime = System.currentTimeMillis()
    }

    override fun requestHeadersEnd(call: Call, request: Request) {
        requestTime = System.currentTimeMillis() - requestStartTime
    }

    override fun responseHeadersStart(call: Call) {
        responseStartTime = System.currentTimeMillis()
    }

    override fun responseHeadersEnd(call: Call, response: Response) {
        responseTime = System.currentTimeMillis() - responseStartTime
        totalTime = System.currentTimeMillis() - startTime

        // 这里的时间可以从 `ExoPlayer` 的回调中获取
        val sniffTime = 35L
        val demuxTime = 50L
        val audioDecodeTime = 20L
        val videoDecodeTime = 45L
        val avSyncTime = 10L
        val renderTime = 85L

        val entity = PlaybackPerformanceEntity(
            url = url,
            dnsTime = dnsTime,
            connectTime = connectTime,
            tlsTime = tlsTime,
            requestTime = requestTime,
            responseTime = responseTime,
            totalTime = totalTime,
            startTime = startTime,
            endTime = System.currentTimeMillis(),
            sniffTime = sniffTime,
            demuxTime = demuxTime,
            audioDecodeTime = audioDecodeTime,
            videoDecodeTime = videoDecodeTime,
            avSyncTime = avSyncTime,
            renderTime = renderTime
        )

        dao.insert(entity)

        Log.d("OkHttpEventListener", "📊 记录数据: ${entity.toJson()}")
    }
}
