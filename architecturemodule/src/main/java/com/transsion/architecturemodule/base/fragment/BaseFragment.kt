package com.transsion.architecturemodule.base.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.transsion.architecturemodule.base.interfaces.BackPressedListener

/**
 *
 * @author chen
 */
abstract class BaseFragment<VB : ViewBinding> : Fragment(), BackPressedListener {

    companion object {
        const val TAG = "BaseFragment"
        const val ARGS = "args"
    }

    protected open var mBinding: VB? = null
    protected var mParentContainer: ViewGroup? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d(TAG, "添加返回监听，viewLifecycleOwner:${this}")
        requireActivity().onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                Log.d(TAG, "handleOnBackPressed, viewLifecycleOwner:${this}")
                // 检查当前 FragmentManager 中的 Fragment 数量
                val fragmentCount = activity?.supportFragmentManager?.backStackEntryCount ?: 0
                this@BaseFragment.onBackKeyPress()
                // 如果只剩下一个 Fragment，直接退出 Activity
                if (fragmentCount <= 1) {
                    activity?.finish()
                } else {
                    activity?.supportFragmentManager?.popBackStackImmediate()
                }
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mParentContainer = container
        mBinding = getViewBinding()
        mBinding?.root?.isClickable = true
        return mBinding?.root
    }

    abstract fun initData()

    abstract fun initView()

    abstract fun initAction()

    abstract fun getViewBinding(): VB

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initStatusBar()
        initNavigationBar()
        initData()
        initView()
        initAction()
    }

    open fun initStatusBar() {

    }

    open fun initNavigationBar() {

    }

    override fun onResume() {
        super.onResume()
    }

    open fun getBundle(): Bundle? {
        return null
    }

    override fun onPause() {
        super.onPause()

    }

    protected open fun onBackKeyPress() {

    }

    override fun onDestroy() {
        super.onDestroy()
        mBinding = null
    }

    override fun handleBackPressed(): Boolean {
        return false
    }
}