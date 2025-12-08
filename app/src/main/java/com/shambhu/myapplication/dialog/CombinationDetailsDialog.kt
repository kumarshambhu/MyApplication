package com.shambhu.myapplication.dialog


import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.google.android.material.chip.Chip
import com.shambhu.myapplication.R
import com.shambhu.myapplication.databinding.DialogCombinationDetailsBinding
import com.shambhu.myapplication.databinding.FragmentGridPairsBinding
import com.shambhu.myapplication.model.NumerologyMobileCombination

class CombinationDetailsDialog : DialogFragment() {

    private var _binding: DialogCombinationDetailsBinding? = null
    private val binding get() = _binding!!

    companion object {
        private const val ARG_COMBINATION = "combination"

        fun newInstance(combination: NumerologyMobileCombination): CombinationDetailsDialog {
            val dialog = CombinationDetailsDialog()
            val args = Bundle()
            args.putSerializable(ARG_COMBINATION, combination)
            dialog.arguments = args
            return dialog
        }
    }

    private lateinit var combination: NumerologyMobileCombination

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        combination = arguments?.getSerializable(ARG_COMBINATION) as NumerologyMobileCombination
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DialogCombinationDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
        displayCombinationDetails()
    }

    private fun setupViews() {
        binding.closeButton.setOnClickListener {
            dismiss()
        }
    }

    private fun displayCombinationDetails() {
        binding.combinationTextView.text = "Combination: ${combination.combination}"
        binding.stateTextView.text = combination.state.replace("_", " ").uppercase()
        binding.stateTextView.setBackgroundColor(getStateColor(combination.state))

        // Display planets as chips
        binding.planetsContainer.removeAllViews()
        combination.planets.forEach { planet ->
            val chip = Chip(requireContext()).apply {
                text = planet
                isClickable = false
                setChipBackgroundColorResource(R.color.chip_background)
                setTextColor(Color.WHITE)
            }
            binding.planetsContainer.addView(chip)
        }

        // Display traits
        binding.traitsContainer.removeAllViews()
        combination.traits.forEach { trait ->
            val traitView = TextView(requireContext()).apply {
                text = "• $trait"
                textSize = 14f
                setTextColor(Color.BLACK)
                setPadding(0, 4, 0, 4)
            }
            binding.traitsContainer.addView(traitView)
        }
    }

    private fun getStateColor(state: String): Int {
        return when (state) {
            "universal benefic" -> Color.parseColor("#4CAF50")
            "neutral combinations" -> Color.parseColor("#FF9800")
            "malefic combinations" -> Color.parseColor("#F44336")
            else -> Color.parseColor("#9E9E9E")
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
    }
}