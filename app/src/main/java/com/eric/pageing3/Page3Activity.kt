package com.eric.pageing3

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.eric.androidstudy.R
import com.eric.base.data.Product
import com.eric.base.db.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

private const val TAG = "Page3Activity"
class Page3Activity : AppCompatActivity() {

    private lateinit var productViewModel: ProductViewModel

    @SuppressLint("LogNotTimber")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_page)

        productViewModel = ViewModelProvider(this)[ProductViewModel::class.java]

        // 插入 2000 条商品信息
        GlobalScope.launch(Dispatchers.IO) {
            val productDao = AppDatabase.getDatabase(this@Page3Activity).productDao()
            val products = List(500) { i -> Product(name = "Product $i", price = i.toDouble()) }
            val results = productDao.insertProducts(products)
            Log.i(TAG,"插入产品:${results}")
        }

        // 监听并展示分页数据
        val adapter = ProductAdapter()
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)  // 设置 LayoutManager
        recyclerView.adapter = adapter

        lifecycleScope.launch {
            productViewModel.productPagingData.collectLatest { pagingData ->
                Log.i(TAG, "Paging data received: $pagingData")  // ✅ 确保收到分页数据
                adapter.submitData(pagingData)
            }
        }
    }
}