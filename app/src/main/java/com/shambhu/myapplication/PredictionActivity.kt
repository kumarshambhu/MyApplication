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
import com.google.android.material.tabs.TabLayoutMediator
import com.shambhu.myapplication.R
import com.shambhu.myapplication.adapter.PredictionPagerAdapter
import com.shambhu.myapplication.databinding.ActivityPredictionBinding
import com.shambhu.myapplication.utils.Constants

class PredictionActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityPredictionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPredictionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewPager = binding.viewPager
        val tabs = binding.tabs

        viewPager.adapter = PredictionPagerAdapter(this)

        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.setCustomView(R.layout.custom_tab)
            val tabIcon = tab.customView?.findViewById<ImageView>(R.id.tab_icon)
            val tabText = tab.customView?.findViewById<TextView>(R.id.tab_text)

            tabText?.text = when (position) {
                0 -> "Horoscope"
                1 -> "Tarot"
                2 -> "Palmistry"
                3 -> "Crystal Ball"
                4 -> "I Ching"
                5 -> "Runes"
                6 -> "Tea Leaf"
                7 -> "Astrology"
                else -> null
            }
            tabIcon?.setImageDrawable(
                when (position) {
                    0 -> getDrawable(R.drawable.ic_moon)
                    1 -> getDrawable(R.drawable.ic_karmic)
                    2 -> getDrawable(R.drawable.ic_personality)
                    3 -> getDrawable(R.drawable.mystical_orb)
                    4 -> getDrawable(R.drawable.ic_grid)
                    5 -> getDrawable(R.drawable.mystical_symbol)
                    6 -> getDrawable(R.drawable.ic_earth)
                    7 -> getDrawable(R.drawable.ic_sunrise)
                    else -> null
                }
            )
        }.attach()

        setSupportActionBar(binding.toolbar)
        val toggle = ActionBarDrawerToggle(
            this, binding.drawerLayout, binding.toolbar,
            R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        binding.navView.setNavigationItemSelectedListener(this)

        val sharedPref = this.getSharedPreferences(Constants.PREFERENCE_NAME, Context.MODE_PRIVATE)
        val fullName = sharedPref?.getString(Constants.PREFERENCE_FULL_NAME, "Guest").toString()
        val dob = sharedPref?.getString(Constants.PREFERENCE_DATE_OF_BIRTH,"0000-00-00").toString()

        val headerView = binding.navView.getHeaderView(0)
        headerView.findViewById<TextView>(R.id.nav_header_full_name).text = fullName
        headerView.findViewById<TextView>(R.id.nav_header_dob).text = dob
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
}
