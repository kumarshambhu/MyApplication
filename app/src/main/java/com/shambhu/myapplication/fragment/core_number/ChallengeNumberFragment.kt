package com.shambhu.myapplication.fragment.core_number


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.shambhu.myapplication.R
import com.shambhu.myapplication.databinding.FragmentChallengeNumberBinding
import com.shambhu.myapplication.service.NumerologyService
import com.shambhu.myapplication.utils.CommonUtils
import com.shambhu.myapplication.utils.Constants

class ChallengeNumberFragment : Fragment() {

    private var _binding: FragmentChallengeNumberBinding? = null
    private val binding get() = _binding!!
    private lateinit var numerologyService: NumerologyService

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChallengeNumberBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            val date = CommonUtils.parseDate(it.getString(Constants.ARG_DOB).toString())
            val day = date.dayOfMonth
            val month = date.monthValue
            val year = date.year

            val challengeNumbers = numerologyService.calculateChallengeNumbers(day, month, year)
            val ageRanges = numerologyService.calculateChallengeNumberAgeRanges(day, month, year)
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
        fun newInstance(dob: String, fullName: String, numerologyService: NumerologyService): ChallengeNumberFragment {
            val fragment = ChallengeNumberFragment()
            fragment.numerologyService = numerologyService
            val args = Bundle()
            args.putString(Constants.Companion.ARG_DOB, dob)
            args.putString(Constants.Companion.ARG_FULL_NAME, fullName)
            fragment.arguments = args
            return fragment
        }
    }
}
