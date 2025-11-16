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
import com.shambhu.myapplication.utils.Constants.Companion.PREFERENCE_DATE_OF_BIRTH
import com.shambhu.myapplication.utils.Constants.Companion.PREFERENCE_FULL_NAME
import com.shambhu.myapplication.utils.Constants.Companion.PREFERENCE_NAME

class PredictionActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityPredictionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPredictionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewPager = binding.viewPager
        val tabs = binding.tabs

        val sharedPref = this.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        val fullName = sharedPref?.getString(PREFERENCE_FULL_NAME, "Guest").toString()
        val dob = sharedPref?.getString(PREFERENCE_DATE_OF_BIRTH,"0000-00-00").toString()

        viewPager.adapter = PredictionPagerAdapter(this, dob, fullName )
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.setCustomView(R.layout.custom_tab)
            val tabIcon = tab.customView?.findViewById<ImageView>(R.id.tab_icon)
            val tabText = tab.customView?.findViewById<TextView>(R.id.tab_text)

            tabText?.text = when (position) {
                0 -> "Core"
                1 -> "Karmic"
                2 -> "Grid"
                else -> null
            }
            tabIcon?.setImageDrawable(
                when (position) {
                    0 -> getDrawable(R.drawable.ic_moon)
                    1 -> getDrawable(R.drawable.ic_karmic)
                    2 -> getDrawable(R.drawable.ic_personality)
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


        val headerView = binding.navView.getHeaderView(0)
        headerView.findViewById<TextView>(R.id.nav_header_full_name).text = fullName
        headerView.findViewById<TextView>(R.id.nav_header_dob).text = dob

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_home -> {
                val i = Intent(this, HomeActivity::class.java)
                startActivity(i)
            }
            R.id.nav_personal -> {
                val i = Intent(this, PersonalActivity::class.java)
                startActivity(i)
            }
            R.id.nav_slideshow -> {
                val i = Intent(this, OtherActivity::class.java)
                startActivity(i)
            }
            R.id.nav_prediction -> {
                // Do nothing since we are already in this activity.
                // Creating a new instance would be inefficient.
            }
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}
