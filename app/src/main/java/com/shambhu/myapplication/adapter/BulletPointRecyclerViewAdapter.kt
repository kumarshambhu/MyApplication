package com.shambhu.myapplication.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.OrientationHelper
import androidx.recyclerview.widget.RecyclerView
import com.shambhu.myapplication.databinding.ItemBulletPointBinding

class BulletPointRecyclerViewAdapter(
    private val isVertical: Boolean,
    private val coreNumbers: List<Pair<String, String>>?
) :
    RecyclerView.Adapter<BulletPointRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemBulletPointBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        if (isVertical) {
            binding.bulletPointLayout.orientation = LinearLayout.VERTICAL
        }
        return ViewHolder(binding)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        coreNumbers?.let {
            val (header, title) = it.get(position)
            holder.binding.bulletStyle.text = header
            holder.binding.bulletValue.text = title
        }
    }

    override fun getItemCount(): Int = coreNumbers?.size ?: 0

    class ViewHolder(val binding: ItemBulletPointBinding) : RecyclerView.ViewHolder(binding.root)
}
