package com.eric.androidstudy

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide

class AvifActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_avif)

        val imageView: ImageView = findViewById(R.id.imageView)

        Glide.with(this)
            .load("file:///storage/emulated/0/Download/sample.avif")
            .into(imageView)
    }
}