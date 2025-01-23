package com.eric.androidstudy

import android.os.Bundle
import com.eric.androidstudy.databinding.ActivityVlayoutBinding
import com.eric.base.logTd
import com.transsion.architecturemodule.base.activity.BaseVMActivity


class VLayoutActivity : BaseVMActivity<ActivityVlayoutBinding, VLayoutViewModel>() {

    companion object {
        val VLAYOUT_TAG = "VLayoutActivity"

    }

    override fun initData() {
        logTd(VLAYOUT_TAG + VLayoutFragment.TAG,"VLayoutActivity initData")
    }

    override fun initView(savedInstanceState: Bundle?) {
        logTd(VLAYOUT_TAG + VLayoutFragment.TAG,"VLayoutActivity initView")
        showPage(R.id.container,VLayoutFragment.newInstance(),VLayoutFragment.TAG,true)
    }

    override fun initObserve() {
        logTd(VLAYOUT_TAG + VLayoutFragment.TAG,"VLayoutActivity initObserve")
    }

    override fun initAction() {
        logTd(VLAYOUT_TAG + VLayoutFragment.TAG,"VLayoutActivity initAction")

    }

    override fun onDestroy() {
        super.onDestroy()
        logTd(VLAYOUT_TAG + VLayoutFragment.TAG,"VLayoutActivity onDestroy")
    }

    override fun viewModelClass(): Class<VLayoutViewModel> = VLayoutViewModel::class.java

    override fun getViewBinding(): ActivityVlayoutBinding = ActivityVlayoutBinding.inflate(layoutInflater)
}