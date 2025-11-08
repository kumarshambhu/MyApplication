package com.shambhu.myapplication.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.shambhu.myapplication.databinding.ItemColorBinding
import org.json.JSONObject

class ColorAdapter(private val colors: List<JSONObject>) : RecyclerView.Adapter<ColorAdapter.ColorViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorViewHolder {
        val binding = ItemColorBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ColorViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ColorViewHolder, position: Int) {
        val color = colors[position]
        holder.binding.colorNameTextView.text = color.getString("color")
        holder.binding.colorDetailTextView.text = color.getString("detail")
    }

    override fun getItemCount() = colors.size

    class ColorViewHolder(val binding: ItemColorBinding) : RecyclerView.ViewHolder(binding.root)
}