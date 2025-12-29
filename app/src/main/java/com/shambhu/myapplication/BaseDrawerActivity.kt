package com.shambhu.myapplication

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.MenuItem
import android.widget.FrameLayout
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.viewbinding.ViewBinding
import com.google.android.material.navigation.NavigationView
import com.google.android.material.switchmaterial.SwitchMaterial
import com.shambhu.myapplication.utils.Constants
import com.shambhu.myapplication.utils.ThemeManager

abstract class BaseDrawerActivity<VB : ViewBinding> : BaseActivity<VB>(), NavigationView.OnNavigationItemSelectedListener {

    protected abstract val drawerLayout: DrawerLayout
    protected abstract val navView: NavigationView
    protected abstract val toolbar: Toolbar

    protected lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        ThemeManager.applyTheme(this)
        super.onCreate(savedInstanceState)
        sharedPreferences = getSharedPreferences(Constants.PREFERENCE_NAME, Context.MODE_PRIVATE)
        setupDrawer()
        populateNavHeader()
        setupBackButton()
        setupThemeToggle()
    }

    private fun setupBackButton() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START)
                } else {
                    finish()
                }
            }
        })
    }

    private fun setupDrawer() {
        setSupportActionBar(toolbar)
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar,
            R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        navView.setNavigationItemSelectedListener(this)
    }

    protected open fun populateNavHeader() {
        val fullName = sharedPreferences?.getString(Constants.PREFERENCE_OFFICIAL_NAME, "Guest").toString()
        val dob = sharedPreferences?.getString(Constants.PREFERENCE_DATE_OF_BIRTH, "0000-00-00").toString()

        val headerView = navView.getHeaderView(0)
        headerView.findViewById<TextView>(R.id.nav_header_full_name).text = fullName
        headerView.findViewById<TextView>(R.id.nav_header_dob).text = dob
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_logout -> {
                startActivity(Intent(this, MainActivity::class.java))
            }
            R.id.nav_home -> {
                startActivity(Intent(this, CoreNumberActivity::class.java))
            }
            R.id.nav_slideshow -> {
                startActivity(Intent(this, SecondaryNumberActivity::class.java))
            }
            R.id.nav_personal -> {
                startActivity(Intent(this, ExpandableActivity::class.java))
            }
            R.id.nav_mobile -> {
                startActivity(Intent(this, MobileNumerologyActivity::class.java))
            }

            R.id.nav_prediction -> {
                startActivity(Intent(this, PredictionActivity::class.java))
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }


    private fun setupThemeToggle() {
        // Check if theme toggle is in the menu
        val themeMenuItem = navView.menu.findItem(R.id.nav_theme_toggle)
        if (themeMenuItem != null) {
            setupMenuThemeToggle(themeMenuItem)
        }
    }

    private fun setupMenuThemeToggle(menuItem: MenuItem) {
        menuItem.setOnMenuItemClickListener {
            ThemeManager.toggleTheme(this)
            recreate() // Recreate activity to apply theme
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }
        updateThemeMenuItem(menuItem)
    }

    private fun updateThemeToggleSwitch(themeMenuItem: MenuItem) {
        val switchView = themeMenuItem.actionView as FrameLayout
        val switch = switchView.findViewById<SwitchMaterial>(R.id.theme_switch)
        switch.isChecked = ThemeManager.isDarkMode(this)
    }
    private fun updateThemeMenuItem(menuItem: MenuItem) {
        val isDarkMode = ThemeManager.isDarkMode(this)
        menuItem.title = if (isDarkMode) "Light Mode" else "Dark Mode"
        menuItem.icon = if (isDarkMode) {
            ContextCompat.getDrawable(this, R.drawable.ic_light_mode)
        } else {
            ContextCompat.getDrawable(this, R.drawable.ic_dark_mode)
        }
        updateThemeToggleSwitch(menuItem)
    }
    override fun onResume() {
        super.onResume()
        updateThemeToggleState()
    }

    private fun updateThemeToggleState() {
        val themeMenuItem = navView.menu.findItem(R.id.nav_theme_toggle)
        if (themeMenuItem != null) {
            updateThemeMenuItem(themeMenuItem)
        }
    }
}
