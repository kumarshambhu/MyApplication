package com.shambhu.myapplication.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.shambhu.myapplication.R
import com.shambhu.myapplication.databinding.ItemLoshuGridPlaneBinding
import com.shambhu.myapplication.model.LoshuGridPlaneAccordionItem

class LoshuGridPlaneRecyclerViewAdapter(
    private val coreNumbers: List<LoshuGridPlaneAccordionItem>,
    private val context: Context,
    private val onItemClick: (Int) -> Unit
) :
    RecyclerView.Adapter<LoshuGridPlaneRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemLoshuGridPlaneBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    fun Context.getDrawableResourceByName(imageName: String): Int {
        return resources.getIdentifier(imageName, "drawable", packageName)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val(header, title, content, imageSource, expanded) = coreNumbers[position]
        holder.binding.planeName.text = header
        holder.binding.planePresentNumber.text = title
        holder.binding.planeDescription.text = content

        val resId = context.getDrawableResourceByName(imageSource)
        holder.binding.planeImage.setImageResource(resId)

        if (expanded) {
            holder.binding.coreNumberContentLayout.visibility = View.VISIBLE
            holder.binding.ivExpand.rotation = 180f
        } else {
            holder.binding.coreNumberContentLayout.visibility = View.GONE
            holder.binding.ivExpand.rotation = 0f
        }

        holder.itemView.setOnClickListener {
            onItemClick(position)
        }

        val background = when (header) {
            "Mental Plane" -> R.drawable.mental_plane_background
            "Emotional Plane" -> R.drawable.emotional_plane_background
            "Practical Plane" -> R.drawable.practical_plane_background
            "Thought Plane" -> R.drawable.thought_plane_background
            "Will Plane" -> R.drawable.will_plane_background
            "Action Plane" -> R.drawable.action_plane_background
            "Silver Success Plane" -> R.drawable.silver_success_plane_background
            "Golden Success Plane" -> R.drawable.golden_success_plane_background
            else -> R.drawable.mystical_gradient_background
        }
        (holder.binding.root).background = ContextCompat.getDrawable(context, background)
    }

    override fun getItemCount(): Int = coreNumbers.size

   class ViewHolder(val binding: ItemLoshuGridPlaneBinding) : RecyclerView.ViewHolder(binding.root)
}
