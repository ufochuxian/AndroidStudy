package com.eric.androidstudy

import android.R
import android.content.Context
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner

class SpinnerDelegate(
    private val context: Context,
    private val spinner: Spinner,
    private val triggerView: View, // 可以是 Button、TextView 等控件
    private val items: List<String>,
    private val onItemSelectedCallback: (String) -> Unit
) {

    init {
        initializeSpinner()
        setupTriggerView()
    }

    // 初始化 Spinner
    private fun initializeSpinner() {
        val adapter = ArrayAdapter(context, R.layout.simple_spinner_item, items)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        // 监听 Spinner 的选择事件
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (position >= 0) {
                    // 回调给调用方
                    onItemSelectedCallback(items[position])
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // 可以处理未选中情况
            }
        }
    }

    // 设置点击触发下拉列表
    private fun setupTriggerView() {
        triggerView.setOnClickListener {
            spinner.performClick() // 触发 Spinner 下拉显示
        }
    }
}
