package com.shambhu.myapplication

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.shambhu.myapplication.adapter.MobilePagerAdapter
import com.shambhu.myapplication.databinding.ActivityMobileBinding

class MobileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMobileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMobileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()

        val viewPager = binding.viewPager
        val tabs = binding.tabs

        viewPager.adapter = MobilePagerAdapter(this)
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.setCustomView(R.layout.custom_tab)
            val tabIcon = tab.customView?.findViewById<ImageView>(R.id.tab_icon)
            val tabText = tab.customView?.findViewById<TextView>(R.id.tab_text)

            tabText?.text = when (position) {
                0 -> getString(R.string.home)
                1 -> getString(R.string.profile)
                2 -> getString(R.string.settings)
                else -> null
            }
            tabIcon?.setImageDrawable(
                when (position) {
                    0 -> getDrawable(R.drawable.ic_home)
                    1 -> getDrawable(R.drawable.ic_profile)
                    2 -> getDrawable(R.drawable.ic_settings)
                    else -> null
                }
            )
        }.attach()

        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = "Mobile Activity"

        binding.tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val tabTextView = tab?.customView?.findViewById<TextView>(R.id.tab_text)
                supportActionBar?.title = "${tabTextView?.text}"
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })
    }
}
