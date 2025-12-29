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
import com.shambhu.myapplication.adapter.page_adapter.CoreNumberPagerAdapter
import com.shambhu.myapplication.databinding.ActivityCoreNumberBinding
import com.shambhu.myapplication.utils.Constants

class CoreNumberActivity : BaseDrawerActivity<ActivityCoreNumberBinding>() {

    override val bindingInflater: (LayoutInflater) -> ActivityCoreNumberBinding
        get() = ActivityCoreNumberBinding::inflate

    override val drawerLayout: DrawerLayout
        get() = binding.drawerLayout
    override val navView: NavigationView
        get() = binding.navView
    override val toolbar: Toolbar
        get() = binding.toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.title = "Core Numbers"

        val viewPager = binding.viewPager
        val tabs = binding.tabs

        //val sharedPref = this.getSharedPreferences(Constants.PREFERENCE_NAME, Context.MODE_PRIVATE)
        val fullName = sharedPreferences?.getString(Constants.PREFERENCE_OFFICIAL_NAME, "Guest").toString()
        val dob = sharedPreferences?.getString(Constants.PREFERENCE_DATE_OF_BIRTH, "0000-00-00").toString()

        viewPager.adapter = CoreNumberPagerAdapter(this, dob, fullName)
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.setCustomView(R.layout.custom_tab)
            val tabIcon = tab.customView?.findViewById<ImageView>(R.id.tab_icon)
            val tabText = tab.customView?.findViewById<TextView>(R.id.tab_text)

            tabText?.text = when (position) {
                0 -> "Number"
                1 -> "Name"
                2 -> "Karmic"
                3 -> "Lucky"
                4 -> "Grid"
                else -> null
            }
            tabIcon?.setImageDrawable(
                when (position) {
                    0 -> getDrawable(R.drawable.ic_dob)
                    1 -> getDrawable(R.drawable.ic_name)
                    2 -> getDrawable(R.drawable.ic_karmic)
                    3 -> getDrawable(R.drawable.ic_moon)
                    4 -> getDrawable(R.drawable.ic_grid)
                    else -> null
                }
            )
        }.attach()

        binding.tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val tabTextView = tab?.customView?.findViewById<TextView>(R.id.tab_text)
                supportActionBar?.title = "${tabTextView?.text} Numbers"
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })
    }
}
