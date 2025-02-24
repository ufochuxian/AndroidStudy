
package com.eric.feature.partygame

import android.os.Bundle
import com.eric.feature.R
import com.eric.feature.databinding.ActivityPartyGameBinding
import com.transsion.architecturemodule.base.activity.BaseVMActivity

class PartyGameActivity : BaseVMActivity<ActivityPartyGameBinding, PartyGameActivityViewModel>() {
    override fun initData() {
        // 初始化数据
    }

    override fun initView(savedInstanceState: Bundle?) {
        // 显示 Fragment
        showPage(R.id.container, PartyGameFragment.newInstance(), PartyGameFragment.TAG, true)
    }

    override fun initObserve() {
        // 观察 LiveData 变化
    }

    override fun initAction() {
        // 初始化点击事件等
    }

    override fun viewModelClass(): Class<PartyGameActivityViewModel> = PartyGameActivityViewModel::class.java

    override fun getViewBinding(): ActivityPartyGameBinding = ActivityPartyGameBinding.inflate(layoutInflater)
}
