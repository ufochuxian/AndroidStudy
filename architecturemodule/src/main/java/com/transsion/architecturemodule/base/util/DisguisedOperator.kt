package com.transsion.architecturemodule.base.util

import android.content.ComponentName
import android.content.Context
import android.content.pm.PackageManager

class DisguisedOperator(val context: Context) {

    fun toNewIcon() {
        context.let {
            val activityName: String = it.packageName + ".launcherB"
            val mainName: String = it.packageName + ".activity.SplashActivity"
            enableLauncher(it, activityName, mainName)
        }
    }

    fun toOldIcon() {
        context.let {
            val activityName: String = it.packageName + ".activity.SplashActivity"
            val mainName: String = it.packageName + ".launcherB"
            enableLauncher(it, activityName, mainName)
        }

    }

    private fun enableLauncher(context: Context, activityPath: String?, mainName: String?) {
        try {
            val pm = context.packageManager
            pm.setComponentEnabledSetting(
                ComponentName(context, mainName!!),
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP
            )
            pm.setComponentEnabledSetting(
                ComponentName(context, activityPath!!),
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}