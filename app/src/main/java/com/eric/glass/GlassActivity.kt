package com.eric.glass

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import com.eric.androidstudy.R

class GlassActivity : AppCompatActivity() {

    private lateinit var imageView: ImageView
    private lateinit var applyButton: Button
    private lateinit var radiusSeekBar: SeekBar
    private lateinit var originalBitmap: Bitmap
    private val frostedGlassNative = FrostedGlassNative()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_glass)

        imageView = findViewById(R.id.imageView)
        applyButton = findViewById(R.id.applyButton)
        radiusSeekBar = findViewById(R.id.radiusSeekBar)

        originalBitmap = BitmapFactory.decodeResource(resources, R.drawable.test)
        imageView.setImageBitmap(originalBitmap)

        applyButton.setOnClickListener {
            val radius = radiusSeekBar.progress
            val mutableBitmap = originalBitmap.copy(Bitmap.Config.ARGB_8888, true)
            frostedGlassNative.applyFrostedGlassEffect(mutableBitmap, radius)
            imageView.setImageBitmap(mutableBitmap)
        }
    }
}