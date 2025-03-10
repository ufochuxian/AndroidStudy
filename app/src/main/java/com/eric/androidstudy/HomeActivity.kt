package com.eric.androidstudy

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.eric.feature.partygame.PartyGameActivity
import com.eric.material.CoordinatorLayoutActivity

class HomeActivity : AppCompatActivity() {

    private val testPages = listOf(
        TestPage("PartyGameActivity", PartyGameActivity::class.java),
        TestPage("CoordinatorLayoutActivity", CoordinatorLayoutActivity::class.java),
        // 添加更多测试页面
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_home)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = TestPageAdapter(this, testPages)
    }
}

// 页面数据类
data class TestPage(val title: String, val targetActivity: Class<*>)

// 适配器
class TestPageAdapter(
    private val context: Context,
    private val pages: List<TestPage>
) : RecyclerView.Adapter<TestPageAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.textView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_test_page, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val page = pages[position]
        holder.textView.text = page.title
        holder.itemView.setOnClickListener {
            context.startActivity(Intent(context, page.targetActivity))
        }
    }

    override fun getItemCount(): Int = pages.size
}
