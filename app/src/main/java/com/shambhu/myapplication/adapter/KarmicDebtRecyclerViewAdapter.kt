package com.shambhu.myapplication.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.shambhu.myapplication.databinding.ItemKarmicDebtBinding
import com.shambhu.myapplication.model.KarmicAccordionItem

class KarmicDebtRecyclerViewAdapter(
    private val karmicDebtNumbers: List<KarmicAccordionItem>,
    private val onItemClick: (Int) -> Unit
) : RecyclerView.Adapter<KarmicDebtRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemKarmicDebtBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val (header, title, content, expanded) = karmicDebtNumbers[position]
        holder.binding.tvKarmicDebtNumber.text = "Karmic Debt Number: $header"
        holder.binding.tvKarmicDebtSource.text = "Source: $title"
        holder.binding.tvKarmicDebtInterpretation.text = content


        if (expanded) {
            holder.binding.contentLayout.visibility = View.VISIBLE
            holder.binding.ivExpand.rotation = 180f
        } else {
            holder.binding.contentLayout.visibility = View.GONE
            holder.binding.ivExpand.rotation = 0f
        }

        holder.itemView.setOnClickListener {
            onItemClick(position)
        }
    }

    override fun getItemCount(): Int {
        return karmicDebtNumbers.size
    }

    class ViewHolder(val binding: ItemKarmicDebtBinding) : RecyclerView.ViewHolder(binding.root)
}
