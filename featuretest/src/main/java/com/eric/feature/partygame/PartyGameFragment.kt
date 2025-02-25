
package com.eric.feature.partygame

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.blankj.utilcode.util.ToastUtils
import com.eric.base.aidl.IRemoteCalculator
import com.eric.base.logTd
import com.eric.base.servicebind.ServiceManagerClient
import com.eric.base.servicebind.ServiceManagerHelper
import com.eric.feature.databinding.FragmentPartygameBinding
import com.transsion.architecturemodule.base.fragment.BaseVMFragment


class PartyGameFragment : BaseVMFragment<FragmentPartygameBinding, PartyGameFragmentViewModel>() {


    private val serviceManagerClient = ServiceManagerClient()

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
        mBinding?.calaulator?.setOnClickListener {
            // 处理点击事件
            logTd("Calculator","远程调用方法的result被点击了")
            serviceManagerClient.queryCalculatorService(activity)
        }
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
