package com.shambhu.myapplication

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import com.shambhu.myapplication.databinding.ActivityMainBinding

import com.shambhu.myapplication.utils.Constants.Companion.PREFERENCE_FULL_NAME
import com.shambhu.myapplication.utils.Constants.Companion.PREFERENCE_DATE_OF_BIRTH
import com.shambhu.myapplication.utils.Constants.Companion.PREFERENCE_PLACE_OF_BIRTH
import com.shambhu.myapplication.utils.Constants.Companion.PREFERENCE_TIME_OF_BIRTH
import com.shambhu.myapplication.utils.Constants.Companion.PREFERENCE_NAME


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()
        //setSupportActionBar(binding.toolbar)
        // Set up the back button
        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setDisplayShowHomeEnabled(true)
        actionBar?.title = "Enter Your Personal Details"

        // Handle button click
        binding.calculateButton.setOnClickListener {
            val fullName = "Shambhuf Kumar"//binding.nameEditText.text.toString()
            //val dob = "17/03/1979"//binding.dobEditText.text.toString()
            val dob = "16/03/1979"//binding.dobEditText.text.toString()
            val time = "01:45";//binding.timeEditText.text.toString()
            val location = "Gaya";//binding.locationEditText.text.toString()

            // Validate inputs
            if (fullName.isNotEmpty() && dob.isNotEmpty() && time.isNotEmpty() && location.isNotEmpty()) {
                Log.i("MainActivity", "Full Name: $fullName")
                Log.i("MainActivity", "Date of Birth: $dob")
                val sharedPref = this.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
                sharedPref.edit {
                    putString(PREFERENCE_FULL_NAME, fullName)
                    putString(PREFERENCE_DATE_OF_BIRTH, dob)
                    putString(PREFERENCE_TIME_OF_BIRTH, time)
                    putString(PREFERENCE_PLACE_OF_BIRTH, location)
                    //putString("state_of_birth", state.selectedItem as String)
                }
                val i = Intent(applicationContext, HomeActivity::class.java)
                startActivity(i)
            } else {
                // Show error message for empty fields
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}