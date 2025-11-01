package com.shambhu.myapplication

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.shambhu.myapplication.adapter.PersonalPagerAdapter
import com.shambhu.myapplication.databinding.ActivityPersonalBinding
import com.shambhu.myapplication.utils.CommonUtils
import com.shambhu.myapplication.utils.Constants
import com.shambhu.myapplication.utils.Constants.Companion.TAB_KEY_KARMIC_NUMBER
import com.shambhu.myapplication.utils.Constants.Companion.TAB_KEY_PERSONAL_MONTH
import com.shambhu.myapplication.utils.Constants.Companion.TAB_KEY_PERSONAL_YEAR

class PersonalActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityPersonalBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPersonalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val toggle = ActionBarDrawerToggle(
            this, binding.drawerLayout, binding.toolbar,
            R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        binding.navView.setNavigationItemSelectedListener(this)


        val sharedPref = this.getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
        val fullName = sharedPref?.getString("fullName", "Guest").toString()
        val dob = sharedPref?.getString("date_of_birth","0000-00-00").toString()
        val time = sharedPref?.getString("time_of_birth", "00:00")
        val location = sharedPref?.getString("place_of_birth", "Unknown Location")

        // Update the navigation header
        val headerView = binding.navView.getHeaderView(0)
        headerView.findViewById<TextView>(R.id.nav_header_full_name).text = fullName
        headerView.findViewById<TextView>(R.id.nav_header_dob).text = dob
        headerView.findViewById<TextView>(R.id.nav_header_time).text = time
        headerView.findViewById<TextView>(R.id.nav_header_location).text = location

        val birthDate = CommonUtils.parseDate(dob)
        val birthDay = birthDate.dayOfMonth
        val birthMonth = birthDate.monthValue
        val birthYear = birthDate.year
        val adapter = PersonalPagerAdapter(this, birthDay, birthMonth, birthYear, fullName)
        binding.content.viewPager.adapter = adapter

        TabLayoutMediator(binding.tabs, binding.content.viewPager) { tab, position ->
            tab.setCustomView(R.layout.custom_tab)
            val tabIcon = tab.customView?.findViewById<ImageView>(R.id.tab_icon)
            val tabText = tab.customView?.findViewById<TextView>(R.id.tab_text)
            tabIcon?.setImageDrawable(
                when (position) {
                    0 -> getDrawable(R.drawable.ic_mirrors)
                    1 -> getDrawable(R.drawable.ic_road)
                    2 -> getDrawable(R.drawable.ic_road)
                    else -> null
                }
            )
            if (tabText != null) {
                tabText.text = when (position) {
                    0 -> TAB_KEY_PERSONAL_MONTH
                    1 -> TAB_KEY_PERSONAL_YEAR
                    2 -> TAB_KEY_KARMIC_NUMBER
                    else -> null
                }
            }
        }.attach()
        supportActionBar?.title = "Personal Year"

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
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_home -> {
                Snackbar.make(binding.root, "Home clicked", Snackbar.LENGTH_SHORT).show()
                val i = Intent(applicationContext, HomeActivity::class.java)
                startActivity(i)
            }
            R.id.nav_personal -> {
                Snackbar.make(binding.root, "Personal clicked", Snackbar.LENGTH_SHORT).show()
                val i = Intent(applicationContext, PersonalActivity::class.java)
                startActivity(i)
            }
            R.id.nav_slideshow -> {
                Snackbar.make(binding.root, "Slideshow clicked", Snackbar.LENGTH_SHORT).show()
            }
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}
