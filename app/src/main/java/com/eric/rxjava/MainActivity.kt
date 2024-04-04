package com.eric.rxjava

import android.content.Intent
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toBitmap
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.eric.jetpack.JetpackActivity
import com.eric.operatprs.JustOperator
import com.eric.routers.TgmRouter
import com.eric.rxjava.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import io.reactivex.rxjava3.core.Observable
import org.github.jamm.MemoryMeter
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        TgmRouter.getInstance().init(this)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        binding.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

//        testCustomRxJava()

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




//        val launchBitmap = binding.icLauncher.drawable.toBitmap()
//
//
//        val meter = MemoryMeter()
//        // 创建对象并估算大小
//        val size = meter.measure(launchBitmap)
//        println("Size of the object: $size bytes")

    }

    private fun testCustomRxJava() {
        val rxjava = CustomRxjava()
//        rxjava.testRxjava()
//        rxjava.testFlatMap()
//        rxjava.testMap()
//        rxjava.testSubscribeOn()
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}