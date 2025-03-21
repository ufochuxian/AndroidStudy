package com.eric.androidstudy

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.eric.SwipeDragAdapter
import com.eric.androidstudy.ItemTouchHelperCallback
import com.eric.androidstudy.R
import com.eric.kotlin.ExpandableTextView

class ExpandableTextViewActivity : AppCompatActivity() {
    private var adapter : SwipeDragAdapter?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expandabletextview)


        val expandableTextView2 = findViewById<ExpandableTextView>(R.id.expandable_text)
        expandableTextView2.setText(
            "The golden light of the setting sun shines on the tall snowy mountains. " +
                    "It is shining, the sky is shining, everything is shining. " +
                    "The mountain is majestic and peaceful, like a silent guardian of the land."
        )


    }
}
