package com.shambhu.myapplication.fragment.core_number

import android.os.Bundle
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
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
        binding.mulankTitle.text = "Mulank: ${mulankData?.name} ($mulankNumber)"
        val mulankDesc = "Ruling Planet: ${mulankData?.rulingPlanet}\n" +
                "Birth Dates: ${mulankData?.birthDates?.joinToString(", ")}"
        binding.mulankDetails.text = mulankDesc
    }

    private fun updateBhagyankUi(bhagyankNumber: Int) {
        binding.bhagyankTitle.text = "Bhagyank: ${bhagyankData?.name} ($bhagyankNumber)"
        val bhagyankDesc = "Ruling Planet: ${bhagyankData?.rulingPlanet}"
        binding.bhagyankDetails.text = bhagyankDesc
    }


    private fun setupAccordion() {
        // Mulank accordion
        binding.mulankHeaderLayout.setOnClickListener {
            TransitionManager.beginDelayedTransition(binding.mulankCard, AutoTransition())
            val isExpanded = binding.mulankContentLayout.isVisible
            binding.mulankContentLayout.visibility = if (isExpanded) View.GONE else View.VISIBLE
            binding.mulankToggleIcon.rotation = if (isExpanded) 0f else 180f
            binding.mulankCard.strokeColor = ContextCompat.getColor(
                requireContext(),
                if (isExpanded) R.color.divider else R.color.gold
            )
            binding.mulankCard.cardElevation = if (isExpanded) 2f else 8f
        }

        // Bhagyank accordion
        binding.bhagyankHeaderLayout.setOnClickListener {
            TransitionManager.beginDelayedTransition(binding.bhagyankCard, AutoTransition())
            val isExpanded = binding.bhagyankContentLayout.isVisible
            binding.bhagyankContentLayout.visibility = if (isExpanded) View.GONE else View.VISIBLE
            binding.bhagyankToggleIcon.rotation = if (isExpanded) 0f else 180f
            binding.bhagyankCard.strokeColor = ContextCompat.getColor(
                requireContext(),
                if (isExpanded) R.color.divider else R.color.gold
            )
            binding.bhagyankCard.cardElevation = if (isExpanded) 2f else 8f
        }

        // Combination accordion
        binding.mulankBhagyankCombinationLayout.combinationHeaderLayout.setOnClickListener {
            TransitionManager.beginDelayedTransition(
                binding.mulankBhagyankCombinationLayout.accordionCard,
                AutoTransition()
            )
            val isExpanded = binding.mulankBhagyankCombinationLayout.combinationContentLayout.isVisible
            binding.mulankBhagyankCombinationLayout.combinationContentLayout.visibility =
                if (isExpanded) View.GONE else View.VISIBLE
            binding.mulankBhagyankCombinationLayout.combinationExpandableIcon.rotation =
                if (isExpanded) 0f else 180f
            binding.mulankBhagyankCombinationLayout.accordionCard.strokeColor = ContextCompat.getColor(
                requireContext(),
                if (isExpanded) R.color.divider else R.color.gold
            )
            binding.mulankBhagyankCombinationLayout.accordionCard.cardElevation = if (isExpanded) 2f else 8f
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
        val reducedMulank = CommonUtils.reduceNumberIgnoreMasterNumber(mulank)
        val reducedBhagyank = CommonUtils.reduceNumberIgnoreMasterNumber(bhagyank)

        val jsonString = CommonUtils.readAssetFile(requireContext(), "dob_combination.json")
        val response = Gson().fromJson(jsonString, MulankBhagyankResponse::class.java)

        val mulankCombination = response.mulank_bhagyank_combinations.find {
            it.day_number == reducedMulank
        }

        val combinationDetail = mulankCombination?.combinations?.find {
            it.combination.contains(reducedBhagyank.toString())
        }

        if (combinationDetail != null) {
            binding.mulankBhagyankCombinationLayout.tvTitle.text = combinationDetail.combination
            binding.mulankBhagyankCombinationLayout.tvRuler.text =
                "Ruled by ${combinationDetail.planets}"
            val items = mutableListOf<Pair<String, String>>()
            with(combinationDetail) {
                if (!luck.isNullOrEmpty()) items.add(Pair("Luck %", luck))
                if (!remark.isNullOrEmpty()) items.add(Pair("Remark", remark))
                if (!character.isNullOrEmpty()) items.add(Pair("Character", character))
                if (!health.isNullOrEmpty()) items.add(Pair("Health", health))
                if (!traits.isNullOrEmpty()) items.add(Pair("Traits", traits))
                if (!warning.isNullOrEmpty()) items.add(Pair("Warning", warning))
                if (!lucky.isNullOrEmpty()) items.add(Pair("Luck status", lucky))
                if (!career.isNullOrEmpty()) items.add(Pair("Career", career))
                if (!solution.isNullOrEmpty()) items.add(Pair("Solution", solution))
            }

            CommonAdapterUtil.setupNumberRecyclerViewAdapter(
                requireContext(),
                binding.mulankBhagyankCombinationLayout.combinationDetailsRecyclerView,
                items
            )
        }
    }
}