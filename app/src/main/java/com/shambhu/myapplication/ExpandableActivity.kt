package com.shambhu.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.shambhu.myapplication.adapter.page_adapter.ExpandablePagerAdapter
import com.shambhu.myapplication.databinding.ActivityExpandableBinding
import com.shambhu.myapplication.utils.Constants

class ExpandableActivity : BaseDrawerActivity<ActivityExpandableBinding>() {

    override val bindingInflater: (LayoutInflater) -> ActivityExpandableBinding
        get() = ActivityExpandableBinding::inflate
    override val drawerLayout: DrawerLayout
        get() = binding.drawerLayout
    override val navView: NavigationView
        get() = binding.navView
    override val toolbar: Toolbar
        get() = binding.toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.title = "Loshu Grid"

        val sharedPref =
            this.getSharedPreferences(Constants.PREFERENCE_NAME, android.content.Context.MODE_PRIVATE)
        val fullName = sharedPref?.getString(Constants.PREFERENCE_OFFICIAL_NAME, "Guest").toString()
        val dob = sharedPref?.getString(Constants.PREFERENCE_DATE_OF_BIRTH, "0000-00-00").toString()

        val viewPager = binding.viewPager
        val tabLayout = binding.tabs

        viewPager.adapter = ExpandablePagerAdapter(this, dob, fullName)

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.setCustomView(R.layout.custom_tab)
            val tabIcon = tab.customView?.findViewById<ImageView>(R.id.tab_icon)
            val tabText = tab.customView?.findViewById<TextView>(R.id.tab_text)
            tabIcon?.setImageDrawable(
                when (position) {
                    0 -> getDrawable(R.drawable.ic_pinnacle)
                    1 -> getDrawable(R.drawable.ic_maturity)
                    2 -> getDrawable(R.drawable.ic_success)
                    3 -> getDrawable(R.drawable.ic_challenge)
                    //4 -> getDrawable(R.drawable.ic_grid)
                    4 -> getDrawable(R.drawable.ic_road)
                    else -> null
                }
            )
            if (tabText != null) {
                tabText.text = when (position) {
                    0 -> "Pinnacle"
                    1 -> "Maturity"
                    2 -> "Success"
                    3 -> "Challenge"
                    //4 -> "Careers"
                    4 -> "LifePath Cycle"
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

    override fun populateNavHeader() {
        super.populateNavHeader()
        val sharedPref =
            this.getSharedPreferences(Constants.PREFERENCE_NAME, android.content.Context.MODE_PRIVATE)
        val time = sharedPref?.getString(Constants.PREFERENCE_CURRENT_NAME, "00:00")
        val location = sharedPref?.getString(Constants.PREFERENCE_PLACE_OF_BIRTH, "Unknown Location")

        val headerView = binding.navView.getHeaderView(0)
        headerView.findViewById<TextView>(R.id.nav_header_time).text = time
        headerView.findViewById<TextView>(R.id.nav_header_location).text = location
    }
}
