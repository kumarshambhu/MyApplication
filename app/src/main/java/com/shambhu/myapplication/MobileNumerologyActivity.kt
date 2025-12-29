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
import com.shambhu.myapplication.adapter.page_adapter.MobilePagerAdapter
import com.shambhu.myapplication.databinding.ActivityMobileNumerologyBinding
import com.shambhu.myapplication.utils.Constants

class MobileNumerologyActivity : BaseDrawerActivity<ActivityMobileNumerologyBinding>() {

    override val bindingInflater: (LayoutInflater) -> ActivityMobileNumerologyBinding
        get() = ActivityMobileNumerologyBinding::inflate
    override val drawerLayout: DrawerLayout
        get() = binding.drawerLayout
    override val navView: NavigationView
        get() = binding.navView
    override val toolbar: Toolbar
        get() = binding.toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.title = "Loshu Grid"

        val viewPager = binding.viewPager
        val tabLayout = binding.tabs

        viewPager.adapter = MobilePagerAdapter(this)

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.setCustomView(R.layout.custom_tab)
            val tabIcon = tab.customView?.findViewById<ImageView>(R.id.tab_icon)
            val tabText = tab.customView?.findViewById<TextView>(R.id.tab_text)
            tabIcon?.setImageDrawable(
                when (position) {
                    0 -> getDrawable(R.drawable.ic_grid)
                    1 -> getDrawable(R.drawable.ic_colors)
                    2 -> getDrawable(R.drawable.ic_mobile)
                    3 -> getDrawable(R.drawable.ic_faq)
                    else -> null
                }
            )
            if (tabText != null) {
                tabText.text = when (position) {
                    0 -> "Pair"
                    1 -> "Comma"
                    2 -> "Mobile"
                    3 -> "Faq"
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
        val time = sharedPreferences?.getString(Constants.PREFERENCE_CURRENT_NAME, "00:00")
        val location = sharedPreferences?.getString(Constants.PREFERENCE_PLACE_OF_BIRTH, "Unknown Location")

        val headerView = binding.navView.getHeaderView(0)
        headerView.findViewById<TextView>(R.id.nav_header_time).text = time
        headerView.findViewById<TextView>(R.id.nav_header_location).text = location
    }
}
