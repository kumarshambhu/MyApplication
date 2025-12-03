package com.shambhu.myapplication.fragment.secondary_number

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.shambhu.myapplication.databinding.FragmentNameColorBinding
import com.shambhu.myapplication.utils.CommonUtils
import com.shambhu.myapplication.utils.Constants
import com.shambhu.myapplication.utils.NumerologyCalculationUtils

class NameColorFragment : Fragment() {

    private var _binding: FragmentNameColorBinding? = null
    private val binding get() = _binding!!

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


    private fun bindColors(fullName: String){
        val colorsJson = CommonUtils.readAssetFile(requireContext(), "colors.json") ?: return

        // Color Group
        val (description, details, matchedColors, group, matchedColorsCount) = NumerologyCalculationUtils.calculateColorGroup(
            fullName, colorsJson)
        binding.colorGroupNameValue.text = matchedColors
        binding.colorGroupDescriptionValue.text = description
        binding.colorGroupDetailsValue.text = NumerologyCalculationUtils.convertToHtml(details)
        binding.colorGroupMatchedColorValue.text = matchedColors
        binding.numberOfColorsMatchedValue.text = matchedColorsCount.toString()

        val colorCounts = NumerologyCalculationUtils.calculateColorCounts(fullName, colorsJson)
        val countsText = colorCounts.entries.joinToString("\n") { (color, count) ->
            "$color: $count"
        }
        binding.individualColorCountsValue.text = countsText

        val matchedGroups = NumerologyCalculationUtils.findAllMatchedColorGroups(fullName, colorsJson)
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