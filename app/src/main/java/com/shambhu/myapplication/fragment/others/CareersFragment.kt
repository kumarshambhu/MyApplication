package com.shambhu.myapplication.fragment.others

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.shambhu.myapplication.adapter.CareersAdapter
import com.shambhu.myapplication.databinding.FragmentCareersBinding
import com.shambhu.myapplication.repository.CareerRepository
import com.shambhu.myapplication.repository.impl.CareerRepositoryImpl
import com.shambhu.myapplication.service.impl.CareerServiceImpl
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class CareersFragment : Fragment() {

    private var _binding: FragmentCareersBinding? = null
    private val binding get() = _binding!!

    private val careerRepository: CareerRepository by lazy {
        CareerRepositoryImpl(CareerServiceImpl(requireContext()))
    }

    private lateinit var careersAdapter: CareersAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCareersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        loadCareers()
    }

    private fun setupRecyclerView() {
        careersAdapter = CareersAdapter()
        binding.recyclerView.apply {
            adapter = careersAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun loadCareers() {
        // For now, let's just use a hardcoded number. This can be passed as an argument later.
        val number = arguments?.getInt(ARG_NUMBER) ?: 1
        lifecycleScope.launch {
            careerRepository.getCareers(number)
                .catch { e ->
                    Log.e("CareersFragment", "Error loading careers", e)
                }
                .collect { careerData ->
                    careersAdapter.submitList(careerData.careers)
                }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val ARG_NUMBER = "number"

        fun newInstance(number: Int): CareersFragment {
            val fragment = CareersFragment()
            val args = Bundle()
            args.putInt(ARG_NUMBER, number)
            fragment.arguments = args
            return fragment
        }
    }
}
