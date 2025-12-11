package com.shambhu.myapplication.fragment.mobile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.shambhu.myapplication.databinding.FragmentResultsBinding
import java.util.*

class ResultsFragment : Fragment() {
    private var _binding: FragmentResultsBinding? = null
    private val binding get() = _binding!!
    private lateinit var numerologyCalculator: NumerologyCalculator

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResultsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        numerologyCalculator = NumerologyCalculator(requireContext())

        val dob = arguments?.getString("dob") ?: ""
        val mobile = arguments?.getString("mobile") ?: ""

        if (dob.isNotEmpty() && mobile.isNotEmpty()) {
            calculateNumerology(dob, mobile)
        }
    }

    private fun calculateNumerology(dob: String, mobile: String) {
        try {
            // 1. Calculate Birthday Number
            val birthdayNumber = numerologyCalculator.calculateBirthdayNumber(dob)
            binding.tvBirthdayNumber.text = "Birthday Number: $birthdayNumber"

            // 2. Calculate Life Path Number
            val lifePathNumber = numerologyCalculator.calculateLifePathNumber(dob)
            binding.tvLifePathNumber.text = "Life Path Number: $lifePathNumber"

            // 3. Calculate Kua Number
            val kuaNumber = numerologyCalculator.calculateKuaNumber(dob)
            binding.tvKuaNumber.text = "Kua Number: $kuaNumber"

            // 4. Create Lo Shu Grid
            val loShuGrid = numerologyCalculator.createLoShuGrid(dob)
            binding.tvLoShuGrid.text = "Lo Shu Grid:\n$loShuGrid"

            // 5. Check Missing Numbers
            val missingNumbers = numerologyCalculator.findMissingNumbers(loShuGrid)
            binding.tvMissingNumbers.text = "Missing Numbers: ${missingNumbers.joinToString()}"

            // 6. Sum of Mobile Number
            val mobileSum = numerologyCalculator.sumMobileNumber(mobile)
            val sumState = numerologyCalculator.getMobileSumState(mobileSum)
            binding.tvMobileSum.text = "Mobile Sum: $mobileSum ($sumState)"

            // 7. Create Pairs from Mobile Number
            val mobilePairs = numerologyCalculator.createMobilePairs(mobile)
            binding.tvMobilePairs.text = "Mobile Pairs:\n${mobilePairs.joinToString(", ")}"

            // 8. Check Pair Combinations
            val pairResults = numerologyCalculator.checkPairCombinations(mobilePairs)
            displayPairResults(pairResults)

        } catch (e: Exception) {
            binding.tvResults.text = "Error: ${e.message}"
        }
    }

    private fun displayPairResults(pairResults: Map<String, String>) {
        val stringBuilder = StringBuilder()
        stringBuilder.append("Pair Analysis:\n")

        pairResults.forEach { (pair, state) ->
            stringBuilder.append("$pair: $state\n")
        }

        binding.tvPairAnalysis.text = stringBuilder.toString()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}