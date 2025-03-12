package com.eric.androidstudy

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import java.io.File

class AvifActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_avif)

        val imageView: ImageView = findViewById(R.id.imageView)

        // 读取应用私有 files 目录中的 AVIF 文件
        val avifFile = File(filesDir, "kimono.crop.avif")
        Glide.with(this)
            .load(avifFile)
            .into(imageView)
    }
}