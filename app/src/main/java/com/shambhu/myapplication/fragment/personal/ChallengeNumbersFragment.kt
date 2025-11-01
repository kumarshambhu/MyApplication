package com.shambhu.myapplication.fragment.personal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.shambhu.myapplication.databinding.FragmentChallengeNumbersBinding
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
            binding.tvFirstChallengeValue.text = challengeNumbers[0].toString()
            binding.tvSecondChallengeValue.text = challengeNumbers[1].toString()
            binding.tvThirdChallengeValue.text = challengeNumbers[2].toString()
            binding.tvFourthChallengeValue.text = challengeNumbers[3].toString()
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
