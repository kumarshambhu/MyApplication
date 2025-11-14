package com.shambhu.myapplication

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayoutMediator
import com.shambhu.myapplication.R
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
            tab.setCustomView(R.layout.custom_tab)
            val tabIcon = tab.customView?.findViewById<ImageView>(R.id.tab_icon)
            val tabText = tab.customView?.findViewById<TextView>(R.id.tab_text)

            tabText?.text = when (position) {
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
            tabIcon?.setImageDrawable(
                when (position) {
                    0 -> getDrawable(R.drawable.ic_moon)
                    1 -> getDrawable(R.drawable.ic_karmic)
                    2 -> getDrawable(R.drawable.ic_personality)
                    3 -> getDrawable(R.drawable.mystical_orb)
                    4 -> getDrawable(R.drawable.ic_grid)
                    5 -> getDrawable(R.drawable.mystical_symbol)
                    6 -> getDrawable(R.drawable.ic_earth)
                    7 -> getDrawable(R.drawable.ic_sunrise)
                    else -> null
                }
            )
        }.attach()
    }
}
