package com.shambhu.myapplication.fragment.personal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.shambhu.myapplication.R
import com.shambhu.myapplication.databinding.FragmentPinnacleNumbersBinding
import com.shambhu.myapplication.utils.Constants.Companion.ARG_DAY
import com.shambhu.myapplication.utils.Constants.Companion.ARG_MONTH
import com.shambhu.myapplication.utils.Constants.Companion.ARG_YEAR
import com.shambhu.myapplication.utils.NumerologyCalculationUtils

class PinnacleNumbersFragment : Fragment() {

    private var _binding: FragmentPinnacleNumbersBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPinnacleNumbersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            val day = it.getInt(ARG_DAY)
            val month = it.getInt(ARG_MONTH)
            val year = it.getInt(ARG_YEAR)

            val pinnacleNumbers = NumerologyCalculationUtils.calculatePinnacleNumbers(day, month, year)
            val ageRanges = NumerologyCalculationUtils.calculatePinnacleNumberAgeRanges(day, month, year)
            val explanations = resources.getStringArray(R.array.pinnacle_number_interpretations)

            binding.tvFirstPinnacleValue.text = pinnacleNumbers[0].toString()
            binding.tvFirstPinnacleAgeRange.text = ageRanges[0]
            binding.tvFirstPinnacleExplanation.text = getPinnacleExplanation(pinnacleNumbers[0], explanations)

            binding.tvSecondPinnacleValue.text = pinnacleNumbers[1].toString()
            binding.tvSecondPinnacleAgeRange.text = ageRanges[1]
            binding.tvSecondPinnacleExplanation.text = getPinnacleExplanation(pinnacleNumbers[1], explanations)

            binding.tvThirdPinnacleValue.text = pinnacleNumbers[2].toString()
            binding.tvThirdPinnacleAgeRange.text = ageRanges[2]
            binding.tvThirdPinnacleExplanation.text = getPinnacleExplanation(pinnacleNumbers[2], explanations)

            binding.tvFourthPinnacleValue.text = pinnacleNumbers[3].toString()
            binding.tvFourthPinnacleAgeRange.text = ageRanges[3]
            binding.tvFourthPinnacleExplanation.text = getPinnacleExplanation(pinnacleNumbers[3], explanations)
        }
    }

    private fun getPinnacleExplanation(pinnacleNumber: Int, explanations: Array<String>): String {
        return when (pinnacleNumber) {
            11 -> explanations[9]
            22 -> explanations[10]
            else -> explanations[pinnacleNumber - 1]
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {


        fun newInstance(day: Int, month: Int, year: Int): PinnacleNumbersFragment {
            val fragment = PinnacleNumbersFragment()
            val args = Bundle()
            args.putInt(ARG_DAY, day)
            args.putInt(ARG_MONTH, month)
            args.putInt(ARG_YEAR, year)
            fragment.arguments = args
            return fragment
        }
    }
}
