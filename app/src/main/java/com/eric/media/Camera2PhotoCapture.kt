package com.eric.media

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.ImageFormat
import android.hardware.camera2.CameraCaptureSession
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraDevice
import android.hardware.camera2.CameraManager
import android.hardware.camera2.CaptureRequest
import android.hardware.camera2.TotalCaptureResult
import android.media.Image
import android.media.ImageReader
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import android.util.Size
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class Camera2PhotoCapture(
    private val context: AppCompatActivity,
    private var config: CameraConfig = CameraConfig()
) : DefaultLifecycleObserver {

    private val TAG = "Camera2PhotoCapture"

    private lateinit var cameraDevice: CameraDevice
    private lateinit var captureSession: CameraCaptureSession
    private lateinit var imageReader: ImageReader
    private var backgroundThread: HandlerThread? = null
    private var backgroundHandler: Handler? = null
    private val cameraManager: CameraManager by lazy {
        context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
    }
    private var permissionManager: PermissionManager = PermissionManager(context)
    private lateinit var cameraId: String
    private val photoDirectory: File by lazy {
        File(context.filesDir, "intruder_photos").apply {
            if (!exists()) mkdirs()
        }
    }

    init {
        context.lifecycle.addObserver(this) // 将观察者绑定到 Activity 的生命周期
    }

    fun takePhoto(onImageSaved: (File) -> Unit) {
        if (permissionManager.hasPermissions()) {
            startBackgroundThread()
            openFrontCamera(onImageSaved)
        } else {
            permissionManager.requestPermissions()
        }
    }

    private fun startBackgroundThread() {
        backgroundThread = HandlerThread("CameraBackground").also { it.start() }
        backgroundHandler = Handler(backgroundThread!!.looper)
    }

    private fun stopBackgroundThread() {
        backgroundThread?.quitSafely()
        backgroundThread = null
        backgroundHandler = null
    }

    @SuppressLint("MissingPermission")
    private fun openFrontCamera(onImageSaved: (File) -> Unit) {
        try {
            // 查找前置摄像头 ID
            cameraId = cameraManager.cameraIdList.find { id ->
                val characteristics = cameraManager.getCameraCharacteristics(id)
                characteristics.get(CameraCharacteristics.LENS_FACING) == CameraCharacteristics.LENS_FACING_FRONT
            } ?: throw IllegalStateException("No front camera found")

            // 获取相机支持的最佳分辨率
            val characteristics = cameraManager.getCameraCharacteristics(cameraId)
            val map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)
            val outputSizes = map?.getOutputSizes(ImageFormat.JPEG)
            val largestSize = outputSizes?.maxByOrNull { it.width * it.height } ?: Size(640, 480)
            config.imageWidth = largestSize.width
            config.imageHeight = largestSize.height

            imageReader = ImageReader.newInstance(
                config.imageWidth, config.imageHeight, config.imageFormat, 1
            )
            imageReader.setOnImageAvailableListener(onImageAvailableListener, backgroundHandler)

            cameraManager.openCamera(cameraId, object : CameraDevice.StateCallback() {
                override fun onOpened(camera: CameraDevice) {
                    cameraDevice = camera
                    createPreviewSession(onImageSaved)
                }

                override fun onDisconnected(camera: CameraDevice) {
                    camera.close()
                }

                override fun onError(camera: CameraDevice, error: Int) {
                    camera.close()
                    Log.e(TAG, "Camera error: $error")
                }
            }, backgroundHandler)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to open front camera: ${e.message}")
        }
    }

    private fun createPreviewSession(onImageSaved: (File) -> Unit) {
        try {
            val previewSurface = imageReader.surface

            val previewRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
            previewRequestBuilder.addTarget(previewSurface)

            cameraDevice.createCaptureSession(
                listOf(previewSurface),
                object : CameraCaptureSession.StateCallback() {
                    override fun onConfigured(session: CameraCaptureSession) {
                        captureSession = session
                        previewRequestBuilder.set(CaptureRequest.CONTROL_MODE, CaptureRequest.CONTROL_MODE_AUTO)
                        captureSession.setRepeatingRequest(previewRequestBuilder.build(), null, backgroundHandler)
                        // 短暂延迟后执行拍照
                        backgroundHandler?.postDelayed({
                            takePicture(onImageSaved)
                        }, 500)
                    }

                    override fun onConfigureFailed(session: CameraCaptureSession) {
                        Log.e(TAG, "Failed to configure camera")
                    }
                },
                backgroundHandler
            )
        } catch (e: Exception) {
            Log.e(TAG, "Failed to create preview session: ${e.message}")
        }
    }

    private fun takePicture(onImageSaved: (File) -> Unit) {
        try {
            val captureRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE)
            captureRequestBuilder.addTarget(imageReader.surface)

            // 获取相机传感器的方向
            val sensorOrientation = cameraManager.getCameraCharacteristics(cameraId)
                .get(CameraCharacteristics.SENSOR_ORIENTATION) ?: 0

            // 强制将 JPEG 图片方向设置为竖屏
            val jpegOrientation = if (sensorOrientation == 90 || sensorOrientation == 270) {
                0 // 如果传感器已经是竖屏方向，则不需要额外旋转
            } else {
                90 // 否则旋转90度以强制竖屏
            }

            captureRequestBuilder.set(CaptureRequest.JPEG_ORIENTATION, jpegOrientation)

            captureSession.stopRepeating() // 停止重复预览请求

            captureSession.capture(captureRequestBuilder.build(), object : CameraCaptureSession.CaptureCallback() {
                override fun onCaptureCompleted(
                    session: CameraCaptureSession,
                    request: CaptureRequest,
                    result: TotalCaptureResult
                ) {
                    Log.d(TAG, "Image captured in portrait mode")
                    captureSession.close() // 拍摄完成后关闭会话
                    cameraDevice.close() // 关闭相机
                }
            }, backgroundHandler)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to take picture: ${e.message}")
        }
    }




    private val onImageAvailableListener = ImageReader.OnImageAvailableListener { reader ->
        backgroundHandler?.post {
            var image: Image? = null
            try {
                image = reader.acquireLatestImage()
                image?.let {
                    saveImageToFile(it) { file ->
                        Log.d(TAG, "Image saved: ${file.absolutePath}")
                        // 处理完图像后，停止监听，防止重复捕获
                        reader.setOnImageAvailableListener(null, null)
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Failed to process image: ${e.message}")
            } finally {
                image?.close()
            }
        }
    }


    private fun saveImageToFile(image: Image, onImageSaved: (File) -> Unit) {
        val buffer = image.planes[0].buffer
        val bytes = ByteArray(buffer.remaining())
        buffer.get(bytes)

        val file = File(
            photoDirectory,
            "${config.photoFileNamePrefix}${SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())}.jpg"
        )
        FileOutputStream(file).use {
            it.write(bytes)
        }

        onImageSaved(file)
    }

    // 使用 DefaultLifecycleObserver 监听生命周期中的 onDestroy 事件
    override fun onDestroy(owner: LifecycleOwner) {
        if (::captureSession.isInitialized) {
            captureSession.close()
        }
        if (::cameraDevice.isInitialized) {
            cameraDevice.close()
        }
        if (::imageReader.isInitialized) {
            imageReader.close()
        }
        stopBackgroundThread()
    }
}
