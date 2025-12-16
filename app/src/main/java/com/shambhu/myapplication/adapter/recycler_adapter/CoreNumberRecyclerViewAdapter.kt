package com.shambhu.myapplication.adapter.recycler_adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.shambhu.myapplication.adapter.CommonAdapterUtil
import com.shambhu.myapplication.databinding.ItemCoreNumberBinding
import com.shambhu.myapplication.model.CoreNumberAccordionItem

class CoreNumberRecyclerViewAdapter(
    private val coreNumbers: List<CoreNumberAccordionItem>,
    private val context: Context,
    private val onItemClick: (Int) -> Unit
) :
    RecyclerView.Adapter<CoreNumberRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCoreNumberBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    fun Context.getDrawableResourceByName(imageName: String): Int {
        return resources.getIdentifier(imageName, "drawable", packageName)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val(header, title, list, imageSource, expanded) = coreNumbers[position]
        holder.binding.coreNumberValue.text = header
        holder.binding.coreNumberWhatItSays.text = title
        //holder.binding.coreNumberDescription.text = content

        val resId = context.getDrawableResourceByName(imageSource)
        holder.binding.numberImageData.setImageResource(resId)
        CommonAdapterUtil.setupNumberRecyclerViewAdapter(context, holder.binding.coreNumberDetailsRecyclerView, list.details)

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
    }

    override fun getItemCount(): Int = coreNumbers.size

   class ViewHolder(val binding: ItemCoreNumberBinding) : RecyclerView.ViewHolder(binding.root)
}