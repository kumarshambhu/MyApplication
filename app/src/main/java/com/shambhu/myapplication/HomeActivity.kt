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
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.shambhu.myapplication.adapter.HomePagerAdapter
import com.shambhu.myapplication.databinding.ActivityHomeBinding
import com.shambhu.myapplication.utils.Constants.Companion.TAB_KEY_EXPRESSION
import com.shambhu.myapplication.utils.Constants.Companion.TAB_KEY_BIRTH_NUMBER
import com.shambhu.myapplication.utils.Constants.Companion.TAB_KEY_PERSONALITY
import com.shambhu.myapplication.utils.Constants.Companion.TAB_KEY_SOUL_URGE
import com.shambhu.myapplication.utils.Constants.Companion.TAB_KEY_LIFE_PATH


class HomeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val toggle = ActionBarDrawerToggle(
            this, binding.drawerLayout, binding.toolbar,
            R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        binding.navView.setNavigationItemSelectedListener(this)

        val viewPager = binding.contentHome.viewPager
        val tabLayout = binding.tabs

        viewPager.adapter = HomePagerAdapter(this)

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.setCustomView(R.layout.custom_tab)
            val tabIcon = tab.customView?.findViewById<ImageView>(R.id.tab_icon)
            val tabText = tab.customView?.findViewById<TextView>(R.id.tab_text)
            tabIcon?.setImageDrawable(
                when (position) {
                    0 -> getDrawable(R.drawable.ic_mirrors)
                    1 -> getDrawable(R.drawable.ic_road)
                    2 -> getDrawable(R.drawable.ic_mic)
                    3 -> getDrawable(R.drawable.ic_sunrise)
                    4 -> getDrawable(R.drawable.ic_heart)
                    else -> null
                }
            )
            if (tabText != null) {
                tabText.text = when (position) {
                    0 -> TAB_KEY_PERSONALITY
                    1 -> TAB_KEY_LIFE_PATH
                    2 -> TAB_KEY_EXPRESSION
                    3 -> TAB_KEY_BIRTH_NUMBER
                    4 -> TAB_KEY_SOUL_URGE
                    else -> null
                }
            }
        }.attach()

        supportActionBar?.title = "Personality"

        binding.tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if(tab?.text?.equals("Per..") == true) {
                    supportActionBar?.title = "Personality\nTesting data for big text"
                } else{
                    supportActionBar?.title = tab?.text
                }
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
