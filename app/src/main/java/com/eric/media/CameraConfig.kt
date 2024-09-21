package com.eric.media

import android.graphics.ImageFormat

class CameraConfig() {

    var imageWidth: Int = 640
    var imageHeight: Int = 480
    var imageFormat: Int = ImageFormat.JPEG

    // 可以扩展其他配置项，例如照片保存的前缀
    var photoFileNamePrefix: String = "IMG_"
}
