
package com.eric.feature.vault

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.eric.feature.databinding.FragmentVaultBinding
import com.transsion.architecturemodule.base.fragment.BaseVMFragment

class VaultFragment : BaseVMFragment<FragmentVaultBinding, VaultFragmentViewModel>() {

    companion object {
        const val TAG = "VaultFragment"
        fun newInstance(args: Bundle? = null): VaultFragment {
            return VaultFragment().apply {
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

    override fun viewModelClass(): Class<VaultFragmentViewModel> = VaultFragmentViewModel::class.java

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentVaultBinding {
        return FragmentVaultBinding.inflate(layoutInflater, mParentContainer, false)
    }
}
