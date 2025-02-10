
package com.eric.androidstudy.applock

import android.os.Bundle
import com.eric.androidstudy.R
import com.eric.androidstudy.databinding.ActivityApplockBinding
import com.transsion.architecturemodule.base.activity.BaseVMActivity

class AppLockActivity : BaseVMActivity<ActivityApplockBinding, AppLockActivityViewModel>() {
    override fun initData() {
        // 初始化数据
    }

    override fun initView(savedInstanceState: Bundle?) {
        // 显示 Fragment
        showPage(R.id.container, AppLockFragment.newInstance(), AppLockFragment.TAG, true)
    }

    override fun initObserve() {
        // 观察 LiveData 变化
    }

    override fun initAction() {
        // 初始化点击事件等
    }

    override fun viewModelClass(): Class<AppLockActivityViewModel> = AppLockActivityViewModel::class.java

    override fun getViewBinding(): ActivityApplockBinding = ActivityApplockBinding.inflate(layoutInflater)
}
