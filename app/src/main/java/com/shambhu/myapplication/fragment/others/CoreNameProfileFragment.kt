package com.shambhu.myapplication.fragment.others

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.shambhu.myapplication.R
import com.shambhu.myapplication.adapter.CommonAdapterUtil
import com.shambhu.myapplication.adapter.recycler_adapter.CoreNumberRecyclerViewAdapter
import com.shambhu.myapplication.databinding.FragmentCoreNameProfileBinding
import com.shambhu.myapplication.model.CoreNameDataModel
import com.shambhu.myapplication.model.CoreNumberAccordionItem
import com.shambhu.myapplication.model.MulankData
import com.shambhu.myapplication.model.NameNumberDataModel
import com.shambhu.myapplication.utils.CommonUtils
import com.shambhu.myapplication.utils.Constants.Companion.ARG_DOB
import com.shambhu.myapplication.utils.Constants.Companion.ARG_FULL_NAME
import com.shambhu.myapplication.utils.NumerologyCalculationUtils

class CoreNameProfileFragment : Fragment() {
    private var _binding: FragmentCoreNameProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var coreNumberRecyclerViewAdapter: CoreNumberRecyclerViewAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCoreNameProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            val dob = it.getString(ARG_DOB)
            val fullName = it.getString(ARG_FULL_NAME)
            if (dob != null && fullName != null) {
                val soulNumberValue = NumerologyCalculationUtils.calculateSoulUrge(fullName)
                val personalityNumberValue =
                    NumerologyCalculationUtils.calculatePersonality(fullName)
                val destinyNumberValue = NumerologyCalculationUtils.calculateExpression(fullName)

                var coreNumbers =
                    CoreNameDataModel(soulNumberValue, personalityNumberValue, destinyNumberValue)
                createCombinationByNameNumber(coreNumbers)
                createRecycleView(coreNumbers)

            }
        }
    }

    private fun createRecycleView(coreNumbers: CoreNameDataModel) {
        val coreNumberItems = mutableListOf<CoreNumberAccordionItem>();
        val soulUrge = getData("soul_urge.json",coreNumbers.soulUrgeNumber)
        val personality = getData("personality.json",coreNumbers.soulUrgeNumber)
        val destiny = getData("destiny.json",coreNumbers.soulUrgeNumber)


        val listType = object : TypeToken<List<CoreNumberAccordionItem>>() {}.type
        val mulankList:List<MulankData> = Gson().fromJson("soul_urge.json", listType)
        val mulankData: MulankData = mulankList.filter { it.id == id }.first()

        val soulUrgeItem = CoreNumberAccordionItem(
            "${getString(R.string.soul_urge_number)} ${coreNumbers.soulUrgeNumber}",
            getString(R.string.soul_title),
            soulUrge,
            "ic_heart", false
        )
        val personalityItem = CoreNumberAccordionItem(
            "${getString(R.string.personality_number)} ${coreNumbers.personalityNumber}",
            getString(R.string.personality_title),
            personality,
            "ic_mirrors", false
        )
        val destinyItem = CoreNumberAccordionItem(
            "${getString(R.string.destiny_number)} ${coreNumbers.destinyNumber}",
            getString(R.string.destiny_title),
            destiny,
            "ic_mirrors", false
        )

        coreNumberItems.add(soulUrgeItem)
        coreNumberItems.add(personalityItem)
        coreNumberItems.add(destinyItem)

        //Log.d("CoreNumber", "Core Number: $coreNumberItems")

        setupCoreNumberRecyclerView(coreNumberItems)
    }

    private fun setupCoreNumberRecyclerView(coreNumberItems: MutableList<CoreNumberAccordionItem>) {
        coreNumberRecyclerViewAdapter =
            CoreNumberRecyclerViewAdapter(coreNumberItems, this.requireContext()) { position ->
                coreNumberItems[position].isExpanded = !coreNumberItems[position].isExpanded
                coreNumberRecyclerViewAdapter.notifyItemChanged(position)
            }
        binding.coreNameRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.coreNameRecyclerView.adapter = coreNumberRecyclerViewAdapter
    }

    private fun createCombinationByNameNumber(coreNumbers: CoreNameDataModel) {
        val elementsJson = CommonUtils.readAssetFile(requireContext(), "combination.json")
        val combination = NumerologyCalculationUtils.calculateCombinationNameNumber(
            elementsJson,
            coreNumbers.destinyNumber,
            coreNumbers.soulUrgeNumber,
            coreNumbers.personalityNumber
        )
        binding.tvDestinyValue.text = coreNumbers.destinyNumber.toString()
        binding.tvSoulValue.text = coreNumbers.soulUrgeNumber.toString()
        binding.tvPersonalityValue.text = coreNumbers.personalityNumber.toString()

        val listType = object : TypeToken<List<String>>() {}.type
        val combinationList:List<String> = Gson().fromJson(combination, listType)
        CommonAdapterUtil.setupNumberBulletRecyclerViewAdapter(
            requireContext(),
            binding.tvCombinationValueRecyclerView,
            combinationList
        )
    }

    private fun getData(jsonName: String, id: Int): NameNumberDataModel
    {
        val listType = object : TypeToken<List<NameNumberDataModel>>() {}.type
        val mulankList:List<NameNumberDataModel> = Gson().fromJson(jsonName, listType)
        val mulankData: NameNumberDataModel = mulankList.filter { it.id == id }.first()
        Log.d(jsonName, "${mulankData}")

        return mulankData
    }



    companion object {
        fun newInstance(dob: String, fullName: String): CoreNameProfileFragment {
            val fragment = CoreNameProfileFragment()
            val args = Bundle()
            args.putString(ARG_DOB, dob)
            args.putString(ARG_FULL_NAME, fullName)
            fragment.arguments = args
            return fragment
        }
    }
}