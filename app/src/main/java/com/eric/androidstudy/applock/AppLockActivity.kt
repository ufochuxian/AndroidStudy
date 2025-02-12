package com.eric.androidstudy.applock

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import com.eric.androidstudy.R
import com.eric.androidstudy.databinding.ActivityApplockBinding
import com.transsion.architecturemodule.base.activity.BaseVMActivity
import org.opencv.android.Utils
import org.opencv.core.Mat
import org.opencv.imgproc.Imgproc

class AppLockActivity : BaseVMActivity<ActivityApplockBinding, AppLockActivityViewModel>() {
    override fun initData() {
        // 初始化数据
    }

    override fun initView(savedInstanceState: Bundle?) {
//        showPage(R.id.container, AppLockFragment.newInstance(), AppLockFragment.TAG, true)

        // 读取 drawable 图片并转换为 Mat
        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.img)
        val originalMat = Mat()
        Utils.bitmapToMat(bitmap, originalMat)

        // 转换为灰度图
        val grayMat = applyGrayScale(originalMat)

        // 可选：如果要显示处理后的图片，需要转换回 Bitmap
        val grayBitmap = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
        Utils.matToBitmap(grayMat, grayBitmap)

        // 在 ImageView 显示灰度图
        mBinding?.imageView?.setImageBitmap(grayBitmap)
    }

    override fun initObserve() {}
    override fun initAction() {}
    override fun viewModelClass(): Class<AppLockActivityViewModel> = AppLockActivityViewModel::class.java
    override fun getViewBinding(): ActivityApplockBinding = ActivityApplockBinding.inflate(layoutInflater)

    /**
     * 处理灰度化
     */
    fun applyGrayScale(src: Mat?): Mat {
        val grayMat = Mat()
        Imgproc.cvtColor(src, grayMat, Imgproc.COLOR_BGR2GRAY)
        return grayMat
    }
}
