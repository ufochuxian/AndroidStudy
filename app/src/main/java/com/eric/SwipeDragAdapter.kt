package com.eric

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.chauthai.swipereveallayout.SwipeRevealLayout
import com.eric.androidstudy.ItemTouchHelperAdapter
import com.eric.androidstudy.R

class SwipeDragAdapter(
    private val items: MutableList<String>,
) : RecyclerView.Adapter<SwipeDragAdapter.ViewHolder>(), ItemTouchHelperAdapter {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val swipeLayout: SwipeRevealLayout = view.findViewById(R.id.swipe_layout)
        val deleteButton: View = view.findViewById(R.id.btn_delete)
        val itemText: TextView = view.findViewById(R.id.text_item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_swipe_drag, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemText.text = items[position]

        // 确保绑定 SwipeRevealLayout，避免复用时状态异常
        holder.swipeLayout.close(true)

        // 删除按钮监听
        holder.deleteButton.setOnClickListener {
            val pos = holder.bindingAdapterPosition
            if (pos != RecyclerView.NO_POSITION) {
                onItemDismiss(pos)
            }
        }
    }

    override fun getItemCount(): Int = items.size

    // 交换 Item 位置
    override fun onItemMove(fromPosition: Int, toPosition: Int) {
        items.add(toPosition, items.removeAt(fromPosition))
        notifyItemMoved(fromPosition, toPosition)
    }

    // 处理滑动删除
    override fun onItemDismiss(position: Int) {
        if (position < items.size) {
            items.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, items.size) // 避免删除后索引错乱
        }
    }
}
