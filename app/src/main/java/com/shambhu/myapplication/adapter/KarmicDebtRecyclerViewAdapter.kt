package com.shambhu.myapplication.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.shambhu.myapplication.databinding.ItemKarmicDebtBinding
import com.shambhu.myapplication.model.KarmicDebt

class KarmicDebtRecyclerViewAdapter(
    private val context: Context,
    private val karmicDebtNumbers: List<KarmicDebt>,
    private val onItemClick: (Int) -> Unit
) : RecyclerView.Adapter<KarmicDebtRecyclerViewAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemKarmicDebtBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val karmicDebt = karmicDebtNumbers[position]

        holder.binding.tvKarmicDebtNumber.text = "Karmic Debt Number: ${karmicDebt.number}"
        holder.binding.tvKarmicDebtSource.text = "Source: ${karmicDebt.source}"

        holder.binding.outcomeTextView.text = karmicDebt.potentialOutcome
        if (karmicDebt.potentialOutcome.isNullOrEmpty()) {
            holder.binding.outcomeTitleTextView.visibility = View.GONE
            holder.binding.outcomeTextView.visibility = View.GONE
        }

        holder.binding.summaryTextView.text = karmicDebt.summary
        if (karmicDebt.summary.isNullOrEmpty()) {
            holder.binding.summaryTitleTextView.visibility = View.GONE
            holder.binding.summaryTextView.visibility = View.GONE
        }

        if (karmicDebt.challengesAndProblems.isNullOrEmpty()) {
            holder.binding.challengesTitleTextView.visibility = View.GONE
            holder.binding.challengesRecyclerView.visibility = View.GONE
        } else {
            CommonAdapterUtil.setupNumberBulletRecyclerViewAdapter(
                context,
                holder.binding.challengesRecyclerView,
                karmicDebt.challengesAndProblems
            )
        }


        if (karmicDebt.qualities.isNullOrEmpty()) {
            holder.binding.qualitiesTitleTextView.visibility = View.GONE
            holder.binding.qualitiesRecyclerView.visibility = View.GONE
        } else {
            CommonAdapterUtil.setupNumberBulletRecyclerViewAdapter(
                context,
                holder.binding.qualitiesRecyclerView,
                karmicDebt.qualities
            )
        }


        if (karmicDebt.keysToOvercome.isNullOrEmpty()) {
            holder.binding.qualitiesTitleTextView.visibility = View.GONE
            holder.binding.keysToOvercomeRecyclerView.visibility = View.GONE
        } else {
            val accordionItems = karmicDebt.keysToOvercome.map { debt ->
                Pair(debt.key, debt.description)
            }
            CommonAdapterUtil.setupNumberRecyclerViewAdapter(
                context,
                holder.binding.keysToOvercomeRecyclerView,
                accordionItems
            )
        }

        if (karmicDebt.isExpanded) {
            holder.binding.contentLayout.visibility = View.VISIBLE
            holder.binding.ivExpand.rotation = 180f
        } else {
            holder.binding.contentLayout.visibility = View.GONE
            holder.binding.ivExpand.rotation = 0f
        }

        holder.itemView.setOnClickListener {
            onItemClick(position)
        }

    }

    override fun getItemCount(): Int {
        return karmicDebtNumbers.size
    }

    class ViewHolder(val binding: ItemKarmicDebtBinding) : RecyclerView.ViewHolder(binding.root)


    /*private fun setupNumberBulletRecyclerViewAdapter(holder: RecyclerView, data: List<String>?) {
        val accordionItems = data?.map { number ->
            Pair(">", number)
        }

        val challengeNumberRecyclerViewAdapter =
            BulletPointRecyclerViewAdapter(false, accordionItems)
        holder.layoutManager = LinearLayoutManager(context)
        holder.adapter = challengeNumberRecyclerViewAdapter
    }*/

    /*private fun setupNumberRecyclerViewAdapter(
        holder: RecyclerView,
        data: List<Pair<String, String>>?
    ) {
        val challengeNumberRecyclerViewAdapter =
            BulletPointRecyclerViewAdapter(true, data)
        holder.layoutManager = LinearLayoutManager(context)
        holder.adapter = challengeNumberRecyclerViewAdapter
    }*/


}
