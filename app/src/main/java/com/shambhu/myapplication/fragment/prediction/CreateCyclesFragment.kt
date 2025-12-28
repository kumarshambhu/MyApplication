package com.shambhu.myapplication.fragment.prediction

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.shambhu.myapplication.databinding.FragmentCreateCyclesBinding
import com.shambhu.myapplication.model.LifeCycleDataResponse
import com.shambhu.myapplication.utils.CommonUtils
import com.shambhu.myapplication.utils.Constants.Companion.PREFERENCE_DATE_OF_BIRTH
import com.shambhu.myapplication.utils.Constants.Companion.PREFERENCE_OFFICIAL_NAME
import com.shambhu.myapplication.utils.Constants.Companion.PREFERENCE_NAME
import com.shambhu.myapplication.utils.NumerologyCalculationUtils

class CreateCyclesFragment : Fragment() {

    private var _binding: FragmentCreateCyclesBinding? = null
    private val binding get() = _binding!!
    private lateinit var officialName: String
    private lateinit var dob: String
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateCyclesBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadData()
        setupClickListeners()
    }

    private fun loadData() {
        val sharedPref = requireContext().getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        officialName = sharedPref?.getString(PREFERENCE_OFFICIAL_NAME, "Guest").toString()
        dob = sharedPref?.getString(PREFERENCE_DATE_OF_BIRTH, "17/12/2020").toString()

        binding.etName.setText(officialName)
        binding.etDob.setText(dob)
    }


    private fun setupClickListeners() {
        binding.btnCalculate.setOnClickListener {
            calculateAllNumbers()
        }

        binding.etYear.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus && binding.etYear.text?.isNotBlank() == true) {
                calculatePersonalYear()
            }
        }
    }

    private fun calculatePersonalYear() {
        val(day, month, year) = CommonUtils.parseDateTriple(dob)
        val yearText = binding.etYear.text.toString()
        if (yearText.isBlank()) {
            binding.tvPersonalYear.text = "#"
            return
        }

        val personalYear = NumerologyCalculationUtils.calculatePersonalYear(day, month, yearText.toInt())
        binding.tvPersonalYear.text = personalYear.toString()
    }
    private fun calculateAllNumbers() {
        val(day, month, year) = CommonUtils.parseDateTriple(dob)
        val lifePath = NumerologyCalculationUtils.calculateLifePath(day, month, year)
        binding.tvLifePath.text = lifePath.toString()

        val destiny = NumerologyCalculationUtils.calculateExpression(officialName)
        binding.tvDestiny.text = destiny.toString()

        val soul = NumerologyCalculationUtils.calculateSoulUrge(officialName)
        binding.tvSoul.text = soul.toString()

        calculateMajorCycles(day, month, year)

        // Calculate Pinnacles
        calculatePinnacles(day, month, year)

        // Calculate Challenges
        calculateChallenges(day, month, year)

        // Calculate Maturity Number
        calculateMaturityNumber(lifePath, destiny)

        if (binding.etYear.text?.isNotBlank() == true) {
            calculatePersonalYear()
        }

        Toast.makeText(this.context, "Numbers calculated successfully!", Toast.LENGTH_SHORT).show()
    }

    private fun calculateMajorCycles(day: Int, month: Int, year: Int) {
        val lifePath = NumerologyCalculationUtils.calculateLifePath(day, month, year)
        val lifePathCycleJson = CommonUtils.readAssetFile(requireContext(), "life_path_cyle.json")
        val lifeCycleDataResponse = Gson().fromJson(lifePathCycleJson, LifeCycleDataResponse::class.java)
        val lifeCycleEntry = lifeCycleDataResponse.cyclesTable.find { it.lifePathNumber == lifePath.toString() }

        // Calculate cycle durations based on Life Path number
        val firstCycle = lifeCycleEntry?.formativeCycle
        val secondCycle = lifeCycleEntry?.productiveCycle
        val thirdCycle = lifeCycleEntry?.harvestCycle

        binding.tvCycle1Range.text = firstCycle.toString()
        binding.tvCycle1Value.text = CommonUtils.reduceNumber(month).toString()
        binding.tvCycle2Range.text =  secondCycle.toString()
        binding.tvCycle2Value.text = CommonUtils.reduceNumber(day).toString()
        binding.tvCycle3Range.text =  thirdCycle.toString()
        binding.tvCycle3Value.text =  CommonUtils.reduceNumber(year).toString()
    }

    private fun calculatePinnacles(day: Int, month: Int, year: Int) {
        val pinnacleNumbers = NumerologyCalculationUtils.calculatePinnacleNumbers(day, month, year)
        val ageRanges = NumerologyCalculationUtils.calculatePinnacleNumberAgeRanges(day, month, year)

        binding.tvPinnacle1Range.text = ageRanges[0]
        binding.tvPinnacle1Value.text = pinnacleNumbers[0].toString()
        binding.tvPinnacle2Range.text = ageRanges[1]
        binding.tvPinnacle2Value.text = pinnacleNumbers[1].toString()
        binding.tvPinnacle3Range.text = ageRanges[2]
        binding.tvPinnacle3Value.text =pinnacleNumbers[2].toString()
        binding.tvPinnacle4Range.text = ageRanges[3]
        binding.tvPinnacle4Value.text = pinnacleNumbers[3].toString()
    }

    private fun calculateChallenges(day: Int, month: Int, year: Int) {
        val challengeNumbers = NumerologyCalculationUtils.calculateChallengeNumbers(day, month, year)
        val ageRanges = NumerologyCalculationUtils.calculateChallengeNumberAgeRanges(day, month, year)

        binding.tvChallenge1Range.text = ageRanges[0]
        binding.tvChallenge2Range.text = ageRanges[1]
        binding.tvChallenge3Range.text = ageRanges[2]
        binding.tvChallenge4Range.text = ageRanges[3]

        binding.tvChallenge1Value.text = challengeNumbers[0].toString()
        binding.tvChallenge2Value.text = challengeNumbers[1].toString()
        binding.tvChallenge3Value.text = challengeNumbers[2].toString()
        binding.tvChallenge4Value.text = challengeNumbers[3].toString()
    }

    private fun calculateMaturityNumber(lifePath: Int, destiny: Int) {
        val maturityNumber = NumerologyCalculationUtils.calculateMaturityNumber(lifePath, destiny)
        binding.tvMaturityNumber.text = if (maturityNumber == 0) "9" else maturityNumber.toString()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


