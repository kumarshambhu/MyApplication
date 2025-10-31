package com.shambhu.myapplication.fragment.home


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.shambhu.myapplication.R

class BirthNumbersFragment : Fragment() {

    private lateinit var tvFullName: TextView
    private lateinit var tvDateOfBirth: TextView
    private lateinit var tvTimeOfBirth: TextView
    private lateinit var tvLocation: TextView
    private lateinit var tvBirthDayNumber: TextView
    private lateinit var tvBirthDayInterpretation: TextView
    private lateinit var tvBirthMonthNumber: TextView
    private lateinit var tvBirthMonthInterpretation: TextView
    private lateinit var tvBirthYearNumber: TextView
    private lateinit var tvBirthYearInterpretation: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_birth_numbers, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvFullName = view.findViewById(R.id.tvFullName)
        tvDateOfBirth = view.findViewById(R.id.tvDateOfBirth)
        tvTimeOfBirth = view.findViewById(R.id.tvTimeOfBirth)
        tvLocation = view.findViewById(R.id.tvLocation)
        tvBirthDayNumber = view.findViewById(R.id.tvBirthDayNumber)
        tvBirthDayInterpretation = view.findViewById(R.id.tvBirthDayInterpretation)
        tvBirthMonthNumber = view.findViewById(R.id.tvBirthMonthNumber)
        tvBirthMonthInterpretation = view.findViewById(R.id.tvBirthMonthInterpretation)
        tvBirthYearNumber = view.findViewById(R.id.tvBirthYearNumber)
        tvBirthYearInterpretation = view.findViewById(R.id.tvBirthYearInterpretation)
    }

    fun updateData(
        fullName: String,
        dob: String,
        time: String,
        location: String,
        birthDay: Int,
        birthMonth: Int,
        birthYear: Int
    ) {
        tvFullName.text = fullName
        tvDateOfBirth.text = dob
        tvTimeOfBirth.text = time
        tvLocation.text = location
        tvBirthDayNumber.text = birthDay.toString()
        tvBirthMonthNumber.text = birthMonth.toString()
        tvBirthYearNumber.text = birthYear.toString()

        // Set interpretations
        val dayInterpretations = resources.getStringArray(R.array.birth_day_interpretations)
        val monthInterpretations = resources.getStringArray(R.array.birth_month_interpretations)
        val yearInterpretations = resources.getStringArray(R.array.birth_year_interpretations)

        tvBirthDayInterpretation.text = dayInterpretations[birthDay - 1]
        tvBirthMonthInterpretation.text = monthInterpretations[birthMonth - 1]
        tvBirthYearInterpretation.text = yearInterpretations[birthYear % 10 - 1]
    }
}
