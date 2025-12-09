package com.shambhu.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.shambhu.myapplication.adapter.MobilePagerAdapter
import com.shambhu.myapplication.databinding.ActivityMobileBinding

class MobileActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

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
                0 -> "Home"
                1 -> "Profile"
                2 -> "Settings"
                else -> null
            }
            tabIcon?.setImageDrawable(
                when (position) {
                    0 -> getDrawable(R.drawable.ic_home)
                    1 -> getDrawable(R.drawable.ic_person)
                    2 -> getDrawable(R.drawable.ic_moon)
                    else -> null
                }
            )
        }.attach()

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
        setSupportActionBar(binding.toolbar)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_home -> {
                val i = Intent(applicationContext, CoreNumberActivity::class.java)
                startActivity(i)
            }

            R.id.nav_logout -> {
                val i = Intent(applicationContext, MainActivity::class.java)
                startActivity(i)
            }

            R.id.nav_personal -> {
                val i = Intent(applicationContext, ExpandableActivity::class.java)
                startActivity(i)
            }
            R.id.nav_slideshow -> {
                val i = Intent(applicationContext, SecondaryNumberActivity::class.java)
                startActivity(i)
            }

        }
        binding.drawerLayout.closeDrawer(GravityCompat.START)

        return true
    }
}
