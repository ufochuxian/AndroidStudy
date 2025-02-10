
package com.eric.androidstudy.applock

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.eric.androidstudy.databinding.FragmentApplockBinding
import com.transsion.architecturemodule.base.fragment.BaseVMFragment

class AppLockFragment : BaseVMFragment<FragmentApplockBinding, AppLockFragmentViewModel>() {

    companion object {
        const val TAG = "AppLockFragment"
        fun newInstance(args: Bundle? = null): AppLockFragment {
            return AppLockFragment().apply {
                arguments = args
            }
        }
    }

    override fun initData() {
        // 初始化数据
    }

    override fun initView(view: View, savedInstanceState: Bundle?) {
        // 初始化 UI
    }

    override fun initAction() {
        // 处理点击事件等
    }

    override fun initObserve() {
        // 观察 LiveData 变化
    }

    override fun viewModelClass(): Class<AppLockFragmentViewModel> = AppLockFragmentViewModel::class.java

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentApplockBinding {
        return FragmentApplockBinding.inflate(layoutInflater, mParentContainer, false)
    }
}
