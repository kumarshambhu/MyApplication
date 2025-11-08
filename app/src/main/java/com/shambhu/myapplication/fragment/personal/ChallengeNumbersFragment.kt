package com.shambhu.myapplication.fragment.personal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.shambhu.myapplication.R
import com.shambhu.myapplication.databinding.FragmentChallengeNumbersBinding
import com.shambhu.myapplication.utils.Constants.Companion.ARG_DAY
import com.shambhu.myapplication.utils.Constants.Companion.ARG_MONTH
import com.shambhu.myapplication.utils.Constants.Companion.ARG_YEAR
import com.shambhu.myapplication.utils.NumerologyCalculationUtils

class ChallengeNumbersFragment : Fragment() {

    private var _binding: FragmentChallengeNumbersBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChallengeNumbersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            val day = it.getInt(ARG_DAY)
            val month = it.getInt(ARG_MONTH)
            val year = it.getInt(ARG_YEAR)

            val challengeNumbers = NumerologyCalculationUtils.calculateChallengeNumbers(day, month, year)
            val ageRanges = NumerologyCalculationUtils.calculateChallengeNumberAgeRanges(day, month, year)
            val explanations = resources.getStringArray(R.array.challenge_number_interpretations)

            binding.tvFirstChallengeValue.text = challengeNumbers[0].toString()
            binding.tvFirstChallengeAgeRange.text = ageRanges[0]
            if (challengeNumbers[0] < explanations.size) {
                binding.tvFirstChallengeExplanation.text = explanations[challengeNumbers[0]]
            } else {
                binding.tvFirstChallengeExplanation.text = "No interpretation available."
            }


            binding.tvSecondChallengeValue.text = challengeNumbers[1].toString()
            binding.tvSecondChallengeAgeRange.text = ageRanges[1]
            if (challengeNumbers[1] < explanations.size) {
                binding.tvSecondChallengeExplanation.text = explanations[challengeNumbers[1]]
            } else {
                binding.tvSecondChallengeExplanation.text = "No interpretation available."
            }

            binding.tvThirdChallengeValue.text = challengeNumbers[2].toString()
            binding.tvThirdChallengeAgeRange.text = ageRanges[2]
            if (challengeNumbers[2] < explanations.size) {
                binding.tvThirdChallengeExplanation.text = explanations[challengeNumbers[2]]
            } else {
                binding.tvThirdChallengeExplanation.text = "No interpretation available."
            }

            binding.tvFourthChallengeValue.text = challengeNumbers[3].toString()
            binding.tvFourthChallengeAgeRange.text = ageRanges[3]
            if (challengeNumbers[3] < explanations.size) {
                binding.tvFourthChallengeExplanation.text = explanations[challengeNumbers[3]]
            } else {
                binding.tvFourthChallengeExplanation.text = "No interpretation available."
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {


        fun newInstance(day: Int, month: Int, year: Int): ChallengeNumbersFragment {
            val fragment = ChallengeNumbersFragment()
            val args = Bundle()
            args.putInt(ARG_DAY, day)
            args.putInt(ARG_MONTH, month)
            args.putInt(ARG_YEAR, year)
            fragment.arguments = args
            return fragment
        }
    }
}
