package com.eric

import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.eric.androidstudy.databinding.ConstraintLayoutActivityBinding

class KeyboardPreviewActivity : AppCompatActivity() {

    private lateinit var binding: ConstraintLayoutActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set window size to wrap content
        window.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT)

        // Add this code to position the window at the top
        val params = window.attributes
        params.gravity = Gravity.BOTTOM
        window.attributes = params

        // 使用 ViewBinding 加载布局
        binding = ConstraintLayoutActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        // 设置点击事件：点击 keyboardView 切换高度
//        binding.keyboardView.setOnClickListener {
//            binding.keyboardView.toggleHeight()
//        }

        // 示例：可以对 topBar 设置背景颜色或内容
//        binding.topBar.setBackgroundColor(0xFFDDDDDD.toInt())

        // 示例：背景图处理（可选 Glide/自定义主题）
        // Glide.with(this).load("xxx.jpg").into(binding.bg)
    }

    //  隐藏navigationBar
    private fun hideNavigationBottomBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.setDecorFitsSystemWindows(false)
            window.insetsController?.let { controller ->
                controller.hide(WindowInsets.Type.navigationBars())
                controller.systemBarsBehavior =
                    WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        } else {
            @Suppress("DEPRECATION")
            window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if(hasFocus) {
            hideNavigationBottomBar()
        }
    }
}
