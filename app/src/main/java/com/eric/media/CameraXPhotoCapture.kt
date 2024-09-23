package com.eric.androidstudy

import android.content.Context
import android.media.AudioManager
import android.view.Surface
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CameraXPhotoCapture(private val context: AppCompatActivity, private val useFrontCamera: Boolean) : DefaultLifecycleObserver {

    private var imageCapture: ImageCapture? = null
    private var cameraExecutor: ExecutorService = Executors.newSingleThreadExecutor()
    private var cameraProvider: ProcessCameraProvider? = null

    // 定义拍照文件存储目录
    private val photoDir: File by lazy {
        File(context.filesDir, "intruder_photos").apply {
            if (!exists()) mkdirs() // 创建目录
        }
    }

    init {
        // 注册到Activity的生命周期中
        (context as? LifecycleOwner)?.lifecycle?.addObserver(this)
        startCamera()
    }

    /**
     * 初始化 CameraX，仅绑定 ImageCapture
     */
    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)

        cameraProviderFuture.addListener({
            cameraProvider = cameraProviderFuture.get()

            // 根据参数选择前置或后置摄像头
            val cameraSelector = if (useFrontCamera) {
                CameraSelector.DEFAULT_FRONT_CAMERA
            } else {
                CameraSelector.DEFAULT_BACK_CAMERA
            }

            // 获取当前设备的旋转方向
            val rotation = context.windowManager.defaultDisplay.rotation

            imageCapture = ImageCapture.Builder()
                .setTargetRotation(Surface.ROTATION_90) // 设置目标旋转角度
                .setFlashMode(ImageCapture.FLASH_MODE_OFF) // 关闭闪光灯
                .build()

            try {
                cameraProvider?.unbindAll()
                cameraProvider?.bindToLifecycle(
                    context as LifecycleOwner,
                    cameraSelector,
                    imageCapture
                )
            } catch (exc: Exception) {
                exc.printStackTrace()
            }
        }, ContextCompat.getMainExecutor(context))
    }

    /**
     * 拍照并将图片保存到应用私有目录
     */
    fun takePhoto(onImageSaved: (File?) -> Unit) {
        val imageCapture = imageCapture ?: return

        // 构建文件名和路径到私有目录
        val photoFile = File(
            photoDir,
            SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(System.currentTimeMillis()) + ".jpg"
        )

        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        // 静音模式
//        setDeviceSilentMode(true)

        // 拍照
        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(context),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
//                    setDeviceSilentMode(false) // 恢复音量
                    onImageSaved(photoFile) // 拍照成功后返回图片路径
                }

                override fun onError(exc: ImageCaptureException) {
//                    setDeviceSilentMode(false) // 恢复音量
                    exc.printStackTrace()
                    onImageSaved(null) // 出错时返回 null
                }
            }
        )
    }

    // 停止相机并释放资源
    private fun stopCamera() {
        cameraProvider?.unbindAll() // 解除相机的所有绑定
        cameraExecutor.shutdown() // 关闭执行器
    }

    // 设置设备静音模式
    private fun setDeviceSilentMode(enable: Boolean) {
        val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        if (enable) {
            audioManager.setStreamVolume(AudioManager.STREAM_SYSTEM, 0, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE)
        } else {
            // 恢复音量到之前的值
            audioManager.setStreamVolume(AudioManager.STREAM_SYSTEM, 5, AudioManager.FLAG_ALLOW_RINGER_MODES)
        }
    }

    // 使用LifecycleObserver在onDestroy时释放资源
    override fun onDestroy(owner: LifecycleOwner) {
        stopCamera() // Activity 或 Fragment 销毁时自动释放相机资源
        (context as? LifecycleOwner)?.lifecycle?.removeObserver(this) // 移除LifecycleObserver
    }
}
