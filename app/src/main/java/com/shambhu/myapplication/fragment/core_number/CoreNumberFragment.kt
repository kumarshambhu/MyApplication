package com.shambhu.myapplication.fragment.core_number

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.shambhu.myapplication.R
import com.shambhu.myapplication.adapter.recycler_adapter.CoreNumberRecyclerViewAdapter
import com.shambhu.myapplication.databinding.FragmentCoreNumberBinding
import com.shambhu.myapplication.model.CoreNumberAccordionItem
import com.shambhu.myapplication.model.CoreNumberModel
import com.shambhu.myapplication.utils.CommonUtils
import com.shambhu.myapplication.utils.Constants.Companion.ARG_DOB
import com.shambhu.myapplication.utils.Constants.Companion.ARG_FULL_NAME
import com.shambhu.myapplication.utils.NumerologyCalculationUtils

class CoreNumberFragment : Fragment() {
    private var _binding: FragmentCoreNumberBinding? = null
    private val binding get() = _binding!!

    private lateinit var coreNumberRecyclerViewAdapter: CoreNumberRecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCoreNumberBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            val dob = it.getString(ARG_DOB)
            val fullName = it.getString(ARG_FULL_NAME)

            if (dob != null && fullName != null) {
                val date = CommonUtils.parseDate(dob)
                val day = date.dayOfMonth
                val month = date.monthValue
                val year = date.year
                val coreNumberItems = mutableListOf<CoreNumberAccordionItem>();
                val birthNumber = NumerologyCalculationUtils.calculateBirthdayNumber(day)
                val lifePath = NumerologyCalculationUtils.calculateLifePath(day, month, year)
                val soulNumberValue = NumerologyCalculationUtils.calculateSoulUrge(fullName)
                val personalityNumberValue = NumerologyCalculationUtils.calculatePersonality(fullName)
                val destinyNumberValue = NumerologyCalculationUtils.calculateExpression(fullName)

                var coreNumbers = CoreNumberModel(birthNumber, lifePath,
                    soulNumberValue, personalityNumberValue, destinyNumberValue )

                createRecycleView(coreNumbers)
                createCombinationByNameNumber(coreNumbers)
                createCombinationByDobNumber(coreNumbers)
            }

        }
    }

    private fun createCombinationByNameNumber(coreNumbers: CoreNumberModel){
        val elementsJson = CommonUtils.readAssetFile(requireContext(), "combination.json")
        val combination = NumerologyCalculationUtils.calculateCombinationNameNumber(
            elementsJson,
            coreNumbers.destinyNumber,
            coreNumbers.soulUrgeNumber,
            coreNumbers.personalityNumber
        )
        binding.contentCombinationLayout.tvDestinyValue.text = coreNumbers.destinyNumber.toString()
        binding.contentCombinationLayout.tvSoulValue.text = coreNumbers.soulUrgeNumber.toString()
        binding.contentCombinationLayout.tvPersonalityValue.text = coreNumbers.personalityNumber.toString()
        binding.contentCombinationLayout.tvCombinationValue.text = combination.toString()
    }


    private fun createCombinationByDobNumber(coreNumbers: CoreNumberModel){
        val elementsJson = CommonUtils.readAssetFile(requireContext(), "combination.json")
        val (remark, luck) = NumerologyCalculationUtils.calculateCombinationDobNumber(
            elementsJson,
            coreNumbers.birthdayNumber,
            CommonUtils.reduceNumberIgnoreMasterNumber(coreNumbers.lifePathNumber)
        )
        Log.d("Combination", "Combination: $remark - $luck")
        binding.contentCombinationLayout.birthNumberMulankValue.text = coreNumbers.birthdayNumber.toString()
        binding.contentCombinationLayout.birthNumberBhagyankValue.text = coreNumbers.lifePathNumber.toString()
        binding.contentCombinationLayout.birthNumberDobCombination.text = remark + " (" + luck+")"
    }


    private fun createRecycleView(coreNumbers: CoreNumberModel){
        val coreNumberItems = mutableListOf<CoreNumberAccordionItem>();

        val birthNumberItem = CoreNumberAccordionItem(
            "${getString(R.string.birthday_number)} ${coreNumbers.birthdayNumber}",
            getString(R.string.birth_day_title),
            CommonUtils.getDescriptionFromAssetFile(
                this.requireContext(),
                "birthday.json",
                coreNumbers.birthdayNumber.toString()
            ),
            "ic_earth", false
        )


        val lifePathItem = CoreNumberAccordionItem(
            "${getString(R.string.lifepath_number)} ${coreNumbers.lifePathNumber}",
            getString(R.string.lifepath_title),
            NumerologyCalculationUtils.getLifePathDescription(requireContext(), coreNumbers.lifePathNumber),
            "ic_road", false
        )


        val soulUrgeItem = CoreNumberAccordionItem(
            "${getString(R.string.soul_urge_number)} ${coreNumbers.soulUrgeNumber}",
            getString(R.string.soul_title),
            CommonUtils.getDescriptionFromAssetFile
                (this.requireContext(), "soul_urge.json", coreNumbers.soulUrgeNumber.toString()),
            "ic_heart", false
        )


        val personalityItem = CoreNumberAccordionItem(
            "${getString(R.string.personality_number)} ${coreNumbers.personalityNumber}",
            getString(R.string.personality_title),
            CommonUtils.getDescriptionFromAssetFile
                (
                this.requireContext(),
                "personality.json",
                coreNumbers.personalityNumber.toString()
            ),
            "ic_mirrors", false
        )
        val destinyItem = CoreNumberAccordionItem(
            "${getString(R.string.destiny_number)} ${coreNumbers.destinyNumber}",
            getString(R.string.destiny_title),
            CommonUtils.getDescriptionFromAssetFile
                (this.requireContext(), "destiny.json", coreNumbers.destinyNumber.toString()),
            "ic_mirrors", false
        )

        coreNumberItems.add(birthNumberItem)
        coreNumberItems.add(lifePathItem)
        coreNumberItems.add(soulUrgeItem)
        coreNumberItems.add(personalityItem)
        coreNumberItems.add(destinyItem)

        //Log.d("CoreNumber", "Core Number: $coreNumberItems")

        setupCoreNumberRecyclerView(coreNumberItems)
    }


    private fun CoreNumberFragment.setupCoreNumberRecyclerView(coreNumberItems: MutableList<CoreNumberAccordionItem>) {
        coreNumberRecyclerViewAdapter = CoreNumberRecyclerViewAdapter(coreNumberItems, this.requireContext()) { position ->
            coreNumberItems[position].isExpanded = !coreNumberItems[position].isExpanded
            coreNumberRecyclerViewAdapter.notifyItemChanged(position)
        }
        binding.coreNumberRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.coreNumberRecyclerView.adapter = coreNumberRecyclerViewAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(dob: String, fullName: String): CoreNumberFragment {
            val fragment = CoreNumberFragment()
            val args = Bundle()
            args.putString(ARG_DOB, dob)
            args.putString(ARG_FULL_NAME, fullName)
            fragment.arguments = args
            return fragment
        }
    }
}


