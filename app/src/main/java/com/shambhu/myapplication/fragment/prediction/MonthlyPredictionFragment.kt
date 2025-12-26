package com.shambhu.myapplication.fragment.prediction

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.shambhu.myapplication.R
import com.shambhu.myapplication.databinding.FragmentMonthlyPredictionBinding
import com.shambhu.myapplication.utils.CommonUtils
import com.shambhu.myapplication.utils.Constants.Companion.PREFERENCE_CURRENT_NAME
import com.shambhu.myapplication.utils.Constants.Companion.PREFERENCE_DATE_OF_BIRTH
import com.shambhu.myapplication.utils.Constants.Companion.PREFERENCE_OFFICIAL_NAME
import com.shambhu.myapplication.utils.Constants.Companion.PREFERENCE_NAME
import com.shambhu.myapplication.utils.NumerologyCalculationUtils
import com.shambhu.myapplication.utils.NumerologyReducer

class MonthlyPredictionFragment : Fragment() {

    private var _binding: FragmentMonthlyPredictionBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMonthlyPredictionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sharedPref =
            requireContext().getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        val officialName = sharedPref?.getString(PREFERENCE_OFFICIAL_NAME, "Guest").toString()
        val currentName = sharedPref?.getString(PREFERENCE_CURRENT_NAME, "Guest").toString()
        val dob = sharedPref?.getString(PREFERENCE_DATE_OF_BIRTH, "17/12/2020").toString()
        val result = calculateNumerology(officialName, dob)
        binding.tvCurrentName.text = currentName
        binding.tvOfficialName.text = officialName
        binding.tvBirthDate.text = dob

        // Populate the view with calculated results
        populateNumerologyData(view, result)
    }

    fun calculateNumerology(name: String, birthDate: String): CommonUtils.NumerologyResult {
        val (day, month, year) = CommonUtils.parseDateTriple(birthDate)
        return CommonUtils.NumerologyResult(
            lifePathNumber = calculateLifePath(day, month, year),
            attitude = NumerologyReducer.getNumerologyValue(
                NumerologyReducer.reduceToSingleDigit(
                    month + day
                )
            ),
            birthDay = NumerologyReducer.getNumerologyValue(day),
            soul = NumerologyReducer.getNumerologyValue(
                NumerologyCalculationUtils.calculateSoulUrge(
                    name
                )
            ),
            personality = NumerologyReducer.getNumerologyValue(
                NumerologyCalculationUtils.calculatePersonality(
                    name
                )
            ),
            destiny = NumerologyReducer.getNumerologyValue(
                NumerologyCalculationUtils.calculateExpression(
                    name
                )
            ),
            maturity = calculateMaturity(day, month, year, name),
            power = calculatePower(day, month, year, binding.tvOfficialName.text.toString()),
            personalYear = calculatePersonalYear(day, month, year)
        )

    }

    private fun calculateLifePath(
        day: Int,
        month: Int,
        year: Int
    ): NumerologyReducer.NumerologyValue {
        val monthReduced = NumerologyReducer.reduceToSingleDigit(month)
        val dayReduced = NumerologyReducer.reduceToSingleDigit(day)
        val yearReduced = NumerologyReducer.reduceToSingleDigit(year)

        val sum = monthReduced + dayReduced + yearReduced
        return NumerologyReducer.getNumerologyValue(sum)
    }


    private fun calculateMaturity(
        day: Int,
        month: Int,
        year: Int,
        name: String
    ): NumerologyReducer.NumerologyValue {
        val lifePath = NumerologyCalculationUtils.calculateLifePath(day, month, year)
        val destiny = NumerologyCalculationUtils.calculateExpression(name)
        return NumerologyReducer.getNumerologyValue(lifePath + destiny)
    }

    private fun calculatePower(
        day: Int,
        month: Int,
        year: Int,
        name: String
    ): NumerologyReducer.NumerologyValue {
        val lifePath = NumerologyCalculationUtils.calculateLifePath(day, month, year)
        val destiny = NumerologyCalculationUtils.calculateExpression(name)
        return NumerologyReducer.getNumerologyValue(lifePath + destiny)
    }

    private fun calculatePersonalYear(
        day: Int,
        month: Int,
        year: Int
    ): NumerologyReducer.NumerologyValue {
        return NumerologyReducer.getNumerologyValue(NumerologyCalculationUtils.calculatePersonalYear(day, month, year))
    }


    private fun getNumberList(result: CommonUtils.NumerologyResult): List<Int> {
        return listOf(
            result.lifePathNumber.reduced,
            result.attitude.reduced,
            result.personality.reduced,
            result.birthDay.reduced,
            result.soul.reduced,
            result.destiny.reduced,
            result.maturity.reduced,
            result.power.reduced
        )
    }

    private fun getNumberStatus(result: CommonUtils.NumerologyResult): List<String> {
        return listOf(
            result.lifePathNumber.masterNumbers,
            result.attitude.masterNumbers,
            result.personality.masterNumbers,
            result.birthDay.masterNumbers,
            result.soul.masterNumbers,
            result.destiny.masterNumbers,
            result.maturity.masterNumbers,
            result.power.masterNumbers
        )
    }

    private fun populateNumerologyData(view: View, result: CommonUtils.NumerologyResult) {
        // Populate each row with calculated data
        populateRow(view.findViewById(R.id.rowLifePath), "LIFE PATH", result.lifePathNumber)
        populateRow(view.findViewById(R.id.rowAttitude), "ATTITUDE", result.attitude)
        populateRow(view.findViewById(R.id.rowBirthDay), "BIRTH DAY", result.birthDay)
        populateRow(view.findViewById(R.id.rowSoul), "SOUL", result.soul)
        populateRow(view.findViewById(R.id.rowPersonality), "PERSONALITY", result.personality)
        populateRow(view.findViewById(R.id.rowDestiny), "DESTINY", result.destiny)
        populateRow(view.findViewById(R.id.rowMaturity), "MATURITY", result.maturity)
        populateRow(view.findViewById(R.id.rowPower), "POWER", result.power)

        // Set Personal Year
        view.findViewById<TextView>(R.id.tvPersonalYearPre).text = result.personalYear.preReduced
        view.findViewById<TextView>(R.id.tvPersonalYearReduced).text =
            result.personalYear.reduced.toString()

        // Set Patterns
        val digitIntensities = CommonUtils.allMostFrequent(getNumberList(result))
        val vibrationIntensities = CommonUtils.repeatedNumbersDesc(getNumberList(result))
        view.findViewById<TextView>(R.id.tvVibrationIntensities).text =
            vibrationIntensities.entries.joinToString(", ") { "${it.key}" }
        view.findViewById<TextView>(R.id.tvDigitIntensities).text =
            digitIntensities.entries.joinToString(", ") { "${it.key}(${it.value})" }

        val patterns = CommonUtils.karmicOrMasterCount(getNumberStatus(result))
        view.findViewById<TextView>(R.id.tvNotes).text =
            "Notes: ${patterns.entries.joinToString(", ") { "${it.key}(${it.value})" }}"
    }

    private fun populateRow(
        rowView: View,
        title: String,
        value: NumerologyReducer.NumerologyValue
    ) {
        rowView.findViewById<TextView>(R.id.tvLifePath).text = title
        rowView.findViewById<TextView>(R.id.tvPreReduced).text = value.preReduced
        rowView.findViewById<TextView>(R.id.tvReduced).text = value.reduced.toString()
        rowView.findViewById<TextView>(R.id.tvMasterNumbers).text =
            value.masterNumbers?.let { "${it}/${value.reduced}" } ?: ""
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
