package com.shambhu.myapplication.fragment.core_number

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.shambhu.myapplication.databinding.FragmentLuckyNumberBinding
import com.shambhu.myapplication.utils.CommonUtils
import com.shambhu.myapplication.utils.Constants
import com.shambhu.myapplication.utils.NumerologyCalculationUtils

class LuckyNumberFragment : Fragment() {

    private var _binding: FragmentLuckyNumberBinding? = null
    private val binding get() = _binding!!
    private lateinit var numerologyData: List<PlanetData>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLuckyNumberBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadNumerologyData()
        arguments?.let {
            val dob = it.getString(Constants.Companion.ARG_DOB)
            val fullName = it.getString(Constants.Companion.ARG_FULL_NAME)
            if (dob != null && fullName != null) {
                bindLuckyUnluckyNumbers(dob, fullName)
            }
        }
    }

    private fun bindLuckyUnluckyNumbers(dob: String, fullName: String) {
        val date = CommonUtils.parseDate(dob)
        val day = date.dayOfMonth
        val month = date.monthValue
        val year = date.year

        // Calculate and display lucky numbers
        val mulank = NumerologyCalculationUtils.calculateBirthdayNumber(day)
        val bhagyank = NumerologyCalculationUtils.calculateLifePath(day, month, year)
        calculateNumerology(mulank, bhagyank)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(dob: String, fullName: String): LuckyNumberFragment {
            val fragment = LuckyNumberFragment()
            val args = Bundle()
            args.putString(Constants.Companion.ARG_DOB, dob)
            args.putString(Constants.Companion.ARG_FULL_NAME, fullName)
            fragment.arguments = args
            return fragment
        }
    }

    private fun calculateNumerology(mulank: Int, bhagyank: Int) {
        var mulank1 = CommonUtils.reduceNumberIgnoreMasterNumber(mulank)
        var bhagyank1 = CommonUtils.reduceNumberIgnoreMasterNumber(bhagyank)

        val mulankData = numerologyData.find { it.number == mulank1 }
        val bhagyankData = numerologyData.find { it.number == bhagyank1 }

        if (mulankData == null || bhagyankData == null) {
            showError("Invalid numerology data")
            return
        }

        // Calculate common numbers
        val commonLucky = mulankData.lucky_numbers.intersect(bhagyankData.lucky_numbers)
        val commonEnemy = (mulankData.enemy_numbers+bhagyankData.enemy_numbers).toSet()
        val commonNeutral = (mulankData.neutral_numbers+bhagyankData.neutral_numbers).toSet()

        // Display results
        displayResults(mulankData, bhagyankData, commonLucky, commonEnemy, commonNeutral)
    }

    private fun loadNumerologyData() {
        try {
            val jsonString = requireContext().assets.open("lucky_numerology_data.json")
                .bufferedReader().use { it.readText() }

            val gson = Gson()
            val type = object : TypeToken<Map<String, List<PlanetData>>>() {}.type
            val data: Map<String, List<PlanetData>> = gson.fromJson(jsonString, type)
            numerologyData = data["numerology_data"] ?: emptyList()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(requireContext(), "Error loading data", Toast.LENGTH_SHORT).show()
        }
    }

    private fun displayResults(
        mulankData: PlanetData,
        bhagyankData: PlanetData,
        commonLucky: Set<Int>,
        commonEnemy: Set<Int>,
        commonNeutral: Set<Int>
    ) {
        // Show results card
        binding.resultsCard.visibility = View.VISIBLE

        // Hide error if visible
        binding.errorTextView.visibility = View.GONE

        // Display common numbers
        binding.luckyNumbersText.text = if (commonLucky.isNotEmpty()) {
            commonLucky.sorted().joinToString(", ")
        } else {
            "None"
        }

        binding.enemyNumbersText.text = if (commonEnemy.isNotEmpty()) {
            commonEnemy.sorted().joinToString(", ")
        } else {
            "None"
        }

        binding.neutralNumbersText.text = if (commonNeutral.isNotEmpty()) {
            commonNeutral.sorted().joinToString(", ")
        } else {
            "None"
        }

        // Display planet information
        binding.mulankPlanetInfo.visibility = View.VISIBLE
        binding.bhagyankPlanetInfo.visibility = View.VISIBLE

        binding.mulankPlanetName.text = "Mulank: ${mulankData.planet} (${mulankData.role})"
        binding.mulankLucky.text = "Lucky: ${mulankData.lucky_numbers.sorted().joinToString(",")}"
        binding.mulankEnemy.text = "Enemy: ${mulankData.enemy_numbers.sorted().joinToString(",")}"
        binding.mulankNeutral.text =
            "Neutral: ${mulankData.neutral_numbers.sorted().joinToString(",")}"

        binding.bhagyankPlanetName.text = "Bhagyank: ${bhagyankData.planet} (${bhagyankData.role})"
        binding.bhagyankLucky.text =
            "Lucky: ${bhagyankData.lucky_numbers.sorted().joinToString(",")}"
        binding.bhagyankEnemy.text =
            "Enemy: ${bhagyankData.enemy_numbers.sorted().joinToString(",")}"
        binding.bhagyankNeutral.text =
            "Neutral: ${bhagyankData.neutral_numbers.sorted().joinToString(",")}"
    }

    private fun showError(message: String) {
        binding.errorTextView.text = message
        binding.errorTextView.visibility = View.VISIBLE
        binding.resultsCard.visibility = View.GONE
    }

    data class PlanetData(
        val number: Int,
        val planet: String,
        val role: String,
        val lucky_numbers: List<Int>,
        val enemy_numbers: List<Int>,
        val neutral_numbers: List<Int>
    )
}