package com.shambhu.myapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayoutMediator
import com.shambhu.myapplication.adapter.PredictionPagerAdapter
import com.shambhu.myapplication.databinding.ActivityPredictionBinding

class PredictionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPredictionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPredictionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewPager = binding.viewPager
        val tabs = binding.tabs

        viewPager.adapter = PredictionPagerAdapter(this)

        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Horoscope"
                1 -> "Tarot"
                2 -> "Palmistry"
                3 -> "Crystal Ball"
                4 -> "I Ching"
                5 -> "Runes"
                6 -> "Tea Leaf"
                7 -> "Astrology"
                else -> null
            }
        }.attach()
    }
}
