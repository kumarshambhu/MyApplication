package com.shambhu.myapplication

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.shambhu.myapplication.adapter.CoreNumberPagerAdapter
import com.shambhu.myapplication.databinding.ActivityCoreNumberBinding
import com.shambhu.myapplication.utils.Constants.Companion.PREFERENCE_DATE_OF_BIRTH
import com.shambhu.myapplication.utils.Constants.Companion.PREFERENCE_FULL_NAME
import com.shambhu.myapplication.utils.Constants.Companion.PREFERENCE_NAME

class CoreNumberActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityCoreNumberBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCoreNumberBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewPager = binding.viewPager
        val tabs = binding.tabs

        val sharedPref = this.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        val fullName = sharedPref?.getString(PREFERENCE_FULL_NAME, "Guest").toString()
        val dob = sharedPref?.getString(PREFERENCE_DATE_OF_BIRTH, "0000-00-00").toString()

        viewPager.adapter = CoreNumberPagerAdapter(this, dob, fullName)
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.setCustomView(R.layout.custom_tab)
            val tabIcon = tab.customView?.findViewById<ImageView>(R.id.tab_icon)
            val tabText = tab.customView?.findViewById<TextView>(R.id.tab_text)

            tabText?.text = when (position) {
                0 -> "Core"
                1 -> "Karmic"
                2 -> "Pinnacle"
                3 -> "Challenge"
                4 -> "Grid"
                else -> null
            }
            tabIcon?.setImageDrawable(
                when (position) {
                    0 -> getDrawable(R.drawable.ic_home)
                    1 -> getDrawable(R.drawable.ic_karmic)
                    2 -> getDrawable(R.drawable.ic_pinnacle)
                    3 -> getDrawable(R.drawable.ic_challenge)
                    4 -> getDrawable(R.drawable.ic_grid)
                    else -> null
                }
            )
        }.attach()

        //adjustTabMargins(binding.tabs, -20)
        supportActionBar?.title = "Core Numbers"

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
        setSupportActionBar(binding.toolbar)
        val toggle = ActionBarDrawerToggle(
            this, binding.drawerLayout, binding.toolbar,
            R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        binding.navView.setNavigationItemSelectedListener(this)


        val headerView = binding.navView.getHeaderView(0)
        headerView.findViewById<TextView>(R.id.nav_header_full_name).text = fullName
        headerView.findViewById<TextView>(R.id.nav_header_dob).text = dob

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_home -> {

            }

            R.id.nav_slideshow -> {
                val i = Intent(this, SecondaryNumberActivity::class.java)
                startActivity(i)
            }

            R.id.nav_personal -> {
                val i = Intent(applicationContext, ExpandableActivity::class.java)
                startActivity(i)
            }
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun adjustTabMargins(tabLayout: TabLayout, marginEndPx: Int) {
        val tabs = tabLayout.getChildAt(0) as ViewGroup
        for (i in 0 until tabs.childCount) {
            val tab = tabs.getChildAt(i)
            val layoutParams = tab.layoutParams as ViewGroup.MarginLayoutParams

            // Set the end margin (right margin for LTR)
            layoutParams.marginEnd = marginEndPx
            // Optionally set start margin as well if needed
            // layoutParams.marginStart = marginEndPx

            tab.layoutParams = layoutParams
            tab.requestLayout() // Request a new layout pass
        }
    }
}
