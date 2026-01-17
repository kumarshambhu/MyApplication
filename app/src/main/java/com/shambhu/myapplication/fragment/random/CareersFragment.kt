package com.shambhu.myapplication.fragment.random

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.shambhu.myapplication.adapter.CommonAdapterUtil
import com.shambhu.myapplication.databinding.FragmentCareersBinding
import com.shambhu.myapplication.repository.MilestoneSuiteRepository
import com.shambhu.myapplication.repository.impl.MilestoneSuiteRepositoryImpl
import com.shambhu.myapplication.service.impl.MilestoneSuiteServiceImpl
import com.shambhu.myapplication.utils.CommonUtils
import com.shambhu.myapplication.utils.Constants
import com.shambhu.myapplication.utils.NumerologyCalculationUtils
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class CareersFragment : Fragment() {

    private var _binding: FragmentCareersBinding? = null
    private val binding get() = _binding!!

    private val careerRepository: MilestoneSuiteRepository by lazy {
        MilestoneSuiteRepositoryImpl(MilestoneSuiteServiceImpl(requireContext()))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCareersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadCareers()
    }


    private fun loadCareers() {
        // For now, let's just use a hardcoded number. This can be passed as an argument later.
        arguments?.let {
            val dob = it.getString(Constants.Companion.ARG_DOB)
            val (day, month, year) = CommonUtils.parseDateTriple(dob.toString())
            val lifepath = NumerologyCalculationUtils.calculateLifePath(day, month, year)
            lifecycleScope.launch {
                careerRepository.getCareers(lifepath)
                    .catch { e ->
                        Log.e("CareersFragment", "Error loading careers", e)
                    }
                    .collect { careerData ->
                        CommonAdapterUtil.setupNumberBulletRecyclerViewAdapter(
                            requireContext(),
                            binding.recyclerView,
                            careerData.careers
                        )
                    }
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val ARG_NUMBER = "number"

        fun newInstance(dob: String, fullName: String): CareersFragment {
            val fragment = CareersFragment()
            val args = Bundle()
            args.putString(Constants.Companion.ARG_DOB, dob)
            args.putString(Constants.Companion.ARG_FULL_NAME, fullName)
            fragment.arguments = args
            return fragment
        }
    }
}