package com.shambhu.myapplication.fragment.secondary_number

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.shambhu.myapplication.databinding.FragmentNameElementBinding
import com.shambhu.myapplication.utils.CommonUtils
import com.shambhu.myapplication.utils.Constants
import com.shambhu.myapplication.utils.NumerologyCalculationUtils

class NameElementFragment : Fragment() {

    private var _binding: FragmentNameElementBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNameElementBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            val fullName = it.getString(Constants.Companion.ARG_FULL_NAME)
                bindingElements(fullName.toString())
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


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(dob: String, fullName: String): NameElementFragment {
            val fragment = NameElementFragment()
            val args = Bundle()
            args.putString(Constants.Companion.ARG_DOB, dob)
            args.putString(Constants.Companion.ARG_FULL_NAME, fullName)
            fragment.arguments = args
            return fragment
        }
    }
}