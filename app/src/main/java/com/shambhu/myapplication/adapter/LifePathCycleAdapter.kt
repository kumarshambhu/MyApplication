package com.shambhu.myapplication.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.shambhu.myapplication.databinding.ItemLifePathCycleBinding
import com.shambhu.myapplication.model.LifePathCycle

class LifePathCycleAdapter(private var cycles: List<LifePathCycle>) :
    RecyclerView.Adapter<LifePathCycleAdapter.LifePathCycleViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LifePathCycleViewHolder {
        val binding =
            ItemLifePathCycleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LifePathCycleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LifePathCycleViewHolder, position: Int) {
        holder.bind(cycles[position])
    }

    override fun getItemCount(): Int = cycles.size

    fun updateData(newCycles: List<LifePathCycle>) {
        cycles = newCycles
        notifyDataSetChanged()
    }

    inner class LifePathCycleViewHolder(private val binding: ItemLifePathCycleBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(cycle: LifePathCycle) {
            binding.tvCycleTitle.text = cycle.title
            binding.tvAgeRange.text = cycle.ageRange
            binding.tvCycleNumber.text = "Cycle Number: ${cycle.cycleNumber}"
            binding.tvDescription.text = cycle.description
        }
    }
}
