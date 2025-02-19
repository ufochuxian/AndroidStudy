package com.eric.androidstudy.applock

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.activity.result.ActivityResult
import com.eric.androidstudy.R
import com.eric.androidstudy.databinding.ActivityApplockBinding
import com.eric.base.logTd
import com.eric.base.mgr.PermissionManager
import com.eric.base.thread.TaskManager
import com.eric.memory.dumpHprof
import com.transsion.architecturemodule.base.activity.BaseVMActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import okhttp3.internal.concurrent.Task
import org.opencv.android.Utils
import org.opencv.core.Mat
import org.opencv.core.Size
import org.opencv.imgproc.Imgproc


class AppLockActivity : BaseVMActivity<ActivityApplockBinding, AppLockActivityViewModel>() {

    var permissionMgr : PermissionManager<Activity>? = null

    override fun initData() {
        // 初始化数据
        permissionMgr = PermissionManager(this)
    }

    override fun initView(savedInstanceState: Bundle?) {
        // 读取 drawable 图片并转换为 Mat
        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.img)
        val originalMat = Mat()
        Utils.bitmapToMat(bitmap, originalMat)

        // 转换为灰度图
//        val grayMat = applyGrayScale(originalMat)

        // 对灰度图进行模糊处理
        val blurredMat = applyBlur(originalMat)

        // 将处理后的 Mat 转换回 Bitmap
        val blurredBitmap = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
        Utils.matToBitmap(blurredMat, blurredBitmap)

        // 在 ImageView 显示模糊后的图片
        mBinding?.imageView?.setImageBitmap(blurredBitmap)

        TaskManager.submitTask(Runnable {
            // 初始的数字
            var counter = 0

            // 每隔 1 秒打印一次数字，直到任务结束
            while (counter < 100) {  // 这里限制打印次数为100次

                //我们在线程池中对应的任务cacel的方式，是通过Future.cancle的方式来调用的，这个只是个标记线程被打断，并不一定立即生效，有可能延迟生效的，所以为了安全的保证
                //我们加入这个Thread.currentThread().isInterrupted判断，保证调用后，异步的任务逻辑，不要继续执行了
                if (Thread.currentThread().isInterrupted) {
                    // 检查中断标志，如果任务被取消，立即退出任务
                    logTd("TaskManager", "Task interrupted, stopping task execution.")
                    return@Runnable
                }

                logTd("TaskManager", "开启一个applock任务，当前任务处于：${Thread.currentThread().name}，当前数字：$counter")

                // 自增
                counter++

                try {
                    Thread.sleep(1000)  // 休眠 1 秒
                } catch (e: InterruptedException) {
                    // 处理中断异常，标记任务已中断
                    logTd("TaskManager", "Task interrupted during sleep, stopping task.")
                    Thread.currentThread().interrupt()  // 设置线程的中断标志
                    return@Runnable
                }
            }
        }, this, "AppLockActivity", "initView")


        Thread().run {
            logTd("threadpool","线程收敛插件测试")
        }
    }

    override fun initObserve() {}
    override fun initAction() {

        mBinding?.imageView?.setOnClickListener {
//            mViewModel.faceDetect(this)
            permissionMgr?.requestStoragePermission(callback = object : PermissionManager.PermissionCallback {
                override fun onPermissionGranted(result: ActivityResult?) {
                    dumpHprof(this@AppLockActivity)
                }

                override fun onPermissionDenied(result: ActivityResult?) {
                }
            })

        }
    }
    override fun viewModelClass(): Class<AppLockActivityViewModel> = AppLockActivityViewModel::class.java
    override fun getViewBinding(): ActivityApplockBinding = ActivityApplockBinding.inflate(layoutInflater)

    /**
     * 处理灰度化
     */
    private fun applyGrayScale(src: Mat?): Mat {
        val grayMat = Mat()
        Imgproc.cvtColor(src, grayMat, Imgproc.COLOR_BGR2GRAY)
        return grayMat
    }

    /**
     * 处理模糊化
     */
    private fun applyBlur(src: Mat?): Mat {
        val blurredMat = Mat()
        // 使用高斯模糊，参数为：输入 Mat、输出 Mat、模糊核大小、SigmaX
        Imgproc.GaussianBlur(src, blurredMat, Size(25.0, 25.0), 0.0)
        return blurredMat
    }

    override fun onDestroy() {
        super.onDestroy()
        TaskManager.cancelTasksForPageInstance(this)
    }

}