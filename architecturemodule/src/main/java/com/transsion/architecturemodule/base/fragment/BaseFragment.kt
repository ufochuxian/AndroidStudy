package com.transsion.architecturemodule.base.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.transsion.architecturemodule.base.interfaces.BackPressedListener

/**
 *
 * @author chen
 */
abstract class BaseFragment<VB : ViewBinding> : Fragment(), BackPressedListener {
    protected open var mBinding: VB? = null
    protected var mParentContainer: ViewGroup? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mParentContainer = container
        mBinding = getViewBinding()
        mBinding?.root?.isClickable = true
        initView()
        return mBinding?.root
    }

    abstract fun initView()

    abstract fun getViewBinding(): VB

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initStatusBar()
        initNavigationBar()
    }

    open fun initStatusBar() {
        // default is empty
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

    override fun onDestroy() {
        super.onDestroy()
        mBinding = null
    }

    override fun handleBackPressed(): Boolean {
        return false
    }
}