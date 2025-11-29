package com.shambhu.myapplication

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout
import com.shambhu.myapplication.adapter.ExpandableRecyclerViewAdapter
import com.shambhu.myapplication.databinding.ActivityExpandableBinding
import com.shambhu.myapplication.utils.Constants

class ExpandableActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var binding: ActivityExpandableBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExpandableBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setup Toolbar
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = "Personal"

        // Setup Drawer
        val toggle = ActionBarDrawerToggle(
            this, binding.drawerLayout, binding.toolbar,
            R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        binding.navView.setNavigationItemSelectedListener(this)

        // Populate Nav Header
        val sharedPref = this.getSharedPreferences(Constants.PREFERENCE_NAME, Context.MODE_PRIVATE)
        val fullName = sharedPref?.getString(Constants.PREFERENCE_FULL_NAME, "Guest").toString()
        val dob = sharedPref?.getString(Constants.PREFERENCE_DATE_OF_BIRTH, "0000-00-00").toString()
        val headerView = binding.navView.getHeaderView(0)
        headerView.findViewById<TextView>(R.id.nav_header_full_name).text = fullName
        headerView.findViewById<TextView>(R.id.nav_header_dob).text = dob

        // Setup Bottom Tabs
        setupTabs()

        // Original functionality
        initViews()
        setupClickListeners()
        setupRecyclerViewExample()
    }

    private fun setupTabs() {
        binding.tabs.addTab(binding.tabs.newTab().setText("Home").setIcon(R.drawable.ic_home_filled))
        binding.tabs.addTab(binding.tabs.newTab().setText("Secondary").setIcon(R.drawable.ic_karmic))
        binding.tabs.addTab(binding.tabs.newTab().setText("Personal").setIcon(R.drawable.ic_pinnacle))

        binding.tabs.getTabAt(2)?.select()

        binding.tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> {
                        val intent = Intent(this@ExpandableActivity, CoreNumberActivity::class.java)
                        startActivity(intent)
                    }
                    1 -> {
                        val intent = Intent(this@ExpandableActivity, SecondaryNumberActivity::class.java)
                        startActivity(intent)
                    }
                    2 -> {
                        // Already here
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {
                // Do nothing
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
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun initViews() {
        binding.expandableText5.setToggleButtonTextColor(getColor(R.color.card_content))
        binding.expandableText5.setShowMoreText("Show more content")
        binding.expandableText5.setShowLessText("Hide content")

        binding.expandableText3.setOnExpandListener { isExpanded ->
            val message = if (isExpanded) "Text expanded!" else "Text collapsed!"
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupClickListeners() {
        binding.btnToggleAll.setOnClickListener {
            binding.expandableText3.toggle()
            binding.expandableText5.toggle()
        }
    }

    private fun setupRecyclerViewExample() {
        val sampleData = listOf(
            "First item with short text",
            "Second item with medium text that might need expansion in some cases",
            "Third item with very long text. Lorem ipsum dolor sit amet, consectetur adipiscing elit.",
            "Fourth item with another long description",
            "Fifth short item"
        )
        showRecyclerView(sampleData)
    }

    private fun showRecyclerView(data: List<String>) {
        val recyclerView = RecyclerView(this).apply {
            layoutManager = LinearLayoutManager(this@ExpandableActivity)
            adapter = ExpandableRecyclerViewAdapter(data)
        }
    }
}
