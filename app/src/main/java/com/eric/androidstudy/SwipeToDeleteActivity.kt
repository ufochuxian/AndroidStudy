package com.eric.androidstudy

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class SwipeToDeleteActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.swipe_to_delete_activity)

        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        val itemList = mutableListOf(
            "任务 1", "任务 2", "任务 3", "任务 4", "任务 5"
        )

        val adapter = SwipeToDeleteAdapter(itemList)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }
}
