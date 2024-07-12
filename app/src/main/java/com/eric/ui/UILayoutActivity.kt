package com.eric.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.eric.androidstudy.databinding.LayoutFigmaAutoAiCodeBinding

class UILayoutActivity : AppCompatActivity() {

    private var mBinding: LayoutFigmaAutoAiCodeBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = LayoutFigmaAutoAiCodeBinding.inflate(layoutInflater)

        setContentView(binding.root)
    }
}