package com.shambhu.myapplication.fragment.core_number

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.shambhu.myapplication.adapter.CommonAdapterUtil
import com.shambhu.myapplication.adapter.recycler_adapter.CoreNumberRecyclerViewAdapter
import com.shambhu.myapplication.databinding.FragmentCoreNameProfileBinding
import com.shambhu.myapplication.model.CoreNumberAccordionItem
import com.shambhu.myapplication.repository.CoreNameProfileRepository
import com.shambhu.myapplication.repository.impl.CoreNameProfileRepositoryImpl
import com.shambhu.myapplication.service.impl.CoreNameProfileServiceImpl
import com.shambhu.myapplication.utils.Constants
import com.shambhu.myapplication.utils.NumerologyCalculationUtils
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class CoreNameProfileFragment : Fragment() {
    private var _binding: FragmentCoreNameProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var coreNumberRecyclerViewAdapter: CoreNumberRecyclerViewAdapter
    private val repository: CoreNameProfileRepository by lazy {
        CoreNameProfileRepositoryImpl(CoreNameProfileServiceImpl())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCoreNameProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            val fullName = it.getString(Constants.Companion.ARG_FULL_NAME)
            if (fullName != null) {
                lifecycleScope.launch {
                    repository.getCoreNameProfileData(requireContext(), fullName)
                        .catch { e ->
                            Log.e("CoreNameProfileFragment", "Error collecting core name profile data", e)
                        }
                        .collect { (coreNumberItems, combinationList) ->
                            val soulNumberValue = NumerologyCalculationUtils.calculateSoulUrge(fullName)
                            val personalityNumberValue =
                                NumerologyCalculationUtils.calculatePersonality(fullName)
                            val destinyNumberValue = NumerologyCalculationUtils.calculateExpression(fullName)
                            binding.tvDestinyValue.text = destinyNumberValue.toString()
                            binding.tvSoulValue.text = soulNumberValue.toString()
                            binding.tvPersonalityValue.text = personalityNumberValue.toString()
                            setupCoreNumberRecyclerView(coreNumberItems.toMutableList())
                            CommonAdapterUtil.setupNumberBulletRecyclerViewAdapter(
                                requireContext(),
                                binding.tvCombinationValueRecyclerView,
                                combinationList
                            )
                        }
                }
            }
        }
    }


    private fun setupCoreNumberRecyclerView(coreNumberItems: MutableList<CoreNumberAccordionItem>) {
        coreNumberItems.forEach {
            it->
            Log.d("Core Number", "${it.numberDataModel}")
        }
        coreNumberRecyclerViewAdapter =
            CoreNumberRecyclerViewAdapter(coreNumberItems, this.requireContext()) { position ->
                val previousExpandedPosition = coreNumberItems.indexOfFirst { it.isExpanded }
                if (previousExpandedPosition != -1 && previousExpandedPosition != position) {
                    coreNumberItems[previousExpandedPosition].isExpanded = false
                    coreNumberRecyclerViewAdapter.notifyItemChanged(previousExpandedPosition)
                }
                coreNumberItems[position].isExpanded = !coreNumberItems[position].isExpanded
                coreNumberRecyclerViewAdapter.notifyItemChanged(position)
            }
        binding.coreNameRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.coreNameRecyclerView.adapter = coreNumberRecyclerViewAdapter
    }

    companion object {
        fun newInstance(dob: String, fullName: String): CoreNameProfileFragment {
            val fragment = CoreNameProfileFragment()
            val args = Bundle()
            args.putString(Constants.Companion.ARG_DOB, dob)
            args.putString(Constants.Companion.ARG_FULL_NAME, fullName)
            fragment.arguments = args
            return fragment
        }
    }
}