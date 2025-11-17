package com.shambhu.myapplication.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.shambhu.myapplication.databinding.ItemKarmicLessonBinding
import com.shambhu.myapplication.model.KarmicAccordionItem

class KarmicLessonRecyclerViewAdapter(private val karmicLessonNumbers: List<KarmicAccordionItem>,
                                      private val onItemClick: (Int) -> Unit) :
    RecyclerView.Adapter<KarmicLessonRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemKarmicLessonBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val (header, title, content, expanded) = karmicLessonNumbers[position]
        holder.binding.karmicLessonNumber.text = "Karmic Lesson Number $header"
        //holder.binding.tvKarmicDebtSource.text = "Source: $title"
        holder.binding.karmicLessonDetail.text = content


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

    override fun getItemCount(): Int = karmicLessonNumbers.size

   class ViewHolder(val binding: ItemKarmicLessonBinding) : RecyclerView.ViewHolder(binding.root)
}
