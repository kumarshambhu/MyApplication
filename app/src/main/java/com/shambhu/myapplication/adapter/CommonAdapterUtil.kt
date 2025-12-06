package com.shambhu.myapplication.adapter

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

object CommonAdapterUtil {
    fun setupNumberBulletRecyclerViewAdapter(
        context: Context,
        holder: RecyclerView,
        data: List<String>?
    ) {
        val accordionItems = data?.map { number ->
            Pair(">", number)
        }

        val challengeNumberRecyclerViewAdapter =
            BulletPointRecyclerViewAdapter(false, accordionItems)
        holder.layoutManager = LinearLayoutManager(context)
        holder.adapter = challengeNumberRecyclerViewAdapter
    }
}