package com.transsion.architecturemodule.base.fragment

import com.transsion.architecturemodule.base.viewmodel.BaseViewModel

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding

/**
 * @author chen
 */

abstract class BaseVMFragment<VB : ViewBinding, VM : BaseViewModel> : BaseFragment<VB>() {
    protected open lateinit var mViewModel: VM

    private val TAG = "BaseVMFragment"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initVM()
        if (requireActivity().isFinishing || requireActivity().isDestroyed)
            return
        initObserve()
        initData()
        initAction()
    }

    private fun initVM() {
        mViewModel = ViewModelProvider(this)[viewModelClass()]
    }

    fun getAct(): AppCompatActivity {
        return requireActivity() as AppCompatActivity
    }

    override fun initView() {

    }

    abstract fun initObserve()

    abstract fun initData()

    abstract fun initAction()

    abstract fun viewModelClass(): Class<VM>

    override fun onDestroy() {
        super.onDestroy()
        try {
            if (this::mViewModel.isInitialized) {
                mViewModel.release()
            }
        } catch (e: Exception) {
            Log.w(TAG, "fragment 释放资源发生异常:${e.message}")
        }
    }
}