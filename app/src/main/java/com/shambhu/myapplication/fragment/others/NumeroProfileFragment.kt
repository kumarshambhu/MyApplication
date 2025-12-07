package com.shambhu.myapplication.fragment.others


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.shambhu.myapplication.R
import com.shambhu.myapplication.adapter.NumeroAccordionAdapter
import com.shambhu.myapplication.databinding.FragmentNumeroProfileBinding
import com.shambhu.myapplication.model.MulankBhagyankResponse
import com.shambhu.myapplication.model.NumeroData
import com.shambhu.myapplication.utils.CommonUtils
import com.shambhu.myapplication.utils.NumeroCalculator
import com.shambhu.myapplication.utils.NumerologyCalculationUtils
import org.json.JSONObject
import androidx.core.view.isVisible

class NumeroProfileFragment : Fragment() {

    private var _binding: FragmentNumeroProfileBinding? = null
    private val binding get() = _binding!!
    private var birthDate: String = ""
    private var mulankData: NumeroData? = null
    private var bhagyankData: NumeroData? = null

    companion object {
        fun newInstance(birthDate: String): NumeroProfileFragment {
            val fragment = NumeroProfileFragment()
            val args = Bundle()
            args.putString("birthDate", birthDate)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            birthDate = it.getString("birthDate", "")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNumeroProfileBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        calculateNumerology()
        setupAccordion()
    }




    private fun calculateNumerology() {
        // Calculate Mulank and Bhagyank from birth date
        val calculator = NumeroCalculator()
        val mulankNumber = calculator.calculateMulank(birthDate)
        val bhagyankNumber = calculator.calculateBhagyank(birthDate)

        getMulankBhagyankSections(mulankNumber, bhagyankNumber)


        val elementsJson = CommonUtils.readAssetFile(requireContext(), "combination.json")
        val (remark, luck) = NumerologyCalculationUtils.calculateCombinationDobNumber(
            elementsJson,
            mulankNumber,
            bhagyankNumber
        )

        binding.mulankBhagyankCombination.text = remark + " (" + luck+")"

        // Load data from JSON files
        mulankData = loadMulankData(mulankNumber)
        bhagyankData = loadBhagyankData(bhagyankNumber)

        // Update UI
        binding.mulankTitle.text = "${mulankData?.name} (Number $mulankNumber)"
        binding.bhagyankTitle.text = "${bhagyankData?.name} (Number $bhagyankNumber)"

        val mulankDesc = "Ruling Planet: ${mulankData?.rulingPlanet}\n" +
                "Birth Dates: ${mulankData?.birthDates?.joinToString(", ")}"
        binding.mulankDetails.text = mulankDesc

        val bhagyankDesc = "Ruling Planet: ${bhagyankData?.rulingPlanet}"
        binding.bhagyankDetails.text = bhagyankDesc
    }

    private fun setupAccordion() {
        // Mulank accordion
        binding.mulankCard.setOnClickListener {
            val isExpanded = binding.mulankRecyclerView.isVisible
            binding.mulankRecyclerView.visibility = if (isExpanded) View.GONE else View.VISIBLE
            binding.mulankToggleIcon.setImageResource(if (isExpanded) R.drawable.ic_add else R.drawable.ic_remove)
        }

        // Bhagyank accordion
        binding.bhagyankCard.setOnClickListener {
            val isExpanded = binding.bhagyankRecyclerView.isVisible
            binding.bhagyankRecyclerView.visibility = if (isExpanded) View.GONE else View.VISIBLE
            binding.bhagyankToggleIcon.setImageResource(if (isExpanded) R.drawable.ic_add else R.drawable.ic_remove)
        }
        binding.mulankBhagyankCombinationLayout.cardDayInfo.setOnClickListener {
            val isExpanded = binding.mulankBhagyankCombinationLayout.combinationDetails.isVisible
            binding.mulankBhagyankCombinationLayout.combinationDetails.visibility = if (isExpanded) View.GONE else View.VISIBLE
            binding.mulankBhagyankCombinationLayout.combinationExpandableIcon.setImageResource(if (isExpanded) R.drawable.ic_add else R.drawable.ic_remove)
        }

        // Setup RecyclerViews
        binding.mulankRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.bhagyankRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        mulankData?.let {
            val adapter = NumeroAccordionAdapter(requireContext(), getMulankSections(it))
            binding.mulankRecyclerView.adapter = adapter
        }

        bhagyankData?.let {
            val adapter = NumeroAccordionAdapter(requireContext(), getBhagyankSections(it))
            binding.bhagyankRecyclerView.adapter = adapter
        }

        // Initially collapse both
        binding.mulankRecyclerView.visibility = View.GONE
        binding.bhagyankRecyclerView.visibility = View.GONE
    }

    private fun getMulankSections(data: NumeroData): List<NumeroAccordionAdapter.Section> {
        return listOf(
            NumeroAccordionAdapter.Section("Characteristics", data.characteristics),
            NumeroAccordionAdapter.Section("Strengths", data.strengths),
            NumeroAccordionAdapter.Section("Weaknesses", data.weaknesses),
            NumeroAccordionAdapter.Section("Advice", data.advice),
            NumeroAccordionAdapter.Section("Favorable Periods",
                data.favorablePeriods?.map { "${it.time}: ${it.description}" } ?: emptyList()),
            NumeroAccordionAdapter.Section("Unfavorable Periods",
                data.unfavorablePeriods?.map { "${it.time}: ${it.description}" } ?: emptyList()),
            NumeroAccordionAdapter.Section("Lucky Colors", data.luckyColors),
            NumeroAccordionAdapter.Section("Color Usage Tips", data.colorUsageTips)
        ).filter { it.items.isNotEmpty() }
    }

    private fun getBhagyankSections(data: NumeroData): List<NumeroAccordionAdapter.Section> {
        return listOf(
            NumeroAccordionAdapter.Section("Traits", data.traits),
            NumeroAccordionAdapter.Section("Advice", data.advice),
            NumeroAccordionAdapter.Section("Career Suggestions", data.careerSuggestions),
            NumeroAccordionAdapter.Section("Gender Specific - Men",
                data.genderSpecific?.get("men") ?: emptyList()),
            NumeroAccordionAdapter.Section("Gender Specific - Women",
                data.genderSpecific?.get("women") ?: emptyList())
        ).filter { it.items.isNotEmpty() }
    }

    private fun loadMulankData(number: Int): NumeroData? {
        return try {
            val jsonString = resources.openRawResource(R.raw.mulank).bufferedReader().use { it.readText() }
            val jsonObject = JSONObject(jsonString)
            val mulankObject = jsonObject.getJSONObject("mulank_data").getJSONObject(number.toString())

            NumeroData(
                name = mulankObject.getString("name"),
                rulingPlanet = mulankObject.getString("ruling_planet"),
                birthDates = parseBirthDates(mulankObject.getJSONArray("birth_dates")),
                characteristics = parseStringArray(mulankObject.getJSONArray("characteristics")),
                strengths = parseStringArray(mulankObject.getJSONArray("strengths")),
                weaknesses = parseStringArray(mulankObject.getJSONArray("weaknesses")),
                advice = parseStringArray(mulankObject.getJSONArray("advice")),
                favorablePeriods = parsePeriods(mulankObject.getJSONArray("favorable_periods")),
                unfavorablePeriods = parsePeriods(mulankObject.getJSONArray("unfavorable_periods")),
                luckyColors = parseStringArray(mulankObject.getJSONArray("lucky_colors")),
                colorUsageTips = parseStringArray(mulankObject.getJSONArray("color_usage_tips"))
            )
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun loadBhagyankData(number: Int): NumeroData? {
        return try {
            val jsonString = resources.openRawResource(R.raw.bhagyank).bufferedReader().use { it.readText() }
            val jsonObject = JSONObject(jsonString)
            val bhagyankObject = jsonObject.getJSONObject("bhagyank_numbers").getJSONObject(number.toString())

            val genderSpecific = mutableMapOf<String, List<String>>()
            if (bhagyankObject.has("gender_specific")) {
                val genderObj = bhagyankObject.getJSONObject("gender_specific")
                if (genderObj.has("men")) {
                    genderSpecific["men"] = parseStringArray(genderObj.getJSONArray("men"))
                }
                if (genderObj.has("women")) {
                    genderSpecific["women"] = parseStringArray(genderObj.getJSONArray("women"))
                }
            }

            NumeroData(
                name = bhagyankObject.getString("name"),
                rulingPlanet = bhagyankObject.getString("ruling_planet"),
                traits = parseStringArray(bhagyankObject.getJSONArray("traits")),
                advice = parseStringArray(bhagyankObject.getJSONArray("advice")),
                careerSuggestions = parseStringArray(bhagyankObject.getJSONArray("career_suggestions")),
                genderSpecific = genderSpecific
            )
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun parseStringArray(jsonArray: org.json.JSONArray): List<String> {
        val list = mutableListOf<String>()
        for (i in 0 until jsonArray.length()) {
            list.add(jsonArray.getString(i))
        }
        return list
    }

    private fun parseBirthDates(jsonArray: org.json.JSONArray): List<Int> {
        val list = mutableListOf<Int>()
        for (i in 0 until jsonArray.length()) {
            list.add(jsonArray.getInt(i))
        }
        return list
    }

    private fun parsePeriods(jsonArray: org.json.JSONArray): List<NumeroData.Period> {
        val list = mutableListOf<NumeroData.Period>()
        for (i in 0 until jsonArray.length()) {
            val periodObj = jsonArray.getJSONObject(i)
            list.add(NumeroData.Period(
                time = periodObj.getString("time"),
                description = periodObj.getString("description")
            ))
        }
        return list
    }


    private fun getMulankBhagyankSections(mulank: Int, bhagyank: Int){
        val data =  Gson().fromJson(CommonUtils.readAssetFile(requireContext(), "dob_combination.json"), MulankBhagyankResponse::class.java)
        val data1 = data.mulank_bhagyank_combinations
        val data2 = data1.stream().filter { it-> it.day_number == mulank }.findFirst().get()

        val data3 = data2.combinations
        val data4 = data3.stream().filter { it-> it.combination.contains(bhagyank.toString()) }.findFirst().get()
        binding.mulankBhagyankCombinationLayout.tvTitle.text = data4.combination
        binding.mulankBhagyankCombinationLayout.tvDayTitle.text = "Day ${data2.day_number}"
        binding.mulankBhagyankCombinationLayout.tvRuler.text = "Ruled by ${data2.ruler}"
        binding.mulankBhagyankCombinationLayout.tvRating.text = data4.rating
        binding.mulankBhagyankCombinationLayout.tvPlanets.text = data4.planets
        binding.mulankBhagyankCombinationLayout.tvCharacter.text = data4.character
        binding.mulankBhagyankCombinationLayout.tvCareer.text = data4.career
        binding.mulankBhagyankCombinationLayout.tvLucky.text = data4.lucky
        binding.mulankBhagyankCombinationLayout.tvHealth.text = data4.health
        binding.mulankBhagyankCombinationLayout.tvWarning.text = data4.warning
        binding.mulankBhagyankCombinationLayout.tvSolution.text = data4.solution
        binding.mulankBhagyankCombinationLayout.tvTraits.text = data4.traits

        val ratingColor = getRatingColor(requireContext(), data4.rating)
        binding.mulankBhagyankCombinationLayout.tvRating.setTextColor(ratingColor)

        if(data4.planets.isNullOrEmpty()){
            binding.mulankBhagyankCombinationLayout.tvPlanets.visibility = View.GONE
        }
        if(data4.character.isNullOrEmpty()){
            binding.mulankBhagyankCombinationLayout.tvCharacter.visibility = View.GONE
        }
        if(data4.career.isNullOrEmpty()){
            binding.mulankBhagyankCombinationLayout.tvCareer.visibility = View.GONE
        }
        if(data4.lucky.isNullOrEmpty()){
            binding.mulankBhagyankCombinationLayout.tvLucky.visibility = View.GONE
        }
        if(data4.health.isNullOrEmpty()){
            binding.mulankBhagyankCombinationLayout.tvHealth.visibility = View.GONE
        }
        if(data4.warning.isNullOrEmpty()){
            binding.mulankBhagyankCombinationLayout.tvWarning.visibility = View.GONE
        }
        if(data4.solution.isNullOrEmpty()){
            binding.mulankBhagyankCombinationLayout.tvSolution.visibility = View.GONE
        }
        if(data4.traits.isNullOrEmpty()){
            binding.mulankBhagyankCombinationLayout.tvTraits.visibility = View.GONE
        }



    }

    private fun getRatingColor(context: android.content.Context, rating: String): Int {
        return when (rating) {
            "very_good", "most_powerful", "luckiest", "very_lucky" ->
                ContextCompat.getColor(context, R.color.green)
            "dangerous", "inimical" ->
                ContextCompat.getColor(context, R.color.red)
            "powerful" ->
                ContextCompat.getColor(context, R.color.orange)
            else ->
                ContextCompat.getColor(context, R.color.gray)
        }
    }
}