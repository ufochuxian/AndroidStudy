package com.transsion.architecturemodule.base.activity

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.transsion.architecturemodule.base.ktx.useImmersiveWindow
import com.transsion.architecturemodule.base.viewmodel.BaseViewModel

/**
 *
 * @author chen
 */
abstract class BaseVMActivity<VB : ViewBinding, VM : BaseViewModel> : BaseActivity<VB>() {
    private val TAG = "BaseVMActivity"
    protected open lateinit var mViewModel: VM
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.useImmersiveWindow(false)
        initVM()
        initObserve()
    }


    private fun initVM() {
        mViewModel = ViewModelProvider(this)[viewModelClass()]
    }

    /**
     * 用于初始化视图
     */
    abstract fun initObserve()

    abstract fun viewModelClass(): Class<VM>

    override fun onDestroy() {
        super.onDestroy()
        try {
            if (this::mViewModel.isInitialized) {
                mViewModel.release()
            }
        } catch (e: Exception) {
            Log.w(TAG, "activity 释放资源发生异常:${e.message}")
        }
    }
}