package com.shambhu.myapplication.adapter

import android.content.Context
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
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
        private val innerRecyclerView: RecyclerView = itemView.findViewById(R.id.innerRecyclerView)
        private val toggleIcon: ImageView = itemView.findViewById(R.id.toggleIcon)
        private val headerLayout: View = itemView.findViewById(R.id.headerLayout)
        private val accordionCard: MaterialCardView = itemView.findViewById(R.id.accordionCard)
        private val innerAdapter: FaqInnerAdapter

        init {
            innerAdapter = FaqInnerAdapter(context, emptyList())
            innerRecyclerView.layoutManager = LinearLayoutManager(context)
            innerRecyclerView.adapter = innerAdapter

            headerLayout.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    TransitionManager.beginDelayedTransition(accordionCard, AutoTransition())
                    val faqItem = faqList[position]
                    faqItem.isExpanded = !faqItem.isExpanded
                    notifyItemChanged(position)
                }
            }
        }

        fun bind(faqItem: FaqDefinition) {
            questionTextView.text = faqItem.name
            if (faqItem.isExpanded) {
                innerRecyclerView.visibility = View.VISIBLE
                toggleIcon.rotation = 180f
                accordionCard.strokeColor = ContextCompat.getColor(context, R.color.gold)
                accordionCard.cardElevation = 8f
                innerAdapter.updateData(faqItem.description)
            } else {
                innerRecyclerView.visibility = View.GONE
                toggleIcon.rotation = 0f
                accordionCard.strokeColor = ContextCompat.getColor(context, R.color.divider)
                accordionCard.cardElevation = 2f
            }
        }
    }
}
