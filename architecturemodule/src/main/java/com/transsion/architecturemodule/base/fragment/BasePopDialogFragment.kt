package com.transsion.architecturemodule.base.fragment

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.viewbinding.ViewBinding


abstract class BasePopDialogFragment<VB : ViewBinding> : DialogFragment() {

    protected open var mBinding: VB? = null

    protected var mParentContainer : ViewGroup? = null

    private var mGravity : Int = Gravity.BOTTOM

    val GRAVITY_DIRECTION = "gravity_direction"
    val ENABLE_CANCEL = "enable_cancel"

    var enableCancelable = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.run {
            mGravity = getInt(GRAVITY_DIRECTION)
            enableCancelable = getBoolean(ENABLE_CANCEL,true)
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): android.app.Dialog {
        return object : android.app.Dialog(requireContext(), theme) {
            override fun onBackPressed() {
                // Handle the back button event
                onBackPressPopClick()
                dismissAllowingStateLoss()
            }
        }
    }

    open fun onBackPressPopClick() {

    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mParentContainer= container
        mBinding = getViewBinding()
        return mBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI(view)
        initAction()
        initObserver()

    }

    open fun initObserver() {

    }

    open fun initAction() {

    }

    open fun initUI(view: View) {

    }

    abstract fun getViewBinding() : VB

    @SuppressLint("ClickableViewAccessibility")
    override fun onStart() {
        super.onStart()
        if (!enableCancelable) {
            dialog?.setCancelable(false)
            dialog?.window?.decorView?.setOnTouchListener { _, event ->
                if (event.action == MotionEvent.ACTION_DOWN) {
                    val dialogView = dialog?.window?.decorView
                    if (dialogView != null) {
                        val outRect = android.graphics.Rect()
                        dialogView.getGlobalVisibleRect(outRect)
                        if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
                            return@setOnTouchListener true
                        }
                    }
                }
                return@setOnTouchListener false
            }
        }
        val window = dialog?.window
        val params = window?.attributes
        params?.gravity = mGravity
        params?.width = WindowManager.LayoutParams.MATCH_PARENT
        window?.attributes = params
        //设置背景透明
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    fun safeShow(fragmentManager: FragmentManager,tag : String?) {
        try {
            show(fragmentManager,tag)
        }catch (e : Exception) {
            Log.i("BasePopDialogFragment",e.toString())
        }
    }

    fun safeDismiss() {
        try {
            dismissAllowingStateLoss()
        }catch (e : Exception) {
            Log.i("BasePopDialogFragment",e.toString())
        }
    }

    fun isDialogShowing(): Boolean {
        return dialog?.isShowing ?: false
    }

}