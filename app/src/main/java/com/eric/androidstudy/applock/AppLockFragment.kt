
package com.eric.androidstudy.applock

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.eric.androidstudy.R
import com.eric.androidstudy.databinding.FragmentApplockBinding
import com.example.guestmode.GuestModeDialog
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
        context?.let {
            val appIcons = listOf(R.drawable.shapeable_view, R.drawable.shapeable_view, R.drawable.shapeable_view, R.drawable.shapeable_view)
            val dialog = GuestModeDialog(
                context = it,
                appIcons = appIcons,
                onActivate = { Toast.makeText(it, "Guest Mode Activated", Toast.LENGTH_SHORT).show() },
                onCancel = { Toast.makeText(it, "Cancelled", Toast.LENGTH_SHORT).show() }
            )
            dialog.show()
        }


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
