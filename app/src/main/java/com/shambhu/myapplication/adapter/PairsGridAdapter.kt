package com.shambhu.myapplication.adapter


import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import com.shambhu.myapplication.R
import com.shambhu.myapplication.model.NumerologyMobileCombination

data class PairGridItem(
    val pair: String,
    val combination: NumerologyMobileCombination? = null,
    val isSelected: Boolean = false
)

class PairsGridAdapter(
    private val onItemClick: (PairGridItem) -> Unit,
    private val onOpenClick: (PairGridItem) -> Unit
) : RecyclerView.Adapter<PairsGridAdapter.PairViewHolder>() {

    private var items: List<PairGridItem> = emptyList()

    fun updateItems(newItems: List<PairGridItem>) {
        items = newItems
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PairViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_pair_grid, parent, false)
        return PairViewHolder(view)
    }

    override fun onBindViewHolder(holder: PairViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = items.size

    inner class PairViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val pairNumber: TextView = itemView.findViewById(R.id.pairNumber)
        private val statusBadge: TextView = itemView.findViewById(R.id.statusBadge)
        private val openButton: MaterialButton = itemView.findViewById(R.id.openButton)

        fun bind(item: PairGridItem) {
            // Set pair number
            pairNumber.text = item.pair

            // Set status badge
            if (item.combination != null) {
                val state = item.combination.state
                statusBadge.text = state.replace("_", " ").split(" ")[0].uppercase()

                // Set badge color based on state
                when (state) {
                    "universal benefic" -> {
                        statusBadge.setBackgroundResource(R.drawable.badge_benefic)
                    }
                    "neutral combinations" -> {
                        statusBadge.setBackgroundResource(R.drawable.badge_neutral)
                    }
                    "malefic combinations" -> {
                        statusBadge.setBackgroundResource(R.drawable.badge_malefic)
                    }
                    else -> {
                        statusBadge.setBackgroundResource(R.drawable.badge_not_found)
                    }
                }

                // Show open button for found combinations
                openButton.visibility = View.VISIBLE
                openButton.setOnClickListener {
                    onOpenClick(item)
                }
            } else {
                statusBadge.text = "NOT FOUND"
                statusBadge.setBackgroundResource(R.drawable.badge_not_found)
                openButton.visibility = View.GONE
            }

            // Handle card click
            itemView.setOnClickListener {
                onItemClick(item)
            }

            // Visual feedback for selection
            itemView.isSelected = item.isSelected
           /* if (item.isSelected) {
                itemView.setCardBackgroundColor(Color.parseColor("#E3F2FD"))
            } else {
                itemView.setCardBackgroundColor(Color.WHITE)
            }*/
            val cardView = itemView as MaterialCardView
            if (item.isSelected) {
                cardView.setCardBackgroundColor(Color.parseColor("#E3F2FD"))
            } else {
                cardView.setCardBackgroundColor(Color.WHITE)
            }
        }
    }
}