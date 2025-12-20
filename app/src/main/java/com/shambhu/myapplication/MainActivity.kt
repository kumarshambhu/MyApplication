package com.shambhu.myapplication

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.RadioButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import com.shambhu.myapplication.databinding.ActivityMainBinding
import com.shambhu.myapplication.utils.Constants.Companion.PREFERENCE_DATE_OF_BIRTH
import com.shambhu.myapplication.utils.Constants.Companion.PREFERENCE_FULL_NAME
import com.shambhu.myapplication.utils.Constants.Companion.PREFERENCE_GENDER
import com.shambhu.myapplication.utils.Constants.Companion.PREFERENCE_NAME
import com.shambhu.myapplication.utils.Constants.Companion.PREFERENCE_PLACE_OF_BIRTH
import com.shambhu.myapplication.utils.Constants.Companion.PREFERENCE_TIME_OF_BIRTH
import java.util.Calendar


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var selectedDate: String? = null

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

        binding.dobLayout.setOnClickListener {
            showDatePickerDialog()
        }

        // Handle button click
        binding.calculateButton.setOnClickListener {
            val fullName = binding.nameEditText.text.toString().ifEmpty { "Swarnav Shubh" }
            val dob = selectedDate ?: "27/08/2012"
            val time = binding.timeEditText.text.toString().ifEmpty { "01:45" }
            val location = binding.locationEditText.text.toString().ifEmpty { "Gaya" }
            val selectedGenderId = binding.genderRadioGroup.checkedRadioButtonId
            val gender = if (selectedGenderId != -1) {
                findViewById<RadioButton>(selectedGenderId).text.toString()
            } else {
                "Male"
            }

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
                    putString(PREFERENCE_GENDER, gender)
                }
                val i = Intent(applicationContext, CoreNumberActivity::class.java)
                startActivity(i)
            } else {
                // Show error message for empty fields
            }
        }
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDay ->
                selectedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                binding.dobTextView.text = selectedDate
            },
            year,
            month,
            day
        )
        datePickerDialog.show()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}