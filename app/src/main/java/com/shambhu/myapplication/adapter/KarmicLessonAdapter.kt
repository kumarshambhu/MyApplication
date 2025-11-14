package com.shambhu.myapplication.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.shambhu.myapplication.R

class KarmicLessonAdapter(private val karmicLessons: List<Pair<String, String>>) :
    RecyclerView.Adapter<KarmicLessonAdapter.KarmicLessonViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KarmicLessonViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_karmic_lesson, parent, false)
        return KarmicLessonViewHolder(view)
    }

    override fun onBindViewHolder(holder: KarmicLessonViewHolder, position: Int) {
        val (number, detail) = karmicLessons[position]
        holder.bind(number, detail)
    }

    override fun getItemCount(): Int = karmicLessons.size

    class KarmicLessonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val numberTextView: TextView = itemView.findViewById(R.id.karmicLessonNumber)
        private val detailTextView: TextView = itemView.findViewById(R.id.karmicLessonDetail)

        fun bind(number: String, detail: String) {
            numberTextView.text = "Karmic Lesson Number $number"
            detailTextView.text = detail
        }
    }
}
