package com.eric.workmanager

import android.content.Context
import android.graphics.BitmapFactory
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.eric.rxjava.R
import java.lang.Exception


/**

 * @Author: chen

 * @datetime: 2024/5/2

 * @desc:

 */


// WorkManager中，需要创建的work对象
class BlurWorker(ctx: Context, params: WorkerParameters) : Worker(ctx, params) {

    companion object {
        private const val TAG = "BlurWorker"
    }

    /**
     *
     * 这部分的代码是运行在子线程中的，所以可以进行一些耗时的操作
     */
    override fun doWork(): Result {

        val appContext = applicationContext

        makeStatusNotification("Blurring image", appContext)

        return try {
            val pic = BitmapFactory.decodeResource(appContext.resources, R.drawable.test)

            val blurBitmap = blurBitmap(pic, appContext)
            val outputUri = writeBitmapToFile(appContext, blurBitmap)

            makeStatusNotification("Output is $outputUri", appContext)

            Log.i(TAG,"Output is $outputUri")

            Result.success()

        } catch (e: Exception) {
            Result.failure()
        } finally {
            Result.failure()
        }
    }
}