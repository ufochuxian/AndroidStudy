package com.transsion.architecturemodule.base.activity

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.transsion.architecturemodule.base.interfaces.BackPressedListener
import androidx.viewbinding.ViewBinding
import com.transsion.architecturemodule.BuildConfig
import com.transsion.architecturemodule.base.ktx.replaceFragment
import com.transsion.architecturemodule.base.ktx.showFragment

/**
 *
 *  @author chen
 */
private const val TAG = "BaseActivity"

abstract class BaseActivity<VB : ViewBinding> : AppCompatActivity() {
    protected open var mBinding: VB? = null

    protected var curFragmentTag: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = getViewBinding()
        setContentView(mBinding?.root)
        initData()
        initView(savedInstanceState)
        initAction()
//        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
//            override fun handleOnBackPressed() {
//                onIMBackPressed()
//            }
//        })
    }


    /**
     * 用于初始化数据
     */
    abstract fun initData()

    abstract fun initView(savedInstanceState : Bundle?)

    /**
     * 用于初始化点击事件
     */
    abstract fun initAction()

    /**
     * 返回键处理，默认finish
     */
    protected open fun onIMBackPressed(setTrackParam: ((bundle: Bundle) -> Unit)? = null) {
        finish()
    }

    abstract fun getViewBinding(): VB

    override fun onResume() {
        super.onResume()

    }

    open fun getBundle(): Bundle? {
        return null
    }

    open fun isTrack(): Boolean {
        return true
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        mBinding = null
    }

//    /**
//     * 拦截事件
//     */
//    open fun interceptBackPressed(): Boolean {
//        supportFragmentManager.fragments.forEach {
//            if (it is BackPressedListener) {
//                if (it.handleBackPressed()) {
//                    return true
//                }
//            }
//        }
//        return false
//    }

//    override fun onBackPressed() {
//        if (!interceptBackPressed()) {
//            reportBackEvent()
//            if (supportFragmentManager.fragments.size > 1) {
//                supportFragmentManager.popBackStackImmediate()
//            } else {
//                super.onBackPressed()
//            }
//        }
//    }

    open fun reportBackEvent() {

    }

    protected fun replacePage(
        containerID: Int,
        fr: Fragment,
        tag: String? = null,
        isAddToBackStack: Boolean = false
    ) {
        curFragmentTag = tag
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "curFragmentTag:$curFragmentTag")
        }
        replaceFragment(containerID, fr, tag, isAddToBackStack)
    }

    protected fun showPage(
        containerID: Int,
        fr: Fragment,
        tag: String? = null,
        isAddToBackStack: Boolean = false
    ) {
        curFragmentTag = tag
        if (BuildConfig.DEBUG) {
            Log.v(TAG, "curFragmentTag:$curFragmentTag")
        }
        showFragment(containerID, fr, tag, isAddToBackStack)
    }
}