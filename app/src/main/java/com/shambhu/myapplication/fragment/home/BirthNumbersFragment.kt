package com.shambhu.myapplication.fragment.home


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.shambhu.myapplication.R
import com.shambhu.myapplication.utils.CommonUtils

class BirthNumbersFragment : Fragment() {

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

        tvBirthDayNumber = view.findViewById(R.id.tvBirthDayNumber)
        tvBirthDayInterpretation = view.findViewById(R.id.tvBirthDayInterpretation)
        tvBirthMonthNumber = view.findViewById(R.id.tvBirthMonthNumber)
        tvBirthMonthInterpretation = view.findViewById(R.id.tvBirthMonthInterpretation)
        tvBirthYearNumber = view.findViewById(R.id.tvBirthYearNumber)
        tvBirthYearInterpretation = view.findViewById(R.id.tvBirthYearInterpretation)

        arguments?.let {
            val birthDay = it.getInt(ARG_BIRTH_DAY)
            val birthMonth = it.getInt(ARG_BIRTH_MONTH)
            val birthYear = it.getInt(ARG_BIRTH_YEAR)

            tvBirthDayNumber.text = birthDay.toString()
            tvBirthMonthNumber.text = birthMonth.toString()
            tvBirthYearNumber.text = birthYear.toString()

            // Set interpretations
            val dayInterpretations = resources.getStringArray(R.array.birth_day_interpretations)
            val monthInterpretations = resources.getStringArray(R.array.birth_month_interpretations)
            val yearInterpretations = resources.getStringArray(R.array.birth_year_interpretations)
            val dayNumber = CommonUtils.reduceNumber(birthDay)
            println("Day Number: $dayNumber")
            if (dayNumber > 0 && dayNumber <= dayInterpretations.size) {
                tvBirthDayInterpretation.text = dayInterpretations[ dayNumber- 1]
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
        private const val ARG_BIRTH_DAY = "birthDay"
        private const val ARG_BIRTH_MONTH = "birthMonth"
        private const val ARG_BIRTH_YEAR = "birthYear"

        fun newInstance(
            birthDay: Int,
            birthMonth: Int,
            birthYear: Int
        ): BirthNumbersFragment {
            val fragment = BirthNumbersFragment()
            val args = Bundle()
            args.putInt(ARG_BIRTH_DAY, birthDay)
            args.putInt(ARG_BIRTH_MONTH, birthMonth)
            args.putInt(ARG_BIRTH_YEAR, birthYear)
            fragment.arguments = args
            return fragment
        }
    }
}
