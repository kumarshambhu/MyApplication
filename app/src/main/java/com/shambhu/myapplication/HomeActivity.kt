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
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.shambhu.myapplication.adapter.HomePagerAdapter
import com.shambhu.myapplication.databinding.ActivityHomeBinding
import com.shambhu.myapplication.fragment.home.BirthNumbersFragment
import com.shambhu.myapplication.fragment.home.ExpressionFragment
import com.shambhu.myapplication.fragment.home.LifePathFragment
import com.shambhu.myapplication.fragment.home.PersonalityFragment
import com.shambhu.myapplication.fragment.home.SoulUrgeFragment
import com.shambhu.myapplication.utils.Constants.Companion.TAB_KEY_BIRTH_NUMBER
import com.shambhu.myapplication.utils.Constants.Companion.TAB_KEY_EXPRESSION
import com.shambhu.myapplication.utils.Constants.Companion.TAB_KEY_LIFE_PATH
import com.shambhu.myapplication.utils.Constants.Companion.TAB_KEY_PERSONALITY
import com.shambhu.myapplication.utils.Constants.Companion.TAB_KEY_SOUL_URGE
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.util.Locale


class HomeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityHomeBinding

    fun getAllFragments(): List<Fragment> {
        return supportFragmentManager.fragments
    }
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

        // Extract data from intent
        val fullName = intent.getStringExtra("fullName") ?: "Guest"
        val dob = intent.getStringExtra("dob") ?: "0000-00-00"
        val time = intent.getStringExtra("time") ?: "00:00"
        val location = intent.getStringExtra("location") ?: "Unknown Location"


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
        // Calculate numerology numbers
        val birthDate = parseDate(dob)
        val lifePath = calculateLifePath(birthDate)
        val expression = calculateExpression(fullName)
        val personality = calculatePersonality(fullName)
        val soulUrge = calculateSoulUrge(fullName)
        val birthDay = birthDate.dayOfMonth
        val birthMonth = birthDate.monthValue
        val birthYear = birthDate.year


        val allFragments = getAllFragments()
        allFragments.forEach { fragment ->
            when (fragment) {
                is LifePathFragment -> {
                    println("LifePathFragment")
                    fragment.updateData(fullName, dob, time, location, lifePath)
                }
                is ExpressionFragment -> {
                    println("ExpressionFragment")
                    fragment.updateData(fullName, dob, time, location, expression) }
                is PersonalityFragment -> {
                    println("PersonalityFragment")
                    fragment.updateData(fullName, dob, time, location, personality) }
                is SoulUrgeFragment -> {
                    println("SoulUrgeFragment")
                    fragment.updateData(fullName, dob, time, location, soulUrge)
                }
                is BirthNumbersFragment -> {
                    println("BirthNumbersFragment")
                    fragment.updateData(fullName, dob, time, location, birthDay, birthMonth, birthYear)
                }
            }
        }


        // Pass data to fragments
        val lifePathFragment = supportFragmentManager.findFragmentByTag("LifePathFragment") as? LifePathFragment
        lifePathFragment?.updateData(fullName, dob, time, location, lifePath)

        val expressionFragment = supportFragmentManager.findFragmentByTag("ExpressionFragment") as? ExpressionFragment
        expressionFragment?.updateData(fullName, dob, time, location, expression)

        val personalityFragment = supportFragmentManager.findFragmentByTag("PersonalityFragment") as? PersonalityFragment
        personalityFragment?.updateData(fullName, dob, time, location, personality)

        val soulUrgeFragment = supportFragmentManager.findFragmentByTag("SoulUrgeFragment") as? SoulUrgeFragment
        soulUrgeFragment?.updateData(fullName, dob, time, location, soulUrge)

        val birthNumbersFragment = supportFragmentManager.findFragmentByTag("BirthNumbersFragment") as? BirthNumbersFragment
        birthNumbersFragment?.updateData(fullName,dob, time, location, birthDay, birthMonth, birthYear)

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




    // Helper function to set interpretations based on number
    private fun setInterpretation(
        textView: TextView,
        number: Int,
        type: String
    ) {
        val resourceId = resources.getIdentifier("$type" + if (number == 11 || number == 22) "_${number}" else "_${number % 9}", "string", packageName)
        if (resourceId != 0) {
            textView.text = resources.getString(resourceId)
        } else {
            textView.text = "Interpretation not available"
        }
    }

    // Helper function to parse date
    private fun parseDate(dateString: String): LocalDate {
        return try {
            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            sdf.parse(dateString)?.let { sdf.format(it) } ?: "Invalid Date"
            val sdfParse = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            sdfParse.parse(dateString)?.toInstant()?.atZone(ZoneId.systemDefault())?.toLocalDate()
                ?: LocalDate.of(0, 1, 1)
        } catch (_: Exception) {
            LocalDate.of(0, 1, 1)
        }
    }

    // Life Path Number Calculation
    private fun calculateLifePath(date: LocalDate): Int {
        val day = date.dayOfMonth
        val month = date.monthValue
        val year = date.year

        // Reduce to single digit or master numbers
        var sum = day + month + year
        while (sum > 9) {
            sum = sum.toString().map { it.toString().toInt() }.sum()
        }
        return sum
    }

    // Expression (Destiny) Number Calculation
    private fun calculateExpression(name: String): Int {
        // Convert name to all uppercase and remove spaces
        val cleanedName = name.replace(" ", "").uppercase(Locale.getDefault())

        // Assign numerology values to each letter
        var total = 0
        for (char in cleanedName) {
            val value = when (char) {
                'A', 'J', 'S' -> 1
                'B', 'K', 'T' -> 2
                'C', 'L', 'U' -> 3
                'D', 'M', 'V' -> 4
                'E', 'N', 'W' -> 5
                'F', 'O', 'X' -> 6
                'G', 'P', 'Y' -> 7
                'H', 'Q', 'Z' -> 8
                'I', 'R' -> 9
                else -> 0 // For non-alphabet characters
            }
            total += value
        }

        // Reduce to single digit or master numbers
        while (total > 9) {
            total = total.toString().map { it.toString().toInt() }.sum()
        }
        return total
    }

    // Personality Number Calculation
    private fun calculatePersonality(name: String): Int {
        // Convert name to all uppercase and remove spaces
        val cleanedName = name.replace(" ", "").uppercase(Locale.getDefault())

        // Assign numerology values to each letter
        var total = 0
        for (char in cleanedName) {
            val value = when (char) {
                'B', 'K', 'T' -> 2
                'C', 'L', 'U' -> 3
                'D', 'M', 'V' -> 4
                'F', 'O', 'X' -> 6
                'G', 'P', 'Y' -> 7
                'H', 'Q', 'Z' -> 8
                else -> 0 // For non-alphabet characters
            }
            total += value
        }

        // Reduce to single digit or master numbers
        while (total > 9) {
            total = total.toString().map { it.toString().toInt() }.sum()
        }
        return total
    }

    // Soul Urge (Heart's Desire) Number Calculation
    private fun calculateSoulUrge(name: String): Int {
        // Convert name to all lowercase to handle vowels properly
        val cleanedName = name.replace(" ", "").lowercase(Locale.getDefault())

        // Count the vowels (a, e, i, o, u) and assign values
        var total = 0
        for (char in cleanedName) {
            val value = when (char) {
                'a' -> 1
                'e' -> 5
                'i' -> 9
                'o' -> 6
                'u' -> 3
                else -> 0 // For consonants
            }
            total += value
        }

        // Reduce to single digit or master numbers
        while (total > 9) {
            total = total.toString().map { it.toString().toInt() }.sum()
        }
        return total
    }
}
