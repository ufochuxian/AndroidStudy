package com.eric.androidstudy

import CameraXPhotoCapture
import android.content.Intent
import android.os.Bundle
import android.os.Parcel
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.eric.ScreenMatchUtil
import com.eric.androidstudy.databinding.ActivityMainBinding
import com.eric.base.media.VideoPlayerActivity
import com.eric.base.media.VideoPlayerSurfaceActivity
import com.eric.base.media.VideoPlayerWithFilterActivity
import com.eric.function.costTime
import com.eric.function.sayHello
import com.eric.kotlin.SPMgr
import com.eric.kotlin.corotinue.broadcast.GlobalEvent
import com.eric.kotlin.corotinue.broadcast.Message
import com.eric.kotlin.corotinue.broadcast.PageA
import com.eric.kotlin.corotinue.broadcast.PageB
import com.eric.operatprs.JustOperator
import com.eric.routers.TgmRouter
import com.eric.ui.UILayoutActivity
import com.eric.ui.WanAndroidActivity
import com.eric.workmanager.BlurWorker
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    //    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        TgmRouter.getInstance().init(this)
        binding = ActivityMainBinding.inflate(layoutInflater)

//        binding.fab.setOnClickListener { view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show()
//        }

//        setSupportActionBar(binding.toolbar)

        SPMgr.isFirst = "no"

        Log.i("FirstFragment","xxx isFirst:${SPMgr.isFirst}")


        setContentView(binding.root)

//        val navController = findNavController(R.id.nav_host_fragment_content_main)
//        appBarConfiguration = AppBarConfiguration(navController.graph)
//        setupActionBarWithNavController(navController, appBarConfiguration)

//        binding.fab.setOnClickListener { view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show()
//        }

//        testCustomRxJava()
        val capture = CameraXPhotoCapture(this,true)

        binding.displayConstraintlayoutChains.setOnClickListener {
//            startActivity(Intent(this, ConstraintLayoutActivity::class.java))
            capture.takePhoto(onImageSaved = { imgFile ->
                Log.d("MainActivity", "${imgFile?.name} is capture & saved success,path:${imgFile?.path}")
            })
        }

        binding.uiLayout.setOnClickListener {
            startActivity(Intent(this, UILayoutActivity::class.java))
        }

        binding.page3.setOnClickListener {
            startActivity(Intent(this, VideoPlayerWithFilterActivity::class.java))
        }

        var flowable = JustOperator()

//        flowable.flowable()
//
//        flowable.just()

//        flowable.zipWith()
//        flowable.interval()
//        flowable.fromIterable()
//        flowable.testMap()
//        flowable.testFlatMap()

//        flowable.testMerge()

//        flowable.testZip()

//        flowable.testReduce()

//        flowable.testAsync()


//        Observable.create<String> {
//            it.onNext("开始")
//        }.delay(1000, TimeUnit.MILLISECONDS)
//            .subscribe {
//                var intent = Intent(this,JetpackActivity::class.java)
//                this.startActivity(intent)
//            }

        val obtain = Parcel.obtain()

        obtain.writeInt(12)

        obtain.writeInt(14)

        obtain.setDataPosition(0)

        println(obtain.readInt())
        println(obtain.readInt())
        println(obtain.readInt())

        this.testWorkManager()

        //需要初始化，先进行注册广播
        val pageA = PageA()
        pageA.registerBroadcastByFlow()
        val pageB = PageB()
        pageB.registerBroadcastByFlow()


        ScreenMatchUtil.log(this)

//        val launchBitmap = binding.icLauncher.drawable.toBitmap()
//
//
//        val meter = MemoryMeter()
//        // 创建对象并估算大小
//        val size = meter.measure(launchBitmap)
//        println("Size of the object: $size bytes")

        //ViewModel scope，协程作用域的使用，自动取消，减少模板代码
        val activityViewModel = ViewModelProvider(this).get<MainActivityViewModel>()
        activityViewModel.viewModelScope.launch {
            delay(800)
            GlobalEvent.sendEvent(Message("msg", "利用SharedFlow实现的全局广播机制"))
        }

        lifecycleScope.launch {
            flow<Int> {
                for (i in 1..20) {
                    emit(i)
                }
            }.map {
                println("测试flow冷流使用:${it * 2}")
            }
                .collect()
        }

        binding.testBtn.setOnClickListener {
            startActivity(Intent(this,WanAndroidActivity::class.java))
        }

        costTime {
            lifecycleScope.launch {
                sayHello("YaoMing")
            }
        }
    }

    private fun testCustomRxJava() {
        val rxjava = CustomRxjava()
//        rxjava.testRxjava()
//        rxjava.testFlatMap()
//        rxjava.testMap()
//        rxjava.testSubscribeOn()
    }


    private fun testWorkManager() {
        val workManager = WorkManager.getInstance(this)
        //构建一个Request对象，包含具体的Work，这里的Request有很多中类型，可以用来区分周期性任务，或者一次性任务等等

        val map = mutableMapOf("a" to "x", "b" to "y")
        //这里使用了build的设计模式来构造数据data
        val data = Data.Builder().putString("a", "传输过来的数据").build()
        val oneTimeWorkRequest =
            OneTimeWorkRequest.Builder(BlurWorker::class.java).setInputData(data).build()
        //提交到WorkManager来进行任务的运行
        workManager.enqueue(oneTimeWorkRequest)
    }


//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        menuInflater.inflate(R.menu.menu_main, menu)
//        return true
//    }

//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        return when (item.itemId) {
//            R.id.action_settings -> true
//            else -> super.onOptionsItemSelected(item)
//        }
//    }
//
//    override fun onSupportNavigateUp(): Boolean {
//        val navController = findNavController(R.id.nav_host_fragment_content_main)
//        return navController.navigateUp(appBarConfiguration)
//                || super.onSupportNavigateUp()
//    }
}