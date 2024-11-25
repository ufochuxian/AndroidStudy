package com.eric.androidstudy

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.github.ihsg.patternlocker.OnPatternChangeListener
import com.github.ihsg.patternlocker.PatternLockerView

class PatternLockActivity : AppCompatActivity() {

    private lateinit var patternLockerView: PatternLockerView
    private lateinit var tvHint: TextView
    private lateinit var tvStatus: TextView

    private var firstPattern: List<Int>? = null
    private var isSettingFirstPattern = true

    companion object {
        const val RESULT_SUCCESS = RESULT_FIRST_USER + 1
        const val RESULT_FAILED = RESULT_FIRST_USER + 2
        const val RESULT_CANCELLED = RESULT_CANCELED
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pattern_lock)

        // 初始化控件
        patternLockerView = findViewById(R.id.patternLockerView)
        tvHint = findViewById(R.id.tv_hint)
        tvStatus = findViewById(R.id.tv_status)

        // 设置图案监听
        patternLockerView.setOnPatternChangedListener(object : OnPatternChangeListener {
            override fun onStart(view: PatternLockerView) {
                // 开始绘制图案时的回调
                tvStatus.visibility = TextView.GONE
            }

            override fun onChange(view: PatternLockerView, hitIndexList: List<Int>) {
                // 图案绘制过程中
            }

            override fun onComplete(view: PatternLockerView, hitIndexList: List<Int>) {
                // 图案绘制完成时的回调
                handlePatternComplete(hitIndexList)
            }

            override fun onClear(view: PatternLockerView) {
                // 图案清除时的回调
            }
        })
    }

    private fun handlePatternComplete(pattern: List<Int>) {
        if (isSettingFirstPattern) {
            if (pattern.size < 4) {
                tvStatus.visibility = TextView.VISIBLE
                tvStatus.text = "密码长度需大于4点"
                return
            }
            // 设置第一遍图案
            firstPattern = pattern
            isSettingFirstPattern = false
            tvHint.text = "请再次绘制相同的图案密码"
        } else {
            if (pattern == firstPattern) {
                // 密码设置成功
                tvStatus.visibility = TextView.VISIBLE
                tvStatus.text = "图形密码设置成功！"
                tvStatus.setTextColor(getColor(android.R.color.holo_green_dark))
                Toast.makeText(this, "图形密码设置成功", Toast.LENGTH_SHORT).show()
                // 设置成功
                setResult(RESULT_SUCCESS)
                finish()
                // TODO: 保存密码到安全存储区域
            } else {
                // 密码设置失败
                tvStatus.visibility = TextView.VISIBLE
                tvStatus.text = "两次密码不一致，请重新设置"
                tvStatus.setTextColor(getColor(android.R.color.holo_red_dark))
                isSettingFirstPattern = true
                tvHint.text = "请重新绘制图形密码"
                setResult(RESULT_FAILED)
                finish()
            }
        }
        patternLockerView.clearHitState() // 清除绘制状态
    }

    override fun onBackPressed() {
        super.onBackPressed()
        setResult(RESULT_CANCELLED)
    }
}
