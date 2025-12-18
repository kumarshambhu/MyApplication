package com.shambhu.myapplication.fragment.mobile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.shambhu.myapplication.R
import com.shambhu.myapplication.databinding.FragmentMainBinding
import java.util.*

class MainFragment : Fragment() {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private lateinit var numerologyCalculator: NumerologyCalculator

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        numerologyCalculator = NumerologyCalculator(requireContext())
        binding.resultLayout.isVisible = false
        binding.searchToolbar.isVisible = false
        binding.btnCalculate.setOnClickListener {
            val dob = "17/03/1979"// binding.etDob.text.toString().trim()
            val mobile = "9686391100"// binding.etMobile.text.toString().trim()

            if (dob.isEmpty()) {
                binding.etDob.error = "Please enter Date of Birth"
                return@setOnClickListener
            }

            if (mobile.isEmpty()) {
                binding.etMobile.error = "Please enter Mobile Number"
                return@setOnClickListener
            }

            binding.btnCalculate.setOnClickListener {
                generateResult()
            }
            binding.toggleSearchButton.setOnClickListener {
                binding.resultLayout.isVisible = false
                binding.searchToolbar.isVisible = false
                binding.searchLayout.isVisible = true
            }
        }
    }

    private fun generateResult(){
        clearResult()
        calculateNumerology(binding.etDob.text.toString(), binding.etMobile.text.toString())

    }
    private fun clearResult(){
        binding.resultLayout.isVisible = true
        binding.searchToolbar.isVisible = true
        binding.searchLayout.isVisible = false
    }

    private fun calculateNumerology(dob: String, mobile: String) {
        try {
            // 1. Calculate Birthday Number
            val birthdayNumber = numerologyCalculator.calculateBirthdayNumber(dob)
            binding.tvBirthdayNumber.text = "🎂 Birthday Number: $birthdayNumber"

            // 2. Calculate Life Path Number
            val lifePathNumber = numerologyCalculator.calculateLifePathNumber(dob)
            binding.tvLifePathNumber.text = "🛤️ Life Path Number: $lifePathNumber"

            // 3. Calculate Kua Number (default to male)
            val kuaNumber = numerologyCalculator.calculateKuaNumber(dob, true)
            binding.tvKuaNumber.text = "🧭 Kua Number: $kuaNumber"

            // 4. Create and Display Lo Shu Grid
            val loShuGrid = numerologyCalculator.createLoShuGrid(dob)
            val formattedGrid = numerologyCalculator.formatLoShuGrid(loShuGrid)
            binding.tvLoShuGrid.text = formattedGrid

            // 5. Check Missing Numbers
            val missingNumbers = numerologyCalculator.findMissingNumbers(loShuGrid)
            binding.tvMissingNumbers.text = "❌ Missing Numbers: ${if (missingNumbers.isEmpty()) "None" else missingNumbers.joinToString(", ")}"

            // 6. Analyze Lo Shu Grid
            val gridAnalysis = numerologyCalculator.analyzeLoShuGrid(loShuGrid)
            binding.tvGridAnalysis.text = gridAnalysis

            // 7. Sum of Mobile Number
            val mobileSum = numerologyCalculator.sumMobileNumber(mobile)
            val sumState = numerologyCalculator.getMobileSumState(mobileSum)
            binding.tvMobileSum.text = "📱 Mobile Sum: $mobileSum\n📊 State: $sumState"

            // 8. Create Pairs from Mobile Number
            val mobilePairs = numerologyCalculator.createMobilePairs(mobile)
            binding.tvMobilePairs.text = "🔗 Mobile Pairs: ${mobilePairs.joinToString(", ")}"

            // 9. Check Pair Combinations
            val pairResults = numerologyCalculator.checkPairCombinations(mobilePairs)
            displayPairResults(pairResults)

            // 10. Get Universal Benefic Pairs
            val universalBeneficPairs = numerologyCalculator.getUniversalBeneficPairs(mobilePairs)
            displayUniversalBeneficPairs(universalBeneficPairs)

        } catch (e: Exception) {
            binding.tvResults.text = "❌ Error: ${e.message}"
        }
    }

    private fun displayPairResults(pairResults: Map<String, String>) {
        val stringBuilder = StringBuilder()
        stringBuilder.append("🎭 Pair Analysis:\n")

        var universalBeneficCount = 0
        var neutralCount = 0
        var maleficCount = 0
        var notFoundCount = 0

        pairResults.forEach { (pair, state) ->
            stringBuilder.append("$pair: ")
            when (state) {
                "universal benefic" -> {
                    stringBuilder.append("✅ Universal Benefic\n")
                    universalBeneficCount++
                }
                "neutral combinations" -> {
                    stringBuilder.append("🟡 Neutral\n")
                    neutralCount++
                }
                "malefic combinations" -> {
                    stringBuilder.append("❌ Malefic\n")
                    maleficCount++
                }
                else -> {
                    stringBuilder.append("⚪ Not Found\n")
                    notFoundCount++
                }
            }
        }

        // Add summary
        stringBuilder.append("\n📈 Summary:\n")
        stringBuilder.append("✅ Universal Benefic: $universalBeneficCount\n")
        stringBuilder.append("🟡 Neutral: $neutralCount\n")
        stringBuilder.append("❌ Malefic: $maleficCount\n")
        stringBuilder.append("⚪ Not Found: $notFoundCount")

        binding.tvPairAnalysis.text = stringBuilder.toString()
    }

    private fun displayUniversalBeneficPairs(pairs: List<String>) {
        if (pairs.isNotEmpty()) {
            binding.tvUniversalBenefic.text =
                "🌟 Universal Benefic Pairs Found: ${pairs.joinToString(", ")}"
        } else {
            binding.tvUniversalBenefic.text = "🌟 No Universal Benefic Pairs Found"
        }
    }
    private fun setupExpanderClick() {
        binding.toggleSearchButton.setOnClickListener {
            val isExpanded = binding.resultLayout.isVisible
            binding.resultLayout.visibility = if (isExpanded) View.GONE else View.VISIBLE
            //binding.resultLayout.setImageResource(if (isExpanded) R.drawable.ic_add else R.drawable.ic_remove)
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}