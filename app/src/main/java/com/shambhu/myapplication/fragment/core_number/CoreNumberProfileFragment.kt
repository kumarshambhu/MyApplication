package com.shambhu.myapplication.fragment.core_number

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.shambhu.myapplication.R
import com.shambhu.myapplication.adapter.CommonAdapterUtil
import com.shambhu.myapplication.adapter.recycler_adapter.NumeroAccordionAdapter
import com.shambhu.myapplication.databinding.FragmentCoreNumberProfileBinding
import com.shambhu.myapplication.model.MulankBhagyankResponse
import com.shambhu.myapplication.model.NumeroData
import com.shambhu.myapplication.repository.CoreNumberRepository
import com.shambhu.myapplication.repository.impl.CoreNumberRepositoryImpl
import com.shambhu.myapplication.service.impl.CoreNumberServiceImpl
import com.shambhu.myapplication.utils.CommonUtils
import com.shambhu.myapplication.utils.NumerologyCalculationUtils
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.zip

class CoreNumberProfileFragment : Fragment() {

    private var _binding: FragmentCoreNumberProfileBinding? = null
    private val binding get() = _binding!!
    private var birthDate: String = ""
    private var mulankData: NumeroData? = null
    private var bhagyankData: NumeroData? = null
    private val coreNumberRepository: CoreNumberRepository by lazy {
        CoreNumberRepositoryImpl(CoreNumberServiceImpl(Gson()))
    }

    companion object {
        fun newInstance(birthDate: String): CoreNumberProfileFragment {
            val fragment = CoreNumberProfileFragment()
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
    ): View {
        _binding = FragmentCoreNumberProfileBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        calculateNumerology()
    }

    private fun calculateNumerology() {
        // Calculate Mulank and Bhagyank from birth date
        val (day, month, year) = CommonUtils.parseDateTriple(birthDate)
        val mulankNumber = NumerologyCalculationUtils.calculateBirthdayNumber(day)
        val bhagyankNumber = NumerologyCalculationUtils.calculateLifePath(day, month, year)

        getMulankBhagyankSections(mulankNumber, bhagyankNumber)

        // Load data using repository
        loadData(mulankNumber, bhagyankNumber)
    }

    private fun loadData(mulankNumber: Int, bhagyankNumber: Int) {
        val mulankFlow = coreNumberRepository.getMulankdataById(requireContext(), mulankNumber)
        val bhagyankFlow = coreNumberRepository.getBhagyankById(requireContext(), bhagyankNumber)

        mulankFlow.zip(bhagyankFlow) { mulankResult, bhagyankResult ->
            Pair(mulankResult, bhagyankResult)
        }.onEach { (mulankResult, bhagyankResult) ->
            mulankResult.onSuccess { data ->
                mulankData = data
                Log.d("Mulank", Gson().toJson(data))
                updateMulankUi(mulankNumber)
            }.onFailure { error ->
                Log.e("NumeroProfileFragment", "Failed to load Mulank data", error)
            }

            bhagyankResult.onSuccess { data ->
                bhagyankData = data
                Log.d("bhagyankData", Gson().toJson(data))
                updateBhagyankUi(bhagyankNumber)
            }.onFailure { error ->
                Log.e("NumeroProfileFragment", "Failed to load Bhagyank data", error)
            }

            // Both flows have emitted, now it's safe to set up the accordion
            if (mulankResult.isSuccess && bhagyankResult.isSuccess) {
                setupAccordion()
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun updateMulankUi(mulankNumber: Int) {
        binding.mulankTitle.text = "${mulankData?.name}"
        val mulankDesc = "Ruling Planet: ${mulankData?.rulingPlanet}\n" +
                "Birth Dates: ${mulankData?.birthDates?.joinToString(", ")}"
        binding.mulankDetails.text = mulankDesc
    }

    private fun updateBhagyankUi(bhagyankNumber: Int) {
        binding.bhagyankTitle.text = "${bhagyankData?.name}"
        val bhagyankDesc = "Ruling Planet: ${bhagyankData?.rulingPlanet}"
        binding.bhagyankDetails.text = bhagyankDesc
    }


    private fun setupAccordion() {
        // Mulank accordion
        binding.mulankHeaderLayout.setOnClickListener {
            val isExpanded = binding.mulankRecyclerView.isVisible
            binding.mulankRecyclerView.visibility = if (isExpanded) View.GONE else View.VISIBLE
            binding.mulankToggleIcon.setImageResource(if (isExpanded) R.drawable.ic_add else R.drawable.ic_remove)
        }

        // Bhagyank accordion
        binding.bhagyankHeaderLayout.setOnClickListener {
            val isExpanded = binding.bhagyankRecyclerView.isVisible
            binding.bhagyankRecyclerView.visibility = if (isExpanded) View.GONE else View.VISIBLE
            binding.bhagyankToggleIcon.setImageResource(if (isExpanded) R.drawable.ic_add else R.drawable.ic_remove)
        }
        binding.mulankBhagyankCombinationLayout.combinationExpandableIcon.setOnClickListener {
            val isExpanded = binding.mulankBhagyankCombinationLayout.combinationDetailsRecyclerView.isVisible
            binding.mulankBhagyankCombinationLayout.combinationDetailsRecyclerView.visibility =
                if (isExpanded) View.GONE else View.VISIBLE
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
            NumeroAccordionAdapter.Section(
                "Favorable Periods",
                data.favorablePeriods?.map { "${it.time}: ${it.description}" } ?: emptyList()),
            NumeroAccordionAdapter.Section(
                "Unfavorable Periods",
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
            NumeroAccordionAdapter.Section(
                "Gender Specific - Men",
                data.genderSpecific?.get("men") ?: emptyList()
            ),
            NumeroAccordionAdapter.Section(
                "Gender Specific - Women",
                data.genderSpecific?.get("women") ?: emptyList()
            )
        ).filter { it.items.isNotEmpty() }
    }


    private fun getMulankBhagyankSections(mulank: Int, bhagyank: Int) {
        var mulankData = mulank
        var bhagyankData = bhagyank
        if(mulank > 0){
            mulankData = CommonUtils.reduceNumberIgnoreMasterNumber(bhagyank)
        }
        if(bhagyank > 0){
            bhagyankData = CommonUtils.reduceNumberIgnoreMasterNumber(mulank)
        }
        val data = Gson().fromJson(
            CommonUtils.readAssetFile(requireContext(), "dob_combination.json"),
            MulankBhagyankResponse::class.java
        )
        val data1 = data.mulank_bhagyank_combinations
        val data2 = data1.stream().filter { it -> it.day_number == mulankData }.findFirst().get()

        val data3 = data2.combinations
        val data4 =
            data3.stream().filter { it -> it.combination.contains(bhagyankData.toString()) }.findFirst()
                .get()
        binding.mulankBhagyankCombinationLayout.tvTitle.text = data4.combination
        binding.mulankBhagyankCombinationLayout.tvRuler.text = "Ruled by ${data4.planets}"
        val items = mutableListOf<Pair<String, String>>()
        if (!data4.luck.isNullOrEmpty()) {
            items.add(Pair("Luck %", data4.luck))
        }

        if (!data4.remark.isNullOrEmpty()) {
            items.add(Pair("Remark", data4.remark))
        }

        if (!data4.character.isNullOrEmpty()) {
            items.add(Pair("Character", data4.character))
        }

        if (!data4.health.isNullOrEmpty()) {
            items.add(Pair("Health", data4.health))
        }

        if (!data4.traits.isNullOrEmpty()) {
            items.add(Pair("Traits", data4.traits))
        }

        if (!data4.warning.isNullOrEmpty()) {
            items.add(Pair("Warning", data4.warning))
        }
        if (!data4.lucky.isNullOrEmpty()) {
            items.add(Pair("Luck status", data4.lucky))
        }
        if (!data4.career.isNullOrEmpty()) {
            items.add(Pair("Career", data4.career))
        }
        if (!data4.solution.isNullOrEmpty()) {
            items.add(Pair("Solution", data4.solution))
        }

        CommonAdapterUtil.setupNumberRecyclerViewAdapter(requireContext(),
            binding.mulankBhagyankCombinationLayout.combinationDetailsRecyclerView,items)

    }
}