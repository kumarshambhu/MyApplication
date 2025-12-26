package com.shambhu.myapplication

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.RadioButton
import androidx.core.content.edit
import com.shambhu.myapplication.databinding.ActivityMainBinding
import com.shambhu.myapplication.utils.Constants
import java.util.Calendar

class MainActivity : BaseActivity<ActivityMainBinding>() {

    private var selectedDate: String? = null
    override val bindingInflater: (LayoutInflater) -> ActivityMainBinding
        get() = ActivityMainBinding::inflate

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       // setupToolbar(binding.toolbar, "Enter Your Personal Details", true)

        binding.dobLayout.setOnClickListener {
            showDatePickerDialog()
        }

        // Handle button click
        binding.calculateButton.setOnClickListener {
            val currentName = binding.currentNameEditText.text.toString().ifEmpty { "Shubhu" }
            val officialName = binding.officialNameEditText.text.toString().ifEmpty { "Swarnav Shubh" }
            val dob = selectedDate ?: "27/08/2012"
            //val time = binding.timeEditText.text.toString().ifEmpty { "01:45" }
            //val location = binding.locationEditText.text.toString().ifEmpty { "Gaya" }
            val selectedGenderId = binding.genderRadioGroup.checkedRadioButtonId
            val gender = if (selectedGenderId != -1) {
                findViewById<RadioButton>(selectedGenderId).text.toString()
            } else {
                "Male"
            }

            // Validate inputs
            if (officialName.isNotEmpty() && dob.isNotEmpty() && currentName.isNotEmpty()) {
                Log.i("MainActivity", "Full Name: $officialName")
                Log.i("MainActivity", "Date of Birth: $dob")
                val sharedPref = this.getSharedPreferences(Constants.PREFERENCE_NAME, Context.MODE_PRIVATE)
                sharedPref.edit {
                    putString(Constants.PREFERENCE_OFFICIAL_NAME, officialName)
                    putString(Constants.PREFERENCE_DATE_OF_BIRTH, dob)
                    putString(Constants.PREFERENCE_CURRENT_NAME, currentName)
                    putString(Constants.PREFERENCE_GENDER, gender)
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
}
