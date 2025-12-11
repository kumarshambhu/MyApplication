package com.shambhu.myapplication.fragment.secondary_number

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.gson.Gson
import com.shambhu.myapplication.databinding.FragmentNameColorBinding
import com.shambhu.myapplication.model.ColorAnalysisResult
import com.shambhu.myapplication.repository.NameAnalysisRepository
import com.shambhu.myapplication.repository.impl.NameAnalysisRepositoryImpl
import com.shambhu.myapplication.service.impl.NameAnalysisServiceImpl
import com.shambhu.myapplication.utils.CommonUtils
import com.shambhu.myapplication.utils.Constants
import com.shambhu.myapplication.utils.NumerologyCalculationUtils
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class NameColorFragment : Fragment() {

    private var _binding: FragmentNameColorBinding? = null
    private val binding get() = _binding!!
    private val nameAnalysisRepository: NameAnalysisRepository by lazy {
        NameAnalysisRepositoryImpl(NameAnalysisServiceImpl(Gson()))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNameColorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            val fullName = it.getString(Constants.Companion.ARG_FULL_NAME)
            bindColors(fullName.toString())
        }
    }

    private fun bindColors(fullName: String) {
        nameAnalysisRepository.getColorGroup(requireContext(), fullName)
            .onEach { result ->
                result.onSuccess { colorAnalysisResult ->
                    binding.colorGroupNameValue.text = colorAnalysisResult.matchedColors
                    binding.colorGroupDescriptionValue.text = colorAnalysisResult.description
                    binding.colorGroupDetailsValue.text =
                        NumerologyCalculationUtils.convertToHtml(colorAnalysisResult.details)
                    binding.colorGroupMatchedColorValue.text = colorAnalysisResult.matchedColors
                    binding.numberOfColorsMatchedValue.text =
                        colorAnalysisResult.matchedColorsCount.toString()
                }.onFailure { error ->
                    Log.e("NameColorFragment", "Failed to load color group data", error)
                }
            }.launchIn(viewLifecycleOwner.lifecycleScope)

        val colorsJson = CommonUtils.readAssetFile(requireContext(), "colors.json") ?: return
        val colorsData = Gson().fromJson(colorsJson, com.shambhu.myapplication.model.ColorsData::class.java)

        val colorCounts = NumerologyCalculationUtils.calculateColorCounts(fullName, colorsData)
        val countsText = colorCounts.entries.joinToString("\n") { (color, count) ->
            "$color: $count"
        }
        binding.individualColorCountsValue.text = countsText

        val matchedGroups =
            NumerologyCalculationUtils.findAllMatchedColorGroups(fullName, colorsData)
        val matchedGroupsText = matchedGroups.entries.joinToString("\n") { (group, colors) ->
            "$group: ${colors.joinToString(", ")}"
        }
        binding.matchedColorGroupsValue.text = matchedGroupsText
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(dob: String, fullName: String): NameColorFragment {
            val fragment = NameColorFragment()
            val args = Bundle()
            args.putString(Constants.Companion.ARG_DOB, dob)
            args.putString(Constants.Companion.ARG_FULL_NAME, fullName)
            fragment.arguments = args
            return fragment
        }
    }
}