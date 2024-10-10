#include <jni.h>
#include <android/bitmap.h>
#include <cstdint>
#include <cmath>
#include <vector>
#include <algorithm>
#include <omp.h> // OpenMP for parallelization

const double PI = 3.14159265358979323846;

// 限制最大模糊半径
const int MAX_RADIUS = 25;

// 盒式模糊函数，用于水平和垂直方向的模糊
void boxBlur(uint32_t* pixels, int width, int height, int radius) {
    std::vector<uint32_t> tempPixels(width * height);

    // 水平方向模糊
#pragma omp parallel for // 并行化处理
    for (int y = 0; y < height; ++y) {
        for (int x = 0; x < width; ++x) {
            int sumR = 0, sumG = 0, sumB = 0, sumA = 0;
            int count = 0;
            for (int i = -radius; i <= radius; ++i) {
                int nx = std::clamp(x + i, 0, width - 1);
                uint32_t pixel = pixels[y * width + nx];
                sumR += (pixel >> 16) & 0xFF;
                sumG += (pixel >> 8) & 0xFF;
                sumB += pixel & 0xFF;
                sumA += (pixel >> 24) & 0xFF;
                count++;
            }
            tempPixels[y * width + x] = (sumA / count << 24) |
                                        (sumR / count << 16) |
                                        (sumG / count << 8) |
                                        (sumB / count);
        }
    }

    // 垂直方向模糊
#pragma omp parallel for // 并行化处理
    for (int x = 0; x < width; ++x) {
        for (int y = 0; y < height; ++y) {
            int sumR = 0, sumG = 0, sumB = 0, sumA = 0;
            int count = 0;
            for (int i = -radius; i <= radius; ++i) {
                int ny = std::clamp(y + i, 0, height - 1);
                uint32_t pixel = tempPixels[ny * width + x];
                sumR += (pixel >> 16) & 0xFF;
                sumG += (pixel >> 8) & 0xFF;
                sumB += pixel & 0xFF;
                sumA += (pixel >> 24) & 0xFF;
                count++;
            }
            pixels[y * width + x] = (sumA / count << 24) |
                                    (sumR / count << 16) |
                                    (sumG / count << 8) |
                                    (sumB / count);
        }
    }
}

// 使用三次盒式模糊近似高斯模糊
void applyGaussianApproximation(uint32_t* pixels, int width, int height, int radius) {
    int boxRadius = radius / 3; // 用三次盒式模糊模拟
    boxRadius = std::min(boxRadius, MAX_RADIUS); // 限制最大模糊半径

    // 连续应用三次盒式模糊来近似高斯模糊
    boxBlur(pixels, width, height, boxRadius);
    boxBlur(pixels, width, height, boxRadius);
    boxBlur(pixels, width, height, boxRadius);
}

// JNI 接口函数，连接 Java 和 C++ 代码
extern "C" JNIEXPORT void JNICALL
Java_com_eric_glass_FrostedGlassNative_applyFrostedGlassEffect(
        JNIEnv* env, jobject /* this */, jobject bitmap, jint radius) {

AndroidBitmapInfo info;
void* pixels;

// 获取 Bitmap 信息
if (AndroidBitmap_getInfo(env, bitmap, &info) < 0) {
return;
}

// 锁定 Bitmap 的像素
if (AndroidBitmap_lockPixels(env, bitmap, &pixels) < 0) {
return;
}

// 应用三次盒式模糊近似高斯模糊
applyGaussianApproximation(static_cast<uint32_t*>(pixels), info.width, info.height, radius);

// 解锁 Bitmap 像素
AndroidBitmap_unlockPixels(env, bitmap);
}
