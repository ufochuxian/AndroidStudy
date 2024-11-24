package com.eric.base.dialog

import android.app.Dialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.annotation.LayoutRes
import com.eric.androidstudy.BuildConfig
import com.eric.androidstudy.R
import com.eric.base.ext.ERIC_TAG

class DialogPresenter(val context: Context) {

    /**
     * 显示一个自定义布局的 Dialog
     *
     * @param layoutId 自定义布局资源 ID
     * @param onConfirm 确认按钮的回调
     * @param onCancel 取消按钮的回调
     */
    fun showCustomDialog(
        @LayoutRes layoutId: Int,
        onConfirm: ((dialog: Dialog) -> Unit)? = null,
        onCancel: ((dialog: Dialog) -> Unit)? = null
    ): Dialog {
        val dialog = Dialog(context).apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setCancelable(true)
        }

        val view = LayoutInflater.from(context).inflate(layoutId, null)
        dialog.setContentView(view)

        // 设置 Dialog 的窗口参数，确保居中
        dialog.window?.apply {
            setLayout(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT
            )
            setBackgroundDrawableResource(android.R.color.transparent)
        }

        // 处理确认和取消按钮的事件
        val confirmButton = view.findViewById<View>(R.id.confirm)
        val cancelButton = view.findViewById<View>(R.id.cancel)

        if (BuildConfig.DEBUG && confirmButton == null) {
            Log.w(ERIC_TAG, "弹窗的确认按钮不存在，请检查布局的id是否正确")
        }

        if (BuildConfig.DEBUG && cancelButton == null) {
            Log.w(ERIC_TAG, "弹窗的取消按钮不存在，请检查布局的id是否正确")
        }

        confirmButton?.setOnClickListener {
            onConfirm?.invoke(dialog)
            dialog.dismiss()
        }

        cancelButton?.setOnClickListener {
            onCancel?.invoke(dialog)
            dialog.dismiss()
        }

        dialog.show()
        return dialog
    }
}
