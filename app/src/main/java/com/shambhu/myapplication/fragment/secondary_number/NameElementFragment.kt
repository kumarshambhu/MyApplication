package com.shambhu.myapplication.fragment.secondary_number

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.gson.Gson
import com.shambhu.myapplication.databinding.FragmentNameElementBinding
import com.shambhu.myapplication.repository.NameAnalysisRepository
import com.shambhu.myapplication.repository.impl.NameAnalysisRepositoryImpl
import com.shambhu.myapplication.service.impl.NameAnalysisServiceImpl
import com.shambhu.myapplication.utils.Constants
import com.shambhu.myapplication.utils.NumerologyCalculationUtils
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class NameElementFragment : Fragment() {

    private var _binding: FragmentNameElementBinding? = null
    private val binding get() = _binding!!
    private val nameAnalysisRepository: NameAnalysisRepository by lazy {
        NameAnalysisRepositoryImpl(NameAnalysisServiceImpl(Gson()))
    }

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

    private fun bindingElements(fullName: String) {
        nameAnalysisRepository.getElements(requireContext(), fullName)
            .onEach { result ->
                result.onSuccess { (dominantElement, score) ->
                    binding.airElementValue.text = score[Constants.Companion.ELEMENT_KEY_AIR].toString()
                    binding.earthElementValue.text =
                        score[Constants.Companion.ELEMENT_KEY_EARTH].toString()
                    binding.fireElementValue.text =
                        score[Constants.Companion.ELEMENT_KEY_FIRE].toString()
                    binding.waterElementValue.text =
                        score[Constants.Companion.ELEMENT_KEY_WATER].toString()
                    Log.i("score", score.toString())
                    binding.elementDescription.text =
                        NumerologyCalculationUtils.convertToHtml(dominantElement)
                }.onFailure { error ->
                    Log.e("NameElementFragment", "Failed to load element data", error)
                }
            }.launchIn(viewLifecycleOwner.lifecycleScope)
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