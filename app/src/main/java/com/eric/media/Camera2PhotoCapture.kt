package com.eric.media

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.ImageFormat
import android.hardware.camera2.CameraCaptureSession
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraDevice
import android.hardware.camera2.CameraManager
import android.hardware.camera2.CaptureRequest
import android.media.Image
import android.media.ImageReader
import android.os.Handler
import android.os.HandlerThread
import android.view.Surface
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
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

    private lateinit var cameraDevice: CameraDevice
    private lateinit var captureSession: CameraCaptureSession
    private lateinit var imageReader: ImageReader
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

    fun startCameraAndTakePhoto(onImageSaved: (File) -> Unit) {
        if (permissionManager.hasPermissions()) {
            startBackgroundThread()
            openFrontCamera(onImageSaved)
        } else {
            permissionManager.requestPermissions()
        }
    }

    fun onRequestPermissionsResult(requestCode: Int, grantResults: IntArray, onPermissionDenied: () -> Unit) {
        permissionManager.handlePermissionResult(requestCode, grantResults,
            onSuccess = {
                startBackgroundThread()
                openFrontCamera {

                }
            },
            onFailure = {
                onPermissionDenied()
            }
        )
    }

    private fun startBackgroundThread() {
        val backgroundThread = HandlerThread("CameraBackground").also { it.start() }
        backgroundHandler = Handler(backgroundThread.looper)
    }

    private fun stopBackgroundThread() {
        backgroundHandler?.looper?.thread?.interrupt()
        backgroundHandler = null
    }

    @SuppressLint("MissingPermission")
    private fun openFrontCamera(onImageSaved: (File) -> Unit) {
        cameraId = cameraManager.cameraIdList.find { id ->
            val characteristics = cameraManager.getCameraCharacteristics(id)
            characteristics.get(CameraCharacteristics.LENS_FACING) == CameraCharacteristics.LENS_FACING_FRONT
        } ?: throw IllegalStateException("No front camera found")

        cameraManager.openCamera(cameraId, object : CameraDevice.StateCallback() {
            override fun onOpened(camera: CameraDevice) {
                cameraDevice = camera
                createCaptureSession(onImageSaved)
            }

            override fun onDisconnected(camera: CameraDevice) {
                cameraDevice.close()
            }

            override fun onError(camera: CameraDevice, error: Int) {
                cameraDevice.close()
            }
        }, backgroundHandler)
    }

    private fun createCaptureSession(onImageSaved: (File) -> Unit) {
        imageReader = ImageReader.newInstance(config.imageWidth, config.imageHeight, config.imageFormat, 1)
        imageReader.setOnImageAvailableListener({ reader ->
            val image = reader.acquireLatestImage()
            image?.let {
                saveImageToFile(it, onImageSaved)
                image.close()
            }
        }, backgroundHandler)

        val captureRequestBuilder = createCaptureRequestBuilder()

        cameraDevice.createCaptureSession(listOf(imageReader.surface), object : CameraCaptureSession.StateCallback() {
            override fun onConfigured(session: CameraCaptureSession) {
                captureSession = session
                captureSession.capture(captureRequestBuilder.build(), null, backgroundHandler)
            }

            override fun onConfigureFailed(session: CameraCaptureSession) {
            }
        }, backgroundHandler)
    }

    private fun createCaptureRequestBuilder(): CaptureRequest.Builder {
        val captureRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE)
        captureRequestBuilder.addTarget(imageReader.surface)

        val jpegOrientation = calculateJpegOrientation()
        captureRequestBuilder.set(CaptureRequest.JPEG_ORIENTATION, jpegOrientation)

        return captureRequestBuilder
    }

    private fun getDeviceRotation(): Int {
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val rotation = windowManager.defaultDisplay.rotation
        return when (rotation) {
            Surface.ROTATION_0 -> 0
            Surface.ROTATION_90 -> 90
            Surface.ROTATION_180 -> 180
            Surface.ROTATION_270 -> 270
            else -> 0
        }
    }

    private fun getCameraSensorOrientation(): Int {
        val characteristics = cameraManager.getCameraCharacteristics(cameraId)
        return characteristics.get(CameraCharacteristics.SENSOR_ORIENTATION) ?: 0
    }

    private fun calculateJpegOrientation(): Int {
        val deviceRotation = getDeviceRotation()
        val sensorOrientation = getCameraSensorOrientation()
        return (deviceRotation + sensorOrientation + 360) % 360
    }

    private fun saveImageToFile(image: Image, onImageSaved: (File) -> Unit) {
        val buffer = image.planes[0].buffer
        val bytes = ByteArray(buffer.remaining())
        buffer.get(bytes)

        val file = File(photoDirectory, "${config.photoFileNamePrefix}${SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())}.jpg")
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


class CameraConfig() {

    var imageWidth: Int = 640
    var imageHeight: Int = 480
    var imageFormat: Int = ImageFormat.JPEG

    // 可以扩展其他配置项，例如照片保存的前缀
    var photoFileNamePrefix: String = "IMG_"
}

