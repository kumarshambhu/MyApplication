package com.shambhu.myapplication.fragment.others


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.shambhu.myapplication.R
import com.shambhu.myapplication.adapter.NumeroAccordionAdapter
import com.shambhu.myapplication.databinding.FragmentNumeroProfileBinding
import com.shambhu.myapplication.model.NumeroData
import com.shambhu.myapplication.utils.CommonUtils
import com.shambhu.myapplication.utils.NumeroCalculator
import com.shambhu.myapplication.utils.NumerologyCalculationUtils
import org.json.JSONObject

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
            val isExpanded = binding.mulankRecyclerView.visibility == View.VISIBLE
            binding.mulankRecyclerView.visibility = if (isExpanded) View.GONE else View.VISIBLE
        }

        // Bhagyank accordion
        binding.bhagyankCard.setOnClickListener {
            val isExpanded = binding.bhagyankRecyclerView.visibility == View.VISIBLE
            binding.bhagyankRecyclerView.visibility = if (isExpanded) View.GONE else View.VISIBLE
        }

        // Setup RecyclerViews
        binding.mulankRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.bhagyankRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        mulankData?.let {
            val adapter = NumeroAccordionAdapter(getMulankSections(it))
            binding.mulankRecyclerView.adapter = adapter
        }

        bhagyankData?.let {
            val adapter = NumeroAccordionAdapter(getBhagyankSections(it))
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
        )
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
        )
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
}