package com.shambhu.myapplication.fragment.others
// fragments/GridPairsFragment.kt

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.animation.doOnEnd
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.shambhu.myapplication.adapter.PairGridItem
import com.shambhu.myapplication.adapter.PairsGridAdapter
import com.shambhu.myapplication.databinding.FragmentGridPairsBinding
import com.shambhu.myapplication.dialog.CombinationDetailsDialog
import com.shambhu.myapplication.model.NumerologyMobileCombination
import com.shambhu.myapplication.utils.JsonParser


class GridPairsFragment : Fragment() {

    private lateinit var gridAdapter: PairsGridAdapter
    private var gridItems: MutableList<PairGridItem> = mutableListOf()
    private var isSearchBarVisible = false

    private var _binding: FragmentGridPairsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGridPairsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
        setupListeners()
        setupRecyclerView()
    }

    private fun setupViews() {
        // Initially hide search bar
        binding.searchBarCard.isVisible = false
        binding.emptyStateTextView.isVisible = true
        binding.pairsGridRecyclerView.isVisible = false
        binding.gridTitle.isVisible = false
        binding.loadingIndicator.isVisible = false

        // Set toolbar elevation
        binding.toolbar.elevation = 8f
    }

    private fun setupListeners() {
        binding.toggleSearchButton.setOnClickListener {
            toggleSearchBar()
        }

        binding.generateButton.setOnClickListener {
            generatePairs()
        }

        binding.numberInput.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == android.view.inputmethod.EditorInfo.IME_ACTION_DONE) {
                generatePairs()
                true
            } else {
                false
            }
        }

        // Close search bar when clicking outside (optional)
        binding.pairsGridRecyclerView.setOnClickListener {
            if (isSearchBarVisible) {
                hideSearchBar()
            }
        }
    }

    private fun setupRecyclerView() {
        val gridLayoutManager = GridLayoutManager(requireContext(), 3) // 3 columns grid
        binding.pairsGridRecyclerView.layoutManager = gridLayoutManager
        binding.pairsGridRecyclerView.setHasFixedSize(true)

        gridAdapter = PairsGridAdapter(
            onItemClick = { item ->
                handleItemClick(item)
            },
            onOpenClick = { item ->
                handleOpenClick(item)
            }
        )

        binding.pairsGridRecyclerView.adapter = gridAdapter
    }

    private fun toggleSearchBar() {
        if (isSearchBarVisible) {
            hideSearchBar()
        } else {
            showSearchBar()
        }
    }

    private fun showSearchBar() {
        if (!isSearchBarVisible) {
            isSearchBarVisible = true
            binding.searchBarCard.isVisible = true

            // Animate slide down
            binding.searchBarCard.translationY = -binding.searchBarCard.height.toFloat()
            ObjectAnimator.ofFloat(binding.searchBarCard, "translationY", 0f)
                .setDuration(300)
                .start()

            // Focus on input
            binding.numberInput.requestFocus()
            // You might want to show keyboard here
            // (requireContext() as Activity).showKeyboard(numberInput)
        }
    }

    private fun hideSearchBar() {
        if (isSearchBarVisible) {
            isSearchBarVisible = false

            // Animate slide up
            /*ObjectAnimator.ofFloat(
                binding.searchBarCard,
                "translationY",
                -binding.searchBarCard.height.toFloat()
            )
                .setDuration(300)
                .start()
                .withEndAction {
                    binding.searchBarCard.isVisible = false
                }*/
            val animator = ObjectAnimator.ofFloat(
                binding.searchBarCard,
                "translationY",
                -binding.searchBarCard.height.toFloat()
            )
            animator.duration = 300
            animator.doOnEnd {
                binding.searchBarCard.isVisible = false
            }
            animator.start()

            // Clear focus and hide keyboard
            binding.numberInput.clearFocus()
            // (requireContext() as Activity).hideKeyboard()
        }
    }

    private fun generatePairs() {
        val input = binding.numberInput.text.toString().trim()

        // Clear previous results
        clearResults()

        // Validate input
        if (!JsonParser.isValidNumberForPairs(input)) {
            showError("Please enter at least 2 valid digits")
            return
        }

        // Hide search bar after generation
        hideSearchBar()

        // Show loading
        binding.loadingIndicator.isVisible = true
        binding.emptyStateTextView.isVisible = false

        // Generate pairs
        val pairs = JsonParser.createPairsFromNumber(input)

        if (pairs.isEmpty()) {
            showError("Could not generate pairs from the number")
            binding.loadingIndicator.isVisible = false
            return
        }

        // Update UI
        updateGridTitle(input, pairs.size)

        // Process pairs in background
        processPairsAsync(pairs)
    }

    private fun processPairsAsync(pairs: List<String>) {
        Thread {
            // Simulate loading
            Thread.sleep(500)

            requireActivity().runOnUiThread {
                binding.loadingIndicator.isVisible = false
                loadPairsIntoGrid(pairs)
            }
        }.start()
    }

    private fun loadPairsIntoGrid(pairs: List<String>) {
        context?.let { context ->
            gridItems.clear()

            pairs.forEach { pair ->
                val combination = JsonParser.getCombinationByNumberPair(context, pair)
                gridItems.add(PairGridItem(pair, combination))
            }

            // Update adapter
            gridAdapter.updateItems(gridItems)

            // Show grid
            binding.pairsGridRecyclerView.isVisible = true
            binding.gridTitle.isVisible = true
            binding.emptyStateTextView.isVisible = false

            // Show summary toast
            val foundCount = gridItems.count { it.combination != null }
            Toast.makeText(
                requireContext(),
                "Generated ${pairs.size} pairs, ${foundCount} combinations found",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun updateGridTitle(input: String, pairCount: Int) {
       // val cleanedInput = JsonParser.cleanNumberInput(input)
        binding.gridTitle.text = "Pairs from '$input' ($pairCount)"
    }

    private fun handleItemClick(item: PairGridItem) {
        // Toggle selection
        val position = gridItems.indexOfFirst { it.pair == item.pair }
        if (position != -1) {
            gridItems[position] = item.copy(isSelected = !item.isSelected)
            gridAdapter.updateItems(gridItems)

            // Show combination info in toast
            if (item.combination != null) {
                val state = item.combination.state.replace("_", " ")
                val planets = item.combination.planets.joinToString(", ")
                Toast.makeText(
                    requireContext(),
                    "${item.pair}: $state - $planets",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    requireContext(),
                    "${item.pair}: No combination found",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun handleOpenClick(item: PairGridItem) {
        if (item.combination != null) {
            showCombinationDetails(item.combination)
        }
    }

    private fun showCombinationDetails(combination: NumerologyMobileCombination) {
        // Create and show details dialog
        val dialog = CombinationDetailsDialog.newInstance(combination)
        dialog.show(childFragmentManager, "CombinationDetailsDialog")
    }

    private fun showError(message: String) {
        binding.errorTextView.text = message
        binding.errorTextView.isVisible = true
        binding.emptyStateTextView.isVisible = true
        binding.pairsGridRecyclerView.isVisible = false
        binding.gridTitle.isVisible = false

        // Auto hide error after 3 seconds
        binding.errorTextView.postDelayed({
            binding.errorTextView.isVisible = false
        }, 3000)
    }

    private fun clearResults() {
        gridItems.clear()
        gridAdapter.updateItems(gridItems)
        binding.pairsGridRecyclerView.isVisible = false
        binding.gridTitle.isVisible = false
        binding.errorTextView.isVisible = false
        binding.emptyStateTextView.isVisible = true
    }
}