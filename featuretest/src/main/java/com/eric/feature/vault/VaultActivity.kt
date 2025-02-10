
package com.eric.feature.vault

import android.os.Bundle
import com.eric.feature.R
import com.eric.feature.databinding.ActivityVaultBinding
import com.transsion.architecturemodule.base.activity.BaseVMActivity

class VaultActivity : BaseVMActivity<ActivityVaultBinding, VaultActivityViewModel>() {
    override fun initData() {
        // 初始化数据
    }

    override fun initView(savedInstanceState: Bundle?) {
        // 显示 Fragment
        showPage(R.id.container, VaultFragment.newInstance(), VaultFragment.TAG, true)
    }

    override fun initObserve() {
        // 观察 LiveData 变化
    }

    override fun initAction() {
        // 初始化点击事件等
    }

    override fun viewModelClass(): Class<VaultActivityViewModel> = VaultActivityViewModel::class.java

    override fun getViewBinding(): ActivityVaultBinding = ActivityVaultBinding.inflate(layoutInflater)
}
