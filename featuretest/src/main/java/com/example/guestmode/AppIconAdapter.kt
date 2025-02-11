package com.example.guestmode

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.eric.feature.R

class AppIconAdapter(private val icons: List<Int>) : RecyclerView.Adapter<AppIconAdapter.IconViewHolder>() {

    inner class IconViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.ivAppIcon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IconViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_app_icon, parent, false)
        return IconViewHolder(view)
    }

    override fun onBindViewHolder(holder: IconViewHolder, position: Int) {
        holder.imageView.setImageResource(icons[position])
    }

    override fun getItemCount(): Int = icons.size
}
