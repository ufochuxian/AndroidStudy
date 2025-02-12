package com.eric.androidstudy.applock

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.eric.androidstudy.R
import com.transsion.architecturemodule.base.viewmodel.BaseViewModel
import org.opencv.android.Utils
import org.opencv.core.Mat
import org.opencv.core.Point
import org.opencv.core.Scalar
import org.opencv.core.Size
import org.opencv.dnn.Dnn
import org.opencv.dnn.Net
import org.opencv.imgcodecs.Imgcodecs
import org.opencv.imgproc.Imgproc
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


class AppLockActivityViewModel : BaseViewModel() {

    // 复制模型文件到 Android data 目录
    fun copyAssetToCache(context: Context, assetFileName: String): String {
        val cacheFile: File = File(context.cacheDir, assetFileName)
        try {
            context.assets.open(assetFileName).use { `is` ->
                FileOutputStream(cacheFile).use { os ->
                    val buffer = ByteArray(1024)
                    var length: Int
                    while ((`is`.read(buffer).also { length = it }) > 0) {
                        os.write(buffer, 0, length)
                    }
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return cacheFile.absolutePath
    }

    fun faceDetect(context: Context) {
        val protoPath = copyAssetToCache(context, "deploy.prototxt")
        val modelPath = copyAssetToCache(context, "res10_300x300_ssd_iter_140000.caffemodel")

        Timber.tag("FaceDetection").d("Proto path: $protoPath")
        Timber.tag("FaceDetection").d("Model path: $modelPath")

        val net: Net = Dnn.readNetFromCaffe(protoPath, modelPath)
        if (net.empty()) {
            Timber.tag("FaceDetection").e("模型加载失败！请检查文件路径")
            return
        }

        val image: Mat = loadDrawableToMat(context, R.drawable.person)
        Timber.tag("FaceDetection").d("Image Size: ${image.size()} channels: ${image.channels()}")

        // 确保 Mat 是 BGR 格式（3 通道）
        if (image.channels() == 4) {
            Imgproc.cvtColor(image, image, Imgproc.COLOR_RGBA2BGR)
        }

        // 归一化 blob 预处理
        val blob = Dnn.blobFromImage(
            image, 1.0, Size(300.0, 300.0),
            Scalar(0.0, 0.0, 0.0), true, false // 测试不同归一化参数
        )
        net.setInput(blob)

        val detections = net.forward()
        Timber.tag("FaceDetection").d("Detections Mat: ${detections.size()} channels: ${detections.channels()}")

        if (detections.rows() <= 0) {
            Timber.tag("FaceDetection").e("❌ 人脸检测失败！")
            return
        }

        for (i in 0 until detections.rows()) {
            val confidence = detections.get(i, 2)[0].toFloat()
            if (confidence > 0.2) { // 降低置信度阈值
                val x1 = (detections.get(i, 3)[0] * image.cols()).toInt()
                val y1 = (detections.get(i, 4)[0] * image.rows()).toInt()
                val x2 = (detections.get(i, 5)[0] * image.cols()).toInt()
                val y2 = (detections.get(i, 6)[0] * image.rows()).toInt()

                Timber.tag("FaceDetection").d("✅ 人脸检测成功: [$x1, $y1, $x2, $y2]")

                Imgproc.rectangle(image, Point(x1.toDouble(), y1.toDouble()), Point(x2.toDouble(), y2.toDouble()), Scalar(255.0, 0.0, 0.0), 10)
            }
        }

        // 确保颜色正确
        Imgproc.cvtColor(image, image, Imgproc.COLOR_BGR2RGB)
        val savedPath = saveImageToFiles(context, image, "face_detected.jpg")
        Timber.tag("FaceDetection").d("✅ 检测结果已保存: $savedPath")
    }





    // 读取 drawable 目录下的图片并转换为 Mat
    fun loadDrawableToMat(context: Context, drawableId: Int): Mat {
        val bitmap: Bitmap = BitmapFactory.decodeResource(context.resources, drawableId)

        val mat = Mat()
        Utils.bitmapToMat(bitmap, mat) // Bitmap 转 Mat

        // 检查通道数并转换为 BGR（3 通道）
        if (mat.channels() == 4) {
            Imgproc.cvtColor(mat, mat, Imgproc.COLOR_RGBA2BGR) // RGBA → BGR
        }
        return mat
    }



    fun saveImageToFiles(context: Context, image: Mat, fileName: String): String {
        val file = File(context.filesDir, fileName)
        Imgcodecs.imwrite(file.absolutePath, image)
        return file.absolutePath
    }


}
