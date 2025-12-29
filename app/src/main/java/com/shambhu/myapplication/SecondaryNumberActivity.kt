package com.shambhu.myapplication

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.shambhu.myapplication.adapter.page_adapter.SecondaryNumberPagerAdapter
import com.shambhu.myapplication.databinding.ActivitySecondaryNumberBinding
import com.shambhu.myapplication.utils.Constants

class SecondaryNumberActivity : BaseDrawerActivity<ActivitySecondaryNumberBinding>() {

    override val bindingInflater: (LayoutInflater) -> ActivitySecondaryNumberBinding
        get() = ActivitySecondaryNumberBinding::inflate
    override val drawerLayout: DrawerLayout
        get() = binding.drawerLayout
    override val navView: NavigationView
        get() = binding.navView
    override val toolbar: Toolbar
        get() = binding.toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.title = "Loshu Grid"

        val fullName = sharedPreferences?.getString(Constants.PREFERENCE_OFFICIAL_NAME, "Guest").toString()
        val dob = sharedPreferences?.getString(Constants.PREFERENCE_DATE_OF_BIRTH, "0000-00-00").toString()

        val viewPager = binding.viewPager
        val tabLayout = binding.tabs

        viewPager.adapter = SecondaryNumberPagerAdapter(this, dob, fullName)

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.setCustomView(R.layout.custom_tab)
            val tabIcon = tab.customView?.findViewById<ImageView>(R.id.tab_icon)
            val tabText = tab.customView?.findViewById<TextView>(R.id.tab_text)
            tabIcon?.setImageDrawable(
                when (position) {
                    0 -> getDrawable(R.drawable.ic_element)
                    1 -> getDrawable(R.drawable.ic_colors)
                    2 -> getDrawable(R.drawable.ic_personal)
                    3 -> getDrawable(R.drawable.ic_name)
                    else -> null
                }
            )
            if (tabText != null) {
                tabText.text = when (position) {
                    0 -> "Elements"
                    1 -> "Colors"
                    2 -> "Personal"
                    3 -> "Name"
                    else -> null
                }
            }
        }.attach()

        binding.tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val tabTextView = tab?.customView?.findViewById<TextView>(R.id.tab_text)
                supportActionBar?.title = tabTextView?.text
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })
    }
}
