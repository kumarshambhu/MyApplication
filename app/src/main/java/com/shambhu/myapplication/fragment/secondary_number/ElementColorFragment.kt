package com.shambhu.myapplication.fragment.secondary_number

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.shambhu.myapplication.databinding.FragmentElementColorBinding
import com.shambhu.myapplication.utils.CommonUtils
import com.shambhu.myapplication.utils.Constants
import com.shambhu.myapplication.utils.NumerologyCalculationUtils

class ElementColorFragment : Fragment() {

    private var _binding: FragmentElementColorBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentElementColorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            val fullName = it.getString(Constants.Companion.ARG_FULL_NAME)
                bindingElements(fullName.toString())
                bindColors(fullName.toString())
        }
    }

    private fun bindingElements(fullName: String){
        val elementsJson = CommonUtils.readAssetFile(requireContext(), "elements.json")?: return
        val (dominantElement, score) = NumerologyCalculationUtils.calculateElements(fullName, elementsJson)
        binding.airElementValue.text = score.get(Constants.Companion.ELEMENT_KEY_AIR).toString()
        binding.earthElementValue.text = score.get(Constants.Companion.ELEMENT_KEY_EARTH).toString()
        binding.fireElementValue.text = score.get(Constants.Companion.ELEMENT_KEY_FIRE).toString()
        binding.waterElementValue.text = score.get(Constants.Companion.ELEMENT_KEY_WATER).toString()
        Log.i("score", score.toString())
        binding.elementDescription.text = NumerologyCalculationUtils.convertToHtml(dominantElement)
    }

    private fun bindColors(fullName: String){
        val colorsJson = CommonUtils.readAssetFile(requireContext(), "colors.json") ?: return

        // Color Group
        val (description, details, matchedColors, group, matchedColorsCount) = NumerologyCalculationUtils.calculateColorGroup(
            fullName, colorsJson)
        binding.colorGroupNameTextView.text = group
        binding.colorGroupDescriptionTextView.text = description
        binding.colorGroupDetailsTextView.text = NumerologyCalculationUtils.convertToHtml(details)
        binding.matchedColorsTextView.text = matchedColors
        binding.matchedColorsCountTextView.text = matchedColorsCount.toString()

        val colorCounts = NumerologyCalculationUtils.calculateColorCounts(fullName, colorsJson)
        val countsText = colorCounts.entries.joinToString("\n") { (color, count) ->
            "$color: $count"
        }
        binding.individualColorCountsTextView.text = countsText

        val matchedGroups = NumerologyCalculationUtils.findAllMatchedColorGroups(fullName, colorsJson)
        val matchedGroupsText = matchedGroups.entries.joinToString("\n") { (group, colors) ->
            "$group: ${colors.joinToString(", ")}"
        }
        binding.matchedColorGroupsTextView.text = matchedGroupsText
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(dob: String, fullName: String): ElementColorFragment {
            val fragment = ElementColorFragment()
            val args = Bundle()
            args.putString(Constants.Companion.ARG_DOB, dob)
            args.putString(Constants.Companion.ARG_FULL_NAME, fullName)
            fragment.arguments = args
            return fragment
        }
    }
}