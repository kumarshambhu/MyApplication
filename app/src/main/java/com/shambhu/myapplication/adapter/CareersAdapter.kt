package com.shambhu.myapplication.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.shambhu.myapplication.databinding.ItemCareerBinding

class CareersAdapter : ListAdapter<String, CareersAdapter.CareerViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CareerViewHolder {
        val binding = ItemCareerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CareerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CareerViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

    class CareerViewHolder(private val binding: ItemCareerBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(career: String) {
            binding.careerTextView.text = career
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String) =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: String, newItem: String) =
            oldItem == newItem
    }
}
