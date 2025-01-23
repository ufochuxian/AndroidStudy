package com.eric

import android.os.Bundle
import com.eric.androidstudy.VLayoutActivity
import com.eric.androidstudy.VLayoutActivity.Companion.VLAYOUT_TAG
import com.eric.androidstudy.VLayoutChildViewModel
import com.eric.androidstudy.VLayoutFragment
import com.eric.androidstudy.databinding.LayoutVlayoutChildFragmentBinding
import com.eric.base.logTd
import com.transsion.architecturemodule.base.fragment.BaseVMFragment

class VLayoutChildFragment : BaseVMFragment<LayoutVlayoutChildFragmentBinding, VLayoutChildViewModel>() {

    companion object {
        const val TAG = "VLayoutChildFragment"
        fun newInstance(args : Bundle? = null) : VLayoutChildFragment{
            val fragment = VLayoutChildFragment()
            args?.apply {
                fragment.arguments = args
            }

            return fragment
        }
    }


    override fun initObserve() {
        logTd(VLayoutActivity.VLAYOUT_TAG + TAG, "VLayoutChildFragment initObserve")

    }

    override fun viewModelClass(): Class<VLayoutChildViewModel> = VLayoutChildViewModel::class.java

    override fun initData() {
        logTd(VLayoutActivity.VLAYOUT_TAG + TAG, "VLayoutChildFragment initData")
    }

    override fun initView() {
        logTd(VLayoutActivity.VLAYOUT_TAG + TAG, "VLayoutChildFragment initView")
    }

    override fun initAction() {
        logTd(VLayoutActivity.VLAYOUT_TAG + TAG, "VLayoutChildFragment initAction")

    }

    override fun onBackKeyPress() {
        super.onBackKeyPress()
        logTd(VLAYOUT_TAG + TAG,"VLayoutChildFragment onBackKeyPress")
    }

    override fun getViewBinding(): LayoutVlayoutChildFragmentBinding = LayoutVlayoutChildFragmentBinding.inflate(layoutInflater,mParentContainer,false)

    override fun onDestroy() {
        super.onDestroy()
        logTd(VLayoutActivity.VLAYOUT_TAG + TAG, "VLayoutChildFragment onDestroy")
    }
}