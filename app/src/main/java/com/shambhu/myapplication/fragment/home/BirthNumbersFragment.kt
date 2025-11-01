package com.shambhu.myapplication.fragment.home


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.shambhu.myapplication.R
import com.shambhu.myapplication.databinding.FragmentBirthNumbersBinding
import com.shambhu.myapplication.utils.CommonUtils
import com.shambhu.myapplication.utils.NumerologyCalculationUtils

class BirthNumbersFragment : Fragment() {

    private var _binding: FragmentBirthNumbersBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBirthNumbersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            val birthDay = it.getInt(ARG_BIRTH_DAY)
            val birthMonth = it.getInt(ARG_BIRTH_MONTH)
            val birthYear = it.getInt(ARG_BIRTH_YEAR)

            binding.tvBirthDayNumber.text = birthDay.toString()
            binding.tvBirthMonthNumber.text = birthMonth.toString()
            binding.tvBirthYearNumber.text = birthYear.toString()

            // Set interpretations
            val dayInterpretations = resources.getStringArray(R.array.birth_day_interpretations)
            val monthInterpretations = resources.getStringArray(R.array.birth_month_interpretations)
            val yearInterpretations = resources.getStringArray(R.array.birth_year_interpretations)
            val dayNumber = CommonUtils.reduceNumber(birthDay)
            println("Day Number: $dayNumber")
            if (dayNumber > 0 && dayNumber <= dayInterpretations.size) {
                binding.tvBirthDayInterpretation.text = dayInterpretations[ dayNumber- 1]
            }
            if (birthMonth > 0 && birthMonth <= monthInterpretations.size) {
                binding.tvBirthMonthInterpretation.text = monthInterpretations[birthMonth - 1]
            }
            if (birthYear > 0 && (birthYear % 10) > 0 && (birthYear % 10) <= yearInterpretations.size) {
                binding.tvBirthYearInterpretation.text = yearInterpretations[birthYear % 10 - 1]
            }

            // Calculate and display lucky number
            val luckyNumber = NumerologyCalculationUtils.calculateLuckyNumber(birthDay)
            binding.tvLuckyNumberValue.text = luckyNumber.toString()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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
