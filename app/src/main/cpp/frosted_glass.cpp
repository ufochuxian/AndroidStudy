#include <jni.h>
#include <android/bitmap.h>
#include <cstdint>
#include <cmath>
#include <vector>
#include <algorithm>

const double PI = 3.14159265358979323846;

std::vector<double> createGaussianKernel(int radius) {
    std::vector<double> kernel(2 * radius + 1);
    double sigma = radius / 3.0;
    double sum = 0.0;

    for (int i = -radius; i <= radius; ++i) {
        kernel[i + radius] = exp(-(i * i) / (2 * sigma * sigma)) / (sqrt(2 * PI) * sigma);
        sum += kernel[i + radius];
    }

    for (int i = 0; i < kernel.size(); ++i) {
        kernel[i] /= sum;
    }

    return kernel;
}

void applyGaussianBlur(uint32_t* pixels, int width, int height, int radius) {
    std::vector<double> kernel = createGaussianKernel(radius);
    std::vector<uint32_t> tempPixels(width * height);

    // Horizontal pass
    for (int y = 0; y < height; ++y) {
        for (int x = 0; x < width; ++x) {
            double r = 0, g = 0, b = 0, a = 0;
            for (int i = -radius; i <= radius; ++i) {
                int nx = std::clamp(x + i, 0, width - 1);
                uint32_t pixel = pixels[y * width + nx];
                double weight = kernel[i + radius];
                r += ((pixel >> 16) & 0xFF) * weight;
                g += ((pixel >> 8) & 0xFF) * weight;
                b += (pixel & 0xFF) * weight;
                a += ((pixel >> 24) & 0xFF) * weight;
            }
            tempPixels[y * width + x] = (static_cast<uint32_t>(a) << 24) |
                                        (static_cast<uint32_t>(r) << 16) |
                                        (static_cast<uint32_t>(g) << 8) |
                                        static_cast<uint32_t>(b);
        }
    }

    // Vertical pass
    for (int x = 0; x < width; ++x) {
        for (int y = 0; y < height; ++y) {
            double r = 0, g = 0, b = 0, a = 0;
            for (int i = -radius; i <= radius; ++i) {
                int ny = std::clamp(y + i, 0, height - 1);
                uint32_t pixel = tempPixels[ny * width + x];
                double weight = kernel[i + radius];
                r += ((pixel >> 16) & 0xFF) * weight;
                g += ((pixel >> 8) & 0xFF) * weight;
                b += (pixel & 0xFF) * weight;
                a += ((pixel >> 24) & 0xFF) * weight;
            }
            pixels[y * width + x] = (static_cast<uint32_t>(a) << 24) |
                                    (static_cast<uint32_t>(r) << 16) |
                                    (static_cast<uint32_t>(g) << 8) |
                                    static_cast<uint32_t>(b);
        }
    }
}

extern "C" JNIEXPORT void JNICALL
Java_com_eric_glass_FrostedGlassNative_applyFrostedGlassEffect(
        JNIEnv* env, jobject /* this */, jobject bitmap, jint radius) {
AndroidBitmapInfo info;
void* pixels;

if (AndroidBitmap_getInfo(env, bitmap, &info) < 0) {
return;
}

if (AndroidBitmap_lockPixels(env, bitmap, &pixels) < 0) {
return;
}

applyGaussianBlur(static_cast<uint32_t*>(pixels), info.width, info.height, radius);

AndroidBitmap_unlockPixels(env, bitmap);
}