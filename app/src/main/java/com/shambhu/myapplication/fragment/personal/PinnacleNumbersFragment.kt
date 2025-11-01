package com.shambhu.myapplication.fragment.personal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.shambhu.myapplication.R
import com.shambhu.myapplication.databinding.FragmentPinnacleNumbersBinding
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
            binding.tvFirstPinnacleExplanation.text = explanations[pinnacleNumbers[0]-1]

            binding.tvSecondPinnacleValue.text = pinnacleNumbers[1].toString()
            binding.tvSecondPinnacleAgeRange.text = ageRanges[1]
            binding.tvSecondPinnacleExplanation.text = explanations[pinnacleNumbers[1]-1]

            binding.tvThirdPinnacleValue.text = pinnacleNumbers[2].toString()
            binding.tvThirdPinnacleAgeRange.text = ageRanges[2]
            binding.tvThirdPinnacleExplanation.text = explanations[pinnacleNumbers[2]-1]

            binding.tvFourthPinnacleValue.text = pinnacleNumbers[3].toString()
            binding.tvFourthPinnacleAgeRange.text = ageRanges[3]
            binding.tvFourthPinnacleExplanation.text = explanations[pinnacleNumbers[3]-1]
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val ARG_DAY = "day"
        private const val ARG_MONTH = "month"
        private const val ARG_YEAR = "year"

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
