package com.transsion.architecturemodule.base.ktx

import android.os.SystemClock
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.transsion.architecturemodule.base.util.LayoutUtils

fun View.setDebouncedOnClickListener(interval: Long = 500L, onClick: (View) -> Unit) {
    var lastClickTime = 0L
    setOnClickListener { view ->
        val currentTime = SystemClock.elapsedRealtime()
        if (currentTime - lastClickTime >= interval) {
            lastClickTime = currentTime
            onClick(view)
        }
    }
}

fun EditText.addTextChangedListenerWithMaxLength(maxLength: Int, textWatcher: TextWatcher) {

    addTextChangedListener(object : TextWatcher {

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            textWatcher.beforeTextChanged(s, start, count, after)
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            s?.let {
                if (s.length > maxLength) {
                    setText(s.toString().substring(0, maxLength))
                    setSelection(maxLength)
                    Toast.makeText(context, "max input answer length is 64", Toast.LENGTH_SHORT).show()
                }
            }
        }

        override fun afterTextChanged(s: Editable?) {
            textWatcher.afterTextChanged(s)

        }
    })

}

fun View.setLayoutDirectionWithRTLCheck() {
    if (LayoutUtils.isRtl()) {
        this.rotation = -180f
    }
}