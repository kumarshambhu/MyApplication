package com.shambhu.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.shambhu.myapplication.adapter.OtherPagerAdapter
import com.shambhu.myapplication.databinding.ActivityOtherBinding
import com.shambhu.myapplication.utils.Constants
import com.shambhu.myapplication.utils.Constants.Companion.PREFERENCE_DATE_OF_BIRTH
import com.shambhu.myapplication.utils.Constants.Companion.PREFERENCE_FULL_NAME
import com.shambhu.myapplication.utils.Constants.Companion.PREFERENCE_PLACE_OF_BIRTH
import com.shambhu.myapplication.utils.Constants.Companion.PREFERENCE_TIME_OF_BIRTH
import com.shambhu.myapplication.utils.Constants.Companion.PREFERENCE_NAME


class OtherActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityOtherBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityOtherBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val toggle = ActionBarDrawerToggle(
            this, binding.drawerLayout, binding.toolbar,
            R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        binding.navView.setNavigationItemSelectedListener(this)

        // Extract data from intent
        val sharedPref = this.getSharedPreferences(PREFERENCE_NAME, android.content.Context.MODE_PRIVATE)
        val fullName = sharedPref?.getString(PREFERENCE_FULL_NAME, "Guest").toString()
        val dob = sharedPref?.getString(PREFERENCE_DATE_OF_BIRTH,"0000-00-00").toString()
        val time = sharedPref?.getString(PREFERENCE_TIME_OF_BIRTH, "00:00")
        val location = sharedPref?.getString(PREFERENCE_PLACE_OF_BIRTH, "Unknown Location")

        val headerView = binding.navView.getHeaderView(0)
        headerView.findViewById<TextView>(R.id.nav_header_full_name).text = fullName
        headerView.findViewById<TextView>(R.id.nav_header_dob).text = dob
        headerView.findViewById<TextView>(R.id.nav_header_time).text = time
        headerView.findViewById<TextView>(R.id.nav_header_location).text = location


        val viewPager = binding.contentOther.viewPager
        val tabLayout = binding.tabs

        viewPager.adapter = OtherPagerAdapter(this, dob, fullName)

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.setCustomView(R.layout.custom_tab)
            val tabIcon = tab.customView?.findViewById<ImageView>(R.id.tab_icon)
            val tabText = tab.customView?.findViewById<TextView>(R.id.tab_text)
            tabIcon?.setImageDrawable(
                when (position) {
                    0 -> getDrawable(R.drawable.ic_grid)
                    1 -> getDrawable(R.drawable.ic_person)
                    2 -> getDrawable(R.drawable.ic_person)
                    3 -> getDrawable(R.drawable.ic_grid)
                    4 -> getDrawable(R.drawable.ic_person)
                    5 -> getDrawable(R.drawable.ic_person)
                    6 -> getDrawable(R.drawable.ic_person)
                    else -> null
                }
            )
            if (tabText != null) {
                tabText.text = when (position) {
                    0 -> "Loshu Grid"
                    1 -> Constants.TAB_KEY_NUMEROLOGY_PLAIN
                    2 -> "Testing"
                    3 -> "Data"
                    4 -> "Multiline"
                    5 -> "Numbers"
                    6 -> "Destiny"
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
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_home -> {
                val i = Intent(applicationContext, HomeActivity::class.java)
                startActivity(i)
            }
            R.id.nav_personal -> {
                val i = Intent(applicationContext, PersonalActivity::class.java)
                startActivity(i)
            }
            R.id.nav_slideshow -> {
                val i = Intent(applicationContext, OtherActivity::class.java)
                startActivity(i)
            }
            R.id.nav_prediction -> {
                val i = Intent(applicationContext, PredictionActivity::class.java)
                startActivity(i)
            }
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START)

        return true
    }

    @Deprecated("This method has been deprecated in favor of using the\n     " +
            " {@link OnBackPressedDispatcher} via {@link #getOnBackPressedDispatcher()}.\n      " +
            "The OnBackPressedDispatcher controls how back button events are dispatched\n      " +
            "to one or more {@link OnBackPressedCallback} objects.")
    @SuppressLint("GestureBackNavigation")
    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}
