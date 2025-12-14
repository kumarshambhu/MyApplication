package com.shambhu.myapplication.adapter.page_adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
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
        //holder.binding.sectionContent.text = section.items.joinToString("\n• ", "• ")
        CommonAdapterUtil.setupNumberBulletRecyclerViewAdapter(context,
            holder.binding.sectionContentRecyclerView,  section.items
        )

        // Show/hide content on click
        holder.binding.sectionTitle.setOnClickListener {
            val isVisible = holder.binding.sectionContentRecyclerView.isVisible
            holder.binding.sectionContentRecyclerView.visibility = if (isVisible) View.GONE else View.VISIBLE
        }
    }

    override fun getItemCount(): Int = sections.size
}