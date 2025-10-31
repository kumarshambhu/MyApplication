package com.shambhu.myapplication

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.shambhu.myapplication.databinding.ActivityMainBinding

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
            val fullName = "Shambhu Kumar"//binding.nameEditText.text.toString()
            val dob = "17/03/1979"//binding.dobEditText.text.toString()
            val time = "01:45";//binding.timeEditText.text.toString()
            val location = "Gaya";//binding.locationEditText.text.toString()

            // Validate inputs
            if (fullName.isNotEmpty() && dob.isNotEmpty() && time.isNotEmpty() && location.isNotEmpty()) {
                val i = Intent(applicationContext, HomeActivity::class.java)
                i.putExtra("fullName", fullName)
                i.putExtra("dob", dob)
                i.putExtra("time", time)
                i.putExtra("location", location)
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