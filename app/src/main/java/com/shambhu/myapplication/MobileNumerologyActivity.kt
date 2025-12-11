package com.shambhu.myapplication

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.shambhu.myapplication.databinding.ActivityMobileNumerologyBinding

class MobileNumerologyActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var binding: ActivityMobileNumerologyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMobileNumerologyBinding.inflate(layoutInflater)
        setContentView(binding.root)

// Setup Navigation
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        binding.bottomNavigationView.setupWithNavController(navController)
    }
}