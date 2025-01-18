package com.eric.androidstudy

import CameraXPhotoCapture
import android.Manifest
import android.accounts.AccountManager
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Parcel
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.activity.result.ActivityResult
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
import com.eric.base.ext.ERIC_TAG
import com.eric.base.media.VideoPlayerWithFilterActivity
import com.eric.base.mgr.PermissionManager
import com.eric.function.costTime
import com.eric.function.sayHello
import com.eric.kotlin.SPMgr
import com.eric.kotlin.corotinue.broadcast.GlobalEvent
import com.eric.kotlin.corotinue.broadcast.Message
import com.eric.kotlin.corotinue.broadcast.PageA
import com.eric.kotlin.corotinue.broadcast.PageB
import com.eric.operatprs.JustOperator
import com.eric.routers.TgmRouter
import com.eric.service.MusicPlayerService
import com.eric.ui.UILayoutActivity
import com.eric.ui.WanAndroidActivity
import com.eric.workmanager.BlurWorker
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.util.regex.Pattern


const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    //    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    private val permissionMgr by lazy { PermissionManager(this)}
    companion object {
        // 这些都是权限请求码的例子
        private const val PERMISSION_REQUEST_CODE = 123        // 获取账户权限
        private const val CAMERA_PERMISSION_CODE = 124        // 相机权限
        private const val LOCATION_PERMISSION_CODE = 125      // 位置权限
    }

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


        // 检查权限
//        checkAndRequestPermission()
    }

    override fun onDestroy() {
        super.onDestroy()

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

    private fun checkAndRequestPermission() {
        if (androidx.core.content.ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_CONTACTS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            androidx.core.app.ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_CONTACTS,Manifest.permission.WRITE_CONTACTS),
                PERMISSION_REQUEST_CODE
            )
        } else {
            getAllEmailAccounts()
//            printAccounts()
        }
    }



    private fun getAllEmailAccounts() {
        val accountManager = AccountManager.get(this)
        val accounts = accountManager.accounts

        // 使用Set去重
        val emailSet = HashSet<String>()

        accounts.forEach { account ->
            val accountName = account.name
            val accountType = account.type

            // 检查是否是邮箱格式
            if (isEmailAccount(accountName)) {
                emailSet.add(accountName)
                Log.d(TAG, "Email: $accountName")
                Log.d(TAG, "Type: $accountType")
            }
        }

        // 显示找到的邮箱数量
        Toast.makeText(this, "找到 ${emailSet.size} 个邮箱账号", Toast.LENGTH_SHORT).show()
    }

    private fun printAccounts() {
//        val emailPattern: Pattern = Patterns.EMAIL_ADDRESS // API level 8+
//        val accounts = AccountManager.get(this).accounts
//        for (account in accounts) {
//            if (emailPattern.matcher(account.name).matches()) {
//                val accountName = account.name
//                val accountType = account.type
//                Log.d(TAG, "Email: $accountName")
//                Log.d(TAG, "Type: $accountType")
//            }
//        }
        baseContext?.let {
            val intent = Intent(this,ExampleActivity::class.java)
            this.startActivity(intent)
        }

    }

    // 检查是否是邮箱格式
    private fun isEmailAccount(account: String?): Boolean {
        return account?.let {
            it.contains("@") && it.contains(".")
        } ?: false
    }

    // 使用正则表达式的邮箱验证方法（可选）
    private fun isEmailAccountWithRegex(account: String?): Boolean {
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+".toRegex()
        return account?.matches(emailPattern) ?: false
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getAllEmailAccounts()
            } else {
                Toast.makeText(this, "需要获取账户权限才能读取邮箱信息", Toast.LENGTH_LONG).show()
            }
        }
    }
}