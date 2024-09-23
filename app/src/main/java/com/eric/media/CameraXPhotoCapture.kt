import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.AudioManager
import android.media.ExifInterface
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*
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
        (context as? LifecycleOwner)?.lifecycle?.addObserver(this)
        startCamera()
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)

        cameraProviderFuture.addListener({
            cameraProvider = cameraProviderFuture.get()

            val cameraSelector = if (useFrontCamera) {
                CameraSelector.DEFAULT_FRONT_CAMERA
            } else {
                CameraSelector.DEFAULT_BACK_CAMERA
            }

            val rotation = (context as AppCompatActivity).windowManager.defaultDisplay.rotation

            imageCapture = ImageCapture.Builder()
                .setTargetRotation(rotation)
                .setFlashMode(ImageCapture.FLASH_MODE_OFF)
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

    fun takePhoto(onImageSaved: (File?) -> Unit) {
        val imageCapture = imageCapture ?: return

        val photoFile = File(
            photoDir,
            SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(System.currentTimeMillis()) + ".jpg"
        )

        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        setDeviceSilentMode(true)

        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(context),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    setDeviceSilentMode(false)
                    rotateImageIfRequired(photoFile) // 手动调整照片方向
                    onImageSaved(photoFile)
                }

                override fun onError(exc: ImageCaptureException) {
                    setDeviceSilentMode(false)
                    exc.printStackTrace()
                    onImageSaved(null)
                }
            }
        )
    }

    // 手动调整图片方向
    private fun rotateImageIfRequired(photoFile: File) {
        try {
            val exif = ExifInterface(photoFile.absolutePath)
            val orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)

            val bitmap = BitmapFactory.decodeFile(photoFile.absolutePath)
            val rotatedBitmap = when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> rotateBitmap(bitmap, 90f)
                ExifInterface.ORIENTATION_ROTATE_180 -> rotateBitmap(bitmap, 180f)
                ExifInterface.ORIENTATION_ROTATE_270 -> rotateBitmap(bitmap, 270f)
                else -> bitmap
            }

            // 保存旋转后的图像
            saveBitmapToFile(rotatedBitmap, photoFile)

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // 旋转Bitmap
    private fun rotateBitmap(bitmap: Bitmap, degrees: Float): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(degrees)
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }

    // 保存旋转后的图片
    private fun saveBitmapToFile(bitmap: Bitmap, photoFile: File) {
        FileOutputStream(photoFile).use { out ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
        }
    }

    private fun stopCamera() {
        cameraProvider?.unbindAll()
        cameraExecutor.shutdown()
    }

    private fun setDeviceSilentMode(enable: Boolean) {
        val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        if (enable) {
            audioManager.setStreamVolume(AudioManager.STREAM_SYSTEM, 0, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE)
        } else {
            audioManager.setStreamVolume(AudioManager.STREAM_SYSTEM, 5, AudioManager.FLAG_ALLOW_RINGER_MODES)
        }
    }

    override fun onDestroy(owner: LifecycleOwner) {
        stopCamera()
        (context as? LifecycleOwner)?.lifecycle?.removeObserver(this)
    }
}
