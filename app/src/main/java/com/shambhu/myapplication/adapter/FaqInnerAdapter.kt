package com.shambhu.myapplication.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.shambhu.myapplication.R
import com.shambhu.myapplication.model.FaqInnerItem

class FaqInnerAdapter(private var items: List<FaqInnerItem>) :
    RecyclerView.Adapter<FaqInnerAdapter.InnerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InnerViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_faq_inner, parent, false)
        return InnerViewHolder(view)
    }

    override fun onBindViewHolder(holder: InnerViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = items.size

    fun updateData(newItems: List<FaqInnerItem>) {
        items = newItems
        notifyDataSetChanged()
    }

    inner class InnerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        private val detailsTextView: TextView = itemView.findViewById(R.id.detailsTextView)

        fun bind(item: FaqInnerItem) {
            titleTextView.text = item.key
            val detailsString = item.details.joinToString(separator = "\n") { "• $it" }
            detailsTextView.text = detailsString
        }
    }
}
