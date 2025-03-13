package com.eric.androidstudy

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.eric.base.util.GlideApp
import java.io.File

class AvifActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_avif)

        val imageView: ImageView = findViewById(R.id.imageView)

        val file = File(filesDir, "1718924786_aniC6mEngif_file-converters-online.com_ytC.avif") // 获取 files 目录下的 a.avif

        Glide.with(this)
            .load(file)
            .into(imageView)

    }
}