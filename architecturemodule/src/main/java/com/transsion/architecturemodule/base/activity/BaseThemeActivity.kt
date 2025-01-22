package com.transsion.architecturemodule.base.activity

import android.os.Bundle
import com.transsion.architecturemodule.base.ktx.useImmersiveWindow
import com.transsion.architecturemodule.base.viewmodel.BaseViewModel
import com.transsion.architecturemodule.databinding.LayoutBaseThemeActivityBinding

abstract class BaseThemeActivity<VM : BaseViewModel> : BaseVMActivity<LayoutBaseThemeActivityBinding, VM>() {
    override fun getViewBinding(): LayoutBaseThemeActivityBinding = LayoutBaseThemeActivityBinding.inflate(layoutInflater)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.useImmersiveWindow(false)
    }

    abstract override fun initView(savedInstanceState: Bundle?)
}