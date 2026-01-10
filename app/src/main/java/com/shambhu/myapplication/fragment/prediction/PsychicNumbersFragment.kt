package com.shambhu.myapplication.fragment.prediction


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.shambhu.myapplication.databinding.FragmentPsychicNumbersBinding


class PsychicNumbersFragment : Fragment() {

    private var _binding: FragmentPsychicNumbersBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPsychicNumbersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set up save button click listener
        binding.saveButton.setOnClickListener {
            saveChartData()
        }

        // You could load existing data here if you have saved state
        loadChartData()
    }

    private fun saveChartData() {
        // Collect all data from input fields
        val chartData = LifeChartData(
            lifePath = binding.lifePathInput.text?.toString() ?: "",
            firstCycle = binding.firstCycleInput.text?.toString() ?: "",
            secondCycle = binding.secondCycleInput.text?.toString() ?: "",
            thirdCycle = binding.thirdCycleInput.text?.toString() ?: "",
            firstAttainment = binding.firstAttainmentInput.text?.toString() ?: "",
            // Add all other fields...
            notes = binding.notesInput.text?.toString() ?: ""
        )

        // Save to SharedPreferences, Database, or ViewModel
        saveToStorage(chartData)

        // Show confirmation
        showSaveConfirmation()
    }

    private fun loadChartData() {
        // Load saved data and populate fields
        val savedData = loadFromStorage()

        binding.lifePathInput.setText(savedData.lifePath)
        binding.firstCycleInput.setText(savedData.firstCycle)
        binding.secondCycleInput.setText(savedData.secondCycle)
        binding.thirdCycleInput.setText(savedData.thirdCycle)
        binding.firstAttainmentInput.setText(savedData.firstAttainment)
        // Set all other fields...
        binding.notesInput.setText(savedData.notes)
    }

    private fun saveToStorage(data: LifeChartData) {
        // Implement your storage logic here
        // Could use SharedPreferences, Room Database, or ViewModel
    }

    private fun loadFromStorage(): LifeChartData {
        // Implement your loading logic here
        return LifeChartData() // Return empty or loaded data
    }

    private fun showSaveConfirmation() {
        // Show toast or snackbar
        android.widget.Toast.makeText(
            requireContext(),
            "Chart saved successfully!",
            android.widget.Toast.LENGTH_SHORT
        ).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // Data class to hold all chart values
    data class LifeChartData(
        var lifePath: String = "",
        var firstCycle: String = "",
        var secondCycle: String = "",
        var thirdCycle: String = "",
        var firstAttainment: String = "",
        var secondAttainment: String = "",
        var thirdAttainment: String = "",
        var fourthAttainment: String = "",
        var firstMinorChallenge: String = "",
        var secondMinorChallenge: String = "",
        var majorChallenge: String = "",
        var soulUrge: String = "",
        var urgesBehindSoulUrge: String = "",
        var soulUrgeChallenge: String = "",
        var quietSelf: String = "",
        var challengeToQuietSelf: String = "",
        var expression: String = "",
        var challengeToExpression: String = "",
        var hiddenPassion: String = "",
        var subConsciousSelf: String = "",
        var eccentricity: String = "",
        var cornerstone: String = "",
        var balanceNumber: String = "",
        var firstKey: String = "",
        var secondKey: String = "",
        var importantCharacterTrait: String = "",
        var achievement: String = "",
        var capstone: String = "",
        var lifeNumber: String = "",
        var notes: String = ""
    )
}