package com.eric.androidstudy

import ShimmerAdapter
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.eric.androidstudy.databinding.ActivityShimmerListBinding

class ShimmerListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityShimmerListBinding
    private lateinit var adapter: ShimmerAdapter
    private val handler = Handler(Looper.getMainLooper())

    companion object {
        private const val TAG = "ShimmerListActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShimmerListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = ShimmerAdapter()
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        Log.d(TAG, "初始化完成，正在显示 shimmer 骨架")

        // 延迟加载真实数据
        handler.postDelayed({
            val data = (1..20).map { "Item $it" }
            Log.d(TAG, "开始加载真实数据，共 ${data.size} 条")
            adapter.submitData(data)
        }, 2500)
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
        Log.d(TAG, "Activity 销毁，清除 Handler 回调")
    }
}