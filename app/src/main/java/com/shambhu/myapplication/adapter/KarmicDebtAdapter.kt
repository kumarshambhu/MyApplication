package com.shambhu.myapplication.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.shambhu.myapplication.databinding.ItemKarmicDebtBinding

class KarmicDebtAdapter(
    private val karmicDebtNumbers: List<Pair<String, Int>>,
    private val interpretations: Map<String, String>
) : RecyclerView.Adapter<KarmicDebtAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemKarmicDebtBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val (source, number) = karmicDebtNumbers[position]
        holder.binding.tvKarmicDebtNumber.text = "Karmic Debt Number: $number"
        holder.binding.tvKarmicDebtSource.text = "Source: $source"
        holder.binding.tvKarmicDebtInterpretation.text = interpretations[number.toString()]
    }

    override fun getItemCount(): Int {
        return karmicDebtNumbers.size
    }

    class ViewHolder(val binding: ItemKarmicDebtBinding) : RecyclerView.ViewHolder(binding.root)
}
