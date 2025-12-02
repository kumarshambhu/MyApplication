package com.shambhu.myapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.shambhu.myapplication.adapter.FaqAdapter
import com.shambhu.myapplication.databinding.ActivityFaqBinding
import com.shambhu.myapplication.model.FaqItem
import java.io.IOException

class FaqActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFaqBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFaqBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.rvFaq.layoutManager = LinearLayoutManager(this)

        val faqList = loadFaqs()
        val adapter = FaqAdapter(faqList)
        binding.rvFaq.adapter = adapter
    }

    private fun loadFaqs(): MutableList<FaqItem> {
        val json: String?
        try {
            val inputStream = assets.open("faq.json")
            json = inputStream.bufferedReader().use { it.readText() }
        } catch (ex: IOException) {
            ex.printStackTrace()
            return mutableListOf()
        }
        val type = object : TypeToken<MutableList<FaqItem>>() {}.type
        return Gson().fromJson(json, type)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
