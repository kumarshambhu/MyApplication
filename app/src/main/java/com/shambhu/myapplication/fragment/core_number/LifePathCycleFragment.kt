package com.shambhu.myapplication.fragment.core_number

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.shambhu.myapplication.adapter.LifePathCycleAdapter
import com.shambhu.myapplication.databinding.FragmentLifePathCycleBinding
import com.shambhu.myapplication.utils.NumerologyCalculationUtils
import com.shambhu.myapplication.utils.Constants

class LifePathCycleFragment : Fragment() {

    private var _binding: FragmentLifePathCycleBinding? = null
    private val binding get() = _binding!!
    private lateinit var lifePathCycleAdapter: LifePathCycleAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLifePathCycleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        loadLifePathCycles()
    }

    private fun setupRecyclerView() {
        lifePathCycleAdapter = LifePathCycleAdapter(emptyList())
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = lifePathCycleAdapter
        }
    }

    private fun loadLifePathCycles() {
        val sharedPref = requireActivity().getSharedPreferences(Constants.PREFERENCE_NAME, Context.MODE_PRIVATE)
        val dob = sharedPref.getString(Constants.PREFERENCE_DATE_OF_BIRTH, null)

        if (dob != null) {
            val dateParts = dob.split("/").map { it.toInt() }
            val day = dateParts[0]
            val month = dateParts[1]
            val year = dateParts[2]

            val cycles = NumerologyCalculationUtils.calculateLifePathCycles(requireContext(), day, month, year)
            lifePathCycleAdapter.updateData(cycles)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
