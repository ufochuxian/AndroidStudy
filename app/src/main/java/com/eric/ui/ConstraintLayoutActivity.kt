package com.eric.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.eric.androidstudy.databinding.LayoutConstraintActivityBinding

class ConstraintLayoutActivity :AppCompatActivity() {

    private var mBinding : LayoutConstraintActivityBinding? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = LayoutConstraintActivityBinding.inflate(layoutInflater)
        setContentView(mBinding?.root)
    }
}