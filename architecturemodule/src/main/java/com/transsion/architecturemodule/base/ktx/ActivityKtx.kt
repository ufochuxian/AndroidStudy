package com.transsion.architecturemodule.base.ktx

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.transsion.architecturemodule.R
import com.transsion.architecturemodule.base.constant.LogConstant
import java.io.Serializable

fun FragmentActivity.showFragment(
    containerID : Int,
    fr: Fragment,
    tag: String? = null,
    isAddToBackStack: Boolean = false
) {
    supportFragmentManager.beginTransaction()
        .setCustomAnimations(
            R.anim.slide_in_right,  // 进入动画
            R.anim.slide_out_left,  // 退出动画
            R.anim.slide_in_left,   // 返回时进入动画
            R.anim.slide_out_right  // 返回时退出动画
        )
        .apply {
            fr.let {
                add(containerID, fr, tag)
                if (isAddToBackStack) {
                    tag?.let {
                        addToBackStack(tag)
                    }
                }
                commitAllowingStateLoss()
            }
        }
}

fun FragmentActivity.replaceFragment(
    containerID: Int,
    fr: Fragment,
    tag: String? = null,
    isAddToBackStack: Boolean = false
) {
    supportFragmentManager.beginTransaction()
        .setCustomAnimations(
            R.anim.slide_in_right,  // 进入动画
            R.anim.slide_out_left,  // 退出动画
            R.anim.slide_in_left,   // 返回时进入动画
            R.anim.slide_out_right  // 返回时退出动画
        )
        .apply {
            fr.let {
                replace(containerID, fr, tag)
                if (isAddToBackStack) {
                    tag?.let {
                        addToBackStack(tag)
                    }
                }
                commitAllowingStateLoss()
            }
        }
}

const val EXTRA_PARAMS = "extra_params"

/**
 * 启动 Activity,扩展，可以带有参数，简化参数的传入和获取逻辑
 */
fun Context.startActivity(
    clazz: Class<out Activity>,
    params: java.io.Serializable? = null,
    options: Bundle? = null,
) {
    val intent = Intent(this, clazz).apply {
        params?.let { putExtra(EXTRA_PARAMS, it) }
    }
    ActivityCompat.startActivity(this, intent, options)
}

/**
 * 获取 Intent 参数
 */
inline fun <reified T : Serializable> Activity.getIntentParams(): T? {
    return intent?.getSerializableExtra(EXTRA_PARAMS) as? T
}

fun AppCompatActivity.registerReceiverExt(receiver: BroadcastReceiver, action: String) {
    val filter = IntentFilter(action)
    registerReceiver(receiver, filter)
}

fun AppCompatActivity.unregisterReceiverExt(receiver: BroadcastReceiver) {
    unregisterReceiver(receiver)
}

fun Fragment.registerReceiverExt(receiver: BroadcastReceiver, action: String) {
    try {
        val filter = IntentFilter(action)
        requireActivity().registerReceiver(receiver, filter)
    } catch (e: Exception) {
        Log.e(LogConstant.ActivityKtxTAG, "注册广播失败")
    }
}

fun Fragment.unregisterReceiverExt(receiver: BroadcastReceiver) {
    try {
        requireActivity().unregisterReceiver(receiver)
    } catch (e: Exception) {
        Log.e(LogConstant.ActivityKtxTAG, "反注册广播失败")
    }
}
