package com.shambhu.myapplication.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.shambhu.myapplication.R
import com.shambhu.myapplication.model.FaqItem

class FaqAdapter(private val faqList: MutableList<FaqItem>) :
    RecyclerView.Adapter<FaqAdapter.FaqViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FaqViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_accordion, parent, false)
        return FaqViewHolder(view)
    }

    override fun onBindViewHolder(holder: FaqViewHolder, position: Int) {
        val faqItem = faqList[position]
        holder.tvHeader.text = faqItem.question
        holder.tvContent.text = faqItem.answer

        holder.itemView.setOnClickListener {
            faqItem.isExpanded = !faqItem.isExpanded
            notifyItemChanged(position)
        }

        if (faqItem.isExpanded) {
            holder.contentLayout.visibility = View.VISIBLE
            holder.ivExpand.setImageResource(R.drawable.ic_arrow_up)
        } else {
            holder.contentLayout.visibility = View.GONE
            holder.ivExpand.setImageResource(R.drawable.ic_arrow_down)
        }
    }

    override fun getItemCount(): Int {
        return faqList.size
    }

    class FaqViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvHeader: TextView = itemView.findViewById(R.id.tvHeader)
        val tvContent: TextView = itemView.findViewById(R.id.tvContent)
        val ivExpand: ImageView = itemView.findViewById(R.id.ivExpand)
        val contentLayout: LinearLayout = itemView.findViewById(R.id.contentLayout)
    }
}
