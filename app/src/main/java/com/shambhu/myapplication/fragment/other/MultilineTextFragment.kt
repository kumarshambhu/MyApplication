package com.shambhu.myapplication.fragment.other

import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.shambhu.myapplication.databinding.FragmentMultilineTextBinding
import com.shambhu.myapplication.utils.CommonUtils
import com.shambhu.myapplication.utils.Constants
import com.shambhu.myapplication.utils.NumerologyCalculationUtils

class MultilineTextFragment : Fragment() {

    private var _binding: FragmentMultilineTextBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMultilineTextBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            val fullName = it.getString(Constants.ARG_FULL_NAME) ?: return
            val colorsJson = CommonUtils.readAssetFile(requireContext(), "colors.json") ?: return

            // Color Group
            val (description, details, matchedColors, group) = NumerologyCalculationUtils.calculateColorGroup(fullName, colorsJson)
            binding.colorGroupNameTextView.text = group
            binding.colorGroupDescriptionTextView.text = description
            binding.colorGroupDetailsTextView.text = Html.fromHtml(details, Html.FROM_HTML_MODE_COMPACT)
            binding.matchedColorsTextView.text = matchedColors
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(dob: String, fullName: String): MultilineTextFragment {
            val fragment = MultilineTextFragment()
            val args = Bundle()
            args.putString(Constants.ARG_DOB, dob)
            args.putString(Constants.ARG_FULL_NAME, fullName)
            fragment.arguments = args
            return fragment
        }
    }
}