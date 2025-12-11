package com.shambhu.myapplication.fragment.core_number

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.shambhu.myapplication.adapter.KarmicDebtRecyclerViewAdapter
import com.shambhu.myapplication.adapter.KarmicLessonRecyclerViewAdapter
import com.shambhu.myapplication.databinding.FragmentKarmicNumberBinding
import com.shambhu.myapplication.model.KarmicAccordionItem
import com.shambhu.myapplication.model.KarmicDebt
import com.shambhu.myapplication.model.KarmicLessonItem
import com.shambhu.myapplication.repository.KarmicAnalysisRepository
import com.shambhu.myapplication.repository.impl.KarmicAnalysisRepositoryImpl
import com.shambhu.myapplication.service.impl.KarmicAnalysisServiceImpl
import com.shambhu.myapplication.utils.CommonUtils
import com.shambhu.myapplication.utils.Constants
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class KarmicNumberFragment : Fragment() {
    private var _binding: FragmentKarmicNumberBinding? = null
    private val binding get() = _binding!!

    private lateinit var karmicDebtRecyclerViewAdapter: KarmicDebtRecyclerViewAdapter
    private lateinit var karmicLessonAdapter: KarmicLessonRecyclerViewAdapter

    private val karmicAnalysisRepository: KarmicAnalysisRepository by lazy {
        KarmicAnalysisRepositoryImpl(KarmicAnalysisServiceImpl(Gson()))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentKarmicNumberBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            val dob = it.getString(Constants.ARG_DOB)
            val fullName = it.getString(Constants.ARG_FULL_NAME)
            if (dob != null && fullName != null) {
                updateKarmicInfo(fullName, dob)
            }
        }
    }

    private fun updateKarmicInfo(fullName: String, dateOfBirth: String) {
        val date = CommonUtils.parseDate(dateOfBirth)
        val day = date.dayOfMonth
        val month = date.monthValue
        val year = date.year

        // Fetch and display Karmic Lessons
        karmicAnalysisRepository.getKarmicLessons(requireContext(), fullName)
            .onEach { result ->
                result.onSuccess { lessons ->
                    binding.karmicLessonNumberValue.text = lessons.joinToString(", ") { it.number.toString() }
                    setupKarmicLessonRecyclerView(lessons)
                }.onFailure {
                    Log.e("KarmicNumberFragment", "Failed to load karmic lessons", it)
                }
            }.launchIn(viewLifecycleOwner.lifecycleScope)

        // Fetch and display Karmic Debts
        karmicAnalysisRepository.getKarmicDebts(requireContext(), day, month, year, fullName)
            .onEach { result ->
                result.onSuccess { debts ->
                    if (debts.isEmpty()) {
                        binding.tvNoKarmicDebt.visibility = View.VISIBLE
                        binding.rvKarmicDebt.visibility = View.GONE
                    } else {
                        binding.tvNoKarmicDebt.visibility = View.GONE
                        binding.rvKarmicDebt.visibility = View.VISIBLE
                        setupKarmicDebtRecyclerView(debts)
                    }
                }.onFailure {
                    Log.e("KarmicNumberFragment", "Failed to load karmic debts", it)
                }
            }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun setupKarmicLessonRecyclerView(karmicLessons: List<KarmicLessonItem>) {
        val accordionItems = karmicLessons.map {
            KarmicAccordionItem(it.number.toString(), "Source Empty", it.description, isExpanded = false)
        }
        karmicLessonAdapter = KarmicLessonRecyclerViewAdapter(accordionItems) { position ->
            // Collapse all items except the clicked one
            accordionItems.forEachIndexed { index, item ->
                if (index != position) item.isExpanded = false
            }
            // Toggle the clicked item
            accordionItems[position].isExpanded = !accordionItems[position].isExpanded
            karmicLessonAdapter.notifyDataSetChanged()
        }
        binding.karmicLessonRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.karmicLessonRecyclerView.adapter = karmicLessonAdapter
    }

    private fun setupKarmicDebtRecyclerView(karmicDebts: List<KarmicDebt>) {
        karmicDebtRecyclerViewAdapter = KarmicDebtRecyclerViewAdapter(requireContext(), karmicDebts) { position ->
            // Collapse all items except the clicked one
            karmicDebts.forEachIndexed { index, item ->
                if (index != position) item.isExpanded = false
            }
            // Toggle the clicked item
            karmicDebts[position].isExpanded = !karmicDebts[position].isExpanded
            karmicDebtRecyclerViewAdapter.notifyDataSetChanged()
        }
        binding.rvKarmicDebt.layoutManager = LinearLayoutManager(context)
        binding.rvKarmicDebt.adapter = karmicDebtRecyclerViewAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(dob: String, fullName: String): KarmicNumberFragment {
            val fragment = KarmicNumberFragment()
            val args = Bundle()
            args.putString(Constants.ARG_DOB, dob)
            args.putString(Constants.ARG_FULL_NAME, fullName)
            fragment.arguments = args
            return fragment
        }
    }
}
