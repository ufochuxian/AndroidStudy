package com.eric.androidstudy

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.eric.SwipeDragAdapter
import com.eric.androidstudy.ItemTouchHelperCallback
import com.eric.androidstudy.R

class SwipeDragActivity : AppCompatActivity() {
    private var adapter : SwipeDragAdapter?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_swipe_drag)

        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val items = mutableListOf("Item 1", "Item 2", "Item 3", "Item 4", "Item 5")
        adapter = SwipeDragAdapter(items)

        val itemTouchHelper = ItemTouchHelper(ItemTouchHelperCallback(adapter))
        itemTouchHelper.attachToRecyclerView(recyclerView)

        recyclerView.adapter = adapter
    }
}
