package com.shambhu.myapplication.fragment.others

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.gson.Gson
import com.shambhu.myapplication.adapter.CommonAdapterUtil
import com.shambhu.myapplication.databinding.FragmentMaturityBinding
import com.shambhu.myapplication.model.MaturityData
import com.shambhu.myapplication.repository.MaturityRepository
import com.shambhu.myapplication.repository.impl.MaturityRepositoryImpl
import com.shambhu.myapplication.service.impl.MaturityServiceImpl
import com.shambhu.myapplication.utils.CommonUtils
import com.shambhu.myapplication.utils.Constants.Companion.ARG_DOB
import com.shambhu.myapplication.utils.Constants.Companion.ARG_FULL_NAME
import com.shambhu.myapplication.utils.NumerologyCalculationUtils
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class MaturityFragment : Fragment() {
    private var _binding: FragmentMaturityBinding? = null
    private val binding get() = _binding!!

    private val maturityRepository: MaturityRepository by lazy {
        MaturityRepositoryImpl(MaturityServiceImpl(Gson()))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMaturityBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            val dob = it.getString(ARG_DOB)
            val fullName = it.getString(ARG_FULL_NAME)
            if (dob != null && fullName != null) {
                calculateNumerology(dob, fullName)
            }
        }
    }

    private fun calculateNumerology(dob: String, name: String) {
        try {
            val (day, month, year) = CommonUtils.parseDateTriple(dob)
            val lifePath = NumerologyCalculationUtils.calculateLifePath(day, month, year)
            val destiny = NumerologyCalculationUtils.calculateExpression(name)
            val maturity = NumerologyCalculationUtils.calculateMaturityNumber(lifePath, destiny)

            binding.tvLifePath.text = "Life Path Number: $lifePath"
            binding.tvDestiny.text = "Destiny Number: $destiny"
            binding.maturityHeader.text = "Maturity Number: $maturity"

            maturityRepository.getMaturityInterpretation(requireContext(), maturity)
                .onEach { result ->
                    result.onSuccess { data ->
                        handleMaturityData(data)
                    }.onFailure {
                        Toast.makeText(requireContext(), "Error: ${it.message}", Toast.LENGTH_SHORT).show()
                    }
                }.launchIn(viewLifecycleOwner.lifecycleScope)

        } catch (e: Exception) {
            Toast.makeText(
                requireContext(),
                "Error in calculation: ${e.message}",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun handleMaturityData(data: MaturityData?) {
        data?.let {
            binding.tvOverview.text = it.overview

            if (it.positiveTraits.isNotEmpty()) {
                CommonAdapterUtil.setupNumberBulletRecyclerViewAdapter(
                    requireContext(),
                    binding.positiveTraitsRecyclerView, it.positiveTraits
                )
            } else {
                binding.positiveTraitsRecyclerView.visibility = View.GONE
                binding.tvPositiveTraits.visibility = View.GONE
            }

            if (it.challenges.isNotEmpty()) {
                CommonAdapterUtil.setupNumberBulletRecyclerViewAdapter(
                    requireContext(),
                    binding.challengesRecyclerView, it.challenges
                )
            } else {
                binding.challengesRecyclerView.visibility = View.GONE
                binding.tvChallenges.visibility = View.GONE
            }

            val additionalInfoList = mutableListOf<String>()
            it.karmicNotes?.let { notes -> additionalInfoList.add("Karmic Notes: $notes") }
            it.lifePurpose?.let { purpose -> additionalInfoList.add("Life Purpose: $purpose") }
            it.lifeOutcome?.let { outcome -> additionalInfoList.add("Life Outcome: $outcome") }

            if (additionalInfoList.isNotEmpty()) {
                CommonAdapterUtil.setupNumberBulletRecyclerViewAdapter(
                    requireContext(),
                    binding.additionalInfoRecyclerView, additionalInfoList
                )
            } else {
                binding.additionalInfoRecyclerView.visibility = View.GONE
                binding.tvAdditionalInfo.visibility = View.GONE
            }
        }
        binding.resultsContainer.visibility = View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(dob: String, fullName: String): MaturityFragment {
            val fragment = MaturityFragment()
            val args = Bundle()
            args.putString(ARG_DOB, dob)
            args.putString(ARG_FULL_NAME, fullName)
            fragment.arguments = args
            return fragment
        }
    }
}
