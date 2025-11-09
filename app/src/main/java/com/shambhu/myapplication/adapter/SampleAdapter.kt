package com.shambhu.myapplication.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.shambhu.myapplication.R
import com.shambhu.myapplication.utils.ExpandableTextView

class SampleAdapter(private val items: List<String>) : RecyclerView.Adapter<SampleAdapter.ViewHolder>() {

    private val expandedPositions = mutableSetOf<Int>()

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val title: TextView = itemView.findViewById(R.id.tvTitle)
        private val expandableText: ExpandableTextView = itemView.findViewById(R.id.expandableText)

        fun bind(item: String, position: Int) {
            title.text = "Item ${position + 1}"
            expandableText.setText(item)

            // Restore expanded state
            if (expandedPositions.contains(position)) {
                expandableText.expand()
            } else {
                expandableText.collapse()
            }

            // Listen for expand/collapse
            expandableText.setOnExpandListener { isExpanded ->
                if (isExpanded) {
                    expandedPositions.add(position)
                } else {
                    expandedPositions.remove(position)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_sample, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position], position)
    }

    override fun getItemCount(): Int = items.size
}