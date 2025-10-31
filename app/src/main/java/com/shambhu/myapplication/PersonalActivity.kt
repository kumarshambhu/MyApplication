package com.shambhu.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import com.shambhu.myapplication.adapter.PersonalPagerAdapter
import com.shambhu.myapplication.databinding.ActivityPersonalBinding

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

        // Extract data from intent and update nav header
        val fullName = intent.getStringExtra("fullName") ?: "Guest"
        val dob = intent.getStringExtra("dob") ?: "0000-00-00"
        val headerView = binding.navView.getHeaderView(0)
        val navHeaderFullName = headerView.findViewById<TextView>(R.id.nav_header_full_name)
        val navHeaderDob = headerView.findViewById<TextView>(R.id.nav_header_dob)
        navHeaderFullName.text = fullName
        navHeaderDob.text = dob

        val adapter = PersonalPagerAdapter(this)
        binding.content.viewPager.adapter = adapter

        TabLayoutMediator(binding.tabs, binding.content.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Month"
                1 -> "Year"
                else -> null
            }
        }.attach()
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
