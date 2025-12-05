package com.shambhu.myapplication.adapter

// adapters/NumeroAccordionAdapter.kt

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.shambhu.myapplication.R

class NumeroAccordionAdapter(
    private val sections: List<Section>
) : RecyclerView.Adapter<NumeroAccordionAdapter.SectionViewHolder>() {

    data class Section(
        val title: String,
        val items: List<String>
    )

    inner class SectionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val sectionTitle: TextView = itemView.findViewById(R.id.sectionTitle)
        val sectionContent: TextView = itemView.findViewById(R.id.sectionContent)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SectionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_numero_section, parent, false)
        return SectionViewHolder(view)
    }

    override fun onBindViewHolder(holder: SectionViewHolder, position: Int) {
        val section = sections[position]

        holder.sectionTitle.text = section.title
        holder.sectionContent.text = section.items.joinToString("\n• ", "• ")

        // Show/hide content on click
        holder.sectionTitle.setOnClickListener {
            val isVisible = holder.sectionContent.visibility == View.VISIBLE
            holder.sectionContent.visibility = if (isVisible) View.GONE else View.VISIBLE
        }
    }

    override fun getItemCount(): Int = sections.size
}