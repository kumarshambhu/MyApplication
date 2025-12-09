package com.shambhu.myapplication.fragment.core_number

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.shambhu.myapplication.R
import com.shambhu.myapplication.databinding.FragmentPinnacleNumberBinding
import com.shambhu.myapplication.repository.NumerologyRepository
import com.shambhu.myapplication.service.NumerologyService
import com.shambhu.myapplication.utils.CommonUtils
import com.shambhu.myapplication.utils.Constants

class PinnacleNumberFragment : Fragment() {
    private var _binding: FragmentPinnacleNumberBinding? = null
    private val binding get() = _binding!!
    private lateinit var numerologyService: NumerologyService

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPinnacleNumberBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val repository = NumerologyRepository(requireContext())
        numerologyService = NumerologyService(repository)

        arguments?.let {
            val date = CommonUtils.parseDate(it.getString(Constants.ARG_DOB).toString())
            val day = date.dayOfMonth
            val month = date.monthValue
            val year = date.year


            val pinnacleNumbers = numerologyService.calculatePinnacleNumbers(day, month, year)
            val ageRanges = numerologyService.calculatePinnacleNumberAgeRanges(day, month, year)
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
        fun newInstance(dob: String, fullName: String): PinnacleNumberFragment {
            val fragment = PinnacleNumberFragment()
            val args = Bundle()
            args.putString(Constants.Companion.ARG_DOB, dob)
            args.putString(Constants.Companion.ARG_FULL_NAME, fullName)
            fragment.arguments = args
            return fragment
        }
    }
}
