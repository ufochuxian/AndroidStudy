package com.eric.material

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.eric.androidstudy.databinding.ItemRecyclerBinding

class SimpleRecyclerViewAdapter(private val items: List<String>) :
    RecyclerView.Adapter<SimpleRecyclerViewAdapter.ViewHolder>() {

    class ViewHolder(private val binding: ItemRecyclerBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(text: String) {
            binding.textView.text = text
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRecyclerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}
