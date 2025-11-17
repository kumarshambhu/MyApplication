package com.shambhu.myapplication.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.shambhu.myapplication.R
import com.shambhu.myapplication.model.KarmicAccordionItem

class AccordionAdapter(
    private val items: List<KarmicAccordionItem>,
    private val onItemClick: (Int) -> Unit
) : RecyclerView.Adapter<AccordionAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val headerTextView: TextView = itemView.findViewById(R.id.tvHeader)
        private val titleTextView: TextView = itemView.findViewById(R.id.tvTitle)
        private val contentTextView: TextView = itemView.findViewById(R.id.tvContent)
        private val expandIcon: ImageView = itemView.findViewById(R.id.ivExpand)
        private val contentLayout: LinearLayout = itemView.findViewById(R.id.contentLayout)

        fun bind(item: KarmicAccordionItem, position: Int) {
            headerTextView.text = item.header
            titleTextView.text = item.title
            contentTextView.text = item.content

            // Set initial state
            if (item.isExpanded) {
                contentLayout.visibility = View.VISIBLE
                expandIcon.rotation = 180f
            } else {
                contentLayout.visibility = View.GONE
                expandIcon.rotation = 0f
            }

            itemView.setOnClickListener {
                onItemClick(position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_accordion, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position], position)
    }

    override fun getItemCount() = items.size
}