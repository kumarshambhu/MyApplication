package com.shambhu.myapplication.fragment.other

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.shambhu.myapplication.databinding.FragmentDestinyBinding
import com.shambhu.myapplication.utils.CommonUtils
import com.shambhu.myapplication.utils.Constants
import com.shambhu.myapplication.utils.NumerologyCalculationUtils

class DestinyFragment : Fragment() {

    private var _binding: FragmentDestinyBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDestinyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            val dob = it.getString(Constants.ARG_DOB)
            val fullName = it.getString(Constants.ARG_FULL_NAME)

            if (dob != null && fullName != null) {
                val date = CommonUtils.parseDate(dob)
                val day = date.dayOfMonth
                val month = date.monthValue
                val year = date.year

                val destiny = NumerologyCalculationUtils.calculateExpression(fullName)
                val soul = NumerologyCalculationUtils.calculateSoulUrge(fullName)
                val personality = NumerologyCalculationUtils.calculatePersonality(fullName)
                val elementsJson = CommonUtils.readAssetFile(requireContext(), "combination.json")
                val combination = NumerologyCalculationUtils.calculateCombinationNumber(elementsJson, destiny, soul, personality)

                binding.tvDestinyValue.text = destiny.toString()
                binding.tvSoulValue.text = soul.toString()
                binding.tvPersonalityValue.text = personality.toString()
                binding.tvCombinationValue.text = combination.toString()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(dob: String, fullName: String): DestinyFragment {
            val fragment = DestinyFragment()
            val args = Bundle()
            args.putString(Constants.ARG_DOB, dob)
            args.putString(Constants.ARG_FULL_NAME, fullName)
            fragment.arguments = args
            return fragment
        }
    }
}
