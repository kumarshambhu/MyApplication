package com.shambhu.myapplication.fragment.other

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.shambhu.myapplication.databinding.FragmentNumerologyPlainBinding
import com.shambhu.myapplication.utils.CommonUtils
import com.shambhu.myapplication.utils.Constants.Companion.ARG_DOB
import com.shambhu.myapplication.utils.Constants.Companion.ARG_FULL_NAME
import com.shambhu.myapplication.utils.NumerologyCalculationUtils

class NumerologyPlainFragment : Fragment() {

    private var _binding: FragmentNumerologyPlainBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNumerologyPlainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            val dob = it.getString(ARG_DOB)
            val fullName = it.getString(ARG_FULL_NAME)

            if (dob != null && fullName != null) {
                val date = CommonUtils.parseDate(dob)
                val day = date.dayOfMonth
                val month = date.monthValue
                val year = date.year

                val soulUrge = NumerologyCalculationUtils.calculateSoulUrge(fullName)
                val lifePath = NumerologyCalculationUtils.calculateLifePath(day, month, year)
                val expression = NumerologyCalculationUtils.calculateExpression(fullName)
                val personality = NumerologyCalculationUtils.calculatePersonality(fullName)
                val personalYear = NumerologyCalculationUtils.calculatePersonalYear(day, month)
                val personalMonth = NumerologyCalculationUtils.calculatePersonalMonth(day, month)
                val karmicNumber =
                    NumerologyCalculationUtils.calculateKarmicNumber(day, month, year)
                val karmicFromName = NumerologyCalculationUtils.calculateKarmicFromName(fullName)
                val challengeNumbers =
                    NumerologyCalculationUtils.calculateChallengeNumbers(day, month, year)
                val challengeNumberAgeRanges =
                    NumerologyCalculationUtils.calculateChallengeNumberAgeRanges(day, month, year)
                val pinnacleNumbers =
                    NumerologyCalculationUtils.calculatePinnacleNumbers(day, month, year)
                val pinnacleNumberAgeRanges =
                    NumerologyCalculationUtils.calculatePinnacleNumberAgeRanges(day, month, year)
                val luckyNumber = NumerologyCalculationUtils.calculateLuckyNumber(day)

                binding.tvSoulUrgeValue.text = soulUrge.toString()
                binding.tvLifePathValue.text = lifePath.toString()
                binding.tvExpressionValue.text = expression.toString()
                binding.tvPersonalityValue.text = personality.toString()
                binding.tvPersonalYearValue.text = personalYear.toString()
                binding.tvPersonalMonthValue.text = personalMonth.toString()
                binding.tvKarmicNumberValue.text = karmicNumber
                binding.tvKarmicFromNameValue.text = karmicFromName
                binding.tvChallengeNumbersValue.text = challengeNumbers.joinToString(", ")
                binding.tvChallengeNumberAgeRangesValue.text =
                    challengeNumberAgeRanges.joinToString("\n")
                binding.tvPinnacleNumbersValue.text = pinnacleNumbers.joinToString(", ")
                binding.tvPinnacleNumberAgeRangesValue.text =
                    pinnacleNumberAgeRanges.joinToString("\n")
                binding.tvLuckyNumberValue.text = luckyNumber.toString()

            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(dob: String, fullName: String): NumerologyPlainFragment {
            val fragment = NumerologyPlainFragment()
            val args = Bundle()
            args.putString(ARG_DOB, dob)
            args.putString(ARG_FULL_NAME, fullName)
            fragment.arguments = args
            return fragment
        }
    }
}
