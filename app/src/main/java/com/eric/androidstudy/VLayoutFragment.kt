package com.eric.androidstudy

import android.os.Bundle
import com.eric.VLayoutChildFragment
import com.eric.VLayoutFragmentViewModel
import com.eric.androidstudy.VLayoutActivity.Companion.VLAYOUT_TAG
import com.eric.androidstudy.databinding.VlayoutFragmentBinding
import com.eric.base.logTd
import com.transsion.architecturemodule.base.fragment.BaseVMFragment
import com.transsion.architecturemodule.base.ktx.showFragment


class VLayoutFragment : BaseVMFragment<VlayoutFragmentBinding, VLayoutFragmentViewModel>() {

    companion object {
        const val TAG = "VLayoutFragment"

        fun newInstance(args : Bundle? = null) : VLayoutFragment {
            val fragment = VLayoutFragment()
            args?.apply {
                fragment.arguments = args
            }
            return fragment
        }
    }

    override fun initView() {
        logTd(VLAYOUT_TAG + VLayoutFragment.TAG,"VLayoutFragment initView")

    }

    override fun initObserve() {
        logTd(VLAYOUT_TAG + VLayoutFragment.TAG,"VLayoutFragment initObserve")
    }

    override fun initData() {
        logTd(VLAYOUT_TAG + VLayoutFragment.TAG,"VLayoutFragment initData")
    }

    override fun initAction() {
        logTd(VLAYOUT_TAG + VLayoutFragment.TAG,"VLayoutFragment initAction")
        mBinding?.goToSecond?.setOnClickListener {
            activity?.showFragment(R.id.container,VLayoutChildFragment.newInstance(),VLayoutChildFragment.TAG,true)
            logTd(VLAYOUT_TAG + VLayoutFragment.TAG,"VLayoutFragment showFragment")
        }


    }

    override fun onBackKeyPress() {
        super.onBackKeyPress()
        logTd(VLAYOUT_TAG + VLayoutFragment.TAG,"VLayoutFragment onBackKeyPress")
    }

    override fun viewModelClass(): Class<VLayoutFragmentViewModel> = VLayoutFragmentViewModel::class.java

    override fun getViewBinding(): VlayoutFragmentBinding = VlayoutFragmentBinding.inflate(layoutInflater,mParentContainer,false)
}