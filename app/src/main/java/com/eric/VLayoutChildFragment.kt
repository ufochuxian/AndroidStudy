package com.eric

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.eric.androidstudy.R
import com.eric.androidstudy.VLayoutActivity
import com.eric.androidstudy.VLayoutActivity.Companion.VLAYOUT_TAG
import com.eric.androidstudy.VLayoutChildViewModel
import com.eric.androidstudy.VLayoutFragment
import com.eric.androidstudy.databinding.LayoutVlayoutChildFragmentBinding
import com.eric.base.logTd
import com.transsion.architecturemodule.base.fragment.BaseVMFragment
import com.transsion.architecturemodule.base.ktx.showFragment

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
        logTd(VLayoutActivity.VLAYOUT_TAG + TAG, "${this.toString()} VLayoutChildFragment initObserve")

    }

    override fun viewModelClass(): Class<VLayoutChildViewModel> = VLayoutChildViewModel::class.java

    override fun initData() {
        logTd(VLayoutActivity.VLAYOUT_TAG + TAG, "${this.toString()} VLayoutChildFragment initData")
    }


    override fun initView(view: View, savedInstanceState: Bundle?) {
        logTd(VLayoutActivity.VLAYOUT_TAG + TAG, "${this.toString()} VLayoutChildFragment initView")
        mBinding?.goToThree?.setOnClickListener {
            activity?.showFragment(R.id.container,VLayoutChildFragment.newInstance(), TAG,true)
        }
    }

    override fun initAction() {
        logTd(VLayoutActivity.VLAYOUT_TAG + TAG, "${this.toString()} VLayoutChildFragment initAction")

    }

    override fun onBackKeyPress() {
        super.onBackKeyPress()
        logTd(VLAYOUT_TAG + TAG,"${this.toString()} VLayoutChildFragment onBackKeyPress")
    }

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?): LayoutVlayoutChildFragmentBinding = LayoutVlayoutChildFragmentBinding.inflate(layoutInflater,mParentContainer,false)

    override fun onDestroy() {
        super.onDestroy()
        logTd(VLayoutActivity.VLAYOUT_TAG + TAG, "${this.toString()} + VLayoutChildFragment onDestroy")
    }
}