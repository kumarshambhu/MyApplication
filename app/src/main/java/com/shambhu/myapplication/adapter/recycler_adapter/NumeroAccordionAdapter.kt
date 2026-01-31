package com.shambhu.myapplication.adapter.recycler_adapter

import android.content.Context
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.shambhu.myapplication.R
import com.shambhu.myapplication.adapter.CommonAdapterUtil
import com.shambhu.myapplication.databinding.ItemNumeroSectionBinding

class NumeroAccordionAdapter(
    private val context: Context,
    private val sections: List<Section>
) : RecyclerView.Adapter<NumeroAccordionAdapter.SectionViewHolder>() {

    data class Section(
        val title: String,
        val items: List<String>
    )

    class SectionViewHolder(val binding: ItemNumeroSectionBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SectionViewHolder {
        val binding =
            ItemNumeroSectionBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return SectionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SectionViewHolder, position: Int) {
        val section = sections[position]

        holder.binding.sectionTitle.text = section.title
        CommonAdapterUtil.setupNumberBulletRecyclerViewAdapter(context,
            holder.binding.sectionContentRecyclerView,  section.items
        )

        val isExpanded = holder.binding.sectionContentRecyclerView.isVisible
        if (isExpanded) {
            holder.binding.ivExpand.rotation = 180f
            holder.binding.accordionCard.strokeColor = ContextCompat.getColor(context, R.color.gold)
            holder.binding.accordionCard.cardElevation = 8f
        } else {
            holder.binding.ivExpand.rotation = 0f
            holder.binding.accordionCard.strokeColor = ContextCompat.getColor(context, R.color.divider)
            holder.binding.accordionCard.cardElevation = 2f
        }

        // Show/hide content on click
        holder.binding.headerLayout.setOnClickListener {
            TransitionManager.beginDelayedTransition(holder.binding.accordionCard, AutoTransition())
            val isVisible = holder.binding.sectionContentRecyclerView.isVisible
            holder.binding.sectionContentRecyclerView.visibility = if (isVisible) View.GONE else View.VISIBLE
            notifyItemChanged(position)
        }
    }

    override fun getItemCount(): Int = sections.size
}