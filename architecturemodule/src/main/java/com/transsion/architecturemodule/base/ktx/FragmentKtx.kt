package com.transsion.architecturemodule.base.ktx

import android.os.Bundle
import androidx.fragment.app.Fragment


// 扩展函数用于创建带有参数 Bundle 的 Fragment 对象
fun Fragment.newInstance(bundle: Bundle): Fragment {
    return Fragment().apply {
        arguments = bundle
    }
}