package com.shambhu.myapplication.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.shambhu.myapplication.databinding.ItemLoshuGridPlaneBinding
import com.shambhu.myapplication.model.LoshuGridPlaneAccordionItem
import com.shambhu.myapplication.utils.CommonUtils

class LoshuGridPlaneRecyclerViewAdapter(
    private val coreNumbers: List<LoshuGridPlaneAccordionItem>,
    private val context: Context,
    private val onItemClick: (Int) -> Unit
) :
    RecyclerView.Adapter<LoshuGridPlaneRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemLoshuGridPlaneBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    fun Context.getDrawableResourceByName(imageName: String): Int {
        return resources.getIdentifier(imageName, "drawable", packageName)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val (header, title, content,  traitsHeader, traits, remedies, imageSource, backgroundColor, headerColor, expanded) = coreNumbers[position]
        holder.binding.planeName.text = header
        if (title.isNotEmpty()) {
            holder.binding.planePresentNumber.text = title
        } else {
            holder.binding.planePresentNumber.visibility = View.GONE
        }
        if(content.isEmpty()){
            holder.binding.planeDescription.visibility = View.GONE
        }else{
            holder.binding.planeDescription.text = content
        }

        holder.binding.traitsHeader.text = traitsHeader
        if(traits.isNotEmpty()){
            CommonAdapterUtil.setupNumberBulletRecyclerViewAdapter(context, holder.binding.loshuRecyclerView, traits)
        }

        if(remedies.isNotEmpty()){
            CommonAdapterUtil.setupNumberBulletRecyclerViewAdapter(context, holder.binding.remediesRecyclerView, remedies)
        }else{
            holder.binding.remediesHeader.visibility = View.GONE
            holder.binding.remediesRecyclerView.visibility = View.GONE
        }

        if (imageSource.isNotEmpty()) {
            val resId = context.getDrawableResourceByName(imageSource)
            holder.binding.planeImage.setImageResource(resId)
        } else {
            holder.binding.planeImage.visibility = View.GONE
        }

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
        (holder.binding.root).background = ContextCompat.getDrawable(context, backgroundColor)
        if (headerColor != 0) {
            holder.binding.headerLayout.background = ContextCompat.getDrawable(context, headerColor)
        }
    }

    override fun getItemCount(): Int = coreNumbers.size

    class ViewHolder(val binding: ItemLoshuGridPlaneBinding) : RecyclerView.ViewHolder(binding.root)
}
