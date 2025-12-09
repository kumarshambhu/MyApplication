package com.shambhu.myapplication.fragment.mobile

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.numerologyapp.utils.StringUtils
import com.google.android.material.button.MaterialButton
import com.google.android.material.chip.Chip
import com.shambhu.myapplication.R
import com.shambhu.myapplication.databinding.FragmentMobileNumberologyBinding
import com.shambhu.myapplication.model.NumerologyMobileCombination
import com.shambhu.myapplication.utils.CommonUtils
import com.shambhu.myapplication.utils.JsonParser

class MobileNumerologyFragment : Fragment() {

    private var _binding: FragmentMobileNumberologyBinding? = null
    private val binding get() = _binding!!


    private var parsedPairs = mutableListOf<String>()
    private var foundCombinations = mutableListOf<NumerologyMobileCombination>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMobileNumberologyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
        setupListeners()
    }

    private fun setupViews() {
        // Set initial visibility
        binding.parsedPairsCard.isVisible = false
        binding.resultsScrollView.isVisible = false
        binding.emptyStateTextView.isVisible = true
        binding.loadingIndicator.isVisible = false
    }

    private fun setupListeners() {
        binding.searchButton.setOnClickListener {
            searchCombinations()
        }

        binding.clearButton.setOnClickListener {
            clearAll()
        }

        binding.combinationsInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.errorTextView.isVisible = false
                binding.parsedTextView.isVisible = false

                // Real-time parsing feedback
                s?.toString()?.let { input ->
                    val cleaned = StringUtils.cleanCommaSeparatedInput(input)
                    if (cleaned.isNotEmpty()) {
                        val pairs = StringUtils.parseCommaSeparatedPairs(cleaned)
                        if (pairs.isNotEmpty()) {
                            binding.parsedTextView.text = "Will parse: ${pairs.joinToString(", ")}"
                            binding.parsedTextView.isVisible = true
                        }
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        binding.combinationsInput.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                searchCombinations()
                true
            } else {
                false
            }
        }
    }

    private fun searchCombinations() {
        val input = binding.combinationsInput.text.toString().trim()

        // Clear previous results
        clearResults()

        // Validate input
        if (input.isEmpty()) {
            showError("Please enter combinations")
            return
        }

        // Clean and parse input
        val cleanedInput = StringUtils.cleanCommaSeparatedInput(input)
        parsedPairs = StringUtils.parseCommaSeparatedPairs(cleanedInput).toMutableList()

        if (parsedPairs.isEmpty()) {
            showError("No valid combinations found. Use format: 13,31,69")
            return
        }

        // Display parsed pairs
        displayParsedPairs(parsedPairs)

        // Show loading
        binding.loadingIndicator.isVisible = true
        binding.emptyStateTextView.isVisible = false

        // Search for combinations (simulate async)
        searchInBackground()
    }

    private fun displayParsedPairs(pairs: List<String>) {
        binding.parsedPairsContainer.removeAllViews()

        pairs.forEach { pair ->
            val chip = Chip(requireContext()).apply {
                text = pair
                isCheckable = false
                isClickable = true
                setChipBackgroundColorResource(R.color.chip_background)
                setTextColor(Color.WHITE)

                // Show info on click
                setOnClickListener {
                    findAndDisplaySingleCombination(pair)
                }

                // Long press to copy
                setOnLongClickListener {
                    copyToClipboard(pair)
                    Toast.makeText(requireContext(), "Copied: $pair", Toast.LENGTH_SHORT).show()
                    true
                }
            }
            binding.parsedPairsContainer.addView(chip)
        }

        binding.parsedPairsHeader.text = "Parsed Combinations (${pairs.size}):"
        binding.parsedPairsCard.isVisible = true
    }

    private fun searchInBackground() {
        // Simulate background processing
        Thread {
            Thread.sleep(500) // Small delay for UX

            requireActivity().runOnUiThread {
                binding.loadingIndicator.isVisible = false
                performSearch()
            }
        }.start()
    }

    private fun performSearch() {
        context?.let { context ->
            // Get all combinations for parsed pairs
            val allResults = JsonParser.getCombinationsForMultiplePairs(context, parsedPairs)

            // Separate found and not found
            foundCombinations.clear()
            val notFoundPairs = mutableListOf<String>()

            allResults.forEach { (pair, combination) ->
                if (combination != null) {
                    foundCombinations.add(combination)
                } else {
                    notFoundPairs.add(pair)
                }
            }

            if (foundCombinations.isNotEmpty()) {
                displayResults(foundCombinations, notFoundPairs)
                binding.resultsScrollView.isVisible = true
                binding.emptyStateTextView.isVisible = false
            } else {
                showNoResults(notFoundPairs)
            }
        }
    }

    private fun displayResults(
        combinations: List<NumerologyMobileCombination>,
        notFoundPairs: List<String>
    ) {
        // Clear containers
        binding.summaryContainer.removeAllViews()
        binding.resultsContainer.removeAllViews()

        // Display summary
        displaySummary(combinations, notFoundPairs)

        // Display each combination
        combinations.forEach { combination ->
            val combinationView = createCombinationView(combination)
            binding.resultsContainer.addView(combinationView)
        }
    }

    private fun displaySummary(
        combinations: List<NumerologyMobileCombination>,
        notFoundPairs: List<String>
    ) {
        // Count by state
        val stateCounts = combinations.groupingBy { it.state }.eachCount()
        val totalPairs = parsedPairs.size
        val foundCount = combinations.size
        val notFoundCount = notFoundPairs.size

        val summaryView = TextView(requireContext()).apply {
            text = buildString {
                append("Search Results:\n\n")
                append("• Total pairs entered: $totalPairs\n")
                append("• Combinations found: $foundCount\n")
                append("• Not found: $notFoundCount\n\n")

                if (stateCounts.isNotEmpty()) {
                    append("By Category:\n")
                    stateCounts.forEach { (state, count) ->
                        val stateName = state.replace("_", " ")
                        append("  • $stateName: $count\n")
                    }
                }

                if (notFoundPairs.isNotEmpty()) {
                    append("\nNot found: ${notFoundPairs.joinToString(", ")}")
                }
            }
            textSize = 14f
            setTextColor(Color.BLACK)
            setLineSpacing(1.2f, 1.2f)
            setPadding(0, 0, 0, 16)
        }

        binding.summaryContainer.addView(summaryView)
    }

    private fun createCombinationView(combination: NumerologyMobileCombination): View {
        val view = LayoutInflater.from(requireContext())
            .inflate(R.layout.item_combination_full, binding.resultsContainer, false)

        val tvCombination = view.findViewById<TextView>(R.id.tvCombination)
        val tvState = view.findViewById<TextView>(R.id.tvState)
        val tvPlanets = view.findViewById<TextView>(R.id.tvPlanets)
        val tvTraits = view.findViewById<TextView>(R.id.tvTraits)
        val expandButton =
            view.findViewById<MaterialButton>(R.id.expandButton)
        val moreTraitsContainer = view.findViewById<LinearLayout>(R.id.moreTraitsContainer)

        tvCombination.text = "Combination: ${combination.combination}"
        tvState.text = combination.state.replace("_", " ").uppercase()
        tvState.setBackgroundColor(CommonUtils.getStateColor(combination.state))
        tvPlanets.text = "Planets: ${combination.planets.joinToString(", ")}"

        // Show first 3 traits initially
        val allTraits = combination.traits
        val initialTraits = allTraits.take(3)
        tvTraits.text = "Traits: ${initialTraits.joinToString(", ")}"

        // Handle expand/collapse
        if (allTraits.size > 3) {
            expandButton.isVisible = true
            expandButton.text = "Show ${allTraits.size - 3} more"

            expandButton.setOnClickListener {
                if (moreTraitsContainer.isVisible) {
                    // Collapse
                    moreTraitsContainer.isVisible = false
                    expandButton.text = "Show ${allTraits.size - 3} more"
                } else {
                    // Expand
                    moreTraitsContainer.removeAllViews()
                    allTraits.drop(3).forEach { trait ->
                        val traitView = TextView(requireContext()).apply {
                            text = "• $trait"
                            textSize = 14f
                            setTextColor(Color.DKGRAY)
                            setPadding(0, 4, 0, 4)
                        }
                        moreTraitsContainer.addView(traitView)
                    }
                    moreTraitsContainer.isVisible = true
                    expandButton.text = "Show less"
                }
            }
        } else {
            expandButton.isVisible = false
        }

        // Add click to copy
        view.setOnLongClickListener {
            copyToClipboard(combination.combination)
            Toast.makeText(
                requireContext(),
                "Copied: ${combination.combination}",
                Toast.LENGTH_SHORT
            ).show()
            true
        }

        return view
    }

    private fun findAndDisplaySingleCombination(pair: String) {
        context?.let { context ->
            val combination = JsonParser.getCombinationByNumberPair(context, pair)

            if (combination != null) {
                showCombinationDetailsDialog(combination)
            } else {
                Toast.makeText(
                    requireContext(),
                    "No combination found for $pair",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun showCombinationDetailsDialog(combination: NumerologyMobileCombination) {
        // You can use the same dialog from previous implementation
        Toast.makeText(
            requireContext(),
            "Details for: ${combination.combination}",
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun showError(message: String) {
        binding.errorTextView.text = message
        binding.errorTextView.isVisible = true
        binding.parsedPairsCard.isVisible = false
        binding.resultsScrollView.isVisible = false
        binding.emptyStateTextView.isVisible = true
    }

    private fun showNoResults(notFoundPairs: List<String>) {
        binding.emptyStateTextView.text = buildString {
            append("No combinations found.\n")
            if (notFoundPairs.isNotEmpty()) {
                append("Invalid pairs: ${notFoundPairs.joinToString(", ")}")
            }
        }
        binding.emptyStateTextView.isVisible = true
        binding.resultsScrollView.isVisible = false
    }

    private fun clearResults() {
        binding.parsedPairsContainer.removeAllViews()
        binding.summaryContainer.removeAllViews()
        binding.resultsContainer.removeAllViews()
        binding.parsedPairsCard.isVisible = false
        binding.resultsScrollView.isVisible = false
        binding.errorTextView.isVisible = false
        binding.loadingIndicator.isVisible = false
    }

    private fun clearAll() {
        binding.combinationsInput.text?.clear()
        clearResults()
        binding.emptyStateTextView.isVisible = true
        parsedPairs.clear()
        foundCombinations.clear()
        binding.parsedTextView.isVisible = false
    }

    private fun copyToClipboard(text: String) {
        val clipboard = requireContext().getSystemService(Context.CLIPBOARD_SERVICE)
                as ClipboardManager
        val clip = ClipData.newPlainText("Numerology Combination", text)
        clipboard.setPrimaryClip(clip)
    }
}