package com.shambhu.myapplication.fragment.others
// SuccessNumberFragment.kt
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.gson.Gson
import com.shambhu.myapplication.adapter.recycler_adapter.BulletPointRecyclerViewAdapter
import com.shambhu.myapplication.adapter.CommonAdapterUtil
import com.shambhu.myapplication.databinding.FragmentSuccessNumberBinding
import com.shambhu.myapplication.model.SuccessNumberResponse
import com.shambhu.myapplication.repository.MilestoneSuiteRepository
import com.shambhu.myapplication.repository.impl.MilestoneSuiteRepositoryImpl
import com.shambhu.myapplication.service.impl.MilestoneSuiteServiceImpl
import com.shambhu.myapplication.utils.CommonUtils
import com.shambhu.myapplication.utils.Constants.Companion.ARG_DOB
import com.shambhu.myapplication.utils.Constants.Companion.ARG_FULL_NAME
import com.shambhu.myapplication.utils.NumerologyCalculationUtils
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class SuccessNumberFragment : Fragment() {
    private var _binding: FragmentSuccessNumberBinding? = null
    private val binding get() = _binding!!
    private val successNumberRepository: MilestoneSuiteRepository by lazy {
        MilestoneSuiteRepositoryImpl(MilestoneSuiteServiceImpl(requireContext()))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSuccessNumberBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            val dob = it.getString(ARG_DOB)
            calculateSuccessNumberDetails(dob.toString())
        }

    }

    private fun calculateSuccessNumberDetails(dob: String) {
        val (day, month, year) = CommonUtils.parseDateTriple(dob)
        val successNumber = NumerologyCalculationUtils.calculateSuccessNumber(day, month)

        lifecycleScope.launch {
            successNumberRepository.getSuccessNumberData()
                .catch { e ->
                    Log.e("SuccessNumberFragment", "Error fetching success number data", e)
                }
                .collect { data ->
                    val data1 = data.success_numbers
                    val data2 = data1[successNumber.toString()]

                    binding.successNumberText.text = "Your Success Number: $successNumber"

                    if (data2 != null) {
                        if (data2.challenges.isNotEmpty()) {
                            CommonAdapterUtil.setupNumberBulletRecyclerViewAdapter(
                                requireContext(),
                                binding.challengesRecyclerView, data2.challenges
                            )
                        } else {
                            binding.challengesRecyclerView.visibility = View.GONE
                            binding.challengesTextView.visibility = View.GONE
                        }

                        if (data2.qualities.isNotEmpty()) {
                            CommonAdapterUtil.setupNumberBulletRecyclerViewAdapter(
                                requireContext(),
                                binding.qualitiesRecyclerView, data2.qualities
                            )
                        } else {
                            binding.qualitiesRecyclerView.visibility = View.GONE
                            binding.qualitiesTextView.visibility = View.GONE
                        }

                        if (!data2.notes.isNullOrEmpty()) {
                            binding.notesTextView.visibility = View.VISIBLE
                            binding.notesTextView.text = "Notes: ${data2.notes}"
                        } else {
                            binding.notesTextView.visibility = View.GONE
                        }
                    } else {
                        // Handle the case where data2 is null, e.g., hide all views
                        binding.challengesRecyclerView.visibility = View.GONE
                        binding.challengesTextView.visibility = View.GONE
                        binding.qualitiesRecyclerView.visibility = View.GONE
                        binding.qualitiesTextView.visibility = View.GONE
                        binding.notesTextView.visibility = View.GONE
                    }

                    binding.resultLayout.visibility = View.VISIBLE
                }
        }
    }


    companion object {
        fun newInstance(dob: String, fullName: String): SuccessNumberFragment {
            val fragment = SuccessNumberFragment()
            val args = Bundle()
            args.putString(ARG_DOB, dob)
            args.putString(ARG_FULL_NAME, fullName)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}