package com.example.guestmode

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import android.view.WindowManager
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.eric.feature.R

class GuestModeDialog(
    context: Context,
    private val appIcons: List<Int>,
    private val onActivate: () -> Unit,
    private val onCancel: () -> Unit
) : Dialog(context) {

    init {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_guest_mode)

        val recyclerView: RecyclerView = findViewById(R.id.rvAppIcons)
        val btnCancel: TextView = findViewById(R.id.btnCancel)
        val btnActivate: TextView = findViewById(R.id.btnActivate)

        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = AppIconAdapter(appIcons)

        btnCancel.setOnClickListener {
            onCancel()
            dismiss()
        }

        btnActivate.setOnClickListener {
            onActivate()
            dismiss()
        }

        // 让 Dialog 的宽度匹配 Figma 设计
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
    }
}

