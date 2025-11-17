package com.shambhu.myapplication.fragment.secondary_number

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.shambhu.myapplication.R
import com.shambhu.myapplication.databinding.FragmentElementColorBinding
import com.shambhu.myapplication.databinding.FragmentPersonalMonthYearBinding
import com.shambhu.myapplication.utils.CommonUtils
import com.shambhu.myapplication.utils.Constants.Companion.ARG_DAY
import com.shambhu.myapplication.utils.Constants.Companion.ARG_FULL_NAME
import com.shambhu.myapplication.utils.Constants.Companion.ARG_DOB
import com.shambhu.myapplication.utils.Constants.Companion.ARG_MONTH
import com.shambhu.myapplication.utils.Constants.Companion.ARG_YEAR
import com.shambhu.myapplication.utils.NumerologyCalculationUtils

class PersonalMonthYearFragment : Fragment() {

    private var _binding: FragmentPersonalMonthYearBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPersonalMonthYearBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            val dob = it.getString(ARG_DOB)
            val date = CommonUtils.parseDate(dob.toString())
            val day = date.dayOfMonth
            val month = date.monthValue
            val year = date.year
            //tvMonth.text = "$day $month $year"
            val personalYear = NumerologyCalculationUtils.calculatePersonalYear(day, month)
            binding.tvPersonalYear.text = "$personalYear"
            val yearInterpretations = resources.getStringArray(R.array.personal_year_interpretations)
            binding.tvPersonalYearInterpretation.text = yearInterpretations[personalYear-1]


            val personalMonth = NumerologyCalculationUtils.calculatePersonalMonth(day, month)
            binding.tvPersonalMonth.text = personalMonth.toString()
            print("Data Received: $day $month $year")

            val monthInterpretations = resources.getStringArray(R.array.personal_month_interpretations)
            binding.tvPersonalMonthInterpretation.text = monthInterpretations[personalMonth-1]

            print("Data Received: $day $month $year")
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PersonalMonthYearFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_DOB, param1)
                    putString(ARG_FULL_NAME, param2)
                }
            }
    }
}