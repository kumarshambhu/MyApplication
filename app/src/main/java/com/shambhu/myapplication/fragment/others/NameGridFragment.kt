package com.shambhu.myapplication.fragment.others

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.myapplication.NameAnalyzer
import com.shambhu.myapplication.R
import com.shambhu.myapplication.databinding.FragmentLuckyNumberBinding
import com.shambhu.myapplication.databinding.FragmentNameGridBinding
import com.shambhu.myapplication.fragment.secondary_number.LuckyNumberFragment
import com.shambhu.myapplication.utils.CommonUtils
import com.shambhu.myapplication.utils.Constants
import com.shambhu.myapplication.utils.NumerologyCalculationUtils


/**
 * A simple [Fragment] subclass.
 * Use the [NameGridFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NameGridFragment : Fragment() {

    private var _binding: FragmentNameGridBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNameGridBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            val dob = it.getString(Constants.ARG_DOB)
            val fullName = it.getString(Constants.ARG_FULL_NAME)
            if (fullName != null) {
                setupNameAnalysisGrid( fullName)
            }
        }
    }

    private fun setupNameAnalysisGrid(name: String) {
        val result = NumerologyCalculationUtils.calculateNameAnalysisGrid(name)
        val nameAnalysisGrid = binding.nameAnalysisGrid
        // Inspired Row
        nameAnalysisGrid.tvMentalInspired.text = result.mentalInspired.toString()
        nameAnalysisGrid.tvPhysicalInspired.text = result.physicalInspired.toString()
        nameAnalysisGrid.tvEmotionalInspired.text = result.emotionalInspired.toString()
        nameAnalysisGrid.tvIntuitiveInspired.text = result.intuitiveInspired.toString()
        nameAnalysisGrid.tvTotalsInspired.text = (result.mentalInspired + result.physicalInspired + result.emotionalInspired + result.intuitiveInspired).toString()

        // Dual Row
        nameAnalysisGrid.tvMentalDual.text = result.mentalDual.toString()
        nameAnalysisGrid.tvPhysicalDual.text = result.physicalDual.toString()
        nameAnalysisGrid.tvEmotionalDual.text = result.emotionalDual.toString()
        nameAnalysisGrid.tvIntuitiveDual.text = result.intuitiveDual.toString()
        nameAnalysisGrid.tvTotalsDual.text = (result.mentalDual + result.physicalDual + result.emotionalDual + result.intuitiveDual).toString()

        // Balanced Row
        nameAnalysisGrid.tvMentalBalanced.text = result.mentalBalanced.toString()
        nameAnalysisGrid.tvPhysicalBalanced.text = result.physicalBalanced.toString()
        nameAnalysisGrid.tvEmotionalBalanced.text = result.emotionalBalanced.toString()
        nameAnalysisGrid.tvIntuitiveBalanced.text = result.intuitiveBalanced.toString()
        nameAnalysisGrid.tvTotalsBalanced.text = (result.mentalBalanced + result.physicalBalanced + result.emotionalBalanced + result.intuitiveBalanced).toString()

        // Total Column
        nameAnalysisGrid.tvMentalTotal.text = result.mentalTotal.toString()
        nameAnalysisGrid.tvPhysicalTotal.text = result.physicalTotal.toString()
        nameAnalysisGrid.tvEmotionalTotal.text = result.emotionalTotal.toString()
        nameAnalysisGrid.tvIntuitiveTotal.text = result.intuitiveTotal.toString()

        // Grand Total
        nameAnalysisGrid.tvTotalsTotal.text = result.grandTotal.toString()




        nameAnalysisGrid.resultsTitle.text = "Analysis for: $name"
        displayLetterDistribution(result)
        displayPercentages(result)
        displayBalanceAnalysis(result)

    }

    private fun displayBalanceAnalysis(result: NameAnalyzer.NameAnalysisResult) {
        val totalLetters = result.grandTotal
        if (totalLetters > 0) {
            val mentalPercent = (result.mentalTotal * 100) / totalLetters
            val physicalPercent = (result.physicalTotal * 100) / totalLetters
            val emotionalPercent = (result.emotionalTotal * 100) / totalLetters
            val intuitivePercent = (result.intuitiveTotal * 100) / totalLetters

            val isBalanced = mentalPercent in 20..30 && physicalPercent in 20..30 &&
                    emotionalPercent in 20..30 && intuitivePercent in 20..30

            val hasSignificantImbalance = Math.abs(mentalPercent - physicalPercent) > 40 ||
                    Math.abs(emotionalPercent - intuitivePercent) > 40

            binding.nameAnalysisGrid.balanceResult.text = when {
                isBalanced -> "✅ Name is WELL BALANCED"
                hasSignificantImbalance -> "⚠️ Name is IMBALANCED"
                else -> "➖ Name is MODERATELY BALANCED"
            }

            // Set background color based on balance
            val backgroundColor = when {
                isBalanced -> R.drawable.mulank_background
                hasSignificantImbalance -> R.color.warning_color
                else -> R.color.info_color
            }
            //binding.nameAnalysisGrid.balanceResult.setBackgroundResource(backgroundColor)
        }
    }

    private fun displayPercentages(result: NameAnalyzer.NameAnalysisResult) {
        val totalLetters = result.grandTotal
        if (totalLetters > 0) {
            val mentalPercent = (result.mentalTotal * 100f) / totalLetters
            val physicalPercent = (result.physicalTotal * 100f) / totalLetters
            val emotionalPercent = (result.emotionalTotal * 100f) / totalLetters
            val intuitivePercent = (result.intuitiveTotal * 100f) / totalLetters

            binding.nameAnalysisGrid.percentageBreakdown.text = String.format(
                "Mental: %.1f%% | Physical: %.1f%% | Emotional: %.1f%% | Intuitive: %.1f%%",
                mentalPercent, physicalPercent, emotionalPercent, intuitivePercent
            )
        }
    }
    private fun displayLetterDistribution(result: NameAnalyzer.NameAnalysisResult) {
        binding.nameAnalysisGrid.letterDistribution.text = String.format(
            "Letters: %d total (Mental: %d, Physical: %d, Emotional: %d, Intuitive: %d)",
            result.grandTotal, result.mentalTotal, result.physicalTotal,
            result.emotionalTotal, result.intuitiveTotal
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(dob: String, fullName: String): NameGridFragment {
            val fragment = NameGridFragment()
            val args = Bundle()
            args.putString(Constants.ARG_DOB, dob)
            args.putString(Constants.ARG_FULL_NAME, fullName)
            fragment.arguments = args
            return fragment
        }
    }
}
