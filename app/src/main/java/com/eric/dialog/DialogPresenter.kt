package com.eric.dialog

import android.app.Dialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.LayoutRes
import com.eric.androidstudy.BuildConfig
import com.eric.androidstudy.R
import com.eric.base.ext.ERIC_TAG
import com.eric.base.mgr.PermissionManager

// 数据类，用于定义权限信息
data class PermissionInfo(
    val permissionName: String, // 权限名称
    val description: String,    // 权限描述文案
    val action: ((itemView : View) -> Unit)?   // 点击权限时的动作（跳转到权限设置页等）
)

// 回调接口，用于处理权限申请结果
interface PermissionDialogCallback {
    fun onPermissionsGranted() // 所有权限已授予
    fun onPermissionsDenied(deniedPermissions: List<String>) // 被拒绝的权限列表
}


class DialogPresenter(val context: Context) {

    // 自定义弹窗方法
    fun showCustomPermissionDialog(
        permissions: List<PermissionInfo>,
        callback: PermissionDialogCallback,
        onCancel: ((dialog: Dialog) -> Unit)? = null
    ) : Dialog{
        // 创建自定义 Dialog
        val dialog = Dialog(context).apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setCancelable(false)
            setContentView(R.layout.custom_permission_dialog) // 自定义布局文件
            window?.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT
            )
            window?.setBackgroundDrawableResource(android.R.color.transparent)
        }

        dialog.setCancelable(true)

        dialog.setOnCancelListener {
            if(onCancel != null) {
                onCancel(dialog)
            }
        }

        // 获取根布局
        val permissionContainer = dialog.findViewById<LinearLayout>(R.id.permission_container)

        // 动态加载权限项
        val inflater = LayoutInflater.from(context)
        permissions.forEach { permission ->
            val itemView =
                inflater.inflate(R.layout.dialog_permission_item, permissionContainer, false)

            val permissionNameView = itemView.findViewById<TextView>(R.id.permission_name)
            val permissionDescriptionView =
                itemView.findViewById<TextView>(R.id.permission_description)
            val permissionStatusView = itemView.findViewById<TextView>(R.id.permission_status)

            permissionNameView.text = permission.permissionName
            permissionDescriptionView.text = permission.description

            // 检查权限状态并更新 UI
            val isGranted = isPermissionGranted(permission.permissionName)
            permissionStatusView.text = if (isGranted) "已授权" else "未授权"
            permissionStatusView.setTextColor(
                context.resources.getColor(
                    if (isGranted) R.color.colorAccent else android.R.color.holo_red_dark
                )
            )

            // 点击权限描述触发对应的动作
            itemView.setOnClickListener {
                permission.action?.invoke(permissionStatusView)
            }

            permissionContainer.addView(itemView)
        }

        dialog.show()
        return dialog
    }


    // 判断权限是否已授予（根据权限名称判断逻辑可自定义）
    private fun isPermissionGranted(permissionName: String): Boolean {
        return when (permissionName) {
            "应用使用情况权限" -> {
                PermissionManager.hasUsageStatsPermission(context)
            }

            "悬浮窗权限" -> {
                PermissionManager.hasOverlayPermission(context)
            }

            else -> false
        }
    }

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
