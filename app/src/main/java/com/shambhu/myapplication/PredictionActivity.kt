package com.shambhu.myapplication

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.shambhu.myapplication.adapter.page_adapter.PredictionPagerAdapter
import com.shambhu.myapplication.databinding.ActivityPredictionBinding
import com.shambhu.myapplication.utils.Constants

class PredictionActivity : BaseDrawerActivity<ActivityPredictionBinding>() {

    override val bindingInflater: (LayoutInflater) -> ActivityPredictionBinding
        get() = ActivityPredictionBinding::inflate
    override val drawerLayout: DrawerLayout
        get() = binding.drawerLayout
    override val navView: NavigationView
        get() = binding.navView
    override val toolbar: Toolbar
        get() = binding.toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.title = "Daily Prediction"

        //setupToolbar(binding.toolbar, "Daily Prediction")
        val fullName = sharedPreferences?.getString(Constants.PREFERENCE_OFFICIAL_NAME, "Guest").toString()
        val dob = sharedPreferences?.getString(Constants.PREFERENCE_DATE_OF_BIRTH, "0000-00-00").toString()


        val viewPager = binding.viewPager
        val tabs = binding.tabs

        viewPager.adapter = PredictionPagerAdapter(this, dob, fullName)
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
