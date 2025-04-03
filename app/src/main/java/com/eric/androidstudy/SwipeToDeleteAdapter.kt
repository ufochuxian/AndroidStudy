package com.eric.androidstudy

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.chauthai.swipereveallayout.SwipeRevealLayout
import com.chauthai.swipereveallayout.ViewBinderHelper

class SwipeToDeleteAdapter(private val itemList: MutableList<String>) :
    RecyclerView.Adapter<SwipeToDeleteAdapter.ViewHolder>() {

    private val viewBinderHelper = ViewBinderHelper()

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val swipeLayout: SwipeRevealLayout = view.findViewById(R.id.swipe_layout)
        val textView: TextView = view.findViewById(R.id.text_item)
        val btnDelete: Button = view.findViewById(R.id.btn_delete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_swipe_delete, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemList[position]

        // 绑定 SwipeRevealLayout 的状态
        viewBinderHelper.bind(holder.swipeLayout, item)
        viewBinderHelper.closeLayout(item)

        holder.textView.text = item
        holder.btnDelete.setOnClickListener {
            itemList.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, itemList.size)
        }
    }

    override fun getItemCount() = itemList.size
}
