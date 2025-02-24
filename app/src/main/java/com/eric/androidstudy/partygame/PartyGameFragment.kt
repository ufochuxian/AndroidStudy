
package com.eric.androidstudy.partygame

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.eric.androidstudy.databinding.FragmentPartygameBinding
import com.transsion.architecturemodule.base.fragment.BaseVMFragment

class PartyGameFragment : BaseVMFragment<FragmentPartygameBinding, PartyGameFragmentViewModel>() {

    companion object {
        const val TAG = "PartyGameFragment"
        fun newInstance(args: Bundle? = null): PartyGameFragment {
            return PartyGameFragment().apply {
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

    override fun viewModelClass(): Class<PartyGameFragmentViewModel> = PartyGameFragmentViewModel::class.java

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentPartygameBinding {
        return FragmentPartygameBinding.inflate(layoutInflater, mParentContainer, false)
    }
}
