package com.shambhu.myapplication.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.shambhu.myapplication.R
import com.shambhu.myapplication.model.FaqDefinition

class FaqAdapter(private val context: Context, private val faqList: List<FaqDefinition>) :
    RecyclerView.Adapter<FaqAdapter.FaqViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FaqViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_faq, parent, false)
        return FaqViewHolder(view)
    }

    override fun onBindViewHolder(holder: FaqViewHolder, position: Int) {
        val faqItem = faqList[position]
        holder.bind(faqItem)
    }

    override fun getItemCount(): Int = faqList.size

    inner class FaqViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val questionTextView: TextView = itemView.findViewById(R.id.questionTextView)
        private val answerTextView: TextView = itemView.findViewById(R.id.answerTextView)
        private val toggleIcon: ImageView = itemView.findViewById(R.id.toggleIcon)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val faqItem = faqList[position]
                    faqItem.isExpanded = !faqItem.isExpanded
                    notifyItemChanged(position)
                }
            }
        }

        fun bind(faqItem: FaqDefinition) {
            questionTextView.text = faqItem.name
            answerTextView.text = faqItem.description[0].key
            answerTextView.visibility = if (faqItem.isExpanded) View.VISIBLE else View.GONE
            toggleIcon.rotation = if (faqItem.isExpanded) 45f else 0f
        }
    }
}
