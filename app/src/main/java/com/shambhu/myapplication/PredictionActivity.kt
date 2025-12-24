package com.shambhu.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.TextView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.shambhu.myapplication.adapter.page_adapter.PredictionPagerAdapter
import com.shambhu.myapplication.databinding.ActivityPredictionBinding

class PredictionActivity : BaseActivity<ActivityPredictionBinding>() {

    override val bindingInflater: (LayoutInflater) -> ActivityPredictionBinding
        get() = ActivityPredictionBinding::inflate

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupToolbar(binding.toolbar, "Daily Prediction")

        val viewPager = binding.viewPager
        val tabs = binding.tabs

        viewPager.adapter = PredictionPagerAdapter(this)
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.setCustomView(R.layout.custom_tab)
            val tabText = tab.customView?.findViewById<TextView>(R.id.tab_text)

            tabText?.text = when (position) {
                0 -> "Daily"
                1 -> "Monthly"
                2 -> "Yearly"
                3 -> "Life"
                else -> null
            }
        }.attach()

        binding.tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val tabTextView = tab?.customView?.findViewById<TextView>(R.id.tab_text)
                supportActionBar?.title = "${tabTextView?.text} Prediction"
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })
    }
}
