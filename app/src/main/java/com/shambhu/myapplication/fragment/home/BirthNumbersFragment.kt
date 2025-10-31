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

        arguments?.let {
            val fullName = it.getString(ARG_FULL_NAME) ?: ""
            val dob = it.getString(ARG_DOB) ?: ""
            val time = it.getString(ARG_TIME) ?: ""
            val location = it.getString(ARG_LOCATION) ?: ""
            val birthDay = it.getInt(ARG_BIRTH_DAY)
            val birthMonth = it.getInt(ARG_BIRTH_MONTH)
            val birthYear = it.getInt(ARG_BIRTH_YEAR)

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

            if (birthDay > 0 && birthDay <= dayInterpretations.size) {
                tvBirthDayInterpretation.text = dayInterpretations[birthDay - 1]
            }
            if (birthMonth > 0 && birthMonth <= monthInterpretations.size) {
                tvBirthMonthInterpretation.text = monthInterpretations[birthMonth - 1]
            }
            if (birthYear > 0 && (birthYear % 10) > 0 && (birthYear % 10) <= yearInterpretations.size) {
                tvBirthYearInterpretation.text = yearInterpretations[birthYear % 10 - 1]
            }
        }
    }

    companion object {
        private const val ARG_FULL_NAME = "fullName"
        private const val ARG_DOB = "dob"
        private const val ARG_TIME = "time"
        private const val ARG_LOCATION = "location"
        private const val ARG_BIRTH_DAY = "birthDay"
        private const val ARG_BIRTH_MONTH = "birthMonth"
        private const val ARG_BIRTH_YEAR = "birthYear"

        fun newInstance(
            fullName: String,
            dob: String,
            time: String,
            location: String,
            birthDay: Int,
            birthMonth: Int,
            birthYear: Int
        ): BirthNumbersFragment {
            val fragment = BirthNumbersFragment()
            val args = Bundle()
            args.putString(ARG_FULL_NAME, fullName)
            args.putString(ARG_DOB, dob)
            args.putString(ARG_TIME, time)
            args.putString(ARG_LOCATION, location)
            args.putInt(ARG_BIRTH_DAY, birthDay)
            args.putInt(ARG_BIRTH_MONTH, birthMonth)
            args.putInt(ARG_BIRTH_YEAR, birthYear)
            fragment.arguments = args
            return fragment
        }
    }
}
