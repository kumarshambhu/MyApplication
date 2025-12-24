package com.shambhu.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.shambhu.myapplication.adapter.page_adapter.ExpandablePagerAdapter
import com.shambhu.myapplication.databinding.ActivityExpandableBinding
import com.shambhu.myapplication.utils.Constants.Companion.PREFERENCE_DATE_OF_BIRTH
import com.shambhu.myapplication.utils.Constants.Companion.PREFERENCE_FULL_NAME
import com.shambhu.myapplication.utils.Constants.Companion.PREFERENCE_NAME
import com.shambhu.myapplication.utils.Constants.Companion.PREFERENCE_PLACE_OF_BIRTH
import com.shambhu.myapplication.utils.Constants.Companion.PREFERENCE_TIME_OF_BIRTH

class ExpandableActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var binding: ActivityExpandableBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExpandableBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()

        setSupportActionBar(binding.toolbar)

        val toggle = ActionBarDrawerToggle(
            this, binding.drawerLayout, binding.toolbar,
            R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        binding.navView.setNavigationItemSelectedListener(this)

        val sharedPref =
            this.getSharedPreferences(PREFERENCE_NAME, android.content.Context.MODE_PRIVATE)
        val fullName = sharedPref?.getString(PREFERENCE_FULL_NAME, "Guest").toString()
        val dob = sharedPref?.getString(PREFERENCE_DATE_OF_BIRTH, "0000-00-00").toString()
        val time = sharedPref?.getString(PREFERENCE_TIME_OF_BIRTH, "00:00")
        val location = sharedPref?.getString(PREFERENCE_PLACE_OF_BIRTH, "Unknown Location")

        val headerView = binding.navView.getHeaderView(0)
        headerView.findViewById<TextView>(R.id.nav_header_full_name).text = fullName
        headerView.findViewById<TextView>(R.id.nav_header_dob).text = dob
        headerView.findViewById<TextView>(R.id.nav_header_time).text = time
        headerView.findViewById<TextView>(R.id.nav_header_location).text = location

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

        supportActionBar?.title = "Loshu Grid"

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

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home -> {
                val i = Intent(this, CoreNumberActivity::class.java)
                startActivity(i)
            }

            R.id.nav_slideshow -> {
                val i = Intent(this, SecondaryNumberActivity::class.java)
                startActivity(i)
            }

            R.id.nav_logout -> {
                val i = Intent(this, MainActivity::class.java)
                startActivity(i)
            }
            R.id.nav_mobile -> {
                val i = Intent(applicationContext, MobileNumerologyActivity::class.java)
                startActivity(i)
            }
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

}
