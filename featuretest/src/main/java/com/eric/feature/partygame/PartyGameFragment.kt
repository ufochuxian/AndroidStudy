package com.eric.feature.partygame

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.RemoteException
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.blankj.utilcode.util.ProcessUtils
import com.eric.base.aidl.DeviceInfo
import com.eric.base.aidl.IRemoteCalculator
import com.eric.base.aidl.VersionInfo
import com.eric.base.logTd
import com.eric.base.servicebind.rpc.ServiceManagerClient
import com.eric.base.servicebind.rpc.RpcServiceName
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

    @SuppressLint("LogNotTimber")
    override fun initView(view: View, savedInstanceState: Bundle?) {
        // 初始化 UI
        mBinding?.calaulator?.setOnClickListener {
            // 处理点击事件
            logTd("${RpcServiceName.REMOTE_CALCULATOR}", "远程调用方法的result被点击了")
            val client = ServiceManagerClient()
            client.queryService<IRemoteCalculator>(context, "${RpcServiceName.REMOTE_CALCULATOR}",  // 转换器：将 IBinder 转换为 IRemoteCalculator 代理对象
                { binder -> IRemoteCalculator.Stub.asInterface(binder) },  // 查询结果的回调
                { service ->
                    service?.let {
                        try {
                            val result = service.get(DeviceInfo(VersionInfo("firstVersion",0)), 3)
                            Log.d("Client", "Calculator 方法调用结果: = ${result.description}")
                            logTd("rpc","调用Calculator服务，pid:${ProcessUtils.getCurrentProcessName()}")
                        } catch (e: RemoteException) {
                            Log.e("Client", "调用 Calculator 服务失败", e)
                        }
                    } ?: {
                        Log.w("Client", "未找到 Calculator 服务");
                    }
                })
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
