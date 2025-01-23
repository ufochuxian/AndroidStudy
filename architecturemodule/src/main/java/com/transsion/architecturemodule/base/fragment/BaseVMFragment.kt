package com.transsion.architecturemodule.base.fragment

import com.transsion.architecturemodule.base.viewmodel.BaseViewModel

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding

/**
 * @author chen
 */

abstract class BaseVMFragment<VB : ViewBinding, VM : BaseViewModel> : BaseFragment<VB>() {
    protected open lateinit var mViewModel: VM

    private val TAG = "BaseVMFragment"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initVM()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (requireActivity().isFinishing || requireActivity().isDestroyed)
            return
        initObserve()
    }

    private fun initVM() {
        mViewModel = ViewModelProvider(this)[viewModelClass()]
    }

    fun getAct(): AppCompatActivity {
        return requireActivity() as AppCompatActivity
    }

    abstract fun initObserve()

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