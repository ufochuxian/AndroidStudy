package com.eric.androidstudy.applock

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import com.eric.androidstudy.R
import com.eric.androidstudy.databinding.ActivityApplockBinding
import com.transsion.architecturemodule.base.activity.BaseVMActivity
import org.opencv.android.Utils
import org.opencv.core.Mat
import org.opencv.core.Size
import org.opencv.imgproc.Imgproc


class AppLockActivity : BaseVMActivity<ActivityApplockBinding, AppLockActivityViewModel>() {
    override fun initData() {
        // 初始化数据
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
    }

    override fun initObserve() {}
    override fun initAction() {

        mBinding?.imageView?.setOnClickListener {
            mViewModel.faceDetect(this)
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

}