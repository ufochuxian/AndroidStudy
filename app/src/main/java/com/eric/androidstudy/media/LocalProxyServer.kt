package com.eric.androidstudy.media

import android.util.Log
import fi.iki.elonen.NanoHTTPD
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.InputStream
import java.net.URL
import java.net.URLDecoder

class LocalProxyServer(port: Int) : NanoHTTPD(port) {

    private val client = OkHttpClient()
    private val allowedUrls = listOf( // 允许代理的 URL
        "https://media.w3.org"
    )

    override fun serve(session: IHTTPSession): Response {
        val timestamp = System.currentTimeMillis()
        val uri = session.uri.substring(1)
        val decodedUri = URLDecoder.decode(uri, "UTF-8")
        val rangeHeader = session.headers["range"]

        Log.d("LocalProxyServer", "📥 [$timestamp] serve() 被调用，请求: $decodedUri")
        Log.d("LocalProxyServer", "🔍 [$timestamp] 请求头: Range = ${rangeHeader ?: "未使用 Range 头"}")
        Log.d("LocalProxyServer", "📋 [$timestamp] 所有请求头: ${session.headers}")

        return if (isUrlAllowed(uri)) {
            // 允许的 URL，代理请求远程资源
            try {
                Log.d("LocalProxyServer", "🔁 代理请求: $uri")
                val request = Request.Builder().url(URL(uri)).build()
                val response = client.newCall(request).execute()

                val mimeType = response.header("Content-Type") ?: "video/mp4"
                Log.d("LocalProxyServer", "✅ 远程服务器响应: ${response.code}, 类型: $mimeType")

                val stream: InputStream = response.body?.byteStream()
                    ?: return newFixedLengthResponse(Response.Status.NOT_FOUND, mimeType, "远程资源不可用")

                Log.d("LocalProxyServer", "📡 数据流已获取，返回给播放器")
                return newChunkedResponse(Response.Status.OK, mimeType, stream)

            } catch (e: Exception) {
                Log.e("LocalProxyServer", "❌ 代理错误: ${e.message}")
                return newFixedLengthResponse(Response.Status.INTERNAL_ERROR, "text/plain", "代理错误: ${e.message}")
            }
        } else {
            // 非允许的 URL，不代理，保持默认处理方式（返回 404）
            Log.w("LocalProxyServer", "⚠️ 请求未被代理，保持原样: $uri")
            return newFixedLengthResponse(Response.Status.NOT_FOUND, "text/plain", "请求未被代理")
        }
    }

    private fun isUrlAllowed(url: String): Boolean {
        val isAllowed = allowedUrls.any { url.startsWith(it) }
        Log.d("LocalProxyServer", if (isAllowed) "✅ 允许代理: $url" else "🚫 拒绝代理: $url")
        return isAllowed
    }
}
